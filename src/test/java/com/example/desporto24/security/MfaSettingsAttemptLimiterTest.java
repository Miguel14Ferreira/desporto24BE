package com.example.desporto24.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class MfaSettingsAttemptLimiterTest {

    @Test
    void sixthAttemptIsBlockedRegardlessOfCaseOrWhitespace() {
        MfaSettingsAttemptLimiter limiter = new MfaSettingsAttemptLimiter();
        for (int i = 0; i < 5; i++) {
            limiter.reserveAttempt("MiGuEl");
        }

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> limiter.reserveAttempt("  MIGUEL  "));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
    }

    @Test
    void validAuthenticatorCodeAllowsSubsequentAttemptsAfterReset() {
        MfaSettingsAttemptLimiter limiter = new MfaSettingsAttemptLimiter();
        for (int i = 0; i < 5; i++) {
            limiter.reserveAttempt("miguel");
        }
        limiter.resetAfterSuccess("miguel");
        assertDoesNotThrow(() -> limiter.reserveAttempt("MIGUEL"));
    }

    @Test
    void rateLimitIsPerAccount() {
        MfaSettingsAttemptLimiter limiter = new MfaSettingsAttemptLimiter();
        for (int i = 0; i < 5; i++) limiter.reserveAttempt("miguel");
        assertDoesNotThrow(() -> limiter.reserveAttempt("other-user"));
    }
}
