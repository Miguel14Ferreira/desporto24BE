package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityJudgedResultsTest {
    private BigDecimal number(String x) { return x == null ? null : new BigDecimal(x); }
    private CommunityRoundEntryRequest entry(String mark, String wave1, String wave2) {
        return new CommunityRoundEntryRequest("player", null, CommunityMatchResult.DRAW,
                0, 0, false, false, null, null, null, null,
                number(mark), number(wave1), number(wave2));
    }
    private void accepted(String sport, String type, CommunityRoundEntryRequest e) {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(sport, type, List.of(e)));
    }
    private void rejected(String sport, String type, CommunityRoundEntryRequest e) {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(sport, type, List.of(e)));
    }
    @Test void surfAllowsManuallyGradedWavesAndNoOtherMetrics() {
        assertEquals("HEAT", CommunityRoundEventType.validate("Surf", "HEAT"));
        accepted("Surf", "HEAT", entry(null, "0.000", "10.000"));
        accepted("Surf", "HEAT", entry(null, "8.500", null));
        accepted("Surf", "HEAT", entry(null, null, null));
        rejected("Surf", "HEAT", entry(null, "-0.001", "5"));
        rejected("Surf", "HEAT", entry(null, "10.001", "5"));
        rejected("Surf", "HEAT", entry(null, "5", "1.1234"));
        rejected("Surf", "HEAT", entry("5", "6", "7"));
        rejected("Futebol", null, entry(null, "4", null));
        rejected("BMX", "FREESTYLE", entry(null, "4", null));
    }
    @Test void gymnasticsRequiresApparatusAndStoresOnlyManuallySuppliedFinalScore() {
        assertEquals("APPARATUS", CommunityRoundEventType.validate(
                "Ginástica Artística", "APPARATUS"));
        assertEquals("FLOOR", CommunityFieldDisciplines.validate(
                "Ginástica Artística", "APPARATUS", "FLOOR"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Ginástica Artística", "APPARATUS", null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Ginástica Artística", "APPARATUS", "LONG_JUMP"));
        accepted("Ginástica Artística", "APPARATUS", entry("15.350", null, null));
        accepted("Ginástica Artística", "APPARATUS", entry(null, null, null));
        rejected("Ginástica Artística", "APPARATUS", entry("-1", null, null));
        rejected("Ginástica Artística", "APPARATUS", entry("100.001", null, null));
        rejected("Ginástica Artística", "APPARATUS", entry("15", "5", null));
        rejected("Atletismo", "JUMP", entry("6", "5", null));
    }
}
