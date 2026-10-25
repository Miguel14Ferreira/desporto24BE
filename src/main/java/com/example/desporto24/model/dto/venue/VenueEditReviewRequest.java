package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.Size;
public record VenueEditReviewRequest(@Size(max = 1000) String note) {}
