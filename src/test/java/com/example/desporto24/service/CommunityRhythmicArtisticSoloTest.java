package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRhythmicArtisticSoloTest {
    private CommunityRoundEntryRequest score(String mark) {
        return new CommunityRoundEntryRequest("athlete", null, CommunityMatchResult.DRAW,
                0, 0, false, false, null, null, null, null,
                mark == null ? null : new BigDecimal(mark), null, null);
    }
    private void allowed(String sport, String type, String mark) {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                sport, type, List.of(score(mark))));
    }
    private void invalid(String sport, String type, String mark) {
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries(sport, type, List.of(score(mark))));
    }
    @Test void separateEventsAndMandatoryRhythmicApparatus() {
        assertEquals("RHYTHMIC_APPARATUS",
                CommunityRoundEventType.validate("Ginástica Rítmica","RHYTHMIC_APPARATUS"));
        assertEquals("ARTISTIC_SOLO_TECHNICAL",
                CommunityRoundEventType.validate("Natação Artística","ARTISTIC_SOLO_TECHNICAL"));
        assertEquals("ARTISTIC_SOLO_FREE",
                CommunityRoundEventType.validate("Natação Artística","ARTISTIC_SOLO_FREE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Natação Artística","RHYTHMIC_APPARATUS"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Ginástica Rítmica","ARTISTIC_SOLO_FREE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Ginástica Rítmica",null));
        assertEquals("HOOP", CommunityFieldDisciplines.validate(
                "Ginástica Rítmica","RHYTHMIC_APPARATUS","HOOP"));
        assertEquals("RIBBON", CommunityFieldDisciplines.validate(
                "Ginástica Rítmica","RHYTHMIC_APPARATUS","RIBBON"));
        for (String code : List.of("HOOP","BALL","CLUBS","RIBBON")) {
            assertEquals(code, CommunityFieldDisciplines.validate(
                    "Ginástica Rítmica","RHYTHMIC_APPARATUS",code));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Ginástica Rítmica","RHYTHMIC_APPARATUS",null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Ginástica Rítmica","RHYTHMIC_APPARATUS","FLOOR"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Natação Artística","ARTISTIC_SOLO_FREE","BALL"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Ginástica Rítmica","RHYTHMIC_APPARATUS"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Natação Artística","ARTISTIC_SOLO_FREE"));
    }
    @Test void rhythmicScoresRequireAtMostThreeDecimalsAndNeverInferAllAround() {
        allowed("Ginástica Rítmica","RHYTHMIC_APPARATUS","0");
        allowed("Ginástica Rítmica","RHYTHMIC_APPARATUS","34.875");
        allowed("Ginástica Rítmica","RHYTHMIC_APPARATUS",null);
        invalid("Ginástica Rítmica","RHYTHMIC_APPARATUS","-1");
        invalid("Ginástica Rítmica","RHYTHMIC_APPARATUS","34.8751");
        invalid("Ginástica Rítmica","RHYTHMIC_APPARATUS","1000000");
    }
    @Test void artisticSoloPreservesFourDecimalMarksAndRejectsOtherSports() {
        allowed("Natação Artística","ARTISTIC_SOLO_FREE","180.1234");
        allowed("Natação Artística","ARTISTIC_SOLO_TECHNICAL","210.0001");
        allowed("Natação Artística","ARTISTIC_SOLO_TECHNICAL",null);
        invalid("Natação Artística","ARTISTIC_SOLO_FREE","180.12345");
        invalid("Natação Artística","ARTISTIC_SOLO_FREE","-1");
        invalid("Natação Artística","ARTISTIC_SOLO_FREE","1000000.0000");
        invalid("Skate","SKATE_PARK","180.1234");
        invalid("Trampolim","TRAMPOLINE_INDIVIDUAL","40.1234");
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Natação Artística","ARTISTIC_SOLO_FREE",
                        List.of(new CommunityRoundEntryRequest("one","Team A",
                                CommunityMatchResult.WIN,1,0,false,false,
                                null,null,null,null,new BigDecimal("180.1234"),null,null))));
    }
}
