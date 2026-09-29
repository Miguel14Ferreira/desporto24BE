package com.example.desporto24.model.venue;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "venue_sport_condition",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_venue_sport_condition_venue_sport",
                columnNames = {"venue_id", "sport"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueSportCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false, length = 80)
    private String sport;

    @Column(nullable = false, length = 1500)
    private String conditions;

    @Enumerated(EnumType.STRING)
    @Column(name = "information_source", nullable = false, length = 20)
    @Builder.Default
    private Venue.InformationSource informationSource = Venue.InformationSource.COMMUNITY;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_profile_id", nullable = false)
    private Profile createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
