package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.UpdateCommunityRaceDecisionRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRaceDecisionsTest {
    @Test void preservesOriginalAndOnlyAddsRecordedTime() {
        var penalty = CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("5.250"),
                        false, "Limites de pista"), new BigDecimal("92.430"));
        assertEquals(new BigDecimal("5.250"), penalty);
        assertEquals(new BigDecimal("97.680"), CommunityRaceDecisions.adjustedTime(
                new BigDecimal("92.430"), penalty));
        assertEquals(new BigDecimal("92.430"), new BigDecimal("92.430"));
    }
    @Test void dsqWithoutTimeIsValidWhenReasonExists() {
        assertEquals(new BigDecimal("0.000"), CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(null, true, "Regra da prova"),
                null));
    }
    @Test void rejectInvalidOrUnsupportedPenalty() {
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("-1"), false, "Motivo"),
                BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("3600.001"), false, "Motivo"),
                BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("0.0001"), false, "Motivo"),
                BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("2.5"), false, ""),
                BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(new BigDecimal("2.5"), false, "motivo"),
                null));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(null, true, null),
                BigDecimal.ONE));
    }
    @Test void clearingDecisionResetsTimePenaltyWithoutChangingRecordedTime() {
        assertEquals(new BigDecimal("0.000"), CommunityRaceDecisions.validate(
                new UpdateCommunityRaceDecisionRequest(BigDecimal.ZERO, false, null),
                new BigDecimal("45.111")));
    }
}
