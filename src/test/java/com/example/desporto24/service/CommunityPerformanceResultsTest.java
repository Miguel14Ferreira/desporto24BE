package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityPerformanceResultsTest {
    private CommunityRoundEntryRequest player(String name, String metric) {
        return new CommunityRoundEntryRequest(name, null, CommunityMatchResult.DRAW,
                0, 0, false, false, null, null, null, null,
                metric == null ? null : new BigDecimal(metric));
    }

    @Test void measuredEventsUseMetresButNeverCrossEventRankings() {
        for (String type : List.of("JUMP", "THROW")) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Atletismo", type,
                    List.of(player("a", "6.340"), player("b", "6.400"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Atletismo", type,
                            List.of(player("a", "0.000"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Atletismo", type,
                            List.of(player("a", "-2.500"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Atletismo", type,
                            List.of(player("a", "1000000.000"))));
        }
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Atletismo", "JUMP", List.of(player("a", null))));
    }

    @Test void judgedEventsAcceptOnlyOneAggregateScoreUpTo100() {
        for (String sport : List.of("BMX", "Snowboard")) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    sport, "FREESTYLE", List.of(player("a", "0.000"), player("b", "91.250"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            sport, "FREESTYLE", List.of(player("a", "101.000"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            sport, "FREESTYLE", List.of(player("a", "91.1234"))));
        }
    }

    @Test void otherSportsAndRaceEventsCannotSmugglePerformanceValues() {
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries(
                        "Futebol", null, List.of(player("a", "5"))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries(
                        "BMX", "RACE", List.of(player("a", "5"))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries(
                        "Atletismo", "TRACK", List.of(player("a", "5"))));
    }
}
