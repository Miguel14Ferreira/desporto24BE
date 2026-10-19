package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "admin_player_moderation_audit")
@Getter
@NoArgsConstructor
public class AdminPlayerModerationAudit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long actorId;
    @Column(nullable = false, updatable = false)
    private Long targetId;
    @Column(nullable = false, updatable = false, length = 16)
    private String action;
    @Column(nullable = false, updatable = false, length = 500)
    private String reason;
    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    public AdminPlayerModerationAudit(Long actorId, Long targetId, String action, String reason, Instant occurredAt) {
        this.actorId = actorId;
        this.targetId = targetId;
        this.action = action;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }
}
