package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.Size;
/**
 * Explicit moderator attestation for publishing new phone/contact details.
 * Source URL validation alone is NOT evidence that the reviewer checked it.
 */
public record VenueEditReviewRequest(@Size(max = 1000) String note, Boolean sourceReviewed) {
    public VenueEditReviewRequest(String note) {
        this(note, null);
    }
}
