package com.example.desporto24.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Restricts cookie-authenticated refresh requests to explicitly configured SPA origins.
 * Unlike CORS alone, rejects POSTs without an Origin header (e.g. HTML form submissions).
 * This is not a general CSRF replacement for endpoints that use other cookies.
 */
@Component
public class RefreshOriginValidator {
    private final Set<String> allowedOrigins;

    public RefreshOriginValidator(@Value("${desporto.cors.allowed-origins}") String origins) {
        this.allowedOrigins = Arrays.stream(origins.split(","))
                .map(String::trim)
                .map(origin -> origin.replaceAll("/+$", ""))
                .filter(origin -> !origin.isBlank() && !"*".equals(origin) && !"null".equals(origin))
                .collect(Collectors.toUnmodifiableSet());
    }

    public void requireAllowedOrigin(String origin) {
        if (origin == null || !allowedOrigins.contains(origin)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Origem não autorizada para renovar a sessão"
            );
        }
    }
}
