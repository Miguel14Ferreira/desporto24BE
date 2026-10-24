package com.example.desporto24.model.dto.venue;

import java.util.List;

public record VenueSuggestionPageResponse(
    List<VenueSuggestionResponse> suggestions, int page, boolean hasMore
) {}
