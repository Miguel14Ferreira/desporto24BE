package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Community steward decision, separate from the original timing and finish order. */
public record UpdateCommunityRaceDecisionRequest(
        @DecimalMin("0.000") @DecimalMax("3600.000")
        @Digits(integer = 4, fraction = 3) BigDecimal timePenaltySeconds,
        boolean disqualified,
        @Size(max = 255) String reason
) {}
