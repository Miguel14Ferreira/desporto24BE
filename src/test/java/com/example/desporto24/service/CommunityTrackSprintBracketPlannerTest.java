package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintBracketPlannerTest {
    private static final long BRACKET=41;
    private static CommunityRoundEntryResponse entry(String name, boolean won) {
        return new CommunityRoundEntryResponse(null,name,name,null,null,
                won?CommunityMatchResult.WIN:CommunityMatchResult.LOSS,0,0,false,false);
    }
    private static CommunityRoundResponse heat(String slot, int number,
            String a, String b, String winner) {
        return new CommunityRoundResponse((long)number, "Heat", Instant.parse("2026-10-25T10:00:00Z"),
                List.of(entry(a,a.equals(winner)),entry(b,b.equals(winner))),
                "Ciclismo de Pista",null,List.of(),"TRACK_SPRINT_HEAT",
                CommunityTrackSprintBracketPlanner.phase(slot), List.of(),null,null,null,
                CommunityTrackSprintBracketPlanner.seriesKey(BRACKET,slot),number);
    }
    private static List<CommunityRoundResponse> pair(String slot, String a, String b, String first, String second) {
        return List.of(heat(slot,1,a,b,first),heat(slot,2,a,b,second));
    }
    private static CommunityTrackSprintBracketDtos.Match match(
            List<CommunityRoundResponse> rounds, String slot) {
        return CommunityTrackSprintBracketPlanner.project(BRACKET,rounds).stream()
                .filter(m->m.slot().equals(slot)).findFirst().orElseThrow();
    }
    private static List<CommunityRoundEntryRequest> riders(String a,String b) {
        return List.of(
                new CommunityRoundEntryRequest(a,null,CommunityMatchResult.WIN,0,0,false,false),
                new CommunityRoundEntryRequest(b,null,CommunityMatchResult.LOSS,0,0,false,false));
    }
    @Test void doesNotGuessOrInventAnythingBeforeSeeding() {
        var empty=CommunityTrackSprintBracketPlanner.project(BRACKET,List.of());
        assertEquals(8,empty.size());
        assertEquals("READY", empty.get(0).status());
        assertEquals("WAITING_PREVIOUS", empty.get(4).status());
        assertNull(empty.get(7).winner());
        assertEquals("SP41-Q1",empty.get(0).seriesKey());
    }
    @Test void semifinalsRequireFourCompletedQuarterfinalSeries() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        rounds.addAll(pair("Q1","a","b","a","a"));
        rounds.addAll(pair("Q2","c","d","d","d"));
        rounds.addAll(pair("Q3","e","f","e","e"));
        rounds.addAll(pair("Q4","g","h","h","h"));
        var semi1=match(rounds,"S1");
        assertEquals("a",semi1.riderA()); assertEquals("d",semi1.riderB());
        assertEquals("READY",semi1.status());
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(BRACKET,"SP41-S1",
                        "SPRINT_SEMI_FINAL",rounds,riders("a","c")));
        assertDoesNotThrow(()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(BRACKET,"SP41-S1",
                        "SPRINT_SEMI_FINAL",rounds,riders("d","a")));
        rounds.add(heat("S1",1,"a","d","a"));
        assertEquals("IN_PROGRESS",match(rounds,"S1").status());
        assertEquals("WAITING_PREVIOUS",match(rounds,"G").status());
    }
    @Test void determinesGoldFromSemifinalWinnersAndBronzeFromLosersOnly() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        rounds.addAll(pair("Q1","a","b","a","a"));
        rounds.addAll(pair("Q2","c","d","d","d"));
        rounds.addAll(pair("Q3","e","f","f","f"));
        rounds.addAll(pair("Q4","g","h","h","h"));
        rounds.addAll(pair("S1","a","d","a","a"));
        rounds.addAll(pair("S2","f","h","h","h"));
        assertEquals(Set.of("a","h"),Set.of(match(rounds,"G").riderA(), match(rounds,"G").riderB()));
        assertEquals(Set.of("d","f"),Set.of(match(rounds,"B").riderA(), match(rounds,"B").riderB()));
        rounds.addAll(pair("G","a","h","h","h"));
        assertEquals("FINISHED",match(rounds,"G").status());
        assertEquals("h",match(rounds,"G").winner());
        assertNull(match(rounds,"B").winner()); // no invented bronze
    }
    @Test void twoWinsRequiredAndThreeHeatsAllowedOnlyForSplit() {
        List<CommunityRoundResponse> rounds=new ArrayList<>(pair("Q1","a","b","a","b"));
        assertEquals("IN_PROGRESS",match(rounds,"Q1").status());
        rounds.add(heat("Q1",3,"a","b","b"));
        assertEquals("b",match(rounds,"Q1").winner());
        assertEquals("FINISHED",match(rounds,"Q1").status());
        var impossible=new ArrayList<>(pair("Q1","a","b","a","a"));
        impossible.add(heat("Q1",3,"a","b","b"));
        assertEquals("CONFLICT",match(impossible,"Q1").status());
    }
    @Test void malformedOrDuplicateQuarterfinalsBlockProgression() {
        List<CommunityRoundResponse> rounds=new ArrayList<>(pair("Q1","a","b","a","a"));
        rounds.addAll(pair("Q2","a","d","d","d"));
        assertEquals("CONFLICT",match(rounds,"Q1").status());
        assertEquals("CONFLICT",match(rounds,"Q2").status());
        assertEquals("CONFLICT",match(rounds,"S1").status());
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(BRACKET,"SP41-Q3",
                        "SPRINT_QUARTER_FINAL",rounds,riders("a","f")));
    }
    @Test void existingUnlinkedHistoryIsNotCounted() {
        CommunityRoundResponse legacy=new CommunityRoundResponse(9L,"Historic",Instant.now(),
                List.of(entry("a",true),entry("b",false)),"Ciclismo de Pista",null,List.of(),
                "TRACK_SPRINT_HEAT","SPRINT_QUARTER_FINAL",List.of(),null,null,null,null,null);
        assertEquals("READY",match(List.of(legacy),"Q1").status());
    }
}
