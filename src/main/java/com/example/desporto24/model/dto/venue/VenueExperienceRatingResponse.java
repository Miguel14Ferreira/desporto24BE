package com.example.desporto24.model.dto.venue;

import java.time.Instant;

public record VenueExperienceRatingResponse(
        Long venueId, int score, Instant updatedAt
) {}
