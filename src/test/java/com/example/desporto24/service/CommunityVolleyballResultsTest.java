package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunitySetScore;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityVolleyballResultsTest {
    private CommunityRoundEntryRequest p(String id, String team) {
        return new CommunityRoundEntryRequest(id, team, CommunityMatchResult.DRAW,
                0, 0, false, false);
    }
    private List<CommunityRoundEntryRequest> indoor = List.of(p("a","A"),p("b","B"));
    private List<CommunityRoundEntryRequest> beach =
            List.of(p("a1","A"),p("a2","A"),p("b1","B"),p("b2","B"));
    private CommunitySetScore s(int a, int b) { return new CommunitySetScore(a,b); }
    private String footvolley(String eventType, List<CommunitySetScore> sets) {
        return CommunityVolleyballResults.validateAndEncode(
                "Futevólei",eventType,null,sets,beach);
    }
    private String indoor(List<CommunitySetScore> sets) {
        return CommunityVolleyballResults.validateAndEncode(
                "Voleibol","VOLLEYBALL_FINAL",null,sets,indoor);
    }
    private String beach(List<CommunitySetScore> sets) {
        return CommunityVolleyballResults.validateAndEncode(
                "Voleibol de Praia","BEACH_VOLLEY_FINAL",null,sets,beach);
    }

    @Test void indoorBestOfFiveValidatesDecidingFifthAndUnlimitedDeuce() {
        assertEquals("VOLLEYBALL_FINAL",CommunityRoundEventType.validate("Voleibol","VOLLEYBALL_FINAL"));
        assertEquals("25:21,26:24,25:23", indoor(List.of(s(25,21),s(26,24),s(25,23))));
        assertEquals("25:23,22:25,25:10,23:25,17:15",
                indoor(List.of(s(25,23),s(22,25),s(25,10),s(23,25),s(17,15))));
        assertEquals("30:28,25:22,25:0",indoor(List.of(s(30,28),s(25,22),s(25,0))));
        assertThrows(IllegalArgumentException.class,() ->
                indoor(List.of(s(25,23),s(25,15),s(25,21),s(25,10))));
        assertThrows(IllegalArgumentException.class,() ->
                indoor(List.of(s(25,23),s(25,15))));
        assertThrows(IllegalArgumentException.class,() ->
                indoor(List.of(s(25,23),s(23,25),s(25,19),s(18,25),s(15,14))));
    }
    @Test void beachBestOfThreeRequires21AndDeciding15WithTwoPointLead() {
        assertEquals("BEACH_VOLLEY_FINAL",
                CommunityRoundEventType.validate("Voleibol de Praia","BEACH_VOLLEY_FINAL"));
        assertEquals("21:18,22:20",beach(List.of(s(21,18),s(22,20))));
        assertEquals("21:19,19:21,15:13",beach(List.of(s(21,19),s(19,21),s(15,13))));
        assertEquals("21:19,19:21,40:38",beach(List.of(s(21,19),s(19,21),s(40,38))));
        assertThrows(IllegalArgumentException.class,() ->
                beach(List.of(s(21,19),s(21,19),s(15,13))));
        assertThrows(IllegalArgumentException.class,() ->
                beach(List.of(s(21,19),s(19,21),s(15,14))));
        assertEquals("25:23,21:19",beach(List.of(s(25,23),s(21,19))));
    }
    @Test void footvolleyCompetitionFormatsUseTheirOwnTargetsAndPairs() {
        for (String type : List.of("FOOTVOLLEY_SINGLE_18",
                "FOOTVOLLEY_BEST_OF_3", "FOOTVOLLEY_SINGLE_15")) {
            assertEquals(type, CommunityRoundEventType.validate("Futevólei", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Futevólei", type));
            assertTrue(CommunityVolleyballResults.isVolleyballEvent(type));
        }
        assertEquals("18:16",footvolley("FOOTVOLLEY_SINGLE_18",List.of(s(18,16))));
        assertEquals("23:21",footvolley("FOOTVOLLEY_SINGLE_18",List.of(s(23,21))));
        assertEquals("15:11",footvolley("FOOTVOLLEY_SINGLE_15",List.of(s(15,11))));
        assertEquals("18:16,18:13",footvolley("FOOTVOLLEY_BEST_OF_3",
                List.of(s(18,16),s(18,13))));
        assertEquals("18:16,16:18,17:15",footvolley("FOOTVOLLEY_BEST_OF_3",
                List.of(s(18,16),s(16,18),s(17,15))));
        assertThrows(IllegalArgumentException.class, () -> footvolley(
                "FOOTVOLLEY_SINGLE_18",List.of(s(15,11))));
        assertThrows(IllegalArgumentException.class, () -> footvolley(
                "FOOTVOLLEY_SINGLE_15",List.of(s(18,11))));
        assertThrows(IllegalArgumentException.class, () -> footvolley(
                "FOOTVOLLEY_BEST_OF_3",List.of(s(18,16),s(18,16),s(15,11))));
        assertThrows(IllegalArgumentException.class, () -> footvolley(
                "FOOTVOLLEY_BEST_OF_3",List.of(s(18,16),s(16,18),s(15,14))));
        assertThrows(IllegalArgumentException.class, () -> footvolley(
                "FOOTVOLLEY_SINGLE_18",List.of(s(18,18))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Voleibol de Praia","FOOTVOLLEY_SINGLE_18"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityVolleyballResults.validateAndEncode(
                        "Futevólei","BEACH_VOLLEY_FINAL",null,List.of(s(21,18),s(21,17)),beach));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityVolleyballResults.validateAndEncode("Futevólei",
                        "FOOTVOLLEY_SINGLE_18",null,List.of(s(18,15)),indoor));
    }
    @Test void detectsIncompleteImpossibleOrIncorrectSetScores() {
        for (CommunitySetScore first : List.of(
                s(24,24),s(25,24),s(26,23),s(24,20),s(-1,25),
                s(1000,998),s(30,27))) {
            assertThrows(IllegalArgumentException.class,() ->
                    indoor(List.of(first,s(25,20),s(25,12))));
        }
        assertThrows(IllegalArgumentException.class, () ->
                indoor(List.of(s(25,20),s(20,25),s(25,20),s(20,25),s(15,15))));
        assertThrows(IllegalArgumentException.class, () ->
                beach(List.of(s(21,20),s(21,18))));
        assertThrows(IllegalArgumentException.class, () ->
                beach(List.of(s(21,18),s(21,18),s(21,18))));
    }
    @Test void rejectFootballStatsOtherSportsAndBadRoster() {
        assertThrows(IllegalArgumentException.class,() ->
                CommunityRoundEventType.validate("Futebol","VOLLEYBALL_FINAL"));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityRoundEventType.validate("Voleibol","BEACH_VOLLEY_FINAL"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Voleibol","VOLLEYBALL_FINAL"));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Voleibol","VOLLEYBALL_FINAL",indoor));
        assertEquals(List.of(s(25,21),s(25,20),s(25,19)),CommunityRacketResults.decode(
                indoor(List.of(s(25,21),s(25,20),s(25,19)))));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityVolleyballResults.validateAndEncode("Voleibol","VOLLEYBALL_FINAL",
                        "DOUBLES",List.of(s(25,19),s(25,18),s(25,17)),indoor));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityVolleyballResults.validateAndEncode("Voleibol","VOLLEYBALL_FINAL",
                        null,List.of(s(25,19),s(25,18),s(25,17)),List.of(p("a","A"),p("b","A"))));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityVolleyballResults.validateAndEncode("Voleibol de Praia","BEACH_VOLLEY_FINAL",
                        null,List.of(s(21,19),s(21,19)),List.of(p("a","A"),p("b","B"))));
        var bad = new CommunityRoundEntryRequest("a","A",CommunityMatchResult.WIN,0,0,false,false);
        assertThrows(IllegalArgumentException.class,() ->
                CommunityVolleyballResults.validateAndEncode("Voleibol","VOLLEYBALL_FINAL",
                        null,List.of(s(25,19),s(25,18),s(25,17)),List.of(bad,p("b","B"))));
    }
}
