package com.example.desporto24.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class RefreshOriginValidatorTest {

    private final RefreshOriginValidator guard = new RefreshOriginValidator(
            "https://d24sports.com,https://www.d24sports.com,http://localhost:4200"
    );

    @Test
    void acceptsOnlyExactConfiguredOrigins() {
        assertDoesNotThrow(() -> guard.requireAllowedOrigin("https://d24sports.com"));
        assertDoesNotThrow(() -> guard.requireAllowedOrigin("https://www.d24sports.com"));
        assertDoesNotThrow(() -> guard.requireAllowedOrigin("http://localhost:4200"));
    }

    @Test
    void rejectsMissingAndCrossSiteOrigins() {
        for (String origin : new String[] {
                "null", "https://evil.example", "https://d24sports.com.evil.example",
                "http://d24sports.com", "https://d24sports.com/",
                "https://d24sports.com:8443", "https://api.d24sports.com"
        }) {
            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class, () -> guard.requireAllowedOrigin(origin), origin
            );
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        }
        assertEquals(HttpStatus.FORBIDDEN,
                assertThrows(ResponseStatusException.class, () -> guard.requireAllowedOrigin(null))
                        .getStatusCode());
    }

    @Test
    void wildcardAndNullNeverBecomeTrustedOrigins() {
        RefreshOriginValidator misconfigured = new RefreshOriginValidator("*,null, https://d24sports.com/");
        assertThrows(ResponseStatusException.class, () -> misconfigured.requireAllowedOrigin("*"));
        assertThrows(ResponseStatusException.class, () -> misconfigured.requireAllowedOrigin("null"));
        assertDoesNotThrow(() -> misconfigured.requireAllowedOrigin("https://d24sports.com"));
    }
}
