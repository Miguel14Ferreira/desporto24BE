package com.example.desporto24.model.dto.analytics;

import java.math.BigDecimal;

public record PilotActivation7dResponse(
        long activatedRegistrations,
        long eligibleRegistrations,
        BigDecimal activationRatePercent,
        long minimumEligibleRegistrations,
        boolean sampleSufficient
) {
}
