package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Officially confirmed eligible skaters. Never derived or updated automatically. */
@Entity
@Table(name = "community_inline_speed_decision",
       uniqueConstraints = @UniqueConstraint(name = "uk_inline_speed_decision_stage",
               columnNames = {"tournament_id", "source_stage"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedDecision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;
    @Column(name = "source_stage", nullable = false, length = 12)
    private String sourceStage;
    @Column(name = "target_stage", nullable = false, length = 12)
    private String targetStage;
    @Column(name = "qualified_player_ids", nullable = false, columnDefinition = "text")
    private String qualifiedPlayerIds;
    @Column(name = "source_fingerprint", nullable = false, length = 64)
    private String sourceFingerprint;
    @Column(name = "judge_reason", length = 500)
    private String judgeReason;
    @Column(name = "confirmed_by", nullable = false, length = 100)
    private String confirmedBy;
    @Column(name = "confirmed_at", nullable = false)
    private Instant confirmedAt;
}
