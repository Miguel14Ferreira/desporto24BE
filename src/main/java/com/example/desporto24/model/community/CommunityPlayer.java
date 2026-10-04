package com.example.desporto24.model.community;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "community_player",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_community_player_name",
                        columnNames = {"community_id", "normalized_name"}
                ),
                @UniqueConstraint(
                        name = "uk_community_player_profile",
                        columnNames = {"community_id", "profile_id"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "normalized_name", nullable = false, length = 120)
    private String normalizedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
