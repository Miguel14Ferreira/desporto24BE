package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Immutable official record of an additional trial actually held; original heats stay untouched. */
@Entity
@Table(name = "community_inline_speed_tie_trial")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedTieTrial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;
    @Column(name = "source_stage", nullable = false, length = 12)
    private String sourceStage;
    @Column(name = "source_fingerprint", nullable = false, length = 64)
    private String sourceFingerprint;
    @Column(name = "official_reference", nullable = false, length = 500)
    private String officialReference;
    @Column(name = "judge_reason", nullable = false, length = 500)
    private String judgeReason;
    @Column(name = "held_at", nullable = false)
    private Instant heldAt;
    @Column(name = "recorded_by", nullable = false, length = 100)
    private String recordedBy;
    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
