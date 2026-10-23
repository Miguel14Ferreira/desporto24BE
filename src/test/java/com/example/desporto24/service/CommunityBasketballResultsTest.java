package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityBasketballScore;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityBasketballResultsTest {
    private CommunityRoundEntryRequest participant(String name, String side) {
        return new CommunityRoundEntryRequest(name, side, CommunityMatchResult.DRAW,
                0, 0, false, false);
    }
    private final List<CommunityRoundEntryRequest> sides =
            List.of(participant("ana","A"), participant("beatriz","B"));

    @Test void eventTypesAreSpecificTo5v5And3x3AndNeverAlterLegacy() {
        assertEquals("BASKETBALL_FINAL",
                CommunityRoundEventType.validate("Basquetebol","BASKETBALL_FINAL"));
        assertEquals("BASKETBALL_3X3_FINAL",
                CommunityRoundEventType.validate("Basquetebol 3x3","BASKETBALL_3X3_FINAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Basquetebol",null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Basquetebol 3x3","BASKETBALL_FINAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol","BASKETBALL_FINAL"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Basquetebol","BASKETBALL_FINAL"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Basquetebol 3x3","BASKETBALL_3X3_FINAL"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Basquetebol","BASKETBALL_FINAL"));
        assertNull(CommunityBasketballResults.decode(null,null));
    }
    @Test void acceptsOrganizerConfirmedFinalPointTotalsNotGoalOrPlayerScores() {
        for (String sport : List.of("Basquetebol","Basquetebol 3x3")) {
            String event = "Basquetebol".equals(sport) ? "BASKETBALL_FINAL" : "BASKETBALL_3X3_FINAL";
            for (CommunityBasketballScore score : List.of(
                    new CommunityBasketballScore(88,79),
                    new CommunityBasketballScore(19,21),
                    new CommunityBasketballScore(22,21),
                    new CommunityBasketballScore(0,1))) {
                assertEquals(score, CommunityBasketballResults.validate(sport,event,sides,score));
            }
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(sport,event,sides));
        }
        assertEquals(new CommunityBasketballScore(92,88),CommunityBasketballResults.decode(92,88));
        assertNull(CommunityBasketballResults.validate("Futebol",null,sides,null));
    }
    @Test void refusesDrawsMissingScoresInvalidTeamsAndUnrelatedSports() {
        for (CommunityBasketballScore score : List.of(
                new CommunityBasketballScore(80,80),
                new CommunityBasketballScore(-1,30),
                new CommunityBasketballScore(1000,5),
                new CommunityBasketballScore(null,21))) {
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityBasketballResults.validate("Basquetebol",
                            "BASKETBALL_FINAL",sides,score));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol","BASKETBALL_FINAL",sides,null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Futebol",null,sides,
                        new CommunityBasketballScore(2,1)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol","BASKETBALL_FINAL",
                        List.of(participant("a","A"),participant("b","A")),
                        new CommunityBasketballScore(11,10)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol","BASKETBALL_FINAL",
                        List.of(participant("a",null), participant("b","B")),
                        new CommunityBasketballScore(11,10)));
        assertThrows(IllegalStateException.class, () -> CommunityBasketballResults.decode(12,null));
    }
    @Test void rejectsInventedFootballMetricsPerPlayer() {
        var bad = new CommunityRoundEntryRequest("ana","A", CommunityMatchResult.DRAW,
                1, 0, false, false);
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol","BASKETBALL_FINAL",
                        List.of(bad,participant("b","B")),new CommunityBasketballScore(20,10)));
        var badResult = new CommunityRoundEntryRequest("ana","A", CommunityMatchResult.WIN,
                0, 0, false, false);
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol 3x3","BASKETBALL_3X3_FINAL",
                        List.of(badResult,participant("b","B")),new CommunityBasketballScore(20,10)));
        var badPerformance = new CommunityRoundEntryRequest("ana","A", CommunityMatchResult.DRAW,
                0,0,false,false,null,null,null,null,new BigDecimal("12"),null,null);
        assertThrows(IllegalArgumentException.class, () ->
                CommunityBasketballResults.validate("Basquetebol","BASKETBALL_FINAL",
                        List.of(badPerformance,participant("b","B")),new CommunityBasketballScore(20,10)));
    }
}
