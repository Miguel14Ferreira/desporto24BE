package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportNormalizedPreviewResponse(
        String sheetName,
        int acceptedRows,
        int skippedRows,
        List<NormalizedRow> rows,
        List<SkippedRow> skipped
) {
    public record NormalizedRow(
            int sourceRow,
            String player,
            String date,
            String result,
            Double win,
            Double draw,
            Double loss,
            Double goals,
            Double assists,
            Double points,
            Double mvp
    ) {
    }

    public record SkippedRow(
            int sourceRow,
            String reason
    ) {
    }
}
