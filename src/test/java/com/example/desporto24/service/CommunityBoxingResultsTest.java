package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityBoxingResultsTest {
    private CommunityRoundEntryRequest boxer(String name, String side,
            CommunityMatchResult decision, BigDecimal inventedTotal, int goals) {
        return new CommunityRoundEntryRequest(name, side, decision,
                goals, 0, false, false, null, null, null, null, inventedTotal);
    }
    private static final CommunityMatchResult W = CommunityMatchResult.WIN;
    private static final CommunityMatchResult L = CommunityMatchResult.LOSS;
    private static final CommunityMatchResult D = CommunityMatchResult.DRAW;
    private List<CommunityRoundEntryRequest> bout(String red, String blue,
            CommunityMatchResult r, CommunityMatchResult b) {
        return List.of(boxer(red,"RED",r,null,0), boxer(blue,"BLUE",b,null,0));
    }

    @Test void registersOnlySupportedWorldBoxingDecisionsForExistingBoxe() {
        for (String type : new String[] {"BOXING_WB_POINTS","BOXING_WB_KO","BOXING_WB_RSC"}) {
            assertEquals(type, CommunityRoundEventType.validate("Boxe", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Boxe", type));
            assertFalse(CommunityRoundEventType.isRaceEvent("Boxe", type));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Boxe", type,
                    bout("boxer1","boxer2",W,L)));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Boxe", type,
                    bout("boxer1","boxer2",L,W)));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Boxe", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Boxe", "BOXING_PRO_DRAW"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Karaté", "BOXING_WB_KO"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Karaté","BOXING_WB_KO",
                        bout("a","b",W,L)));
    }

    @Test void rejectsTiesDuplicateSidesOrIdenticalBoxers() {
        for (String type : new String[] {"BOXING_WB_POINTS","BOXING_WB_KO","BOXING_WB_RSC"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,bout("a","b",D,D)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,bout("a","b",W,W)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,bout("a","a",W,L)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,
                            List.of(boxer("a","RED",W,null,0),boxer("b","RED",L,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,
                            List.of(boxer("a","BLUE",W,null,0))));
        }
    }

    @Test void neverAcceptsInventedJudgeTotalsOrOtherSportsMetrics() {
        for (String type : new String[] {"BOXING_WB_POINTS","BOXING_WB_KO","BOXING_WB_RSC"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,
                            List.of(boxer("a","RED",W,new BigDecimal("30"),0),
                                    boxer("b","BLUE",L,null,0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Boxe",type,
                            List.of(boxer("a","RED",W,null,1),
                                    boxer("b","BLUE",L,null,0))));
        }
    }
}
