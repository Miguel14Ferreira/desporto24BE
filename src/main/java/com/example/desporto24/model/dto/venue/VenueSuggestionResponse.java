package com.example.desporto24.model.dto.venue;

import java.time.Instant;

public record VenueSuggestionResponse(
    Long id, String status, CreateVenueRequest venue, String submittedByUsername,
    Instant submittedAt, String reviewedByUsername, Instant reviewedAt, Long publishedVenueId
) {}
