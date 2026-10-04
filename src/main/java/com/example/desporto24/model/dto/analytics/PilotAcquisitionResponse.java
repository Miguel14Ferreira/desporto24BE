package com.example.desporto24.model.dto.analytics;

public record PilotAcquisitionResponse(
        String cohort,
        long attributedAccesses,
        long minimumAttributedAccesses,
        boolean targetMet
) {
}
