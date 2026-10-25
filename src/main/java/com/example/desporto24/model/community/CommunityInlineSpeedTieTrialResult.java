package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/** One actual participant placing; optional time is never copied into the original heat. */
@Entity
@Table(name = "community_inline_speed_tie_trial_result",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_inline_speed_trial_player",
                    columnNames = {"trial_id", "player_id"}),
            @UniqueConstraint(name = "uk_inline_speed_trial_place",
                    columnNames = {"trial_id", "place"})
        })
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedTieTrialResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trial_id", nullable = false)
    private CommunityInlineSpeedTieTrial trial;
    @Column(name = "player_id", nullable = false)
    private Long playerId;
    @Column(name = "place", nullable = false)
    private Integer place;
    @Column(name = "seconds", precision = 9, scale = 3)
    private BigDecimal seconds;
}
