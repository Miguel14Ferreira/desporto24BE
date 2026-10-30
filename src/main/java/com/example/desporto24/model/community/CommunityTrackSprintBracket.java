package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

/** Fixed eight-rider Sprint finals draw, explicitly created within one season.
 * The existing Sprint heat records remain the only source of athletic results.
 */
@Entity
@Table(name = "community_track_sprint_bracket")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTrackSprintBracket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private CommunitySeason season;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "format", nullable = false, length = 30)
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualifying_round_id")
    private CommunityRound qualifyingRound;

    // Snapshot the exact official 200m seed order on creation.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "community_track_sprint_qualified_rider",
            joinColumns = @JoinColumn(name = "bracket_id"))
    @OrderColumn(name = "qualifying_seed")
    @Column(name = "username", nullable = false, length = 100)
    private List<String> qualifiedRiders;

    // Immutable official UCI 3.2.027 source decision recorded at creation.
    // Nullable for the entire historical catalogue and unambiguous 200m times.
    @Column(name = "tie_decisions_json", columnDefinition = "TEXT")
    private String tieDecisionsJson;

    @Column(name = "tie_decisions_recorded_by", length = 100)
    private String tieDecisionsRecordedBy;

    // Official, immutable end-of-event report. Separate from sporting rounds.
    @Column(name = "interruption_stage", length = 36)
    private String interruptionStage;

    @Column(name = "interruption_reference", length = 255)
    private String interruptionReference;

    @Column(name = "interruption_reason", length = 500)
    private String interruptionReason;

    @Column(name = "interruption_recorded_by", length = 100)
    private String interruptionRecordedBy;

    @Column(name = "interruption_recorded_at")
    private Instant interruptionRecordedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
