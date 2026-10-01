package com.example.desporto24.model.dto.analytics;

import java.math.BigDecimal;

public record PilotRepetition30dResponse(
        long repeatedParticipants,
        long eligibleParticipants,
        BigDecimal repetitionRatePercent,
        long minimumEligibleParticipants,
        boolean sampleSufficient
) {
}
