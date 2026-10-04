package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CommunityImportMappingRequest(
        @NotBlank String sheetName,
        @NotEmpty List<ColumnMapping> columns
) {
    public record ColumnMapping(
            @Min(0) int columnIndex,
            @NotBlank String field
    ) {
    }
}
