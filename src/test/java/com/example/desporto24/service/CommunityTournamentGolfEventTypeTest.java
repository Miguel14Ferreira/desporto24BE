package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentScoringMode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class CommunityTournamentGolfEventTypeTest {
    @Test
    void golfStrokeFormatsMustNotFallBackToUnrelatedTournamentScoringEngines() {
        for (String event : new String[] {"GOLF_STROKE_9", "GOLF_STROKE_18"}) {
            for (TournamentScoringMode mode : new TournamentScoringMode[] {
                    TournamentScoringMode.RACE_POINTS,
                    TournamentScoringMode.CHESS_HALF_POINTS,
                    TournamentScoringMode.FIELD_BEST_MARK
            }) {
                ResponseStatusException error = assertThrows(ResponseStatusException.class,
                        () -> CommunityTournamentEventType.validate(
                                "Golfe", event, TournamentEntryType.INDIVIDUAL, mode));
                assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
            }
            assertThrows(ResponseStatusException.class,
                    () -> CommunityTournamentEventType.validate(
                            "Golfe", event, TournamentEntryType.TEAM, null));
        }
    }

    @Test
    void dedicatedGolfModeAcceptsOnlyIndividualNineOrEighteenHoleEvents() {
        for (String event : new String[] {"GOLF_STROKE_9", "GOLF_STROKE_18"}) {
            assertEquals(event, CommunityTournamentEventType.validate(
                    "Golfe", event, TournamentEntryType.INDIVIDUAL,
                    TournamentScoringMode.GOLF_GROSS_STROKES));
            assertNull(CommunityTournamentEventType.validateDiscipline(
                    "Golfe", event, null, TournamentScoringMode.GOLF_GROSS_STROKES));
            assertThrows(ResponseStatusException.class,
                    () -> CommunityTournamentEventType.validate(
                            "Golfe", event, TournamentEntryType.TEAM,
                            TournamentScoringMode.GOLF_GROSS_STROKES));
            assertThrows(ResponseStatusException.class,
                    () -> CommunityTournamentEventType.validateDiscipline(
                            "Golfe", event, "EXTRA", TournamentScoringMode.GOLF_GROSS_STROKES));
        }
        assertThrows(ResponseStatusException.class,
                () -> CommunityTournamentEventType.validate(
                        "Golfe", "GOLF_STABLEFORD", TournamentEntryType.INDIVIDUAL,
                        TournamentScoringMode.GOLF_GROSS_STROKES));
        assertThrows(ResponseStatusException.class,
                () -> CommunityTournamentEventType.validate(
                        "Futebol", "GOLF_STROKE_9", TournamentEntryType.INDIVIDUAL,
                        TournamentScoringMode.GOLF_GROSS_STROKES));
    }

    @Test
    void golfStrokeFormatsCannotBeReusedByOtherSports() {
        assertThrows(ResponseStatusException.class,
                () -> CommunityTournamentEventType.validate("Futebol", "GOLF_STROKE_9",
                        TournamentEntryType.TEAM, null));
    }
}
