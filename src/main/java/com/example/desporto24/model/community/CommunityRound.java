package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_round")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private CommunitySeason season;

    /** The source session can contribute to standings at most once. */
    @Column(name = "source_session_id", unique = true)
    private Long sourceSessionId;

    @Column(nullable = false, length = 100)
    private String name;

    // Historical rows remain null until their discipline is explicitly known.
    @Column(name = "sport", length = 100)
    private String sport;

    // NULL for historical rounds; differentiates race and freestyle/field events.
    @Column(name = "event_type", length = 64)
    private String eventType;

    // NULL for historical rounds. Explicitly identifies the athletics field discipline.
    @Column(name = "event_discipline", length = 32)
    private String eventDiscipline;

    // One final result per group, not repeated across each athlete.
    // JSON only for new group routines; nullable and untouched on legacy rounds.
    @Column(name = "group_scores_json", columnDefinition = "text")
    private String groupScoresJson;

    // One final basketball score per side; never repeated as player goals.
    // Nullable: old community rounds are preserved without reinterpretation.
    @Column(name = "basketball_points_a")
    private Integer basketballPointsA;

    @Column(name = "basketball_points_b")
    private Integer basketballPointsB;

    // Organizer-confirmed team total. Separate from basketball and individual football metrics.
    @Column(name = "team_score_a")
    private Integer teamScoreA;

    @Column(name = "team_score_b")
    private Integer teamScoreB;

    // NULL for old rounds and all non-cricket disciplines. Team scores store runs.
    @Column(name = "cricket_wickets_a")
    private Integer cricketWicketsA;

    @Column(name = "cricket_balls_a")
    private Integer cricketBallsA;

    @Column(name = "cricket_wickets_b")
    private Integer cricketWicketsB;

    @Column(name = "cricket_balls_b")
    private Integer cricketBallsB;

    // NULL for legacy rounds and for disciplines without structured set scoring.
    @Column(name = "match_format", length = 12)
    private String matchFormat;

    @Column(name = "set_scores", length = 255)
    private String setScores;

    // Optional explicit grouping. Never inferred from historic names or riders.
    @Column(name = "sprint_series_key", length = 40)
    private String sprintSeriesKey;

    @Column(name = "sprint_heat_number")
    private Integer sprintHeatNumber;

    @Column(name = "played_at", nullable = false)
    private Instant playedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
