package com.example.desporto24.service;

import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A presence signal, not a login-based guess. One row per profile avoids counting
 * multiple tabs as distinct people. Stale rows are excluded by the time cutoff.
 */
@Service
@RequiredArgsConstructor
public class OnlinePresenceService {
    public static final int WINDOW_SECONDS = 150;
    private final ProfileRepository profiles;
    private final JdbcTemplate jdbc;

    @Transactional
    public void heartbeat(String username) {
        var user = profiles.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!Boolean.TRUE.equals(user.getEnabled()) || !Boolean.TRUE.equals(user.getNotLocked())
                || (user.getEmail() != null
                    && user.getEmail().toLowerCase(java.util.Locale.ROOT).endsWith("@deleted.invalid"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        jdbc.update("""
                INSERT INTO desporto24.profile_presence (profile_id, last_seen_at)
                VALUES (?, ?)
                ON CONFLICT (profile_id) DO UPDATE
                SET last_seen_at = GREATEST(
                    desporto24.profile_presence.last_seen_at, EXCLUDED.last_seen_at)
                """, user.getId(), Timestamp.from(Instant.now()));
    }

    @Transactional(readOnly = true)
    public long countOnlineAt(Instant now) {
        Long result = jdbc.queryForObject("""
                SELECT COUNT(*) FROM desporto24.profile_presence
                WHERE last_seen_at >= ?
                """, Long.class, Timestamp.from(now.minus(WINDOW_SECONDS, ChronoUnit.SECONDS)));
        return result == null ? 0L : result;
    }
}
