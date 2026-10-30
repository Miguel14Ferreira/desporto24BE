package com.example.desporto24.model.dto.venue;

import java.util.List;

/**
 * Opt-in nearby-locality search. Distances are straight-line estimates from
 * saved venue coordinates in the reference city, not from an official city centre.
 */
public record VenueNearbyCityPageResponse(
        String referenceCity,
        double radiusKm,
        boolean referenceAvailable,
        List<NearbyCityVenueResponse> venues,
        int page,
        boolean hasMore) {

    public record NearbyCityVenueResponse(VenueResponse venue, double distanceKm) {}
}
