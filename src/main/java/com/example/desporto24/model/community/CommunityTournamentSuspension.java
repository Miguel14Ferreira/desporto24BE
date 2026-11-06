package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Time-bound eligibility restriction scoped to one community's tournaments.
 * It never suspends the account or silently changes existing results.
 */
@Entity
@Table(name = "community_tournament_suspension", schema = "desporto24")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTournamentSuspension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id", nullable = false, updatable = false)
    private Long communityId;

    @Column(name = "target_profile_id", nullable = false, updatable = false)
    private Long targetProfileId;

    @Column(name = "issued_by_profile_id", nullable = false, updatable = false)
    private Long issuedByProfileId;

    @Column(name = "reason", nullable = false, updatable = false, length = 1000)
    private String reason;

    @Column(name = "starts_at", nullable = false, updatable = false)
    private Instant startsAt;

    @Column(name = "ends_at", nullable = false, updatable = false)
    private Instant endsAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "revoked_by_profile_id")
    private Long revokedByProfileId;

    @Column(name = "revocation_reason", length = 1000)
    private String revocationReason;
}
