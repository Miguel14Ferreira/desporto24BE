package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

/**
 * Immutable attribution of one actually held additional trial to the exact
 * jury ruling revision that cited it. Never changes original round results.
 */
@Entity
@Table(name = "community_inline_speed_trial_usage",
        uniqueConstraints = @UniqueConstraint(name = "uk_inline_speed_trial_revision_usage",
                columnNames = {"revision_id", "trial_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityInlineSpeedTrialUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "revision_id", nullable = false)
    private CommunityInlineSpeedTieBreakRevision revision;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trial_id", nullable = false)
    private CommunityInlineSpeedTieTrial trial;
}
