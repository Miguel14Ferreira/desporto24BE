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

    // Null = legacy presentation (animated GIFs remain letterboxed by default).
    @Column(name = "photo_zoom")
    private Double photoZoom;
    @Column(name = "photo_focus_x")
    private Double photoFocusX;
    @Column(name = "photo_focus_y")
    private Double photoFocusY;

    // Null means legacy COVER; changing this never modifies the stored image.
    @Column(name = "photo_fit_mode", length = 16)
    private String photoFitMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityVisibility visibility;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_profile_id", nullable = false)
    private Profile owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_venue_id")
    private Venue defaultVenue;

    // Alternative to an existing venue: a confirmed Google Maps or browser GPS point.
    // Null source preserves legacy defaultVenue-only communities.
    @Column(name = "habitual_location_source", length = 8)
    private String habitualLocationSource;

    @Column(name = "habitual_latitude")
    private Double habitualLatitude;

    @Column(name = "habitual_longitude")
    private Double habitualLongitude;

    @Column(name = "habitual_city", length = 160)
    private String habitualCity;

    @Column(name = "habitual_address", length = 350)
    private String habitualAddress;

    // Legacy communities retain the existing all-members permission.
    @Builder.Default
    @Column(name = "members_can_create_sessions", nullable = false)
    private boolean membersCanCreateSessions = true;

    @Column(name = "rankings_enabled", nullable = false)
    private boolean rankingsEnabled;

    // Optional community modules. All groups always retain sessions and their history.
    @Builder.Default
    @Column(name = "individual_tournaments_enabled", nullable = false)
    private boolean individualTournamentsEnabled = true;

    @Builder.Default
    @Column(name = "team_tournaments_enabled", nullable = false)
    private boolean teamTournamentsEnabled = true;

    @Column(name = "win_points")
    private Integer winPoints;

    @Column(name = "draw_points")
    private Integer drawPoints;

    @Column(name = "loss_points")
    private Integer lossPoints;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
