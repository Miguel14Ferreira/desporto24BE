package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentMatchesPageResponse(
        List<CommunityTournamentMatchResponse> matches,
        int page,
        boolean hasMore
) {
}
