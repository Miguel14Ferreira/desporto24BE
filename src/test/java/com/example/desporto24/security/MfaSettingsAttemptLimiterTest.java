package com.example.desporto24.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Duration;
import static org.mockito.Mockito.*;
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
    void settingsThrottleSharesGuessesBetweenApiInstances() {
        DistributedAuthRateLimiter shared = mock(DistributedAuthRateLimiter.class);
        MfaSettingsAttemptLimiter one = new MfaSettingsAttemptLimiter();
        MfaSettingsAttemptLimiter two = new MfaSettingsAttemptLimiter();
        ReflectionTestUtils.setField(one, "distributedAttempts", shared);
        ReflectionTestUtils.setField(two, "distributedAttempts", shared);

        one.reserveAttempt("MIGUEL");
        two.reserveAttempt("miguel");
        verify(shared).reserveAttempt("mfa-settings", "MIGUEL", 5, Duration.ofMinutes(15));
        verify(shared).reserveAttempt("mfa-settings", "miguel", 5, Duration.ofMinutes(15));
        two.resetAfterSuccess("miguel");
        verify(shared).clearAfterSuccess("mfa-settings", "miguel");
    }

    @Test
    void rateLimitIsPerAccount() {
        MfaSettingsAttemptLimiter limiter = new MfaSettingsAttemptLimiter();
        for (int i = 0; i < 5; i++) limiter.reserveAttempt("miguel");
        assertDoesNotThrow(() -> limiter.reserveAttempt("other-user"));
    }
}
