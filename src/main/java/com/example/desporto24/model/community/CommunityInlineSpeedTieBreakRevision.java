package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Append-only snapshot of each jury tie-break ruling and later corrections. */
@Entity
@Table(name = "community_inline_speed_tiebreak_revision",
        uniqueConstraints = @UniqueConstraint(name = "uk_inline_speed_tiebreak_revision",
                columnNames = {"tiebreak_id", "revision_number"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedTieBreakRevision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tiebreak_id", nullable = false)
    private CommunityInlineSpeedTieBreak tieBreak;
    @Column(name = "revision_number", nullable = false)
    private Integer revisionNumber;
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
    @Column(name = "trial_order_exception_reason", length = 500)
    private String trialOrderExceptionReason;
    @Column(name = "confirmed_by", nullable = false, length = 100)
    private String confirmedBy;
    @Column(name = "confirmed_at", nullable = false)
    private Instant confirmedAt;
}
