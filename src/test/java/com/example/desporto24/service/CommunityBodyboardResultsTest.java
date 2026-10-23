package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityBodyboardResultsTest {
    private BigDecimal number(String n) { return n == null ? null : new BigDecimal(n); }
    private CommunityRoundEntryRequest entry(String aggregate, String first, String second) {
        return new CommunityRoundEntryRequest(
                "participant", null, CommunityMatchResult.DRAW, 0, 0, false, false,
                null, null, null, null, number(aggregate), number(first), number(second));
    }
    private void accepts(CommunityRoundEntryRequest e) {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Bodyboard", "BODYBOARD_HEAT", List.of(e)));
    }
    private void rejects(CommunityRoundEntryRequest e) {
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Bodyboard", "BODYBOARD_HEAT", List.of(e)));
    }

    @Test void bodyboardIsAnExplicitHeatTypeNotAnAliasForSurf() {
        assertEquals("BODYBOARD_HEAT",
                CommunityRoundEventType.validate("Bodyboard", "BODYBOARD_HEAT"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Bodyboard", "HEAT"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Surf", "BODYBOARD_HEAT"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol", "BODYBOARD_HEAT"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Bodyboard", null));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Bodyboard", "BODYBOARD_HEAT"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Bodyboard", "BODYBOARD_HEAT"));
    }

    @Test void disciplinesRequireAnExplicitBodyboardStance() {
        assertEquals("PRONE", CommunityFieldDisciplines.validate(
                "Bodyboard", "BODYBOARD_HEAT", "PRONE"));
        assertEquals("DROP_KNEE", CommunityFieldDisciplines.validate(
                "Bodyboard", "BODYBOARD_HEAT", "DROP_KNEE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Bodyboard", "BODYBOARD_HEAT", null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Bodyboard", "BODYBOARD_HEAT", "FLOOR"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Surf", "HEAT", "PRONE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Bodyboard", null, "PRONE"));
    }

    @Test void waveScoresAreOptionalBoundedAndNeverAggregateOrFootballMetrics() {
        accepts(entry(null, "0", "10"));
        accepts(entry(null, "9.675", null));
        accepts(entry(null, null, null));
        rejects(entry(null, "-0.001", "5"));
        rejects(entry(null, "10.001", "6"));
        rejects(entry(null, "2", "1.0001"));
        rejects(entry("15.000", "8", "7"));
        rejects(entry("15.000", null, null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Bodyboard", "BODYBOARD_HEAT", List.of(
                        new CommunityRoundEntryRequest("participant", "A",
                                CommunityMatchResult.WIN, 1, 0, false, false,
                                null, null, null, null, null, number("5"), number("4")))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("BMX", "FREESTYLE",
                        List.of(entry(null, "6", null))));
    }
}
