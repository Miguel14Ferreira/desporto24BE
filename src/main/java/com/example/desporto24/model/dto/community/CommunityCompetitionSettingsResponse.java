package com.example.desporto24.model.dto.community;

public record CommunityCompetitionSettingsResponse(
        boolean rankingsEnabled,
        int winPoints,
        int drawPoints,
        int lossPoints
) {
}
