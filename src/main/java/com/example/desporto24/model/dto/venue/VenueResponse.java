package com.example.desporto24.model.dto.venue;

import java.time.Instant;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        String sports,
        String venueType,
        String ownershipType,
        String surfaceType,
        String environmentType,
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
        Boolean cafe,
        String services,
        String transport,
        String openingHours,
        String priceInfo,
        String website,
        String contact,
        boolean parking,
        boolean lighting,
        Double latitude,
        Double longitude,
        String informationSource,
        String createdByUsername,
        Instant createdAt
) {
}
