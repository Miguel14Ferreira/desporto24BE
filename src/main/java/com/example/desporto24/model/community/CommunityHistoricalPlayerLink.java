package com.example.desporto24.model.community;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * A verified identity association; original imported names and round entries are never rewritten.
 */
@Entity
@Table(name = "community_historical_player_link",
        uniqueConstraints = @UniqueConstraint(name = "uk_community_historical_player_link",
                columnNames = "community_player_id"))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityHistoricalPlayerLink {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_player_id", nullable = false)
    private CommunityPlayer player;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "proposed_by_username", nullable = false, length = 100)
    private String proposedByUsername;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;
}
