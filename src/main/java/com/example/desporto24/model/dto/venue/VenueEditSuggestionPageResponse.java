package com.example.desporto24.model.dto.venue;

import java.util.List;
public record VenueEditSuggestionPageResponse(
        List<VenueEditSuggestionResponse> suggestions, int page, boolean hasMore
) {}
