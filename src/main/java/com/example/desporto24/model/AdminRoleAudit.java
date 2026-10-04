package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "admin_role_audit")
@Getter
@NoArgsConstructor
public class AdminRoleAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long actorId;
    @Column(nullable = false, updatable = false)
    private Long targetId;
    @Column(nullable = false, updatable = false, length = 32)
    private String previousRole;
    @Column(nullable = false, updatable = false, length = 32)
    private String newRole;
    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    public AdminRoleAudit(Long actorId, Long targetId, String previousRole, String newRole, Instant occurredAt) {
        this.actorId = actorId;
        this.targetId = targetId;
        this.previousRole = previousRole;
        this.newRole = newRole;
        this.occurredAt = occurredAt;
    }
}
