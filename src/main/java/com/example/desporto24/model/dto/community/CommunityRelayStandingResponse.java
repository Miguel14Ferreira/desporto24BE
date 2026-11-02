package com.example.desporto24.model.dto.community;

import java.math.BigDecimal;
import java.util.List;

public record CommunityRelayStandingResponse(
        int position, Long teamId, String teamName, BigDecimal bestSeconds,
        int recordedHeats, List<Long> runnerProfileIds
) {}
