package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Single optional post-game rating: one whole number from 1 to 5. */
public record SessionExperienceRatingRequest(
        @NotNull @Min(1) @Max(5) Integer overallScore
) {
}
