package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_standings_archive_row",
        uniqueConstraints = @UniqueConstraint(name = "uk_archive_row_name",
                columnNames = {"archive_id", "normalized_name"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityStandingsArchiveRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private CommunityStandingsArchive archive;

    @Column(nullable = false)
    private int position;

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "normalized_name", nullable = false, length = 120)
    private String normalizedName;

    private Integer played;
    private Integer wins;
    private Integer draws;
    private Integer losses;

    @Column(nullable = false)
    private Integer points;

    @Column(name = "goals_for")
    private Integer goalsFor;

    @Column(name = "goals_against")
    private Integer goalsAgainst;
}
