package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Organizer-confirmed link from one completed heat to another.
 * There is no automatic World Skate ratification or derived qualification.
 */
@Entity
@Table(name = "community_inline_heat_progression",
       uniqueConstraints = @UniqueConstraint(name = "uk_inline_heat_progression_pair",
               columnNames = {"source_round_id", "target_round_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineHeatProgression {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_round_id", nullable = false)
    private CommunityRound sourceRound;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_round_id", nullable = false)
    private CommunityRound targetRound;

    @Column(name = "confirmed_by_username", length = 100, nullable = false)
    private String confirmedByUsername;

    @Column(name = "confirmation_reason", length = 255, nullable = false)
    private String confirmationReason;

    @Column(name = "confirmed_at", nullable = false)
    private Instant confirmedAt;
}
