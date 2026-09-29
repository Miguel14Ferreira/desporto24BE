package com.example.desporto24.model.dto.venue;

import java.time.Instant;

public record VenuePhotoResponse(
        Long id,
        String photoUrl,
        String uploadedByUsername,
        Instant createdAt
) {
}
