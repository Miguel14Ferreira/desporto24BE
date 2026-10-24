package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentTeamTieBreakPriority;
import com.example.desporto24.enumeration.TournamentScoringMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_tournament")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(nullable = false, length = 120)
    private String name;

    // A competition has its own sport, independent of the community sport list.
    // Null is retained for old, previously sportless tournaments.
    @Column(name = "sport", length = 100)
    private String sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false, length = 16)
    @Builder.Default
    private TournamentEntryType entryType = TournamentEntryType.INDIVIDUAL;

    // Set when the competition is created. Once fixtures are published it is immutable.
    @Builder.Default
    @Column(name = "round_robin_legs", nullable = false)
    private int roundRobinLegs = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_tiebreak_priority", nullable = false, length = 32)
    @Builder.Default
    private TournamentTeamTieBreakPriority teamTieBreakPriority =
            TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST;

    // Only configured for the GROUP_KNOCKOUT team format.
    @Column(name = "group_count")
    private Integer groupCount;

    @Column(name = "players_per_team")
    private Integer playersPerTeam;

    // Null preserves the meaning of existing competitions and their stored results.
    @Enumerated(EnumType.STRING)
    @Column(name = "scoring_mode", length = 40)
    private TournamentScoringMode scoringMode;

    // A Karting championship snapshots its own community-defined per-place points.
    @Column(name = "race_points_by_place", length = 160)
    private String racePointsByPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private TournamentFormat format = TournamentFormat.LEAGUE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private TournamentStatus status = TournamentStatus.DRAFT;

    @Column(name = "win_points", nullable = false)
    @Builder.Default
    private int winPoints = 3;

    @Column(name = "draw_points", nullable = false)
    @Builder.Default
    private int drawPoints = 1;

    @Column(name = "loss_points", nullable = false)
    @Builder.Default
    private int lossPoints = 0;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    // Immutable champion snapshot at finalization; a null ID with championTied=true
    // represents an actual tie after all sporting tie-breakers.
    @Column(name = "champion_profile_id")
    private Long championProfileId;

    @Column(name = "champion_username", length = 100)
    private String championUsername;

    @Column(name = "champion_display_name", length = 255)
    private String championDisplayName;

    @Column(name = "champion_team_id")
    private Long championTeamId;

    @Column(name = "champion_team_name", length = 100)
    private String championTeamName;

    @Column(name = "champion_points")
    private Long championPoints;

    @Column(name = "champion_tied", nullable = false)
    @Builder.Default
    private boolean championTied = false;

    @Version
    @Column(nullable = false)
    private long version;
}
