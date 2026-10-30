package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "community_round_entry",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_community_round_entry_player",
                columnNames = {"round_id", "community_player_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRoundEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private CommunityRound round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_player_id", nullable = false)
    private CommunityPlayer communityPlayer;

    @Column(length = 100)
    private String team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CommunityMatchResult result;

    @Column(nullable = false)
    private int goals;

    @Column(nullable = false)
    private int assists;

    @Column(nullable = false)
    private boolean mvp;

    @Column(name = "clean_sheet", nullable = false)
    private boolean cleanSheet;

    // Optional sport-specific race metrics; absent on existing historical records.
    @Column(name = "finish_position")
    private Integer finishPosition;

    @Column(name = "elapsed_seconds", precision = 11, scale = 3)
    private BigDecimal elapsedSeconds;

    @Column(name = "best_lap_seconds", precision = 11, scale = 3)
    private BigDecimal bestLapSeconds;

    @Column(name = "laps_completed")
    private Integer lapsCompleted;

    // One aggregate mark (metres for field events, 0..100 points for freestyle).
    // Never interpreted as a football score or compared across event disciplines.
    @Column(name = "performance_value", precision = 10, scale = 4)
    private BigDecimal performanceValue;

    // Two already-judged surfing wave marks (0..10 each), absent on legacy rounds.
    @Column(name = "wave_score_1", precision = 5, scale = 3)
    private BigDecimal waveScore1;

    @Column(name = "wave_score_2", precision = 5, scale = 3)
    private BigDecimal waveScore2;

    // Sports-specific penalty, kept separate from the measured finishing position.
    @Builder.Default
    @Column(name = "race_penalty_points", nullable = false)
    private int racePenaltyPoints = 0;

    @Column(name = "race_penalty_reason", length = 255)
    private String racePenaltyReason;

    @Builder.Default
    @Column(name = "race_time_penalty_seconds", nullable = false, precision = 7, scale = 3)
    private BigDecimal raceTimePenaltySeconds = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "race_disqualified", nullable = false)
    private boolean raceDisqualified = false;

    @Column(name = "race_steward_reason", length = 255)
    private String raceStewardReason;
}
