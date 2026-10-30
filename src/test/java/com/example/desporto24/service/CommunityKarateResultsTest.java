package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityKarateResultsTest {
    private CommunityRoundEntryRequest entry(String player, String side,
            CommunityMatchResult outcome, String score) {
        return new CommunityRoundEntryRequest(player, side, outcome,
                0, 0, false, false, null, null, null, null,
                score == null ? null : new BigDecimal(score));
    }
    private static final CommunityMatchResult W = CommunityMatchResult.WIN;
    private static final CommunityMatchResult L = CommunityMatchResult.LOSS;

    @Test void exposesOnlyExplicitFormatsForExistingKarateSport() {
        for (var type : new String[] {"KARATE_KUMITE_POINTS", "KARATE_KUMITE_SENSHU",
                "KARATE_KUMITE_HANTEI", "KARATE_KATA_JUDGES"}) {
            assertEquals(type, CommunityRoundEventType.validate("Karaté", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Karaté", type));
            assertFalse(CommunityRoundEventType.isRaceEvent("Karaté", type));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Karaté", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol", "KARATE_KUMITE_POINTS"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Karaté", "KARATE_KUMITE_HANSOKU"));
    }

    @Test void kumitePointsRequireCorrectWinnerAndWholeNumberTotals() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Karaté",
                "KARATE_KUMITE_POINTS", List.of(entry("red","AKA",W,"8"),entry("blue","AO",L,"0"))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Karaté",
                "KARATE_KUMITE_POINTS", List.of(entry("red","AKA",L,"3"),entry("blue","AO",W,"5"))));
        assertInvalid("KARATE_KUMITE_POINTS","8","8",W,L);
        assertInvalid("KARATE_KUMITE_POINTS","2","3",W,L);
        assertInvalid("KARATE_KUMITE_POINTS","3.5","2",W,L);
        assertInvalid("KARATE_KUMITE_POINTS",null,"2",W,L);
        assertInvalid("KARATE_KUMITE_POINTS","-1","0",W,L);
    }

    @Test void tiedScoresRequireConfirmedDecisionsNeverComputedAutomatically() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Karaté",
                "KARATE_KUMITE_SENSHU", List.of(entry("red","AKA",W,"1"),entry("blue","AO",L,"1"))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Karaté",
                "KARATE_KUMITE_HANTEI", List.of(entry("red","AKA",L,"0"),entry("blue","AO",W,"0"))));
        assertInvalid("KARATE_KUMITE_SENSHU","0","0",W,L);
        assertInvalid("KARATE_KUMITE_HANTEI","2","1",W,L);
        assertInvalid("KARATE_KUMITE_SENSHU","3","2",W,L);
        assertInvalid("KARATE_KUMITE_HANTEI","1","1",W,W);
    }

    @Test void kataUsesJudgesDecisionAndNeverAggregatesMarks() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Karaté",
                "KARATE_KATA_JUDGES", List.of(entry("red","AKA",W,null),entry("blue","AO",L,null))));
        assertInvalid("KARATE_KATA_JUDGES","8","9",W,L);
        assertInvalid("KARATE_KATA_JUDGES",null,null,W,W);
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Judo", "KARATE_KATA_JUDGES",
                        List.of(entry("red","AKA",W,null),entry("blue","AO",L,null))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Karaté", "KARATE_KATA_JUDGES",
                        List.of(entry("red","WHITE",W,null),entry("blue","AO",L,null))));
    }

    private void assertInvalid(String type, String red, String blue,
            CommunityMatchResult r, CommunityMatchResult b) {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Karaté", type,
                        List.of(entry("red","AKA",r,red), entry("blue","AO",b,blue))), type);
    }
}
