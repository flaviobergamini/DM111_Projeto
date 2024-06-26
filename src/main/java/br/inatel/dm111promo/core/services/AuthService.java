package br.inatel.dm111promo.core.services;

import br.inatel.dm111promo.core.aggregates.user.User;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.core.enums.AppErrorCode;
import br.inatel.dm111promo.core.models.AuthRequest;
import br.inatel.dm111promo.core.models.AuthResponse;
import br.inatel.dm111promo.infrastructure.repositories.UserFirebaseRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    @Value("${jwt.custom.issuer}")
    private String tokenIssuer;

    private final PrivateKey privateKey;
    private final PasswordCryptoService crypto;
    private final UserFirebaseRepository repository;

    public AuthService(PrivateKey privateKey, PasswordCryptoService crypto, UserFirebaseRepository repository) {
        this.privateKey = privateKey;
        this.crypto = crypto;
        this.repository = repository;
    }

    public AuthResponse authUser(AuthRequest request) throws ApiExceptionModel {
        try {
            var user = repository.findByEmail(request.email())
                    .orElseThrow(() -> new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS));
            var encryptedPassword = crypto.encryptPassword(request.password());
            if (encryptedPassword.equals(user.getPassword())) {
                var token = generateToken(user);
                return new AuthResponse(token, user.getId());
            } else {
                throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private String generateToken(User user) {
        var now = Instant.now();
        return Jwts.builder()
                .issuer(tokenIssuer)
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(300)))
                .signWith(privateKey)
                .compact();
    }
}
