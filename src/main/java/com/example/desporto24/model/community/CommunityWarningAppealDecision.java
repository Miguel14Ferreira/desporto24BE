package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** Permanent, single independent-review decision on a player's warning appeal. */
@Entity
@Table(name = "community_warning_appeal_decision", schema = "desporto24",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_community_warning_appeal_decision_appeal", columnNames = "appeal_id"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityWarningAppealDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appeal_id", nullable = false, updatable = false)
    private Long appealId;

    @Column(name = "community_id", nullable = false, updatable = false)
    private Long communityId;

    @Column(name = "reviewed_by_profile_id", nullable = false, updatable = false)
    private Long reviewedByProfileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", nullable = false, updatable = false, length = 16)
    private Outcome outcome;

    @Column(name = "reason", nullable = false, updatable = false, length = 1000)
    private String reason;

    @Column(name = "decided_at", nullable = false, updatable = false)
    private Instant decidedAt;

    public enum Outcome { UPHELD, OVERTURNED }
}
