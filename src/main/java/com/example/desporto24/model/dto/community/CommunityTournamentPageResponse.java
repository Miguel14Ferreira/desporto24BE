package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentPageResponse(
        List<CommunityTournamentResponse> tournaments,
        int page,
        boolean hasMore
) {
}
