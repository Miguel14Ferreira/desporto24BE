package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityChessGameTest {
    private CommunityRoundEntryRequest entry(String user, String color,
            CommunityMatchResult outcome) {
        return new CommunityRoundEntryRequest(user, color, outcome, 0, 0, false, false);
    }

    @Test void catalogueAndEventAreExplicitAndNeutralToGenericSeasonStandings() {
        assertEquals("CHESS_GAME", CommunityRoundEventType.validate("Xadrez", "CHESS_GAME"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Xadrez", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol", "CHESS_GAME"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Xadrez", "CHESS_GAME"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Xadrez", "CHESS_GAME"));
    }

    @Test void acceptsBothDecisiveDirectionsAndARealDraw() {
        for (CommunityMatchResult[] results : new CommunityMatchResult[][] {
                {CommunityMatchResult.WIN, CommunityMatchResult.LOSS},
                {CommunityMatchResult.LOSS, CommunityMatchResult.WIN},
                {CommunityMatchResult.DRAW, CommunityMatchResult.DRAW}}) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Xadrez",
                    "CHESS_GAME", List.of(entry("brancas", "WHITE", results[0]),
                                           entry("pretas", "BLACK", results[1]))));
        }
    }

    @Test void rejectsUnpairedResultsMissingOrRepeatedColorsAndExtraPlayers() {
        var white = entry("a", "WHITE", CommunityMatchResult.WIN);
        var black = entry("b", "BLACK", CommunityMatchResult.LOSS);
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(white)));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(white, white)));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(white, black, black)));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(
                        white, entry("b","BLACK",CommunityMatchResult.DRAW))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(
                        entry("a","WHITE",CommunityMatchResult.DRAW), black)));
    }

    @Test void rejectsFootballStatisticsOrNonChessNumericMarks() {
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(
                        new CommunityRoundEntryRequest("a", "WHITE", CommunityMatchResult.WIN,
                                1, 0, false, false),
                        entry("b", "BLACK", CommunityMatchResult.LOSS))));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType
                .validateEntries("Xadrez", "CHESS_GAME", List.of(
                        new CommunityRoundEntryRequest("a", "WHITE", CommunityMatchResult.WIN,
                                0, 0, false, false, 1, null, null, null),
                        entry("b", "BLACK", CommunityMatchResult.LOSS))));
    }
}
