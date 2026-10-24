package com.example.desporto24.security;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensitiveActionRateLimiter {

    private final ConcurrentHashMap<String, Instant> lastAllowed = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private DistributedAuthRateLimiter distributedAttempts;

    public void requireCooldown(String action, String subject, Duration cooldown) {
        String normalizedSubject = subject == null
                ? ""
                : subject.trim().toLowerCase(Locale.ROOT);
        // The common database counter enforces cooldown after process restarts
        // and across replicas. No untrusted proxy/IP headers are involved.
        if (distributedAttempts != null) {
            distributedAttempts.reserveAttempt("cooldown:" + action,
                    normalizedSubject, 1, cooldown);
        }
        String key = action + ":" + normalizedSubject;
        Instant now = Instant.now();

        lastAllowed.compute(key, (ignored, previous) -> {
            if (previous != null) {
                Instant nextAllowed = previous.plus(cooldown);
                if (nextAllowed.isAfter(now)) {
                    long retryAfter = Math.max(1, Duration.between(now, nextAllowed).toSeconds());
                    throw new ResponseStatusException(
                            HttpStatus.TOO_MANY_REQUESTS,
                            "Tenta novamente dentro de " + retryAfter + " segundos"
                    );
                }
            }
            return now;
        });

        if (lastAllowed.size() > 10_000) {
            Instant staleBefore = now.minus(Duration.ofHours(1));
            lastAllowed.entrySet().removeIf(entry -> entry.getValue().isBefore(staleBefore));
        }
    }
}
