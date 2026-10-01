package com.example.desporto24.model.dto.analytics;

import java.math.BigDecimal;

public record PilotRealizationResponse(
        long realizedSessions,
        long eligiblePlannedSessions,
        BigDecimal realizationRatePercent,
        BigDecimal minimumRatePercent,
        boolean targetMet
) {
}
