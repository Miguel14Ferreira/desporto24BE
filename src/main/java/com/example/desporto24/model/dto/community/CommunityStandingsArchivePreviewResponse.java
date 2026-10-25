package com.example.desporto24.model.dto.community;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommunityStandingsArchivePreviewResponse(
        String sheetName,
        int sourceRows,
        int acceptedRows,
        int duplicateRows,
        List<String> warnings,
        List<StandingRow> rows
) {
    public record StandingRow(
            int position,
            String name,
            Integer played,
            Integer wins,
            Integer draws,
            Integer losses,
            int points,
            Integer goalsFor,
            Integer goalsAgainst
    ) {}
    @JsonProperty("warningDetails")
    public List<CommunityImportDiagnostic> warningDetails() {
        return CommunityImportDiagnostic.fromLegacyMessages(warnings);
    }

}
