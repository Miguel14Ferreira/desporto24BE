package com.example.desporto24.model.session;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/** A reusable session configuration owned exclusively by its creator. No kick-off time or password. */
@Entity
@Table(name = "session_preset", indexes = @Index(name = "ix_session_preset_owner", columnList = "owner_profile_id"))
@Getter
@Setter
@NoArgsConstructor
public class SessionPreset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_profile_id", nullable = false)
    private Profile owner;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(name = "settings_json", nullable = false, columnDefinition = "TEXT")
    private String settingsJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }
}
