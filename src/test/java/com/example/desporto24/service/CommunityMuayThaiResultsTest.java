package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityMuayThaiResultsTest {
    private static final CommunityMatchResult W = CommunityMatchResult.WIN;
    private static final CommunityMatchResult L = CommunityMatchResult.LOSS;
    private static final CommunityMatchResult D = CommunityMatchResult.DRAW;

    private CommunityRoundEntryRequest fighter(String user, String corner,
            CommunityMatchResult result, BigDecimal fakeJudgeTotal, int goals) {
        return new CommunityRoundEntryRequest(user,corner,result,goals,0,false,false,
                null,null,null,null,fakeJudgeTotal);
    }
    private List<CommunityRoundEntryRequest> contest(CommunityMatchResult red,
            CommunityMatchResult blue) {
        return List.of(fighter("red","RED",red,null,0),
                fighter("blue","BLUE",blue,null,0));
    }

    @Test void fourDistinct2026IfmaFullContactOutcomesOnlyForExistingMuayThai() {
        for (String type : new String[] {"IFMA_FULL_WP","IFMA_FULL_KO",
                "IFMA_FULL_RSCS","IFMA_FULL_RSCI"}) {
            assertEquals(type,CommunityRoundEventType.validate("Muay Thai",type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Muay Thai",type));
            assertFalse(CommunityRoundEventType.isRaceEvent("Muay Thai",type));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Muay Thai",type,contest(W,L)));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Muay Thai",type,contest(L,W)));
        }
        assertFalse(CommunityMuayThaiResults.isMuayThaiEvent(null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Muay Thai",null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Muay Thai","IFMA_WAI_KRU"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Kickboxing","IFMA_FULL_KO"));
    }

    @Test void rejectsBothWinsBothDrawsWrongCornerOrSingleContestant() {
        for (String type : new String[] {"IFMA_FULL_WP","IFMA_FULL_KO",
                "IFMA_FULL_RSCS","IFMA_FULL_RSCI"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,contest(W,W)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,contest(D,D)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,List.of(
                            fighter("a","RED",W,null,0),fighter("b","RED",L,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,List.of(
                            fighter("a","RED",W,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,List.of(
                            fighter("a","RED",W,null,0),fighter("a","BLUE",L,null,0))));
        }
    }

    @Test void neverAllowsMadeUpJudgeTotalsOrUnrelatedSportMetrics() {
        for (String type : new String[] {"IFMA_FULL_WP","IFMA_FULL_KO",
                "IFMA_FULL_RSCS","IFMA_FULL_RSCI"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,List.of(
                            fighter("a","RED",W,new BigDecimal("30"),0),
                            fighter("b","BLUE",L,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Muay Thai",type,List.of(
                            fighter("a","RED",W,null,1),fighter("b","BLUE",L,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,contest(W,L)));
        }
    }
}
