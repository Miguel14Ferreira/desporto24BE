package com.example.desporto24.service;

import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunitySeason;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class CommunityRoundSeasonPolicyTest {
    private CommunitySeason season(Long communityId, String sport, boolean active) {
        return CommunitySeason.builder()
                .id(44L).community(Community.builder().id(communityId).build())
                .sport(sport).active(active).build();
    }

    @Test
    void explicitSeasonRequiresMatchingCommunityAndActiveStatus() {
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(7L, null, "Futebol", true))
                .getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(
                        7L, season(8L, "Futebol", true), "Futebol", true))
                .getStatusCode());
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(
                        7L, season(7L, "Futebol", false), "Futebol", true))
                .getStatusCode());
    }

    @Test
    void sportTaggedSeasonRejectsOtherSportsButSharedSeasonsRemainValid() {
        var tennis = season(7L, "Ténis", true);
        assertSame(tennis, CommunityRoundSeasonPolicy.validate(7L, tennis, "tenis", true));
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(7L, tennis, "Karting", true))
                .getStatusCode());
        var shared = season(7L, null, true);
        assertSame(shared, CommunityRoundSeasonPolicy.validate(7L, shared, "Karting", true));
    }

    @Test
    void olderClientsCanStillCreateUnassignedRounds() {
        assertNull(CommunityRoundSeasonPolicy.validate(7L, null, "Futsal", false));
    }
}
