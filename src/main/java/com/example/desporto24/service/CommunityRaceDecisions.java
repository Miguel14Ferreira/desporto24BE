package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.UpdateCommunityRaceDecisionRequest;
import java.math.BigDecimal;

/** Does not infer a new finishing order from timing penalties. */
final class CommunityRaceDecisions {
    private CommunityRaceDecisions() {}

    static BigDecimal validate(UpdateCommunityRaceDecisionRequest request,
            BigDecimal recordedElapsedSeconds) {
        if (request == null) throw new IllegalArgumentException("Decisão obrigatória");
        BigDecimal seconds = request.timePenaltySeconds() == null
                ? BigDecimal.ZERO : request.timePenaltySeconds();
        if (seconds.signum() < 0 || seconds.compareTo(new BigDecimal("3600.000")) > 0
                || seconds.scale() > 3) {
            throw new IllegalArgumentException(
                    "A penalização de tempo deve estar entre 0 e 3600 segundos, até 3 decimais");
        }
        if (seconds.signum() > 0 && recordedElapsedSeconds == null) {
            throw new IllegalArgumentException(
                    "Só podes aplicar tempo extra quando há tempo original registado");
        }
        if ((seconds.signum() > 0 || request.disqualified())
                && (request.reason() == null || request.reason().isBlank())) {
            throw new IllegalArgumentException(
                    "Indica o motivo da penalização ou desclassificação");
        }
        if (request.reason() != null && request.reason().length() > 255) {
            throw new IllegalArgumentException("O motivo tem de ter no máximo 255 caracteres");
        }
        return seconds.setScale(3);
    }

    static BigDecimal adjustedTime(BigDecimal elapsed, BigDecimal penalty) {
        if (elapsed == null) return null;
        return elapsed.add(penalty == null ? BigDecimal.ZERO : penalty);
    }
}
