package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportRoundsPlanResponse(
        String sheetName,
        int acceptedRows,
        int distinctRounds,
        int rowsWithoutRound,
        int roundsNeedingYear,
        List<RoundPlan> rounds
) {
    public record RoundPlan(
            String sourceDate,
            String parsedDate,
            boolean needsYear,
            int players,
            int wins,
            int draws,
            int losses,
            int unresolvedResults
    ) {}
}
