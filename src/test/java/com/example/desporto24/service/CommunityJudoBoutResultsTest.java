package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityJudoBoutResultsTest {
    private CommunityRoundEntryRequest entry(String username, String judogi,
            CommunityMatchResult result) {
        return new CommunityRoundEntryRequest(username, judogi, result, 0, 0, false, false);
    }
    @Test void recognizesExactlyTheFiveExplicitIJFOutcomeMethodsForExistingJudo() {
        for (String method : List.of("JUDO_IPPON","JUDO_AWASETE_IPPON",
                "JUDO_WAZA_ARI","JUDO_YUKO","JUDO_HANSOKU_MAKE")) {
            assertEquals(method, CommunityRoundEventType.validate("Judo", method));
            assertTrue(CommunityJudoBoutResults.isJudoBout(method));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Judo", method));
            assertFalse(CommunityRoundEventType.isRaceEvent("Judo", method));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validate("Xadrez", method));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Judo", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Judo", "JUDO_SHIDO_POINTS"));
    }

    @Test void acceptsWhiteOrBlueWinnerForEachMethodWithoutInventedNumericPoints() {
        for (String method : List.of("JUDO_IPPON","JUDO_AWASETE_IPPON",
                "JUDO_WAZA_ARI","JUDO_YUKO","JUDO_HANSOKU_MAKE")) {
            for (CommunityMatchResult first : List.of(
                    CommunityMatchResult.WIN, CommunityMatchResult.LOSS)) {
                CommunityMatchResult second = first == CommunityMatchResult.WIN
                        ? CommunityMatchResult.LOSS : CommunityMatchResult.WIN;
                assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Judo", method,
                        List.of(entry("a", "WHITE", first),entry("b", "BLUE", second))));
            }
        }
    }

    @Test void rejectsRepeatedOrMissingJudogiDrawsAndUnpairedWinners() {
        var white = entry("a", "WHITE", CommunityMatchResult.WIN);
        var blue = entry("b", "BLUE", CommunityMatchResult.LOSS);
        for (var entries : List.of(
                List.of(white),
                List.of(white, white),
                List.of(white, blue, blue),
                List.of(white, entry("b", "BLUE", CommunityMatchResult.WIN)),
                List.of(entry("a", "WHITE", CommunityMatchResult.DRAW), blue),
                List.of(entry("a", "RED", CommunityMatchResult.WIN), blue))) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Judo",
                            "JUDO_IPPON", entries));
        }
    }

    @Test void rejectsFootballRaceAndJudgingNumbersAndForeignSport() {
        var blue = entry("b", "BLUE", CommunityMatchResult.LOSS);
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Judo", "JUDO_YUKO",
                        List.of(new CommunityRoundEntryRequest("a", "WHITE", CommunityMatchResult.WIN,
                                1, 0, false, false), blue)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Judo", "JUDO_IPPON",
                        List.of(new CommunityRoundEntryRequest("a", "WHITE", CommunityMatchResult.WIN,
                                0,0,false,false,1,null,null,null),blue)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Xadrez", "JUDO_IPPON",
                        List.of(entry("a","WHITE",CommunityMatchResult.WIN),blue)));
    }
}
