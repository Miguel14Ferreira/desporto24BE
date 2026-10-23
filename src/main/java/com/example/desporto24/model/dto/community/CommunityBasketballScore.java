package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Final basketball points, one score for each team, never individual goals. */
public record CommunityBasketballScore(
        @NotNull @Min(0) @Max(999) Integer sideA,
        @NotNull @Min(0) @Max(999) Integer sideB) {}
