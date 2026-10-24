package com.example.desporto24.security;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.RefreshTokenSession;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import com.example.desporto24.utility.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Server-side, one-time refresh cookie registry.
 *
 * One account row is pessimistically locked throughout rotation, so even
 * concurrent requests on different Render instances cannot consume a token
 * twice. Pre-rollout cookies without a JWT ID are accepted ONCE and migrated.
 *
 * A valid signed token with a JWT ID MUST already be in the registry. This
 * makes DB errors fail closed rather than silently restoring stateless refresh.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenRotationService {

    private static final String BAD_REFRESH = "Invalid refresh session";
    private final ProfileRepository profiles;
    private final RefreshTokenSessionRepository sessions;
    private final JWTokenProvider tokens;

    @Transactional
    public void registerIssued(String username, String refreshToken) {
        Profile profile = lockAndValidate(username, refreshToken);
        String hash = fingerprint(refreshToken);
        if (sessions.existsById(hash)) throw denied();
        sessions.save(new RefreshTokenSession(
                hash, profile.getId(), tokens.getRefreshTokenExpiry(refreshToken), null));
    }

    @Transactional
    public void rotate(String oldToken, String newToken, String username) {
        // This lock serializes two requests using the same old cookie, even if
        // they hit different JVMs. It also serializes simultaneous new logins.
        Profile profile = lockAndValidate(username, oldToken);
        if (!tokens.isRefreshTokenValid(username, newToken)
                || tokens.getTokenVersion(newToken) != profile.getTokenVersion()
                || !Objects.equals(username, tokens.getSubject(newToken))
                || tokens.getRefreshTokenId(newToken) == null) {
            throw denied();
        }
        Instant now = Instant.now();
        String oldHash = fingerprint(oldToken);
        RefreshTokenSession prior = sessions.findById(oldHash).orElse(null);
        if (prior == null) {
            // A previously-issued cookie (no jti) has no persisted record.
            // Migration is one-time and the spent hash is now recorded.
            if (tokens.getRefreshTokenId(oldToken) != null) throw denied();
            sessions.save(new RefreshTokenSession(
                    oldHash, profile.getId(), tokens.getRefreshTokenExpiry(oldToken), now));
        } else {
            if (!profile.getId().equals(prior.getProfileId())
                    || prior.getConsumedAt() != null || !prior.getExpiresAt().isAfter(now)) {
                throw denied();
            }
            prior.setConsumedAt(now);
            sessions.save(prior);
        }
        String newHash = fingerprint(newToken);
        if (sessions.existsById(newHash)) throw denied();
        sessions.save(new RefreshTokenSession(
                newHash, profile.getId(), tokens.getRefreshTokenExpiry(newToken), null));
    }

    // Scheduled purge bounds storage to refresh TTL while retaining expired
    // hash records long enough for replay detection during the token's lifetime.
    @Scheduled(cron = "0 0 4 * * *", zone = "UTC")
    @Transactional
    public void deleteExpiredSessions() {
        sessions.deleteByExpiresAtBefore(Instant.now().minusSeconds(86400));
    }

    private Profile lockAndValidate(String username, String token) {
        if (username == null || username.isBlank()
                || token == null || token.isBlank()
                || !tokens.isRefreshTokenValid(username, token)
                || !username.equals(tokens.getSubject(token))) {
            throw denied();
        }
        Profile identity = profiles.findUserByUsername(username).orElseThrow(this::denied);
        if (identity.getId() == null) throw denied();
        Profile profile = profiles.findForRoleChange(identity.getId()).orElseThrow(this::denied);
        if (!username.equals(profile.getUsername())
                || !Boolean.TRUE.equals(profile.getEnabled())
                || !Boolean.TRUE.equals(profile.getNotLocked())
                || profile.getModerationSuspendedAt() != null
                || profile.getTokenVersion() != tokens.getTokenVersion(token)) {
            throw denied();
        }
        return profile;
    }

    private String fingerprint(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 missing", impossible);
        }
    }

    private BadCredentialsException denied() {
        return new BadCredentialsException(BAD_REFRESH);
    }
}
