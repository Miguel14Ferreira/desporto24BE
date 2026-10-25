package com.example.desporto24.model.dto.community;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Read-only interpretation of a historical fixture/result export.
 * Raw date and round are deliberately preserved: no fixtures are written.
 */
public record CommunityHistoricalMatchPreviewResponse(
        String sheetName,
        int sourceRows,
        int acceptedRows,
        int potentialDuplicates,
        List<String> warnings,
        List<MatchRow> rows
) {
    public record MatchRow(
            int sourceRow,
            String home,
            String away,
            int homeScore,
            int awayScore,
            String sourceDate,
            String sourceRound,
            boolean potentialDuplicate
    ) {}
    @JsonProperty("warningDetails")
    public List<CommunityImportDiagnostic> warningDetails() {
        return CommunityImportDiagnostic.fromLegacyMessages(warnings);
    }

}
