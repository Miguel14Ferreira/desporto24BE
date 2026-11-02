package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityMeasuredStandingsTest {
    private CommunityRoundEntryResponse mark(long id,String metres) {
        BigDecimal value=metres==null?null:new BigDecimal(metres);
        return new CommunityRoundEntryResponse(null,"athlete"+id,"Athlete "+id,null,null,
                CommunityMatchResult.DRAW,0,0,false,false,null,null,null,null,
                id+100,id,0,null,BigDecimal.ZERO,null,false,null,value,null,null);
    }
    private CommunityRoundResponse round(long roundId,CommunityRoundEntryResponse... entries) {
        return new CommunityRoundResponse(roundId,"Jornada",Instant.now(),List.of(entries),
                "Atletismo",null,List.of(),"JUMP","LONG_JUMP");
    }
    @Test void choosesLargestValidMarkAndSharesExactTiesWithoutInventingPoints() {
        var result=CommunityMeasuredStandings.calculate(List.of(
                round(1,mark(1,"8.12"),mark(2,"8.120"),mark(3,"7.59")),
                round(2,mark(1,"8.75"),mark(2,"8.750"),mark(4,null))));
        assertEquals(3,result.size());
        assertEquals(List.of(1,1,3),result.stream().map(CommunityMeasuredStandingResponse::position).toList());
        assertEquals(new BigDecimal("8.75"),result.get(0).bestMetres());
        assertEquals(2,result.get(0).validMarks());
        assertEquals(3L,result.get(2).communityPlayerId());
    }
    @Test void respectsDatabaseScalePaddingWithoutTreatingItAsExtraPrecision() {
        var result=CommunityMeasuredStandings.calculate(List.of(round(1,
                mark(1,"7.2500"),mark(2,"7.2499"),mark(3,"7.250"))));
        assertEquals(2,result.size());
        assertEquals(List.of(1,1),result.stream().map(CommunityMeasuredStandingResponse::position).toList());
        assertEquals(0,result.get(0).bestMetres().compareTo(new BigDecimal("7.25")));
    }

    @Test void excludesMissingZeroNegativeAndOverPreciseMarks() {
        var result=CommunityMeasuredStandings.calculate(List.of(round(1,
                mark(1,null),mark(2,"0"),mark(3,"-0.2"),mark(4,"5.1234"),mark(5,"6.25"))));
        assertEquals(1,result.size());
        assertEquals(5L,result.get(0).communityPlayerId());
    }
}
