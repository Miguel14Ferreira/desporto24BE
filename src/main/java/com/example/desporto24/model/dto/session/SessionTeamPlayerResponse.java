package com.example.desporto24.model.dto.session;

public record SessionTeamPlayerResponse(
        String username,
        double strengthScore,
        boolean statsAvailable,
        int games,
        int wins,
        int goals,
        int assists
) {
}
