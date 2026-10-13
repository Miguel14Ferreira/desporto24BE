package com.example.desporto24.model.dto.session;

import java.time.Instant;

public record SessionExperienceRatingResponse(
        int environmentScore,
        int venueScore,
        int gameScore,
        Instant updatedAt
) {
}
