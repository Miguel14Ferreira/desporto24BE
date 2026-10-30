package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityCricketDetails;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityCricketResultsTest {
    private final List<CommunityRoundEntryRequest> teams = List.of(
            new CommunityRoundEntryRequest("batfirst", "A", CommunityMatchResult.DRAW, 0, 0, false, false),
            new CommunityRoundEntryRequest("chasing", "B", CommunityMatchResult.DRAW, 0, 0, false, false));
    private CommunityTeamFinalScore runs(int a, int b) {
        return new CommunityTeamFinalScore(a, b);
    }
    private CommunityCricketDetails details(int wA, int ballsA, int wB, int ballsB) {
        return new CommunityCricketDetails(wA, ballsA, wB, ballsB);
    }

    @Test void allowsOnlyTheExistingCricketDisciplineAndAnExplicitNeutralEvent() {
        assertEquals("CRICKET_T20", CommunityRoundEventType.validate("Cricket","CRICKET_T20"));
        assertThrows(IllegalArgumentException.class,()->CommunityRoundEventType.validate("Cricket",null));
        assertThrows(IllegalArgumentException.class,()->CommunityRoundEventType.validate("Futebol","CRICKET_T20"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Cricket","CRICKET_T20"));
        assertTrue(CommunityTeamFinalResults.isTeamFinalEvent("CRICKET_T20"));
    }

    @Test void acceptsCompletedRegularInningsAndChasesButNeverInventsSuperOver() {
        for (CommunityTeamFinalScore score : List.of(runs(165,166),runs(161,161),runs(180,170))) {
            assertEquals(score, CommunityTeamFinalResults.validate("Cricket","CRICKET_T20",teams,score));
        }
        assertEquals(details(5,120,4,115), CommunityCricketResults.validate("Cricket","CRICKET_T20",
                runs(165,166),details(5,120,4,115)));
        assertEquals(details(10,103,10,116), CommunityCricketResults.validate("Cricket","CRICKET_T20",
                runs(148,140),details(10,103,10,116)));
        assertEquals(details(5,120,6,120), CommunityCricketResults.validate("Cricket","CRICKET_T20",
                runs(151,151),details(5,120,6,120)));
    }

    @Test void rejectsIncompleteInningsAndImpossibleRanges() {
        for (CommunityCricketDetails invalid : List.of(
                details(-1,120,3,100),details(11,120,3,100),details(1,121,2,120),
                details(1,0,3,120),details(1,119,2,120),details(4,120,3,118))) {
            assertThrows(IllegalArgumentException.class,()->CommunityCricketResults.validate(
                    "Cricket","CRICKET_T20",runs(140,139),invalid));
        }
        assertThrows(IllegalArgumentException.class,()->CommunityCricketResults.validate(
                "Cricket","CRICKET_T20",runs(120,120),details(5,120,5,115)));
        assertThrows(IllegalArgumentException.class,()->CommunityCricketResults.validate(
                "Cricket","CRICKET_T20",runs(120,121),null));
        assertThrows(IllegalArgumentException.class,()->CommunityCricketResults.validate(
                "Futebol",null,null,details(5,120,2,100)));
        assertNull(CommunityCricketResults.decode(null,null,null,null));
        assertEquals(details(1,120,2,119),CommunityCricketResults.decode(1,120,2,119));
        assertThrows(IllegalStateException.class,()->CommunityCricketResults.decode(1,null,2,120));
    }

    @Test void preventsFootballStatisticsFakeParticipantsAndCrossSportScores() {
        assertThrows(IllegalArgumentException.class,()->CommunityTeamFinalResults.validate(
                "Rugby","CRICKET_T20",teams,runs(120,121)));
        assertThrows(IllegalArgumentException.class,()->CommunityTeamFinalResults.validate(
                "Cricket","CRICKET_T20",List.of(teams.get(0)),runs(120,121)));
        var withGoals=new CommunityRoundEntryRequest("batfirst","A",CommunityMatchResult.DRAW,2,0,false,false);
        assertThrows(IllegalArgumentException.class,()->CommunityTeamFinalResults.validate(
                "Cricket","CRICKET_T20",List.of(withGoals,teams.get(1)),runs(120,121)));
    }
}
