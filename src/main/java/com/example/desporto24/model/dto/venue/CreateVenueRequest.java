package com.example.desporto24.model.dto.venue;

import com.example.desporto24.model.venue.Venue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record CreateVenueRequest(
        @NotBlank @Size(max = 140) String name,
        @NotBlank @Size(max = 120) String city,
        @Size(max = 255) String address,
        @Size(max = 255) String sports,
        @Size(max = 80) String venueType,
        Venue.OwnershipType ownershipType,
        @Size(max = 80) String surfaceType,
        Venue.EnvironmentType environmentType,
        boolean indoor,
        boolean changingRooms,
        @Size(max = 120) String changingRoomTypes,
        Boolean toilets,
        Boolean lockers,
        Boolean showers,
        Boolean stands,
        Boolean drinkingFountain,
        Boolean accessible,
        Boolean firstAid,
        Boolean aed,
        Boolean medicalStation,
        Boolean lifeguard,
        Boolean staffOnSite,
        Boolean securitySurveillance,
        Boolean cafe,
        Boolean restaurant,
        Boolean vendingMachines,
        Boolean sportsShop,
        Boolean wifi,
        Boolean phoneCharging,
        Integer nearbyRadiusMeters,
        Boolean nearbyPharmacy,
        Boolean nearbyAtm,
        @Size(max = 500) String services,
        @Size(max = 500) String transport,
        @Size(max = 1000) String openingHours,
        @Size(max = 255) String priceInfo,
        @Size(max = 255) String website,
        @Size(max = 255) String contact,
        boolean parking,
        Boolean freeParking,
        boolean lighting,
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        @Size(max = 120) String district
) {
    /** Preserve existing Java constructors while accepting district from HTTP JSON. */
    public CreateVenueRequest(
            String name,
            String city,
            String address,
            String sports,
            String venueType,
            Venue.OwnershipType ownershipType,
            String surfaceType,
            Venue.EnvironmentType environmentType,
            boolean indoor,
            boolean changingRooms,
            String changingRoomTypes,
            Boolean toilets,
            Boolean lockers,
            Boolean showers,
            Boolean stands,
            Boolean drinkingFountain,
            Boolean accessible,
            Boolean firstAid,
            Boolean aed,
            Boolean medicalStation,
            Boolean lifeguard,
            Boolean staffOnSite,
            Boolean securitySurveillance,
            Boolean cafe,
            Boolean restaurant,
            Boolean vendingMachines,
            Boolean sportsShop,
            Boolean wifi,
            Boolean phoneCharging,
            Integer nearbyRadiusMeters,
            Boolean nearbyPharmacy,
            Boolean nearbyAtm,
            String services,
            String transport,
            String openingHours,
            String priceInfo,
            String website,
            String contact,
            boolean parking,
            Boolean freeParking,
            boolean lighting,
            Double latitude,
            Double longitude) {
        this(name, city, address, sports, venueType, ownershipType, surfaceType, environmentType, indoor, changingRooms, changingRoomTypes, toilets, lockers, showers, stands, drinkingFountain, accessible, firstAid, aed, medicalStation, lifeguard, staffOnSite, securitySurveillance, cafe, restaurant, vendingMachines, sportsShop, wifi, phoneCharging, nearbyRadiusMeters, nearbyPharmacy, nearbyAtm, services, transport, openingHours, priceInfo, website, contact, parking, freeParking, lighting, latitude, longitude, null);
    }
    /** Blank postal address is valid only alongside a complete mainland GPS pair. */
    @AssertTrue(message = "Indica uma morada ou uma localização GPS válida")
    public boolean isValidVenueLocation() {
        boolean anyGps = latitude != null || longitude != null;
        if (anyGps && (latitude == null || longitude == null
                || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < 36.8 || latitude > 42.3
                || longitude < -9.6 || longitude > -6.0)) {
            return false;
        }
        return (address != null && !address.isBlank()) || anyGps;
    }
}
