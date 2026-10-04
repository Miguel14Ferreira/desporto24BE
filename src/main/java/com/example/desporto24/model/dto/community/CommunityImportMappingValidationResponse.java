package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportMappingValidationResponse(
        String sheetName,
        boolean valid,
        int dataRows,
        int validRows,
        List<String> errors,
        List<String> warnings
) {
}
