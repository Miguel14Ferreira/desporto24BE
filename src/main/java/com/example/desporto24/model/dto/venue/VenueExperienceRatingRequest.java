package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Only the condition of the facility is scored, never other players. */
public record VenueExperienceRatingRequest(
        @NotNull @Min(1) @Max(5) Integer score
) {}
