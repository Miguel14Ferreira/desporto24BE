package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRaceMetricsTest {
    private static CommunityRoundEntryRequest racer(String name, Integer place, String seconds) {
        return new CommunityRoundEntryRequest(name, null, CommunityMatchResult.DRAW,
                0, 0, false, false, place,
                seconds == null ? null : new BigDecimal(seconds), null, null);
    }

    @Test void recognizesRaceFamiliesButNeverFootballOrDuels() {
        for (String sport : List.of("Karting", "Automobilismo", "Motocross",
                "Corrida", "Natação", "Ciclismo", "Canoagem", "Vela")) {
            assertTrue(CommunityRaceMetrics.isRaceSport(sport), sport);
        }
        assertFalse(CommunityRaceMetrics.isRaceSport("Futebol"));
        assertFalse(CommunityRaceMetrics.isRaceSport("Ténis"));
        assertFalse(CommunityRaceMetrics.isRaceSport(null));
    }

    @Test void acceptsRealRacePositionsAndTimesWithoutInferringScores() {
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate("Karting", List.of(
                racer("maria", 1, "92.430"), racer("joao", 2, "93.120"))));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate("Karting", List.of(
                racer("maria", null, null), racer("joao", 2, null))));
    }

    @Test void roadTimeTrialRequiresRecordedTimeForEveryPlacedFinisher() {
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                "Ciclismo de Estrada", "TIME_TRIAL", List.of(
                        racer("ana", 1, "102.500"), racer("rui", 2, "104.800"),
                        racer("dnf", null, null))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Ciclismo de Estrada", "TIME_TRIAL", List.of(racer("ana", 1, null))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Ciclismo de Estrada", "TIME_TRIAL", List.of(racer("ana", null, "102.500"))));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                "Ciclismo de Estrada", "ROAD_RACE", List.of(racer("ana", 1, null))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Ciclismo de Estrada", "ROAD_RACE", List.of(racer("ana", 1, "-1.0"))));
    }

    @Test void preventsDuplicateFinishPositionsAndInvalidOrFakeStats() {
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Karting", List.of(racer("maria", 1, null), racer("joao", 1, null))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Karting", List.of(racer("maria", 0, null))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Karting", List.of(racer("maria", 1, "-1.0"))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Karting", List.of(racer("maria", 1, "92.4301"))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Futsal", List.of(racer("maria", 1, null))));
        var goals = new CommunityRoundEntryRequest("maria", null,
                CommunityMatchResult.DRAW, 2, 0, false, false, null, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> CommunityRaceMetrics.validate(
                "Karting", List.of(goals)));
    }
}
