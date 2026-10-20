package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotNull;

/** Explicit full set of optional community features; sessions are always available. */
public record UpdateCommunityFeaturesRequest(
        @NotNull Boolean rankingsEnabled,
        @NotNull Boolean individualTournamentsEnabled,
        @NotNull Boolean teamTournamentsEnabled
) {}
