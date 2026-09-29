package com.example.desporto24.model.dto.venue;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        String sports,
        String venueType,
        String ownershipType,
        String surfaceType,
        boolean indoor,
        boolean changingRooms,
        Boolean toilets,
        Boolean lockers,
        Boolean accessible,
        Boolean firstAid,
        Boolean aed,
        boolean parking,
        boolean lighting,
        Double latitude,
        Double longitude,
        String informationSource,
        String createdByUsername
) {
}
