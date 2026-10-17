package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.TournamentMatchStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;

@Entity
@Table(name = "community_tournament_team_match")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTournamentTeamMatch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CommunityTournament tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private CommunityTournamentTeam homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private CommunityTournamentTeam awayTeam;

    @Column(name = "round_number", nullable = false)
    private int roundNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    @Builder.Default
    private TournamentMatchStatus status = TournamentMatchStatus.SCHEDULED;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "completed_at")
    private Instant completedAt;
}
