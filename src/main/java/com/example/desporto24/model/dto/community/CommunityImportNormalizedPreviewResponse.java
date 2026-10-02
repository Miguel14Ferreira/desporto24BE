package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportNormalizedPreviewResponse(
        String sheetName,
        List<NormalizedRow> rows
) {
    public record NormalizedRow(
            int sourceRow,
            String player,
            String date,
            String result,
            Double goals,
            Double assists,
            Double points,
            Double mvp
    ) {
    }
}
