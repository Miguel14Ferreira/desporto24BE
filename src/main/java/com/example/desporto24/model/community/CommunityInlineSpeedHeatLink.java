package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_inline_speed_heat",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_inline_speed_round", columnNames = "round_id"),
                @UniqueConstraint(name = "uk_inline_speed_stage_heat",
                        columnNames = {"tournament_id", "stage", "heat_number"})
        })
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedHeatLink {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_id", nullable = false)
    private CommunityRound round;
    @Column(name = "stage", nullable = false, length = 12)
    private String stage;
    @Column(name = "heat_number", nullable = false)
    private int heatNumber;
}
