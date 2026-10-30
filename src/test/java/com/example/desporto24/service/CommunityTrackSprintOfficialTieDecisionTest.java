package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.TieDecision;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintOfficialTieDecisionTest {
    private static BigDecimal seconds(int mills) {
        return BigDecimal.valueOf(mills, 3);
    }
    private static CommunityRoundEntryResponse rider(String username, int mills) {
        return new CommunityRoundEntryResponse(null, username, username, null, null,
                CommunityMatchResult.DRAW,0,0,false,false,
                null,seconds(mills),null,null);
    }
    private static CommunityRoundResponse qualifier(List<CommunityRoundEntryResponse> entries) {
        return new CommunityRoundResponse(100L,"200 m official fixture",
                Instant.parse("2026-10-25T10:00:00Z"),entries,
                "Ciclismo de Pista",null,List.of(),"TRACK_FLYING_200",
                null,List.of(),null,null,null,null,null);
    }
    private static List<CommunityRoundEntryResponse> riders(int n) {
        List<CommunityRoundEntryResponse> all=new ArrayList<>();
        for(int i=1;i<=n;i++) all.add(rider("r"+i,10000+i));
        return all;
    }
    private static TieDecision hundred(String a, String b, int a100, int b100,
                                        List<String> order) {
        return new TieDecision("LAST_100",null,order,
                Map.of(a,seconds(a100),b,seconds(b100)),
                "Official timing sheet 2026, 200 m event #41");
    }
    private static TieDecision draw(String a, String b, String reason,
                                    Map<String,BigDecimal> times,
                                    List<String> order) {
        return new TieDecision("DRAW_LOTS",reason,order,times,
                "Witnessed draw by commissaires, protocol #41/2026");
    }
    private static void fails(CommunityRoundResponse round,int starters,
                              List<TieDecision> evidence) {
        assertThrows(IllegalArgumentException.class,
                ()->CommunityTrackSprintQualifying16.seeds(round,starters,evidence));
    }

    @Test void last100mBreaksOlympic200mTieOnlyFromExplicitPreciseOfficialTimes() {
        var entries=riders(24);
        entries.set(1,rider("r2",10001));
        var round=qualifier(entries);
        fails(round,24,List.of()); // NEVER alphabetically order a dead heat
        var official=hundred("r1","r2",5100,5050,List.of("r2","r1"));
        var ordered=CommunityTrackSprintQualifying16.seeds(round,24,List.of(official));
        assertEquals(24,ordered.size());
        assertEquals(List.of("r2","r1","r3"),ordered.subList(0,3));
        // This is a verifiable official decision, not an arbitrary text order.
        fails(round,24,List.of(hundred("r1","r2",5100,5050,List.of("r1","r2"))));
        fails(round,24,List.of(hundred("r1","r2",5100,5100,List.of("r1","r2"))));
        fails(round,24,List.of(hundred("r1","r2",10500,5000,List.of("r2","r1"))));
        fails(round,24,List.of(new TieDecision("LAST_100",null,List.of("r1","r2"),
                Map.of("r1",seconds(5000)),"Official sheet with missing second split")));
    }

    @Test void drawWhenLast100UnavailableNeedsWitnessedDocumentAndExactGroup() {
        var entries=riders(24);
        entries.set(2,rider("r3",10002));
        var round=qualifier(entries);
        var evidence=draw("r2","r3","LAST_100_UNAVAILABLE",
                Map.of(),List.of("r3","r2"));
        var selected=CommunityTrackSprintQualifying16.seeds(round,24,List.of(evidence));
        assertEquals("r3",selected.get(1));
        assertEquals("r2",selected.get(2));
        fails(round,24,List.of(new TieDecision("DRAW_LOTS","LAST_100_UNAVAILABLE",
                List.of("r3","r2"),Map.of(),"x")));
        fails(round,24,List.of(draw("r2","r3","LAST_100_UNAVAILABLE",
                Map.of("r2",seconds(5000),"r3",seconds(5000)),List.of("r3","r2"))));
        fails(round,24,List.of(draw("r2","r3",null,Map.of(),List.of("r3","r2"))));
        fails(round,24,List.of(draw("r2","r3","LAST_100_UNAVAILABLE",
                Map.of(),List.of("r3","unqualified"))));
    }

    @Test void last100StillTiedAllowsDrawButNeverOverridesUnequal100m() {
        var entries=riders(16);
        entries.set(1,rider("r2",10001));
        var round=qualifier(entries);
        var draw=draw("r1","r2","LAST_100_STILL_TIED",
                Map.of("r1",seconds(4900),"r2",seconds(4900)),List.of("r2","r1"));
        assertEquals("r2",CommunityTrackSprintQualifying16.seeds(round,16,List.of(draw)).get(0));
        fails(round,16,List.of(draw("r1","r2","LAST_100_STILL_TIED",
                Map.of("r1",seconds(4900),"r2",seconds(4950)),List.of("r2","r1"))));
        fails(round,16,List.of(draw("r1","r2","LAST_100_STILL_TIED",
                Map.of(),List.of("r2","r1"))));
    }

    @Test void cutoffTieMustIncludeAllTiedRidersEvenWhenOnlyOneCanQualify() {
        var entries=riders(26);
        entries.set(24,rider("r25",10024)); // 24th and 25th 200 m times equal
        var round=qualifier(entries);
        fails(round,24,List.of());
        var last=hundred("r24","r25",5100,4900,List.of("r25","r24"));
        var seeded=CommunityTrackSprintQualifying16.seeds(round,24,List.of(last));
        assertTrue(seeded.contains("r25"));
        assertFalse(seeded.contains("r24"));
        assertEquals("r25",seeded.get(23));
        fails(round,24,List.of(hundred("r24","r25",5100,4900,List.of("r25","r24")),
                new TieDecision("DRAW_LOTS","LAST_100_UNAVAILABLE",
                        List.of("r25","r24"),Map.of(),"Duplicate official decision #41")));
    }

    @Test void untouchedUntiedQualificationRejectsAnyFakeOverridingDecision() {
        var round=qualifier(riders(28));
        assertEquals("r1",CommunityTrackSprintQualifying16.seeds(round,28,List.of()).get(0));
        fails(round,28,List.of(draw("r1","r2","LAST_100_UNAVAILABLE",
                Map.of(),List.of("r2","r1"))));
    }

    @Test void tiesBeyondEliminationCutoffAreIrrelevantToSelectedSeeds() {
        var entries=riders(28);
        entries.set(27,rider("r28",10027));
        var round=qualifier(entries);
        var top=CommunityTrackSprintQualifying16.seeds(round,24,List.of());
        assertEquals("r24",top.get(23));
        fails(round,24,List.of(draw("r27","r28","LAST_100_UNAVAILABLE",
                Map.of(),List.of("r28","r27"))));
    }

    @Test void noResultCanBeInventedForWrongSportRoundOrMissingTimes() {
        fails(null,24,List.of());
        fails(qualifier(riders(23)),24,List.of());
        fails(qualifier(riders(24)),12,List.of());
        var entries=riders(24);
        entries.set(1,rider("r1",10002));
        fails(qualifier(entries),24,List.of());
    }
}
