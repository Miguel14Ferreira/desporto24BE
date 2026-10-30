package com.example.desporto24.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stores only a SHA-256 fingerprint of each refresh JWT. Never persist the
 * bearer value itself; the cookie remains HttpOnly/Secure.
 */
@Entity
@Table(name = "refresh_token_session")
public class RefreshTokenSession {
    @Id
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    // Each browser login creates an independent family. The family is
    // inherited across refresh rotations and is never derived from a JWT.
    @Column(name = "family_id", length = 36)
    private String familyId;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "device_label", length = 80)
    private String deviceLabel;

    @Column(name = "created_at")
    private Instant createdAt;

    protected RefreshTokenSession() {}

    public RefreshTokenSession(String tokenHash, Long profileId, Instant expiresAt, Instant consumedAt) {
        this.tokenHash = tokenHash;
        this.profileId = profileId;
        this.expiresAt = expiresAt;
        this.consumedAt = consumedAt;
    }

    public RefreshTokenSession(String tokenHash, Long profileId, Instant expiresAt,
                               Instant consumedAt, String familyId) {
        this(tokenHash, profileId, expiresAt, consumedAt);
        this.familyId = familyId;
    }

    public String getDeviceLabel() { return deviceLabel; }
    public void setDeviceLabel(String deviceLabel) { this.deviceLabel = deviceLabel; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getFamilyId() { return familyId; }
    public void setFamilyId(String familyId) { this.familyId = familyId; }
    public Instant getRevokedAt() { return revokedAt; }

    public String getTokenHash() { return tokenHash; }
    public Long getProfileId() { return profileId; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getConsumedAt() { return consumedAt; }
    public void setConsumedAt(Instant consumedAt) { this.consumedAt = consumedAt; }
}
