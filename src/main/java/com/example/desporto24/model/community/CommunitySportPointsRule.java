package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_sport_points_rule",
       uniqueConstraints = @UniqueConstraint(name = "uq_community_sport_points_rule_season_sport",
               columnNames = {"season_id", "sport"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySportPointsRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private CommunitySeason season;

    @Column(nullable = false, length = 100)
    private String sport;

    @Column(name = "win_points", nullable = false)
    private int winPoints;

    @Column(name = "draw_points", nullable = false)
    private int drawPoints;

    @Column(name = "loss_points", nullable = false)
    private int lossPoints;
}
