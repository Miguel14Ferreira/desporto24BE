package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportPreviewResponse(
        String fileName,
        List<SheetPreview> sheets
) {
    public record SheetPreview(
            String name,
            int nonEmptyRows
    ) {
    }
}
