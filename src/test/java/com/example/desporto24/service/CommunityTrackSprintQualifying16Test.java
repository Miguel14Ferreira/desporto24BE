package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintQualifying16Test {
    private static CommunityRoundEntryResponse qualifyingEntry(String name, BigDecimal seconds) {
        return new CommunityRoundEntryResponse(null, name, name, null, null,
                CommunityMatchResult.DRAW, 0, 0, false, false,
                null, seconds, null, null);
    }
    private static CommunityRoundResponse qualifying(List<CommunityRoundEntryResponse> entries,
            String type) {
        return new CommunityRoundResponse(801L, "Qualificação 200 m",
                Instant.parse("2026-10-25T10:00:00Z"), entries,
                "Ciclismo de Pista", null, List.of(), type, null,
                List.of(), null, null, null, null, null);
    }
    private static List<CommunityRoundEntryResponse> entrants(int n) {
        List<CommunityRoundEntryResponse> result = new ArrayList<>();
        for (int i=1;i<=n;i++)
            result.add(qualifyingEntry("r" + i, BigDecimal.valueOf(10_000L+i).movePointLeft(3)));
        return result;
    }
    private static CommunityRoundResponse heat(String slot, String a, String b, String winner) {
        return new CommunityRoundResponse(100L, "Manga 1", Instant.now(),
                List.of(new CommunityRoundEntryResponse(null,a,a,null,null,
                                winner.equals(a) ? CommunityMatchResult.WIN : CommunityMatchResult.LOSS,
                                0,0,false,false),
                        new CommunityRoundEntryResponse(null,b,b,null,null,
                                winner.equals(b) ? CommunityMatchResult.WIN : CommunityMatchResult.LOSS,
                                0,0,false,false)),
                "Ciclismo de Pista",null,List.of(),"TRACK_SPRINT_HEAT",
                CommunityTrackSprintBracketPlanner.phase(slot),
                List.of(),null,null,null,
                CommunityTrackSprintBracketPlanner.seriesKey(41L,slot),1);
    }
    @Test void requiresUnambiguousTimedFlying200AndKeepsSixteenRanks() {
        var top16 = CommunityTrackSprintQualifying16.seeds(
                qualifying(entrants(18),"TRACK_FLYING_200"));
        assertEquals(16,top16.size());
        assertEquals("r1",top16.get(0));
        assertEquals("r16",top16.get(15));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintQualifying16.seeds(
                qualifying(entrants(15),"TRACK_FLYING_200")));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintQualifying16.seeds(
                qualifying(entrants(18),"TRACK_SCRATCH")));
    }
    @Test void refuses200mTiesIncludingCutoffRatherThanGuessingCommissionerDecision() {
        List<CommunityRoundEntryResponse> tied=new ArrayList<>(entrants(17));
        tied.set(16,qualifyingEntry("r17",BigDecimal.valueOf(10016,3)));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintQualifying16.seeds(
                qualifying(tied,"TRACK_FLYING_200")));
    }
    @Test void eighthFinalsFollowSixteenSeedOrderAndQuarterfinalsWaitForWinners() {
        List<String> seeds=CommunityTrackSprintQualifying16.seeds(
                qualifying(entrants(16),"TRACK_FLYING_200"));
        var all=CommunityTrackSprintBracketPlanner.project(41L,List.of(),seeds);
        assertEquals(16,all.size());
        assertEquals("r1",all.get(0).riderA());
        assertEquals("r16",all.get(0).riderB());
        assertEquals("READY",all.get(7).status());
        assertEquals("WAITING_PREVIOUS",all.get(8).status());
        List<CommunityRoundResponse> rounds=List.of(
                heat("R1","r1","r16","r1"),
                heat("R8","r8","r9","r8"));
        var next=CommunityTrackSprintBracketPlanner.project(41L,rounds,seeds);
        var q1=next.stream().filter(m->"Q1".equals(m.slot())).findFirst().orElseThrow();
        assertEquals(Set.of("r1","r8"),Set.of(q1.riderA(),q1.riderB()));
        assertEquals("READY",q1.status());
        assertDoesNotThrow(()->CommunityTrackSprintBracketPlanner.validateNewHeat(
                41L,"SP41-Q1","SPRINT_QUARTER_FINAL",rounds,
                List.of(new CommunityRoundEntryRequest("r8",null,CommunityMatchResult.WIN,0,0,false,false),
                        new CommunityRoundEntryRequest("r1",null,CommunityMatchResult.LOSS,0,0,false,false)),
                seeds));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintBracketPlanner.validateNewHeat(
                41L,"SP41-Q1","SPRINT_QUARTER_FINAL",rounds,
                List.of(new CommunityRoundEntryRequest("r1",null,CommunityMatchResult.WIN,0,0,false,false),
                        new CommunityRoundEntryRequest("r9",null,CommunityMatchResult.LOSS,0,0,false,false)),
                seeds));
    }
    @Test void eighthFinalNeedsJustOneWinnerAndCannotBeExtended() {
        var heats=List.of(heat("R1","r1","r16","r1"));
        var project=CommunityTrackSprintBracketPlanner.project(41L,heats,
                CommunityTrackSprintQualifying16.seeds(qualifying(entrants(16),"TRACK_FLYING_200")));
        assertEquals("FINISHED",project.get(0).status());
        assertEquals("r1",project.get(0).winner());
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintSeries.validate(
                "TRACK_SPRINT_HEAT","SPRINT_EIGHTH_FINAL","SP41-R1",2,
                List.of(new CommunityTrackSprintSeries.Heat("SPRINT_EIGHTH_FINAL",1,
                        "r1","r16","r1")),
                List.of(new CommunityRoundEntryRequest("r1",null,CommunityMatchResult.LOSS,0,0,false,false),
                        new CommunityRoundEntryRequest("r16",null,CommunityMatchResult.WIN,0,0,false,false))));
    }
}
