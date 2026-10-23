package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_racket_rule",
       uniqueConstraints = @UniqueConstraint(name = "uq_community_racket_rule_season_sport_format",
               columnNames = {"season_id", "sport", "match_format"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRacketRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private CommunitySeason season;

    @Column(nullable = false, length = 100)
    private String sport;

    @Column(name = "match_format", nullable = false, length = 12)
    private String matchFormat;

    @Column(name = "win_points", nullable = false)
    private int winPoints;

    @Column(name = "loss_points", nullable = false)
    private int lossPoints;

    @Column(name = "tie_break", nullable = false, length = 20)
    private String tieBreak;
}
