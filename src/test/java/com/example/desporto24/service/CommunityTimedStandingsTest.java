package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTimedStandingsTest {
    private CommunityRoundEntryResponse entry(long communityPlayerId,String username,String time) {
        BigDecimal seconds=time==null?null:new BigDecimal(time);
        return new CommunityRoundEntryResponse(null,username,username,null,null,
                CommunityMatchResult.DRAW,0,0,false,false,null,seconds,null,null,
                communityPlayerId+100,communityPlayerId,0,null,
                BigDecimal.ZERO,seconds,false,null,null,null,null);
    }
    private CommunityRoundResponse round(long id,CommunityRoundEntryResponse... entries) {
        return new CommunityRoundResponse(id,"Natação",Instant.now(),List.of(entries),
                "Natação",null,List.of(),"SWIM_LCM","FREE_50");
    }

    @Test void usesBestRealTimePerStableCommunityPlayerWithoutAddingArtificialPoints() {
        var first=round(1,entry(10,"ana","29.11"),entry(20,"bruno","28.47"),entry(30,"carla",null));
        var second=round(2,entry(10,"ana","27.89"),entry(20,"bruno","28.48"));
        var result=CommunityTimedStandings.calculate(List.of(first,second));
        assertEquals(2,result.size());
        assertEquals("ana",result.get(0).username());
        assertEquals(new BigDecimal("27.89"),result.get(0).bestSeconds());
        assertEquals(2,result.get(0).validAttempts());
        assertEquals(1,result.get(0).position());
        assertEquals("bruno",result.get(1).username());
        assertEquals(new BigDecimal("28.47"),result.get(1).bestSeconds());
    }

    @Test void tiesHaveEqualPositionAndNoUnrecordedTimesBecomeResults() {
        var result=CommunityTimedStandings.calculate(List.of(round(1,
                entry(10,"ana","30.10"),entry(20,"bruno","30.1"),
                entry(30,"carla","31.00"),entry(40,"desconhecido",null))));
        assertEquals(List.of(1,1,3),result.stream().map(CommunityTimedStandingResponse::position).toList());
        assertEquals(3,result.size());
    }

    @Test void trackSupportsMillisecondsButSwimmingRequiresHundredths() {
        var first=round(1,entry(10,"ana","10.123"),entry(20,"bruno","10.125"));
        assertEquals(2,CommunityTimedStandings.calculate(List.of(first),3).size());
        assertEquals(0,CommunityTimedStandings.calculate(List.of(first),2).size());
        var padded=round(2,entry(10,"ana","10.120"));
        assertEquals(1,CommunityTimedStandings.calculate(List.of(padded),2).size());
    }

    @Test void ignoresInvalidAndDisqualifiedTimeDataDefensively() {
        var valid=entry(10,"ana","31.25");
        var invalid=entry(20,"bruno","0.00");
        var precise=entry(30,"carla","31.259");
        var result=CommunityTimedStandings.calculate(List.of(round(1,valid,invalid,precise)));
        assertEquals(List.of("ana"),result.stream().map(CommunityTimedStandingResponse::username).toList());
    }
}
