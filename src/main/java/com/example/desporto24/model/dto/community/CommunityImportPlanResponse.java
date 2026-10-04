package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportPlanResponse(
        String sheetName,
        int acceptedRows,
        int skippedRows,
        int distinctPlayers,
        int newPlayers,
        int existingPlayers,
        List<PlayerPlan> players
) {
    public record PlayerPlan(
            String displayName,
            String normalizedName,
            String action
    ) {
    }
}
