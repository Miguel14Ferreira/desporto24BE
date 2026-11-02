package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRelayStandingsTest {
    private CommunityRelayHeatResponse heat(long id,long team,String name,int n,String seconds,
            Long... runners){
        return new CommunityRelayHeatResponse(id,team,name,n,new BigDecimal(seconds),
                List.of(runners),Instant.now());
    }
    @Test void ranksCollectiveBestTimeAndKeepsActualFourRunnerOrder(){
        var heats=List.of(
                heat(1,10,"A",1,"43.812",1L,2L,3L,4L),
                heat(2,20,"B",1,"43.801",5L,6L,7L,8L),
                heat(3,10,"A",2,"43.790",4L,3L,2L,1L));
        var result=CommunityRelayStandings.calculate(heats);
        assertEquals(List.of(10L,20L),result.stream().map(CommunityRelayStandingResponse::teamId).toList());
        assertEquals(new BigDecimal("43.790"),result.get(0).bestSeconds());
        assertEquals(2,result.get(0).recordedHeats());
        assertEquals(List.of(4L,3L,2L,1L),result.get(0).runnerProfileIds());
    }
    @Test void equalTeamTimesSharePositionWithoutInventingOfficialTieBreak(){
        var result=CommunityRelayStandings.calculate(List.of(
                heat(1,10,"A",1,"48.100",1L,2L,3L,4L),
                heat(2,20,"B",1,"48.10",5L,6L,7L,8L),
                heat(3,30,"C",1,"49.200",9L,10L,11L,12L)));
        assertEquals(List.of(1,1,3),result.stream().map(CommunityRelayStandingResponse::position).toList());
    }
    @Test void disregardsInvalidDuplicateLegs(){
        var result=CommunityRelayStandings.calculate(List.of(
                heat(1,10,"A",1,"44.00",1L,1L,2L,3L),
                heat(2,20,"B",1,"45.00",4L,5L,6L,7L)));
        assertEquals(List.of(20L),result.stream().map(CommunityRelayStandingResponse::teamId).toList());
    }
}
