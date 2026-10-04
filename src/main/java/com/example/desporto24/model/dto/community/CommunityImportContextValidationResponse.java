package com.example.desporto24.model.dto.community;

public record CommunityImportContextValidationResponse(
        Long seasonId,
        String seasonName,
        Integer defaultYear,
        boolean valid
) {}
