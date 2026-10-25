package com.example.desporto24.model.dto.community;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommunityImportMappingValidationResponse(
        String sheetName,
        boolean valid,
        int dataRows,
        int validRows,
        List<String> errors,
        List<String> warnings
) {
    @JsonProperty("errorDetails")
    public List<CommunityImportDiagnostic> errorDetails() {
        return CommunityImportDiagnostic.fromLegacyMessages(errors);
    }

    @JsonProperty("warningDetails")
    public List<CommunityImportDiagnostic> warningDetails() {
        return CommunityImportDiagnostic.fromLegacyMessages(warnings);
    }

}
