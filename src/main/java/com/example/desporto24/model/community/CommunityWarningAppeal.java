package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** Immutable player response to one warning. An appeal is not an automatic reversal. */
@Entity
@Table(name = "community_warning_appeal", schema = "desporto24",
       uniqueConstraints = @UniqueConstraint(name = "uk_community_warning_appeal_warning", columnNames = "warning_id"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityWarningAppeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warning_id", nullable = false, updatable = false)
    private Long warningId;

    @Column(name = "community_id", nullable = false, updatable = false)
    private Long communityId;

    @Column(name = "author_profile_id", nullable = false, updatable = false)
    private Long authorProfileId;

    @Column(name = "statement", nullable = false, updatable = false, length = 2000)
    private String statement;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
