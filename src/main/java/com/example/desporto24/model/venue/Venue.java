package com.example.desporto24.model.venue;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "venue")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    public enum InformationSource {
        COMMUNITY,
        OFFICIAL
    }

    public enum OwnershipType {
        PUBLIC,
        PRIVATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 255)
    private String sports;

    @Column(name = "venue_type", length = 80)
    private String venueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type", length = 20)
    private OwnershipType ownershipType;

    @Column(name = "surface_type", length = 80)
    private String surfaceType;

    @Column(nullable = false)
    private boolean indoor;

    @Column(name = "changing_rooms", nullable = false)
    private boolean changingRooms;

    @Column
    private Boolean toilets;

    @Column
    private Boolean lockers;

    @Column
    private Boolean accessible;

    @Column(nullable = false)
    private boolean parking;

    @Column(nullable = false)
    private boolean lighting;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "information_source", length = 20)
    @Builder.Default
    private InformationSource informationSource = InformationSource.COMMUNITY;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_profile_id", nullable = false)
    private Profile createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
