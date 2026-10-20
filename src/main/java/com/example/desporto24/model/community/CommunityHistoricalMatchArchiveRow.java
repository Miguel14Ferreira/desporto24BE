package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_match_archive_row",
        uniqueConstraints = @UniqueConstraint(name = "uk_match_archive_source_row",
                columnNames = {"archive_id", "source_row"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityHistoricalMatchArchiveRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private CommunityHistoricalMatchArchive archive;

    @Column(name = "source_row", nullable = false)
    private int sourceRow;

    @Column(name = "home_name", nullable = false, length = 120)
    private String home;

    @Column(name = "away_name", nullable = false, length = 120)
    private String away;

    @Column(name = "home_score", nullable = false)
    private int homeScore;

    @Column(name = "away_score", nullable = false)
    private int awayScore;

    @Column(name = "source_date", nullable = false, length = 80)
    private String sourceDate;

    @Column(name = "source_round", nullable = false, length = 80)
    private String sourceRound;

    @Column(name = "potential_duplicate", nullable = false)
    private boolean potentialDuplicate;
}
