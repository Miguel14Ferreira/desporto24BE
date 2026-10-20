package com.example.desporto24.model.dto.session;

import java.time.Instant;

public record SessionExperienceRatingResponse(
        int overallScore,
        Instant updatedAt
) {
}
