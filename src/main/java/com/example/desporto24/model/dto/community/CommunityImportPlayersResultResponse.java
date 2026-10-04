package com.example.desporto24.model.dto.community;

public record CommunityImportPlayersResultResponse(
        int distinctPlayers,
        int createdPlayers,
        int reusedPlayers
) {
}
