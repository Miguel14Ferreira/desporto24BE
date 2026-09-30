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

    public enum EnvironmentType {
        INDOOR,
        OUTDOOR,
        BOTH
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

    @Enumerated(EnumType.STRING)
    @Column(name = "environment_type", length = 20)
    private EnvironmentType environmentType;

    @Column(name = "changing_rooms", nullable = false)
    private boolean changingRooms;

    @Column(name = "changing_room_types", length = 120)
    private String changingRoomTypes;

    @Column
    private Boolean toilets;

    @Column
    private Boolean lockers;

    @Column
    private Boolean showers;

    @Column
    private Boolean stands;

    @Column(name = "drinking_fountain")
    private Boolean drinkingFountain;

    @Column
    private Boolean accessible;

    @Column(name = "first_aid")
    private Boolean firstAid;

    @Column
    private Boolean aed;

    @Column(name = "medical_station")
    private Boolean medicalStation;

    @Column
    private Boolean lifeguard;

    @Column(name = "staff_on_site")
    private Boolean staffOnSite;

    @Column(name = "security_surveillance")
    private Boolean securitySurveillance;

    @Column
    private Boolean cafe;

    @Column
    private Boolean restaurant;

    @Column
    private Boolean vendingMachines;

    @Column
    private Boolean sportsShop;

    @Column
    private Boolean wifi;

    @Column(name = "phone_charging")
    private Boolean phoneCharging;

    @Column(name = "nearby_radius_meters")
    private Integer nearbyRadiusMeters;

    @Column(name = "nearby_pharmacy")
    private Boolean nearbyPharmacy;

    @Column(name = "nearby_atm")
    private Boolean nearbyAtm;

    @Column(length = 500)
    private String services;

    @Column(length = 500)
    private String transport;

    @Column(name = "opening_hours", length = 1000)
    private String openingHours;

    @Column(name = "price_info", length = 255)
    private String priceInfo;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String contact;

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
