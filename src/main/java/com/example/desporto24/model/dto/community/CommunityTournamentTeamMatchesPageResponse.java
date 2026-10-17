package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentTeamMatchesPageResponse(
        List<CommunityTournamentTeamMatchResponse> matches, int page, boolean hasMore) {
}
