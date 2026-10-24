package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityKickboxingResultsTest {
    static final CommunityMatchResult WIN = CommunityMatchResult.WIN;
    static final CommunityMatchResult LOSS = CommunityMatchResult.LOSS;
    static final CommunityMatchResult DRAW = CommunityMatchResult.DRAW;

    private CommunityRoundEntryRequest entry(String user, String side, CommunityMatchResult result,
            String score, int goals) {
        return new CommunityRoundEntryRequest(user,side,result,goals,0,false,false,
                null,null,null,null,score == null ? null : new BigDecimal(score));
    }
    private List<CommunityRoundEntryRequest> bout(CommunityMatchResult red,
            CommunityMatchResult blue,String a,String b) {
        return List.of(entry("red","RED",red,a,0),entry("blue","BLUE",blue,b,0));
    }

    @Test void allSixWakoDisciplinesAreAllowedOnlyForExistingKickboxing() {
        for (String type : new String[] {"WAKO_POINT_FIGHTING","WAKO_LIGHT_CONTACT",
                "WAKO_KICK_LIGHT","WAKO_FULL_CONTACT","WAKO_LOW_KICK","WAKO_K1"}) {
            assertEquals(type,CommunityRoundEventType.validate("Kickboxing",type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Kickboxing",type));
            assertFalse(CommunityRoundEventType.isRaceEvent("Kickboxing",type));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Kickboxing",null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Boxe","WAKO_K1"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Kickboxing","BOXING_WB_KO"));
    }

    @Test void pointFightingRecordsOnlyClearFinalIntegerPointsVictory() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Kickboxing",
                "WAKO_POINT_FIGHTING",bout(WIN,LOSS,"12","7")));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Kickboxing",
                "WAKO_POINT_FIGHTING",bout(LOSS,WIN,"0","2")));
        for(String bad : new String[] {null,"-1","2.5","1000000"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Kickboxing",
                            "WAKO_POINT_FIGHTING",bout(WIN,LOSS,bad,"0")));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Kickboxing",
                        "WAKO_POINT_FIGHTING",bout(WIN,LOSS,"5","5")));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Kickboxing",
                        "WAKO_POINT_FIGHTING",bout(WIN,LOSS,"1","2")));
    }

    @Test void judgedDisciplinesKeepWinnerWithoutFakeScalarMarks() {
        for (String type : new String[] {"WAKO_LIGHT_CONTACT","WAKO_KICK_LIGHT",
                "WAKO_FULL_CONTACT","WAKO_LOW_KICK","WAKO_K1"}) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Kickboxing",
                    type,bout(WIN,LOSS,null,null)));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Kickboxing",
                    type,bout(LOSS,WIN,null,null)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Kickboxing",
                            type,bout(WIN,LOSS,"30",null)));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Kickboxing",
                            type,bout(DRAW,DRAW,null,null)));
        }
    }

    @Test void rejectsWrongCornersSameAthleteNonKickboxingAndFootballScores() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Kickboxing",
                        "WAKO_POINT_FIGHTING",List.of(
                                entry("a","RED",WIN,"5",0),entry("b","RED",LOSS,"2",0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Kickboxing",
                        "WAKO_POINT_FIGHTING",List.of(
                                entry("a","RED",WIN,"5",0),entry("a","BLUE",LOSS,"2",0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Kickboxing",
                        "WAKO_POINT_FIGHTING",List.of(
                                entry("a","RED",WIN,"5",1),entry("b","BLUE",LOSS,"2",0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Boxe",
                        "WAKO_POINT_FIGHTING",bout(WIN,LOSS,"5","2")));
    }
}
