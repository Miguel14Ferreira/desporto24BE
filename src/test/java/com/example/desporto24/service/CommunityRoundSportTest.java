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
    @Test void resolvesSelectedSportWithAccentsCaseAndWhitespace() {
        Community community = Community.builder()
                .sports(List.of("Ténis", "Ciclismo de Pista", "Futebol")).build();
        assertEquals("Ténis", CommunityRoundSport.resolve(community, "  TENIS  "));
        assertEquals("Ciclismo de Pista", CommunityRoundSport.resolve(community, "ciclismo de pista"));
        assertEquals("Futebol", CommunityRoundSport.resolve(community, "FUTEBOL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundSport.resolve(community, "Karting"));
    }
    @Test void doesNotGuessSportInMultiSportCommunityWithBlankRoundSport() {
        Community community = Community.builder()
                .sport("Futebol").sports(List.of("Futebol", "Surf")).build();
        assertNull(CommunityRoundSport.resolve(community, "  "));
        assertEquals("Surf", CommunityRoundSport.resolve(community, "surf"));
    }
    @Test void legacyCommunityPrimarySportStillWorks() {
        Community community = Community.builder().sport("Futebol").build();
        assertEquals("Futebol", CommunityRoundSport.resolve(community, null));
    }
}
