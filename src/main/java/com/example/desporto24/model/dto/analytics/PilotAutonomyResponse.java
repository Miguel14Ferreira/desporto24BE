package com.example.desporto24.model.dto.analytics;

import java.math.BigDecimal;

public record PilotAutonomyResponse(
        long autonomousSessions,
        long realizedSessions,
        BigDecimal autonomyRatePercent,
        BigDecimal minimumRatePercent,
        boolean targetMet
) {
}
