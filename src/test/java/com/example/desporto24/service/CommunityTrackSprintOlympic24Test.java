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
    @Test void signedSingleTwoUpOfficialAwardProjectsResultWithoutInventingHeats() {
        var award=Map.of("SP53-A1","r1");
        List<CommunityRoundResponse> physicallyRaced=List.of();
        var matches=CommunityTrackSprintOlympic24.project(
            BRACKET,physicallyRaced,seeds(),Map.of(),award);
        var awarded=matches.stream().filter(m->"A1".equals(m.slot())).findFirst().orElseThrow();
        assertEquals("FINISHED",awarded.status());
        assertEquals("r1",awarded.winner());
        assertEquals(1,awarded.winsA());
        assertEquals(0,awarded.winsB());
        assertTrue(physicallyRaced.isEmpty()); // administrative award is not a raced heat
        var entry=new CommunityRoundEntryRequest("r1",null,
            CommunityMatchResult.WIN,0,0,false,false);
        var loser=new CommunityRoundEntryRequest("r24",null,
            CommunityMatchResult.LOSS,0,0,false,false);
        assertThrows(IllegalArgumentException.class,()->CommunityTrackSprintOlympic24
            .validateNewHeat(BRACKET,"SP53-A1","SPRINT_THIRTY_SECOND_FINAL",
                "TRACK_SPRINT_HEAT",physicallyRaced,List.of(entry,loser),
                seeds(),Map.of(),award));
        assertThrows(IllegalStateException.class,()->CommunityTrackSprintOlympic24.project(
            BRACKET,List.of(),seeds(),Map.of(),Map.of("SP53-A1","r24-unrelated")));
        assertEquals("READY",match(List.of(),"A1").status()); // older projection unchanged
    }

    @Test void disqualifiedRiderNeverAdvancesToOlympicRepechage() {
        List<CommunityRoundResponse> raced=new ArrayList<>();
        for(int i=2;i<=12;i++){var m=match(raced,"A"+i);raced.add(record(m,m.riderA()));}
        var award=Map.of("SP53-A1","r1");
        var result=CommunityTrackSprintOlympic24.project(
                BRACKET,raced,seeds(),Map.of(),award,Set.of("r24"));
        var a1=result.stream().filter(x->x.slot().equals("A1")).findFirst().orElseThrow();
        var u1=result.stream().filter(x->x.slot().equals("U1")).findFirst().orElseThrow();
        assertEquals("FINISHED",a1.status());
        assertEquals("r1",a1.winner());
        assertEquals("WAITING_PREVIOUS",u1.status());
        assertNull(u1.riderA());
        assertNull(u1.winner());
        assertTrue(raced.stream().noneMatch(x->"SP53-A1".equals(x.sprintSeriesKey())));
        assertThrows(IllegalArgumentException.class,()->
            CommunityTrackSprintOlympic24.validateNewHeat(
                BRACKET,"SP53-U1","SPRINT_REPECHAGE_1_32",
                "TRACK_SPRINT_REPECHAGE",raced,List.of(),seeds(),
                Map.of(),award,Set.of("r24")));
        assertEquals("READY",CommunityTrackSprintOlympic24.project(
                BRACKET,raced,seeds(),Map.of(),award,Set.of()).stream()
                .filter(x->x.slot().equals("U1")).findFirst().orElseThrow().status());
    }
    @Test void officialBestThreeHeatRelegationDoesNotInventARaceOrSeriesVictory() {
        List<CommunityRoundResponse> raced=new ArrayList<>();
        for(int i=1;i<=12;i++){var m=match(raced,"A"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(raced,"U"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=8;i++){var m=match(raced,"C"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(raced,"V"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=6;i++){var m=match(raced,"E"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=2;i++){var m=match(raced,"W"+i);raced.add(record(m,m.riderA()));}
        var quarter=match(raced,"Q1");
        var first=Map.of(quarter.seriesKey(),Map.of(1,quarter.riderA()));
        var projected=CommunityTrackSprintOlympic24.project(BRACKET,raced,seeds(),
                Map.of(),Map.of(),Set.of(),first);
        var one=projected.stream().filter(x->"Q1".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("IN_PROGRESS",one.status());
        assertEquals(1,one.winsA());
        assertNull(one.winner());
        assertEquals("WAITING_PREVIOUS",projected.stream().filter(x->"S1".equals(x.slot()))
                .findFirst().orElseThrow().status());
        var second=Map.of(quarter.seriesKey(),Map.of(1,quarter.riderA(),2,quarter.riderA()));
        var two=CommunityTrackSprintOlympic24.project(BRACKET,raced,seeds(),
                Map.of(),Map.of(),Set.of(),second).stream()
                .filter(x->"Q1".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("FINISHED",two.status());
        assertEquals(quarter.riderA(),two.winner());
        assertTrue(raced.stream().noneMatch(r->quarter.seriesKey().equals(r.sprintSeriesKey())));
        var invalid=Map.of(quarter.seriesKey(),Map.of(2,quarter.riderA()));
        var gap=CommunityTrackSprintOlympic24.project(BRACKET,raced,seeds(),
                Map.of(),Map.of(),Set.of(),invalid).stream()
                .filter(x->"Q1".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("CONFLICT",gap.status());
    }

    @Test void signedBo3DisqualificationVerdictKeepsRealWinsAndBlocksF58() {
        List<CommunityRoundResponse> raced=new ArrayList<>();
        for(int i=1;i<=12;i++){var m=match(raced,"A"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(raced,"U"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=8;i++){var m=match(raced,"C"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=4;i++){var m=match(raced,"V"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=6;i++){var m=match(raced,"E"+i);raced.add(record(m,m.riderA()));}
        for(int i=1;i<=2;i++){var m=match(raced,"W"+i);raced.add(record(m,m.riderA()));}
        var quarter=match(raced,"Q1");
        var winner=quarter.riderA();
        var sanctioned=quarter.riderB();
        var award=Map.of(quarter.seriesKey(),winner);
        var current=CommunityTrackSprintOlympic24.project(
                BRACKET,raced,seeds(),Map.of(),award,Set.of(sanctioned),Map.of());
        var q1=current.stream().filter(x->"Q1".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("FINISHED",q1.status());
        assertEquals(winner,q1.winner());
        assertEquals(0,q1.winsA()); // No imaginary 2-0 score.
        assertEquals(0,q1.winsB());
        var placement=current.stream().filter(x->"F58".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("WAITING_PREVIOUS",placement.status());
        assertNull(placement.riderA());
        var partiallyRaced=Map.of(quarter.seriesKey(),Map.of(1,sanctioned));
        var partial=CommunityTrackSprintOlympic24.project(BRACKET,raced,seeds(),
                Map.of(),award,Set.of(sanctioned),partiallyRaced).stream()
                .filter(x->"Q1".equals(x.slot())).findFirst().orElseThrow();
        assertEquals("FINISHED",partial.status());
        assertEquals(0,partial.winsA());
        assertEquals(1,partial.winsB());
        assertEquals(winner,partial.winner());
        assertTrue(raced.stream().noneMatch(x->quarter.seriesKey().equals(x.sprintSeriesKey())));
        assertThrows(IllegalStateException.class,()->CommunityTrackSprintOlympic24.project(
                BRACKET,raced,seeds(),Map.of(),
                Map.of(quarter.seriesKey(),"fictitious-unrelated"),Set.of(),Map.of()));
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
