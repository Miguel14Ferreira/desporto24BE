package com.example.desporto24.model.dto.analytics;

import java.math.BigDecimal;

public record PilotRegistrationResponse(
        String cohort,
        long attributedRegistrations,
        long attributedAccesses,
        BigDecimal conversionRatePercent,
        long minimumAttributedRegistrations,
        boolean targetMet
) {
}
