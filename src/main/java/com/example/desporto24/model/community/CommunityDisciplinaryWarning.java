package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** Immutable, community-scoped warning. No platform-wide account penalties. */
@Entity
@Table(name = "community_disciplinary_warning", schema = "desporto24")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDisciplinaryWarning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id", nullable = false, updatable = false)
    private Long communityId;

    @Column(name = "target_profile_id", nullable = false, updatable = false)
    private Long targetProfileId;

    @Column(name = "issued_by_profile_id", nullable = false, updatable = false)
    private Long issuedByProfileId;

    @Column(name = "reason", length = 1000, nullable = false, updatable = false)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
