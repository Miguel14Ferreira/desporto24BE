package com.example.desporto24.model.dto.venue;

public record ResolveVenueMapLinkResponse(
        String resolvedUrl,
        Double latitude,
        Double longitude
) {
}
