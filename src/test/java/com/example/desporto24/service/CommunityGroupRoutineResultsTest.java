package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityGroupRoutineScore;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityGroupRoutineResultsTest {
    private CommunityRoundEntryRequest entrant(String username, String group) {
        return new CommunityRoundEntryRequest(username, group, CommunityMatchResult.DRAW,
                0, 0, false, false, null, null, null, null,
                null, null, null);
    }
    private List<CommunityRoundEntryRequest> group(String name, int size) {
        List<CommunityRoundEntryRequest> entries = new ArrayList<>();
        for (int n = 1; n <= size; n++) entries.add(entrant(name+n, name));
        return entries;
    }
    private CommunityGroupRoutineScore score(String name, String mark) {
        return new CommunityGroupRoutineScore(name, mark == null ? null : new BigDecimal(mark));
    }
    private String validate(String event, List<CommunityRoundEntryRequest> participants,
                            List<CommunityGroupRoutineScore> marks) {
        return CommunityGroupRoutineResults.validateAndEncode(
                event.startsWith("RHYTHMIC") ? "Ginástica Rítmica" : "Natação Artística",
                event, participants, marks);
    }

    @Test void validatesDuetsWithOneSharedFourDecimalScoreAndNoPerAthleteScore() {
        assertEquals("ARTISTIC_DUET_TECH",
                CommunityRoundEventType.validate("Natação Artística","ARTISTIC_DUET_TECH"));
        assertEquals("ARTISTIC_DUET_FREE",
                CommunityRoundEventType.validate("Natação Artística","ARTISTIC_DUET_FREE"));
        var two=group("Dueto Azul",2);
        var scores=List.of(score("Dueto Azul","210.1234"));
        String json=validate("ARTISTIC_DUET_TECH",two,scores);
        var stored=CommunityGroupRoutineResults.decode(json);
        assertEquals(1,stored.size());
        assertEquals(new BigDecimal("210.1234"),stored.get(0).score());
        assertNull(two.get(0).performanceValue());
        assertThrows(IllegalArgumentException.class,() ->
                validate("ARTISTIC_DUET_FREE",group("Dueto",3),List.of(score("Dueto","40"))));
        assertThrows(IllegalArgumentException.class,() ->
                validate("ARTISTIC_DUET_FREE",two,List.of(score("Dueto Azul","50.12345"))));
        assertThrows(IllegalArgumentException.class,() ->
                validate("ARTISTIC_DUET_FREE",two,List.of(score("Dueto Azul","-1"))));
        assertThrows(IllegalArgumentException.class,() ->
                validate("ARTISTIC_DUET_FREE",two,List.of(score("Outro","80"))));
    }

    @Test void validatesArtisticTeamsAndPreservesOneScoreForMultipleAthletes() {
        for (String event:List.of("ARTISTIC_TEAM_TECH","ARTISTIC_TEAM_FREE")) {
            assertEquals(event,CommunityRoundEventType.validate("Natação Artística",event));
            var all=new ArrayList<CommunityRoundEntryRequest>(group("A",4));
            all.addAll(group("B",8));
            var result=CommunityGroupRoutineResults.decode(validate(event,all,
                    List.of(score("A","201.1234"),score("B",null))));
            assertEquals(2,result.size());
            assertNull(result.get(1).score());
            assertEquals(12,all.size());
            assertThrows(IllegalArgumentException.class,() ->
                    validate(event,group("C",3),List.of(score("C","300"))));
            assertThrows(IllegalArgumentException.class,() ->
                    validate(event,group("C",9),List.of(score("C","300"))));
        }
    }

    @Test void rhythmicGroupFormsAreDistinctFromIndividualApparatus() {
        for(String event:List.of("RHYTHMIC_GROUP_5","RHYTHMIC_GROUP_3_2")) {
            assertEquals(event,CommunityRoundEventType.validate("Ginástica Rítmica",event));
            assertEquals(1,CommunityGroupRoutineResults.decode(validate(event,
                    group("Conjunto",5),List.of(score("Conjunto","34.875")))).size());
            assertThrows(IllegalArgumentException.class,() ->
                    validate(event,group("Conjunto",4),List.of(score("Conjunto","34.875"))));
            assertThrows(IllegalArgumentException.class,() ->
                    validate(event,group("Conjunto",5),List.of(score("Conjunto","34.8751"))));
        }
        assertThrows(IllegalArgumentException.class,() ->
                CommunityRoundEventType.validate("Ginástica Rítmica","ARTISTIC_TEAM_FREE"));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityRoundEventType.validate("Natação Artística","RHYTHMIC_GROUP_5"));
    }

    @Test void rejectsDuplicateCaseInsensitiveGroupsMissingScoresAndIndividualMetrics() {
        var group=group("Conjunto",5);
        assertThrows(IllegalArgumentException.class,() ->
                validate("RHYTHMIC_GROUP_5",group,List.of(
                        score("Conjunto","1"),score("CONJUNTO","2"))));
        assertThrows(IllegalArgumentException.class,() ->
                validate("RHYTHMIC_GROUP_5",group,List.of()));
        assertThrows(IllegalArgumentException.class,() ->
                validate("RHYTHMIC_GROUP_5",group,List.of(score("Outro","10"))));
        var mixed=new ArrayList<>(group);
        mixed.add(entrant("noGroup",null));
        assertThrows(IllegalArgumentException.class,() ->
                validate("RHYTHMIC_GROUP_5",mixed,List.of(score("Conjunto","10"))));
        mixed=new ArrayList<>(group);
        mixed.set(0,new CommunityRoundEntryRequest("Conjunto1","Conjunto",
                CommunityMatchResult.DRAW,0,0,false,false,null,null,null,null,
                new BigDecimal("10"),null,null));
        var withPerAthleteScore=mixed;
        assertThrows(IllegalArgumentException.class,() ->
                validate("RHYTHMIC_GROUP_5",withPerAthleteScore,List.of(score("Conjunto","10"))));
        assertThrows(IllegalArgumentException.class,() ->
                CommunityGroupRoutineResults.validateAndEncode("Futebol",null,
                        group,List.of(score("Conjunto","10"))));
        assertNull(CommunityGroupRoutineResults.validateAndEncode("Futebol",null,
                group,null));
        assertTrue(CommunityGroupRoutineResults.decode(null).isEmpty());
    }
}
