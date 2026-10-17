package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_standings_archive")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityStandingsArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private CommunitySeason season;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "entry_type", nullable = false, length = 16)
    private String entryType;

    @Column(name = "source_filename", nullable = false, length = 255)
    private String sourceFilename;

    @Column(name = "created_by_username", nullable = false, length = 100)
    private String createdByUsername;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
