package com.example.desporto24.model.dto.venue;

import java.util.List;

/** Bounded search response for venue suggestions. */
public record VenueSearchPageResponse(
        List<VenueResponse> venues, int page, boolean hasMore) {}
