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

    @Column(nullable = false, length = 100)
    private String name;

    // Historical rows remain null until their discipline is explicitly known.
    @Column(name = "sport", length = 100)
    private String sport;

    // NULL for historical rounds; differentiates race and freestyle/field events.
    @Column(name = "event_type", length = 20)
    private String eventType;

    // NULL for historical rounds. Explicitly identifies the athletics field discipline.
    @Column(name = "event_discipline", length = 32)
    private String eventDiscipline;

    // One final result per group, not repeated across each athlete.
    // JSON only for new group routines; nullable and untouched on legacy rounds.
    @Column(name = "group_scores_json", columnDefinition = "text")
    private String groupScoresJson;

    // NULL for legacy rounds and for disciplines without structured set scoring.
    @Column(name = "match_format", length = 12)
    private String matchFormat;

    @Column(name = "set_scores", length = 255)
    private String setScores;

    @Column(name = "played_at", nullable = false)
    private Instant playedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
