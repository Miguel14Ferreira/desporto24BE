package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityInlineHeatProgressionResponse(
        Long targetRoundId,
        List<Long> sourceRoundIds,
        String confirmedByUsername,
        String reason,
        Instant confirmedAt
) {}
