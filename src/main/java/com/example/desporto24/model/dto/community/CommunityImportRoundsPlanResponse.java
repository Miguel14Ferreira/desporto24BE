package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportRoundsPlanResponse(
        String sheetName,
        int acceptedRows,
        int distinctRounds,
        int rowsWithoutRound,
        List<RoundPlan> rounds
) {
    public record RoundPlan(
            String sourceDate,
            int players
    ) {}
}
