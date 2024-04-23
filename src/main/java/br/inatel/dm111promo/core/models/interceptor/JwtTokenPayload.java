package br.inatel.dm111promo.core.models.interceptor;

public record JwtTokenPayload(String issuer,
                              String subject,
                              String role,
                              String method,
                              String uri) {
}
