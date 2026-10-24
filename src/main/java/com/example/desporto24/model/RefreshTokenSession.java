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

    protected RefreshTokenSession() {}

    public RefreshTokenSession(String tokenHash, Long profileId, Instant expiresAt, Instant consumedAt) {
        this.tokenHash = tokenHash;
        this.profileId = profileId;
        this.expiresAt = expiresAt;
        this.consumedAt = consumedAt;
    }

    public String getTokenHash() { return tokenHash; }
    public Long getProfileId() { return profileId; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getConsumedAt() { return consumedAt; }
    public void setConsumedAt(Instant consumedAt) { this.consumedAt = consumedAt; }
}
