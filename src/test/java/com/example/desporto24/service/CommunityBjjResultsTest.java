package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityBjjResultsTest {
    private CommunityRoundEntryRequest entry(String name, String team,
            CommunityMatchResult result, String points) {
        return new CommunityRoundEntryRequest(name, team, result,
                0, 0, false, false, null, null, null, null,
                points == null ? null : new BigDecimal(points));
    }
    private static final CommunityMatchResult W = CommunityMatchResult.WIN;
    private static final CommunityMatchResult L = CommunityMatchResult.LOSS;
    private static final CommunityMatchResult D = CommunityMatchResult.DRAW;
    private List<CommunityRoundEntryRequest> match(String a, String b,
            CommunityMatchResult resultA, CommunityMatchResult resultB) {
        return List.of(entry("a", "A", resultA, a), entry("b", "B", resultB, b));
    }

    @Test void onlyOfficiallyListedBjjExistingCatalogueHasTypedEvents() {
        assertEquals("BJJ_IBJJF_GI_POINTS",
                CommunityRoundEventType.validate("Jiu-Jitsu Brasileiro", "BJJ_IBJJF_GI_POINTS"));
        assertEquals("BJJ_IBJJF_GI_SUBMISSION",
                CommunityRoundEventType.validate("Jiu-Jitsu Brasileiro", "BJJ_IBJJF_GI_SUBMISSION"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Jiu-Jitsu Brasileiro", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Judo", "BJJ_IBJJF_GI_POINTS"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Jiu-Jitsu Brasileiro", "BJJ_IBJJF_NO_GI_POINTS"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Jiu-Jitsu Brasileiro", "BJJ_IBJJF_GI_POINTS"));
    }

    @Test void submissionAcceptsConfirmedWinButNoInventedPointTotals() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                "BJJ_IBJJF_GI_SUBMISSION", match(null,null,W,L)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_SUBMISSION", match("4",null,W,L)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_SUBMISSION", match(null,null,D,D)));
    }

    @Test void clearPointsWinRequiresCorrectWinnerAndNonTiedNonFractionalTotals() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                "BJJ_IBJJF_GI_POINTS", match("4","2",W,L)));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                "BJJ_IBJJF_GI_POINTS", match("0","7",L,W)));
        for (String bad : new String[] {null,"1","1.5","-2","1000000"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                            "BJJ_IBJJF_GI_POINTS", match(bad,"0",W,L)), "invalid: "+bad);
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_POINTS", match("4","4",W,L)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_POINTS", match("2","4",W,L)));
    }

    @Test void rejectsWrongSidesSameIdentityAndOtherSportMetrics() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_POINTS",
                        List.of(entry("one","A",W,"4"),entry("two","A",L,"2"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_POINTS",
                        List.of(entry("same","A",W,"4"),entry("same","B",L,"2"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Futebol",
                        "BJJ_IBJJF_GI_POINTS", match("4","2",W,L)));
        var wrongStats = new CommunityRoundEntryRequest("a","A",W,2,0,false,false,
                null,null,null,null,new BigDecimal("4"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Jiu-Jitsu Brasileiro",
                        "BJJ_IBJJF_GI_POINTS",
                        List.of(wrongStats,entry("b","B",L,"2"))));
    }
}
