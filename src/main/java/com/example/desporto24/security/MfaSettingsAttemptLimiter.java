package com.example.desporto24.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Bounded per-account MFA settings attempts. A stolen access token must not
 * permit unlimited guesses at an active authenticator's 6-digit TOTP.
 *
 * This is per JVM: distributed/edge rate limiting remains a separate task.
 */
@Component
public class MfaSettingsAttemptLimiter {
    private static final int MAX_ATTEMPTS = 5;
    private static final int WINDOW_MINUTES = 15;

    private final Cache<String, Integer> attempts;

    @Autowired(required = false)
    private DistributedAuthRateLimiter distributedAttempts;

    public MfaSettingsAttemptLimiter() {
        this(CacheBuilder.newBuilder()
                .expireAfterWrite(WINDOW_MINUTES, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build());
    }

    MfaSettingsAttemptLimiter(Cache<String, Integer> attempts) {
        this.attempts = attempts;
    }

    public void reserveAttempt(String username) {
        if (distributedAttempts != null) {
            distributedAttempts.reserveAttempt("mfa-settings", username,
                    MAX_ATTEMPTS, Duration.ofMinutes(WINDOW_MINUTES));
        }
        String key = normalize(username);
        // Reserve before TOTP verification, with atomic increments even under
        // concurrent requests from multiple tabs.
        int count = attempts.asMap().merge(key, 1,
                (previous, increment) -> Math.min(MAX_ATTEMPTS + 1, previous + increment));
        if (count > MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiadas tentativas MFA. Tenta novamente mais tarde.");
        }
    }

    public void resetAfterSuccess(String username) {
        attempts.invalidate(normalize(username));
        if (distributedAttempts != null) {
            distributedAttempts.clearAfterSuccess("mfa-settings", username);
        }
    }

    private String normalize(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }
}
