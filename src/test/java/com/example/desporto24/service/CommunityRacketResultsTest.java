package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunitySetScore;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRacketResultsTest {
    private static CommunityRoundEntryRequest player(String name, String team,
            CommunityMatchResult result) {
        return new CommunityRoundEntryRequest(name, team, result,
                0, 0, false, false);
    }
    private static List<CommunitySetScore> sets() {
        return List.of(new CommunitySetScore(6, 4), new CommunitySetScore(3, 6),
                new CommunitySetScore(7, 5));
    }
    @Test void supportsRacketSportsOnlyAndPreservesLegacy() {
        for (String sport : List.of("Ténis", "Padel", "Badminton", "Squash",
                "Pickleball", "Ténis de mesa", "Ténis de praia")) {
            assertTrue(CommunityRacketResults.isSupported(sport));
        }
        assertFalse(CommunityRacketResults.isSupported("Futebol"));
        assertNull(CommunityRacketResults.validateAndEncode("Futebol", null, null,
                List.of(player("a", "A", CommunityMatchResult.WIN))));
    }
    @Test void recordsCompletedSinglesAndDoublesWithConsistentWinner() {
        var singles = List.of(player("a", "A", CommunityMatchResult.WIN),
                player("b", "B", CommunityMatchResult.LOSS));
        String encoded = CommunityRacketResults.validateAndEncode(
                "Ténis", "SINGLES", sets(), singles);
        assertEquals("6:4,3:6,7:5", encoded);
        assertEquals(sets(), CommunityRacketResults.decode(encoded));
        var doubles = List.of(player("a", "A", CommunityMatchResult.WIN),
                player("a2", "A", CommunityMatchResult.WIN),
                player("b", "B", CommunityMatchResult.LOSS),
                player("b2", "B", CommunityMatchResult.LOSS));
        assertEquals("6:4,3:6,7:5", CommunityRacketResults.validateAndEncode(
                "Padel", "DOUBLES", sets(), doubles));
    }
    @Test void rejectsMismatchedSidesScoresAndNonRacketSports() {
        var entries = List.of(player("a", "A", CommunityMatchResult.WIN),
                player("b", "B", CommunityMatchResult.LOSS));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Futebol", "SINGLES", sets(), entries));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Ténis", "DOUBLES", sets(), entries));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Ténis", "SINGLES", List.of(new CommunitySetScore(6, 6)), entries));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Ténis", "SINGLES", List.of(
                        new CommunitySetScore(6,4), new CommunitySetScore(4,6)), entries));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Ténis", "SINGLES", sets(), List.of(
                        player("a", "A", CommunityMatchResult.LOSS),
                        player("b", "B", CommunityMatchResult.WIN))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRacketResults
                .validateAndEncode("Ténis", "SINGLES", sets(), List.of(
                        player("a", "A", CommunityMatchResult.WIN),
                        player("b", "A", CommunityMatchResult.LOSS))));
    }
}
