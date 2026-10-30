package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.security.DistributedAuthRateLimiter;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Duration;
import com.google.common.base.Ticker;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginAttemptServiceTest {

    private static final String USER = "fixture-user";
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final PasswordHashService hashes = mock(PasswordHashService.class);
    private final AtomicLong timeNanos = new AtomicLong();
    private final Ticker ticker = new Ticker() {
        @Override public long read() { return timeNanos.get(); }
    };
    private final LoginAttemptService guard = new LoginAttemptService(profiles, hashes, ticker);

    @Test
    void productionSharedLimiterChecksAndRecordsFailuresAndClearsOnSuccess() {
        DistributedAuthRateLimiter distributed = mock(DistributedAuthRateLimiter.class);
        ReflectionTestUtils.setField(guard, "distributedAttempts", distributed);
        Profile profile = activeProfile();
        when(hashes.matchesAndUpgrade(profile, "bad")).thenReturn(false);
        when(hashes.matchesAndUpgrade(profile, "good")).thenReturn(true);

        guard.requireAttemptAllowed("  fixture-USER ");
        assertThrows(BadCredentialsException.class,
                () -> guard.handleLoginAttempt(USER, "bad", profile));
        guard.handleLoginAttempt(USER, "good", profile);

        verify(distributed, atLeastOnce()).requireAllowed("login", USER, 5);
        verify(distributed).reserveAttempt("login", USER, 5, Duration.ofMinutes(15));
        verify(distributed).clearAfterSuccess("login", USER);
    }

    @Test
    void sharedLimitRejectionHappensBeforePasswordVerification() {
        DistributedAuthRateLimiter distributed = mock(DistributedAuthRateLimiter.class);
        ReflectionTestUtils.setField(guard, "distributedAttempts", distributed);
        doThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS))
                .when(distributed).requireAllowed("login", USER, 5);

        assertThrows(ResponseStatusException.class,
                () -> guard.handleLoginAttempt(USER, "any", activeProfile()));
        verifyNoInteractions(hashes);
    }

    private Profile activeProfile() {
        Profile profile = new Profile();
        profile.setUsername(USER);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        return profile;
    }

    @Test
    void fiveFailuresCauseTemporaryThrottleNotPermanentAccountLock() {
        Profile profile = activeProfile();
        when(hashes.matchesAndUpgrade(profile, "bad")).thenReturn(false);
        for (int i = 1; i <= 5; i++) {
            assertThrows(BadCredentialsException.class,
                    () -> guard.handleLoginAttempt(USER, "bad", profile));
            assertEquals(i, profile.getLoginAttempts());
        }
        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> guard.handleLoginAttempt(USER, "bad", profile));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        assertTrue(profile.getNotLocked());
        verify(profiles, times(5)).save(profile);

        timeNanos.addAndGet(TimeUnit.MINUTES.toNanos(16));
        when(hashes.matchesAndUpgrade(profile, "good")).thenReturn(true);
        assertDoesNotThrow(() -> guard.handleLoginAttempt(USER, "good", profile));
        assertTrue(profile.getNotLocked());
    }

    @Test
    void unknownUsernameGetsSameBoundedWindowCaseInsensitively() {
        for (int i = 0; i < 5; i++) {
            guard.requireAttemptAllowed("  MiSsInG  ");
            guard.recordMissingAccountAttempt("missing");
        }
        assertEquals(HttpStatus.TOO_MANY_REQUESTS,
                assertThrows(ResponseStatusException.class,
                        () -> guard.requireAttemptAllowed("MISSING")).getStatusCode());
        timeNanos.addAndGet(TimeUnit.MINUTES.toNanos(16));
        assertDoesNotThrow(() -> guard.requireAttemptAllowed("missing"));
        verifyNoInteractions(profiles, hashes);
    }

    @Test
    void correctPasswordClearsPreviousFailures() {
        Profile profile = activeProfile();
        when(hashes.matchesAndUpgrade(profile, "bad")).thenReturn(false);
        when(hashes.matchesAndUpgrade(profile, "good")).thenReturn(true);

        for (int i = 0; i < 4; i++) {
            assertThrows(BadCredentialsException.class,
                    () -> guard.handleLoginAttempt(USER, "bad", profile));
        }
        guard.handleLoginAttempt(USER, "good", profile);
        assertDoesNotThrow(() -> guard.requireAttemptAllowed(USER));

        assertThrows(BadCredentialsException.class,
                () -> guard.handleLoginAttempt(USER, "bad", profile));
        assertDoesNotThrow(() -> guard.requireAttemptAllowed(USER));
        assertTrue(profile.getNotLocked());
    }
}
