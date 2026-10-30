package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_tournament_race_round",
        uniqueConstraints = @UniqueConstraint(name = "uk_tournament_race_round",
                columnNames = {"tournament_id", "round_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTournamentRaceRound {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private CommunityRound round;
}
