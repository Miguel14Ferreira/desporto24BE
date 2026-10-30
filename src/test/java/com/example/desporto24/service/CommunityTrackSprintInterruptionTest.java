package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.Match;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintInterruptionTest {
    private static Match match(String slot,String status,String winner,String a,String b) {
        return new Match("SPRINT",slot,"SP7-"+slot,a,b,0,0,winner,status);
    }
    private static List<Match> fixture(int completedQ,int completedS,boolean finals) {
        List<Match> matches=new ArrayList<>();
        for(int i=1;i<=4;i++)matches.add(match("Q"+i,i<=completedQ?"FINISHED":"READY",
            i<=completedQ?"qa_q"+i:null,"qa_q"+i,"qa_q_alt"+i));
        matches.add(match("S1",completedS>=1?"FINISHED":"READY",
            completedS>=1?"qa_s1":null,"qa_s1","qa_s1_alt"));
        matches.add(match("S2",completedS>=2?"FINISHED":"READY",
            completedS>=2?"qa_s2":null,"qa_s2","qa_s2_alt"));
        matches.add(match("B",finals?"FINISHED":"READY",
            finals?"qa_b":null,"qa_b","qa_b_alt"));
        matches.add(match("G",finals?"FINISHED":"READY",
            finals?"qa_g":null,"qa_g","qa_g_alt"));
        return matches;
    }

    @Test void beforeQuartersHasNoFinalRankingNorPointInferences() {
        var x=fixture(3,0,false);
        assertEquals("BEFORE_QUARTERS",CommunityTrackSprintInterruption.stage(x));
        assertTrue(CommunityTrackSprintInterruption.placements("BEFORE_QUARTERS",x).isEmpty());
    }
    @Test void completedQuartersLeaveFourSemifinalistsJointFourth() {
        var x=fixture(4,1,false);
        assertEquals("AFTER_QUARTERS",CommunityTrackSprintInterruption.stage(x));
        var ranks=CommunityTrackSprintInterruption.placements("AFTER_QUARTERS",x);
        assertEquals(4,ranks.size());
        assertEquals(Set.of("qa_q1","qa_q2","qa_q3","qa_q4"),
            new HashSet<>(ranks.stream().map(p->p.username()).toList()));
        assertTrue(ranks.stream().allMatch(p->p.position()==4));
    }
    @Test void completedSemisGiveTwoJointSecondsAndTwoJointFourths() {
        var x=fixture(4,2,false);
        assertEquals("AFTER_SEMIS",CommunityTrackSprintInterruption.stage(x));
        var ranks=CommunityTrackSprintInterruption.placements("AFTER_SEMIS",x);
        assertEquals(4,ranks.size());
        assertEquals(2,ranks.stream().filter(p->p.position()==2).count());
        assertEquals(2,ranks.stream().filter(p->p.position()==4).count());
        assertEquals(Set.of("qa_s1","qa_s2"),
            new HashSet<>(ranks.stream().filter(p->p.position()==2)
                .map(p->p.username()).toList()));
    }
    @Test void cannotInterruptAfterMedalFinals() {
        assertThrows(IllegalStateException.class,
            ()->CommunityTrackSprintInterruption.stage(fixture(4,2,true)));
    }
    @Test void cannotInventWinnersOnConflictingHeats() {
        var x=fixture(4,2,false);
        x.set(0,match("Q1","CONFLICT",null,"a","b"));
        assertThrows(IllegalStateException.class,
            ()->CommunityTrackSprintInterruption.stage(x));
        assertThrows(IllegalStateException.class,
            ()->CommunityTrackSprintInterruption.placements("AFTER_SEMIS",x));
    }
}
