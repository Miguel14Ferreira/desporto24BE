package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportPreviewResponse(
        String fileName,
        List<SheetPreview> sheets
) {
    public record SheetPreview(
            String name,
            int nonEmptyRows,
            String structure,
            List<String> headers,
            List<List<String>> sampleRows,
            List<ColumnSuggestion> columnSuggestions
    ) {
    }

    public record ColumnSuggestion(
            int columnIndex,
            String header,
            String field,
            double confidence
    ) {
    }
}
