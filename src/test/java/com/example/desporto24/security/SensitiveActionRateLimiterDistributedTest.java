package com.example.desporto24.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensitiveActionRateLimiterDistributedTest {
    @Test
    void recoveryEmailsEnforceSharedCooldownAcrossInstances() {
        var shared = mock(DistributedAuthRateLimiter.class);
        var apiOne = new SensitiveActionRateLimiter();
        var apiTwo = new SensitiveActionRateLimiter();
        ReflectionTestUtils.setField(apiOne, "distributedAttempts", shared);
        ReflectionTestUtils.setField(apiTwo, "distributedAttempts", shared);

        apiOne.requireCooldown("password-reset", "USER@example.test", Duration.ofSeconds(60));
        apiTwo.requireCooldown("password-reset", "user@example.test", Duration.ofSeconds(60));

        // Both independent JVM-like guards reach the shared store.
        verify(shared, times(2)).reserveAttempt("cooldown:password-reset",
                "user@example.test", 1, Duration.ofSeconds(60));
    }

    @Test
    void deniedSharedCooldownDoesNotReachLocalUpdate() {
        var shared = mock(DistributedAuthRateLimiter.class);
        var guard = new SensitiveActionRateLimiter();
        ReflectionTestUtils.setField(guard, "distributedAttempts", shared);
        doThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS))
                .when(shared).reserveAttempt("cooldown:registration-confirmation",
                        "alice", 1, Duration.ofSeconds(60));

        assertThrows(ResponseStatusException.class, () ->
                guard.requireCooldown("registration-confirmation",
                        "alice", Duration.ofSeconds(60)));
    }
}
