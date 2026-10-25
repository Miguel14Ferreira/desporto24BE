package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Explicit jury ruling for a multiway tie at the World Skate 2026 16th-place
 * cutoff. Never overwrites the timing/placing recorded on the source rounds.
 */
@Entity
@Table(name = "community_inline_speed_tiebreak",
        uniqueConstraints = @UniqueConstraint(name = "uk_inline_speed_tiebreak_stage",
                columnNames = {"tournament_id", "source_stage"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedTieBreak {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;
    @Column(name = "source_stage", nullable = false, length = 12)
    private String sourceStage;
    @Column(name = "selected_player_ids", nullable = false, columnDefinition = "text")
    private String selectedPlayerIds;
    @Column(name = "source_fingerprint", nullable = false, length = 64)
    private String sourceFingerprint;
    @Column(name = "judge_reason", nullable = false, length = 500)
    private String judgeReason;
    @Column(name = "evidence_type", length = 32)
    private String evidenceType;
    @Column(name = "evidence_reference", length = 500)
    private String evidenceReference;
    @Column(name = "confirmed_by", nullable = false, length = 100)
    private String confirmedBy;
    @Column(name = "confirmed_at", nullable = false)
    private Instant confirmedAt;
}
