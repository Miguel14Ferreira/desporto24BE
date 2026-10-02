package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityImportMappingValidationResponse(
        String sheetName,
        boolean valid,
        List<String> errors
) {
}
