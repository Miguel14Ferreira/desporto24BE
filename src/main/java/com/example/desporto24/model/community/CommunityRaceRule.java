package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_race_rule",
       uniqueConstraints = @UniqueConstraint(name = "uq_community_race_rule_season_sport",
               columnNames = {"season_id", "sport"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityRaceRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private CommunitySeason season;

    @Column(nullable = false, length = 100)
    private String sport;

    // One descending, comma-separated integer point value per finish position (max 20).
    @Column(name = "points_by_place", nullable = false, length = 160)
    private String pointsByPlace;
}
