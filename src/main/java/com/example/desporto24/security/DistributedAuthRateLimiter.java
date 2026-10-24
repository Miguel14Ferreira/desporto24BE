package com.example.desporto24.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

/**
 * Shared, database-backed security attempt counter. Does not trust X-Forwarded-For
 * or store a raw username, email, IP address, challenge or bearer token.
 *
 * PostgreSQL ON CONFLICT makes reservations atomic across Render JVM replicas;
 * REQUIRES_NEW commits counts even when a rejected authentication fails in
 * the caller's transaction. Database errors fail closed, not open.
 */
@Component
public class DistributedAuthRateLimiter {
    private static final String BLOCKED = "Demasiadas tentativas. Tenta novamente mais tarde.";
    private final JdbcTemplate jdbc;
    private final byte[] hmacKey;

    public DistributedAuthRateLimiter(JdbcTemplate jdbc,
            @Value("${desporto.app.jwtSecret}") String secret) {
        this.jdbc = jdbc;
        this.hmacKey = secret.getBytes(StandardCharsets.UTF_8);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void requireAllowed(String action, String subject, int maximum) {
        String key = fingerprint(action, subject);
        List<Integer> attempts = jdbc.query(
                "select attempt_count from auth_rate_limit_bucket "
                    + "where bucket_key = ? and expires_at > clock_timestamp()",
                (rs, row) -> rs.getInt(1), key);
        if (!attempts.isEmpty() && attempts.getFirst() >= maximum) {
            throw blocked();
        }
    }

    /**
     * Reserve before testing an MFA code; count failures after testing a
     * password. A 6th attempt in a five-attempt window is rejected immediately.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int reserveAttempt(String action, String subject,
                              int maximum, Duration window) {
        if (maximum < 1 || window == null || window.toSeconds() < 1) {
            throw new IllegalArgumentException("Invalid security rate limit");
        }
        String key = fingerprint(action, subject);
        int seconds = Math.toIntExact(window.toSeconds());
        Integer count = jdbc.queryForObject("""
                insert into auth_rate_limit_bucket(bucket_key, attempt_count, expires_at)
                values (?, 1, clock_timestamp() + (? * interval '1 second'))
                on conflict (bucket_key) do update
                  set attempt_count = case
                        when auth_rate_limit_bucket.expires_at <= clock_timestamp()
                        then 1
                        else least(auth_rate_limit_bucket.attempt_count + 1, ?)
                      end,
                      expires_at = case
                        when auth_rate_limit_bucket.expires_at <= clock_timestamp()
                        then clock_timestamp() + (? * interval '1 second')
                        else auth_rate_limit_bucket.expires_at
                      end
                returning attempt_count
                """, Integer.class, key, seconds, maximum + 1, seconds);
        if (count == null) throw new IllegalStateException("Missing rate-limit result");
        if (count > maximum) throw blocked();
        return count;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clearAfterSuccess(String action, String subject) {
        jdbc.update("delete from auth_rate_limit_bucket where bucket_key = ?",
                fingerprint(action, subject));
    }

    @Scheduled(cron = "0 20 4 * * *", zone = "UTC")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteExpiredBuckets() {
        jdbc.update("delete from auth_rate_limit_bucket "
                + "where expires_at < clock_timestamp() - interval '1 day'");
    }

    private String fingerprint(String action, String subject) {
        String normalized = subject == null ? "" : subject.trim().toLowerCase(Locale.ROOT);
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
            return HexFormat.of().formatHex(
                    mac.doFinal((action + "\u0000" + normalized).getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.GeneralSecurityException failure) {
            throw new IllegalStateException("Authentication rate-limit hashing unavailable", failure);
        }
    }

    private ResponseStatusException blocked() {
        return new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, BLOCKED);
    }
}
