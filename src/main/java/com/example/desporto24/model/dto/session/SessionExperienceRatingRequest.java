package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SessionExperienceRatingRequest(
        @NotNull @Min(1) @Max(5) Integer environmentScore,
        @NotNull @Min(1) @Max(5) Integer venueScore,
        @NotNull @Min(1) @Max(5) Integer gameScore
) {
}
