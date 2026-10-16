package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentParticipantsPageResponse(
        List<CommunityTournamentParticipantResponse> participants,
        int page,
        boolean hasMore
) {
}
