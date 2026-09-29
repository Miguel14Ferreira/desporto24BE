package com.example.desporto24.model.dto.venue;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        String sports,
        String surfaceType,
        boolean indoor,
        boolean changingRooms,
        boolean parking,
        boolean lighting,
        Double latitude,
        Double longitude,
        String informationSource,
        String createdByUsername
) {
}
