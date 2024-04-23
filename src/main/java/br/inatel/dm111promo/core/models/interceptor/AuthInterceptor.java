package br.inatel.dm111promo.core.models.interceptor;

import br.inatel.dm111promo.core.enums.AppErrorCode;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.infrastructure.repositories.UserFirebaseRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Value("${jwt.custom.issuer}")
    private String tokenIssuer;

    private final JwtParser jwtParser;
    private final UserFirebaseRepository repository;

    public AuthInterceptor(JwtParser jwtParser, UserFirebaseRepository repository) {
        this.jwtParser = jwtParser;
        this.repository = repository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var method = request.getMethod();
        var uri = request.getRequestURI();

        if (!isJwtAuthRequired(uri, method)) {
            return true;
        }

        var token = request.getHeader("Token");
        if (!Strings.hasText(token)) {
            log.error("Token has not being provided.");
            throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
        }

        try {
            var jwt = (DefaultJws) jwtParser.parse(token);
            var payloadClaims = (Map<String, String>) jwt.getPayload();
            var issuer = payloadClaims.get("iss");
            var subject = payloadClaims.get("sub");
            var role = payloadClaims.get("role");
            var tokenPayload = new JwtTokenPayload(issuer, subject, role, method, uri);
            authenticateUser(tokenPayload);

            return true;
        } catch (JwtException e) {
            log.error("Failure to validate the token: ", e);
            throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
        }
    }

    private boolean isJwtAuthRequired(String uri, String method) {
        if (uri.equals("/dm111/promotions")) {
            if (method.equals(HttpMethod.POST.name())) {
                return false;
            }
        }

        if (uri.startsWith("/dm111/promotions")) {
            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Post Handle logic, if necessary.");
    }

    private void authenticateUser(JwtTokenPayload tokenPayload) throws ApiExceptionModel {
        if (!tokenPayload.issuer().equals(tokenIssuer)) {
            throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
        }

        try {
            var user = repository.findByEmail(tokenPayload.subject())
                    .orElseThrow(() -> new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS));

            if (!user.getRole().equals(tokenPayload.role())) {
                throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
            }

            if (tokenPayload.uri().startsWith("/dm111/promotions")) {
                if (tokenPayload.method().equals(HttpMethod.POST.name()) ||
                        tokenPayload.method().equals(HttpMethod.PUT.name()) ||
                        tokenPayload.method().equals(HttpMethod.DELETE.name()))
                    if (!user.getRole().equals("ADMIN")) {
                        throw new ApiExceptionModel(AppErrorCode.PROMOTIONS_OPERATION_NOT_ALLOWED);
                    }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.INVALID_CREDENTIALS);
        }
    }
}
