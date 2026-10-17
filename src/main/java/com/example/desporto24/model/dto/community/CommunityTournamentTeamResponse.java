package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityTournamentTeamResponse(
        Long id, String name, int playersPerTeam, Instant createdAt,
        List<Player> players
) {
    public record Player(Long profileId, String username, String displayName) {}
}
