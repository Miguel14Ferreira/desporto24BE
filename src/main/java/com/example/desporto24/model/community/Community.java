package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.venue.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(length = 100)
    private String sport;

    // Explicitly ordered; "sport" remains the legacy primary modality.
    @ElementCollection
    @CollectionTable(name = "community_sport", joinColumns = @JoinColumn(name = "community_id"))
    @OrderColumn(name = "position")
    @Column(name = "sport", nullable = false, length = 100)
    @Builder.Default
    private List<String> sports = new ArrayList<>();

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityVisibility visibility;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_profile_id", nullable = false)
    private Profile owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_venue_id")
    private Venue defaultVenue;

    @Column(name = "rankings_enabled", nullable = false)
    private boolean rankingsEnabled;

    @Column(name = "win_points")
    private Integer winPoints;

    @Column(name = "draw_points")
    private Integer drawPoints;

    @Column(name = "loss_points")
    private Integer lossPoints;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
