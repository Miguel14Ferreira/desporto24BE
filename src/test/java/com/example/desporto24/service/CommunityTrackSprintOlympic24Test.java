package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintOlympic24Test {
    static final long BRACKET=53L;
    static List<String> seeds() {
        List<String> riders=new ArrayList<>();
        for(int i=1;i<=24;i++)riders.add("r"+i);
        return List.copyOf(riders);
    }
    static CommunityRoundResponse qualifier(int n, boolean tied) {
        List<CommunityRoundEntryResponse> entries=new ArrayList<>();
        for(int i=1;i<=n;i++)entries.add(new CommunityRoundEntryResponse(
                null,"r"+i,"r"+i,null,null,CommunityMatchResult.DRAW,
                0,0,false,false,null,
                BigDecimal.valueOf(9_000L+(tied&&i==24?23:i),3),
                null,null));
        return new CommunityRoundResponse(22L,"200 m",Instant.now(),
                entries,"Ciclismo de Pista",null,List.of(),"TRACK_FLYING_200",
                null,List.of(),null,null,null,null,null);
    }
    static CommunityTrackSprintBracketDtos.Match match(List<CommunityRoundResponse> recorded,
            String slot) {
        return CommunityTrackSprintOlympic24.project(BRACKET,recorded,seeds())
                .stream().filter(x->x.slot().equals(slot)).findFirst().orElseThrow();
    }
    static CommunityRoundResponse record(CommunityTrackSprintBracketDtos.Match m,String winner) {
        List<CommunityRoundEntryResponse> entries=new ArrayList<>();
        for(String p:List.of(m.riderA(),m.riderB()))entries.add(entry(p,winner.equals(p)));
        if(m.riderC()!=null)entries.add(entry(m.riderC(),winner.equals(m.riderC())));
        return new CommunityRoundResponse(1L,"Sprint",Instant.now(),entries,
                "Ciclismo de Pista",null,List.of(),
                CommunityTrackSprintOlympic24.isRepechage(m.slot())
                        ?"TRACK_SPRINT_REPECHAGE":"TRACK_SPRINT_HEAT",
                m.phase(),List.of(),null,null,null,m.seriesKey(),1);
    }
    static CommunityRoundEntryResponse entry(String p,boolean win) {
        return new CommunityRoundEntryResponse(null,p,p,null,null,
                win?CommunityMatchResult.WIN:CommunityMatchResult.LOSS,0,0,false,false);
    }
    static List<CommunityRoundEntryRequest> entrants(CommunityTrackSprintBracketDtos.Match m) {
        List<CommunityRoundEntryRequest> entries=new ArrayList<>();
        for(String name:List.of(m.riderA(),m.riderB()))
            entries.add(new CommunityRoundEntryRequest(name,null,CommunityMatchResult.LOSS,
                    0,0,false,false));
        if(m.riderC()!=null)entries.add(new CommunityRoundEntryRequest(m.riderC(),null,
                CommunityMatchResult.WIN,0,0,false,false));
        return entries;
    }
    @Test void qualifyingRequiresTwentyFourAndNoUnresolvedTie() {
        assertEquals(24,CommunityTrackSprintQualifying16.seeds(qualifier(26,false),24).size());
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintQualifying16.seeds(qualifier(23,false),24));
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintQualifying16.seeds(qualifier(25,true),24));
        assertEquals(8,CommunityTrackSprintBracketPlanner.project(BRACKET,List.of()).size());
    }
    @Test void olympicFirstRoundAndThreeUpRepechageAreNotInvented() {
        var all=CommunityTrackSprintBracketPlanner.project(BRACKET,List.of(),seeds());
        assertEquals(45,all.size()); // includes independently recorded 5th–8th race
        assertEquals("r1",match(List.of(),"A1").riderA());
        assertEquals("r24",match(List.of(),"A1").riderB());
        assertEquals("r12",match(List.of(),"A12").riderA());
        assertEquals("r13",match(List.of(),"A12").riderB());
        assertEquals("WAITING_PREVIOUS",match(List.of(),"U1").status());
        assertEquals("WAITING_PREVIOUS",match(List.of(),"C1").status());
        assertNull(match(List.of(),"U1").winner());
    }
    @Test void firstRepechageHasExactlyThreeLosersAndWinnerFeedsOneSixteenth() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        for(int i=1;i<=12;i++){
            var m=match(rounds,"A"+i);
            rounds.add(record(m,m.riderA()));
        }
        var u1=match(rounds,"U1");
        assertEquals(Set.of("r24","r17","r16"),
                Set.of(u1.riderA(),u1.riderB(),u1.riderC()));
        assertEquals("READY",u1.status());
        assertEquals("WAITING_PREVIOUS",match(rounds,"C4").status());
        CommunityTrackSprintOlympic24.validateNewHeat(BRACKET,u1.seriesKey(),u1.phase(),
                "TRACK_SPRINT_REPECHAGE",rounds,entrants(u1),seeds());
        assertThrows(IllegalArgumentException.class,()->
                CommunityTrackSprintOlympic24.validateNewHeat(BRACKET,u1.seriesKey(),u1.phase(),
                        "TRACK_SPRINT_HEAT",rounds,entrants(u1),seeds()));
        rounds.add(record(u1,u1.riderC()));
        assertEquals("FINISHED",match(rounds,"U1").status());
        assertEquals(u1.riderC(),match(rounds,"C4").riderB());
    }
    @Test void progressesThroughAllThreeRepechagesToQuartersWithoutSyntheticWins() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        for(int i=1;i<=12;i++){
            var m=match(rounds,"A"+i);
            rounds.add(record(m,m.riderA()));
        }
        for(int i=1;i<=4;i++){
            var m=match(rounds,"U"+i);
            assertEquals("READY",m.status());
            rounds.add(record(m,m.riderC()));
        }
        for(int i=1;i<=8;i++){
            var m=match(rounds,"C"+i);
            assertEquals("READY",m.status());
            rounds.add(record(m,m.riderA()));
        }
        for(int i=1;i<=4;i++){
            var m=match(rounds,"V"+i);
            assertEquals("READY",m.status());
            assertNull(m.riderC());
            rounds.add(record(m,m.riderA()));
        }
        for(int i=1;i<=6;i++){
            var m=match(rounds,"E"+i);
            assertEquals("READY",m.status());
            rounds.add(record(m,m.riderA()));
        }
        for(int i=1;i<=2;i++){
            var m=match(rounds,"W"+i);
            assertEquals("READY",m.status());
            assertNotNull(m.riderC());
            rounds.add(record(m,m.riderA()));
        }
        for(int i=1;i<=4;i++){
            var m=match(rounds,"Q"+i);
            assertEquals("READY",m.status());
            assertNull(m.winner());
        }
        assertEquals("WAITING_PREVIOUS",match(rounds,"S1").status());
    }
    @Test void cannotRepeatOrCorruptCompletedRepechageAndProtectsChildren() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        for(int i=1;i<=12;i++){
            var m=match(rounds,"A"+i);
            rounds.add(record(m,m.riderA()));
        }
        var m=match(rounds,"U1");
        rounds.add(record(m,m.riderA()));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,m.seriesKey(),m.phase(),"TRACK_SPRINT_REPECHAGE",
                rounds,entrants(m),seeds()));
        assertEquals(List.of("U1","C1"),CommunityTrackSprintOlympic24.nextSlots("A1"));
        assertEquals(List.of("C4"),CommunityTrackSprintOlympic24.nextSlots("U1"));
        assertEquals(List.of("Q1"),CommunityTrackSprintOlympic24.nextSlots("W2"));
        assertEquals(List.of("B","G"),CommunityTrackSprintOlympic24.nextSlots("S1"));
    }

    static CommunityRoundResponse numberedHeat(CommunityTrackSprintBracketDtos.Match m,
            String winner,int heat) {
        var original=record(m,winner);
        return new CommunityRoundResponse(original.id(), original.name(),
                original.playedAt(), original.entries(), original.sport(),
                original.matchFormat(),original.setScores(),original.eventType(),
                original.eventDiscipline(),original.groupScores(),
                original.basketballScore(),original.teamScore(),original.cricketDetails(),
                original.sprintSeriesKey(),heat);
    }
    static List<CommunityRoundResponse> throughQuarters() {
        List<CommunityRoundResponse> rounds=new ArrayList<>();
        for(int i=1;i<=12;i++){var m=match(rounds,"A"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(rounds,"U"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=8;i++){var m=match(rounds,"C"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(rounds,"V"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=6;i++){var m=match(rounds,"E"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=2;i++){var m=match(rounds,"W"+i);rounds.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){
            var m=match(rounds,"Q"+i);
            assertEquals("READY",m.status());
            rounds.add(numberedHeat(m,m.riderA(),1));
            rounds.add(numberedHeat(m,m.riderA(),2));
        }
        return rounds;
    }
    static CommunityRoundResponse placement(CommunityTrackSprintBracketDtos.Match m) {
        List<CommunityRoundEntryResponse> entries=new ArrayList<>();
        for(int pos=5;pos<=8;pos++) {
            String rider=switch(pos) {
                case 5->m.riderC();case 6->m.riderD();
                case 7->m.riderB();default->m.riderA();
            };
            entries.add(new CommunityRoundEntryResponse(null,rider,rider,null,null,
                    CommunityMatchResult.DRAW,0,0,false,false,pos,null,null,null));
        }
        return new CommunityRoundResponse(942L,"Prova lugares 5–8",Instant.now(),
                entries,"Ciclismo de Pista",null,List.of(),"TRACK_SPRINT_5_8",
                "SPRINT_PLACES_5_8",List.of(),null,null,null,m.seriesKey(),1);
    }
    @Test void fourLosersHaveIndependentFifthToEighthRaceAndNoGuessedPlaces() {
        List<CommunityRoundResponse> rounds=throughQuarters();
        var m=match(rounds,"F58");
        assertEquals("READY",m.status());
        assertEquals(Set.of("r1","r2","r3","r4").size(),
                Set.of(m.riderA(),m.riderB(),m.riderC(),m.riderD()).size());
        assertTrue(m.rankings().isEmpty());
        assertEquals("WAITING_PREVIOUS",match(List.of(),"F58").status());
        assertEquals("READY",match(rounds,"S1").status());
        var race=placement(m);
        List<CommunityRoundEntryRequest> proposed=new ArrayList<>();
        for(var e:race.entries()) proposed.add(new CommunityRoundEntryRequest(
                e.username(),null,CommunityMatchResult.DRAW,0,0,false,false,
                e.finishPosition(),null,null,null));
        assertDoesNotThrow(()->CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,m.seriesKey(),m.phase(),"TRACK_SPRINT_5_8",rounds,proposed,seeds()));
        rounds.add(race);
        var completed=match(rounds,"F58");
        assertEquals("FINISHED",completed.status());
        assertNull(completed.winner());
        assertEquals(List.of(5,6,7,8),completed.rankings().stream()
                .map(CommunityTrackSprintBracketDtos.RankedRider::position).toList());
        assertEquals(4,completed.rankings().size());
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,m.seriesKey(),m.phase(),"TRACK_SPRINT_5_8",rounds,proposed,seeds()));
        assertEquals(List.of("S1","F58"),CommunityTrackSprintOlympic24.nextSlots("Q1"));
    }
    @Test void fifthToEighthRejectsWrongRanksAndUnrelatedRiders() {
        List<CommunityRoundResponse> rounds=throughQuarters();
        var m=match(rounds,"F58");
        var first=new CommunityRoundEntryRequest(m.riderA(),null,CommunityMatchResult.DRAW,
                0,0,false,false,5,null,null,null);
        var duplicate=new CommunityRoundEntryRequest(m.riderB(),null,CommunityMatchResult.DRAW,
                0,0,false,false,5,null,null,null);
        var third=new CommunityRoundEntryRequest(m.riderC(),null,CommunityMatchResult.DRAW,
                0,0,false,false,7,null,null,null);
        var fourth=new CommunityRoundEntryRequest(m.riderD(),null,CommunityMatchResult.DRAW,
                0,0,false,false,8,null,null,null);
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24.validatePlacings(
                List.of(first,duplicate,third,fourth)));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,m.seriesKey(),m.phase(),"TRACK_SPRINT_5_8",rounds,
                List.of(new CommunityRoundEntryRequest("outsider",null,CommunityMatchResult.DRAW,
                        0,0,false,false,6,null,null,null),first,third,fourth),seeds()));
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,m.seriesKey(),m.phase(),"TRACK_SPRINT_HEAT",rounds,
                List.of(first,duplicate,third,fourth),seeds()));
    }
}
