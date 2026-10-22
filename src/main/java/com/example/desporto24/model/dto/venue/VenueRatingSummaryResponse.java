package com.example.desporto24.model.dto.venue;

/** Under three distinct reviewers, count and average must both remain hidden. */
public record VenueRatingSummaryResponse(
        Long venueId, long ratingCount, Double averageScore
) {}
