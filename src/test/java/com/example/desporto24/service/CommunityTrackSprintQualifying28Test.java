package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintQualifying28Test {
    private static CommunityRoundResponse qualification(int count, boolean tie) {
        List<CommunityRoundEntryResponse> entries = new ArrayList<>();
        for (int i=1;i<=count;i++) {
            BigDecimal t = BigDecimal.valueOf(10000L+(tie && i==28 ? 27 : i),3);
            entries.add(new CommunityRoundEntryResponse(null,"r"+i,"Rider "+i,null,null,
                    CommunityMatchResult.DRAW,0,0,false,false,null,t,null,null));
        }
        return new CommunityRoundResponse(500L,"200m",Instant.parse("2026-10-25T11:00:00Z"),
                entries,"Ciclismo de Pista",null,List.of(),"TRACK_FLYING_200",null,
                List.of(),null,null,null,null,null);
    }
    private static List<String> seeds() {
        return CommunityTrackSprintQualifying16.seeds(qualification(28,false),28);
    }
    private static CommunityRoundResponse heat(String slot,String a,String b,String winner) {
        return new CommunityRoundResponse(99L,"Heat",Instant.now(),
                List.of(new CommunityRoundEntryResponse(null,a,a,null,null,
                            a.equals(winner)?CommunityMatchResult.WIN:CommunityMatchResult.LOSS,0,0,false,false),
                        new CommunityRoundEntryResponse(null,b,b,null,null,
                            b.equals(winner)?CommunityMatchResult.WIN:CommunityMatchResult.LOSS,0,0,false,false)),
                "Ciclismo de Pista",null,List.of(),"TRACK_SPRINT_HEAT",
                CommunityTrackSprintBracketPlanner.phase(slot),List.of(),null,null,null,
                CommunityTrackSprintBracketPlanner.seriesKey(41L,slot),1);
    }
    private static CommunityTrackSprintBracketDtos.Match match(
            List<CommunityRoundResponse> heats,String slot) {
        return CommunityTrackSprintBracketPlanner.project(41L,heats,seeds())
                .stream().filter(m -> m.slot().equals(slot)).findFirst().orElseThrow();
    }
    private static List<CommunityRoundEntryRequest> players(String a,String b) {
        return List.of(new CommunityRoundEntryRequest(a,null,CommunityMatchResult.WIN,0,0,false,false),
                new CommunityRoundEntryRequest(b,null,CommunityMatchResult.LOSS,0,0,false,false));
    }
    @Test void sixteenPreviousFormatAndUnlinkedHistoryAreUnchanged() {
        assertEquals(28,seeds().size());
        assertEquals(16,CommunityTrackSprintQualifying16.seeds(qualification(28,false)).size());
        assertEquals(8,CommunityTrackSprintBracketPlanner.project(41L,List.of()).size());
    }
    @Test void twentyEightRequireTwentyEightUniqueTimedRidersIncludingCutoff() {
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintQualifying16.seeds(qualification(27,false),28));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintQualifying16.seeds(qualification(29,true),28));
        assertEquals("r28",seeds().get(27));
    }
    @Test void uciSixteenthSeedsAndByesAreNotFabricatedAsVictories() {
        var matches=CommunityTrackSprintBracketPlanner.project(41L,List.of(),seeds());
        assertEquals(28,matches.size()); // 12 + 8 + 4 + 2 + 2
        assertEquals("P5",matches.get(0).slot());
        assertEquals("r5",match(List.of(),"P5").riderA());
        assertEquals("r28",match(List.of(),"P5").riderB());
        assertEquals("r16",match(List.of(),"P16").riderA());
        assertEquals("r17",match(List.of(),"P16").riderB());
        var firstEighth=match(List.of(),"R1");
        assertEquals("r1",firstEighth.riderA());
        assertNull(firstEighth.riderB());
        assertEquals("WAITING_PREVIOUS",firstEighth.status());
        assertNull(firstEighth.winner());
        assertEquals("WAITING_PREVIOUS",match(List.of(),"R5").status());
    }
    @Test void earlyWinnerMustBeConfirmedBeforeSeededEighthCanStart() {
        var rounds=List.of(heat("P16","r16","r17","r17"),
                heat("P5","r5","r28","r28"),heat("P12","r12","r21","r12"));
        assertEquals("FINISHED",match(rounds,"P16").status());
        var r1=match(rounds,"R1");
        assertEquals("READY",r1.status());
        assertEquals(Set.of("r1","r17"),Set.of(r1.riderA(),r1.riderB()));
        assertEquals(Set.of("r28","r12"),
                Set.of(match(rounds,"R5").riderA(),match(rounds,"R5").riderB()));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(41L,"SP41-R1",
                    "SPRINT_EIGHTH_FINAL",rounds,players("r1","r16"),seeds()));
        assertDoesNotThrow(()-> CommunityTrackSprintBracketPlanner.validateNewHeat(41L,"SP41-R1",
                    "SPRINT_EIGHTH_FINAL",rounds,players("r1","r17"),seeds()));
    }
    @Test void singleHeatSixteenthRejectsNextHeatAndWrongStage() {
        var rounds=List.of(heat("P5","r5","r28","r5"));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(41L,"SP41-P5",
                        "SPRINT_SIXTEENTH_FINAL",rounds,players("r5","r28"),seeds()));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintBracketPlanner.validateNewHeat(41L,"SP41-P16",
                        "SPRINT_EIGHTH_FINAL",List.of(),players("r16","r17"),seeds()));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintSeries.validate("TRACK_SPRINT_HEAT",
                        "SPRINT_SIXTEENTH_FINAL","SP41-P5",2,
                        List.of(new CommunityTrackSprintSeries.Heat("SPRINT_SIXTEENTH_FINAL",1,
                                "r5","r28","r5")),players("r5","r28")));
    }
}
