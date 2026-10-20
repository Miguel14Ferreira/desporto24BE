package com.example.desporto24.model.community;

import com.example.desporto24.model.Profile;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "community_tournament_participant",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_community_tournament_participant",
                columnNames = {"tournament_id", "profile_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTournamentParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private CommunityTournament tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    // Existing enrollments are backfilled as CONFIRMED by Liquibase.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    @Builder.Default
    private TournamentParticipantStatus status = TournamentParticipantStatus.CONFIRMED;
}
