package com.example.desporto24.model.session;

import com.example.desporto24.model.Profile;
import com.example.desporto24.enumeration.OrganizerExclusionReason;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** Organizer-only future enrollment restriction. Not a global user block. */
@Entity
@Table(name = "session_organizer_exclusion",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_session_organizer_exclusion",
                columnNames = {"organizer_profile_id", "excluded_profile_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionOrganizerExclusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_profile_id", nullable = false)
    private Profile organizer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "excluded_profile_id", nullable = false)
    private Profile excluded;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", length = 40)
    private OrganizerExclusionReason reason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
