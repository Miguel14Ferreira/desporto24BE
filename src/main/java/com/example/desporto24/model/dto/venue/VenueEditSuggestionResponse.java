package com.example.desporto24.model.dto.venue;

import java.time.Instant;
import java.util.Map;

public record VenueEditSuggestionResponse(
        Long id, Long venueId, String venueName, String status,
        Map<String, String> before, Map<String, String> proposed,
        String reason, String submittedByUsername, Instant submittedAt,
        String reviewedByUsername, Instant reviewedAt, String decisionNote,
        String sourceUrl, Instant sourceReviewConfirmedAt
) {}
