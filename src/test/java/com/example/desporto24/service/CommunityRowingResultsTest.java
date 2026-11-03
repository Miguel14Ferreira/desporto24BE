package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRowingResultsTest {
    private CommunityRoundEntryRequest time(String username, String seconds) {
        return new CommunityRoundEntryRequest(username,null,CommunityMatchResult.DRAW,
                0,0,false,false,null,seconds == null ? null : new BigDecimal(seconds),null,null);
    }
    @Test void acceptsOnlySkiff 1xIndividualTimesAtMillisecondPrecision() {
        assertDoesNotThrow(() -> CommunityRowingResults.validate("Remo",
                List.of(time("ana","47.123"),time("rui","48.700"))));
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Remo",List.of(time("ana","47.1234"))));
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Remo",List.of(time("ana","0"))));
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Remo",List.of(time("ana","47.123"),time("ANA","48.123"))));
    }
    @Test void rejectsFootballGoalsPositionsAndOtherSports() {
        var position=new CommunityRoundEntryRequest("ana",null,CommunityMatchResult.DRAW,
                0,0,false,false,1,new BigDecimal("47.123"),null,null);
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Remo",List.of(position)));
        var football=new CommunityRoundEntryRequest("ana",null,CommunityMatchResult.WIN,
                2,0,false,false,null,new BigDecimal("47.123"),null,null);
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Remo",List.of(football)));
        assertThrows(IllegalArgumentException.class,()->CommunityRowingResults.validate(
                "Natação",List.of(time("ana","47.123"))));
    }
}
