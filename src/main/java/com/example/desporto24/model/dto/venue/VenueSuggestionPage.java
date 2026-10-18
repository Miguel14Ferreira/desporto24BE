package com.example.desporto24.model.dto.venue;

import java.util.List;

/** JSON response already expected by the desktop venue autocomplete. */
public record VenueSuggestionPage(
        List<VenueResponse> venues,
        int page,
        boolean hasMore
) {
}
