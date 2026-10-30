package com.example.desporto24.service;

import com.example.desporto24.model.community.Community;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRoundSportTest {
    @Test void singleSportLegacyRoundInheritsCommunitySport() {
        Community community = Community.builder().sport("Futsal").sports(List.of("Futsal")).build();
        assertEquals("Futsal", CommunityRoundSport.resolve(community, null));
    }
    @Test void multiSportRoundDoesNotGuessItsSport() {
        Community community = Community.builder().sports(List.of("Futsal", "Karting")).build();
        assertNull(CommunityRoundSport.resolve(community, null));
        assertEquals("Karting", CommunityRoundSport.resolve(community, "karting"));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundSport.resolve(community, "Ténis"));
    }
    @Test void legacyCommunityPrimarySportStillWorks() {
        Community community = Community.builder().sport("Futebol").build();
        assertEquals("Futebol", CommunityRoundSport.resolve(community, null));
    }
}
