package com.example.desporto24.model.session;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "session_experience_rating",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_session_experience_rating_session_profile",
                columnNames = {"session_id", "profile_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionExperienceRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "environment_score", nullable = false)
    private Integer environmentScore;

    @Column(name = "venue_score", nullable = false)
    private Integer venueScore;

    @Column(name = "game_score", nullable = false)
    private Integer gameScore;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
