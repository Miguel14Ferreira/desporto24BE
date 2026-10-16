package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;
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

    @Column(name = "champion_points")
    private Long championPoints;

    @Column(name = "champion_tied", nullable = false)
    @Builder.Default
    private boolean championTied = false;

    @Version
    @Column(nullable = false)
    private long version;
}
