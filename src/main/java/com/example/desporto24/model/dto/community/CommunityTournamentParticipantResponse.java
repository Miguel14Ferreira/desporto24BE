package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityTournamentParticipantResponse(
        Long profileId,
        String username,
        String displayName,
        Instant joinedAt
) {
}
