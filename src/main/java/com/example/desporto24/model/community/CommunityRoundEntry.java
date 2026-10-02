package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "community_round_entry",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_community_round_entry_player",
                columnNames = {"round_id", "community_player_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRoundEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private CommunityRound round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_player_id", nullable = false)
    private CommunityPlayer communityPlayer;

    @Column(length = 100)
    private String team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CommunityMatchResult result;

    @Column(nullable = false)
    private int goals;

    @Column(nullable = false)
    private int assists;

    @Column(nullable = false)
    private boolean mvp;

    @Column(name = "clean_sheet", nullable = false)
    private boolean cleanSheet;
}
