package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunitySeason;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunitySeasonCreationPolicyTest {
    private CommunitySeason active(String sport) {
        return CommunitySeason.builder().sport(sport).active(true).build();
    }

    @Test
    void allowsDifferentTaggedSportsSimultaneously() {
        assertDoesNotThrow(() -> CommunitySeasonCreationPolicy.check(
                List.of(active("Futebol"), active("Karting")), "Ténis"));
        assertDoesNotThrow(() -> CommunitySeasonCreationPolicy.check(List.of(), null));
    }

    @Test
    void rejectsDuplicateSportIgnoringCaseAndAccents() {
        assertThrows(IllegalStateException.class, () -> CommunitySeasonCreationPolicy.check(
                List.of(active("Ténis")), "  TENIS  "));
        assertThrows(IllegalStateException.class, () -> CommunitySeasonCreationPolicy.check(
                List.of(active("Karting")), "karting"));
    }

    @Test
    void sharedLegacySeasonCannotOverlapTaggedSeasons() {
        assertThrows(IllegalStateException.class, () -> CommunitySeasonCreationPolicy.check(
                List.of(active(null)), "Futebol"));
        assertThrows(IllegalStateException.class, () -> CommunitySeasonCreationPolicy.check(
                List.of(active("Futebol")), null));
        assertThrows(IllegalStateException.class, () -> CommunitySeasonCreationPolicy.check(
                List.of(active("Futebol"), active("Karting")), ""));
    }
}
