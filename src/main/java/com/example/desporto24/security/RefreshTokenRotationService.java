package com.example.desporto24.security;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.RefreshTokenSession;
import com.example.desporto24.model.dto.auth.DeviceSessionResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import com.example.desporto24.utility.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.time.Duration;

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
    // Normal double-submit/retry traffic must not trigger family-wide denial.
    // An older spent JWT is denied immediately, but only a later replay
    // signals likely theft and revokes the associated browser session family.
    private static final Duration REPLAY_GRACE = Duration.ofSeconds(15);
    private final ProfileRepository profiles;
    private final RefreshTokenSessionRepository sessions;
    private final JWTokenProvider tokens;

    @Transactional
    public String registerIssued(String username, String refreshToken) {
        Profile profile = lockAndValidate(username, refreshToken);
        String hash = fingerprint(refreshToken);
        if (sessions.existsById(hash)) throw denied();
        String family = UUID.randomUUID().toString();
        RefreshTokenSession issued = new RefreshTokenSession(
                hash, profile.getId(), tokens.getRefreshTokenExpiry(refreshToken),
                null, family);
        issued.setCreatedAt(Instant.now());
        issued.setDeviceLabel(safeDeviceLabel());
        sessions.save(issued);
        return family;
    }

    // For intentional replay rejection, commit revocation before returning 401.
    // Otherwise Spring would roll back the revocation along with the exception.
    // DB failures still roll back and do not grant tokens.
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = BadCredentialsException.class)
    public String rotate(String oldToken, String newToken, String username) {
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
        String familyId;
        String deviceLabel = safeDeviceLabel();
        Instant createdAt = now;
        if (prior == null) {
            // Pre-rollout cookies with no signed jti are migrated once. Modern
            // signed cookies absent from the registry fail closed, not guessed.
            if (tokens.getRefreshTokenId(oldToken) != null) throw denied();
            familyId = UUID.randomUUID().toString();
            sessions.save(new RefreshTokenSession(
                    oldHash, profile.getId(), tokens.getRefreshTokenExpiry(oldToken),
                    now, familyId));
        } else {
            if (!profile.getId().equals(prior.getProfileId())
                    || !prior.getExpiresAt().isAfter(now)
                    || prior.getRevokedAt() != null) {
                throw denied();
            }
            if (prior.getConsumedAt() != null) {
                // A legacy spent record predating this migration might not
                // have a family. We cannot infer it without risking revoking
                // an unrelated device's session.
                if (prior.getFamilyId() != null
                        && !prior.getConsumedAt().plus(REPLAY_GRACE).isAfter(now)) {
                    sessions.revokeFamily(prior.getFamilyId(), now);
                }
                // Always reject a replay, including those inside the short
                // benign double-submit grace period.
                throw denied();
            }
            familyId = prior.getFamilyId();
            if (prior.getDeviceLabel() != null) deviceLabel = prior.getDeviceLabel();
            if (prior.getCreatedAt() != null) createdAt = prior.getCreatedAt();
            if (familyId == null) {
                // Existing live cookies issued before the family schema
                // acquire a family on their next legitimate refresh.
                familyId = UUID.randomUUID().toString();
                prior.setFamilyId(familyId);
            }
            prior.setConsumedAt(now);
            sessions.save(prior);
        }
        String newHash = fingerprint(newToken);
        if (sessions.existsById(newHash)) throw denied();
        RefreshTokenSession successor = new RefreshTokenSession(
                newHash, profile.getId(), tokens.getRefreshTokenExpiry(newToken),
                null, familyId);
        successor.setDeviceLabel(deviceLabel);
        successor.setCreatedAt(createdAt);
        sessions.save(successor);
        return familyId;
    }

    @Transactional(readOnly = true)
    public List<DeviceSessionResponse> activeSessions(String username, String currentFamily) {
        Profile profile = profiles.findUserByUsername(username).orElseThrow(this::denied);
        return sessions.activeForProfile(profile.getId(), Instant.now()).stream()
                .map(row -> new DeviceSessionResponse(
                        row.getFamilyId(),
                        row.getDeviceLabel() == null ? "Navegador" : row.getDeviceLabel(),
                        row.getCreatedAt(),
                        row.getExpiresAt(),
                        row.getFamilyId().equals(currentFamily)))
                .toList();
    }

    @Transactional
    public void revokeDevice(String username, String familyId) {
        // Lock profile to serialize against refresh rotation on all replicas;
        // family ID is not a credential and never substitutes for authorization.
        if (familyId == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        try {
            UUID.fromString(familyId);
        } catch (IllegalArgumentException invalid) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Profile identity = profiles.findUserByUsername(username).orElseThrow(this::denied);
        Profile profile = profiles.findForRoleChange(identity.getId()).orElseThrow(this::denied);
        int updated = sessions.revokeOwnedFamily(profile.getId(), familyId, Instant.now());
        if (updated == 0) {
            // Do not disclose whether another user's family exists.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public boolean accessFamilyActive(Long profileId, String familyId) {
        if (familyId == null || familyId.isBlank()) return false;
        return sessions.existsByProfileIdAndFamilyIdAndConsumedAtIsNullAndRevokedAtIsNullAndExpiresAtAfter(
                profileId, familyId, Instant.now());
    }

    private String safeDeviceLabel() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs)) {
            return "Navegador";
        }
        String raw = attrs.getRequest().getHeader("User-Agent");
        if (raw == null) return "Navegador";
        String ua = raw.toLowerCase(Locale.ROOT);
        String os = ua.contains("android") ? "Android"
                : ua.contains("iphone") || ua.contains("ipad") ? "iOS"
                : ua.contains("windows") ? "Windows"
                : ua.contains("mac os") || ua.contains("macintosh") ? "macOS"
                : ua.contains("linux") ? "Linux" : "Dispositivo";
        String browser = ua.contains("edg/") ? "Edge"
                : ua.contains("firefox/") ? "Firefox"
                : ua.contains("chrome/") ? "Chrome"
                : ua.contains("safari/") ? "Safari" : "Navegador";
        return os + " · " + browser;
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
