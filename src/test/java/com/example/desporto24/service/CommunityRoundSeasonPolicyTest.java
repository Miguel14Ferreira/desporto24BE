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
    void automaticSeasonAttributionNeverUsesForeignOrClosedSeasons() {
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(
                        7L, season(8L, "Futsal", true), "Futsal", false))
                .getStatusCode());
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validate(
                        7L, season(7L, "Futsal", false), "Futsal", false))
                .getStatusCode());
        var active = season(7L, "Futsal", true);
        assertSame(active, CommunityRoundSeasonPolicy.validate(7L, active, "Futsal", false));
    }

    @Test
    void choosesMatchingSportSeasonOverLegacySharedSeason() {
        var football = season(7L, "Futebol", true);
        var karting = season(7L, "Karting", true);
        var legacy = season(7L, null, true);
        assertSame(karting, CommunityRoundSeasonPolicy.selectForRound(
                java.util.List.of(football, legacy, karting), "karting"));
        assertSame(football, CommunityRoundSeasonPolicy.selectForRound(
                java.util.List.of(karting, football), "FUTEBOL"));
        assertSame(legacy, CommunityRoundSeasonPolicy.selectForRound(
                java.util.List.of(football, legacy), "Ténis"));
    }

    @Test
    void ambiguousOrUnmatchedActiveSeasonsRequireExplicitSelection() {
        var football = season(7L, "Futebol", true);
        var karting = season(7L, "Karting", true);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.selectForRound(
                        java.util.List.of(football, karting), null)).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.selectForRound(
                        java.util.List.of(football, karting), "Ténis")).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.selectForRound(
                        java.util.List.of(football, season(7L, "futebol", true)), "Futebol"))
                .getStatusCode());
    }

    @Test
    void legacyZeroOrSingleActiveSeasonBehaviorRemainsStable() {
        var football = season(7L, "Futebol", true);
        assertNull(CommunityRoundSeasonPolicy.selectForRound(java.util.List.of(), "Futebol"));
        assertSame(football, CommunityRoundSeasonPolicy.selectForRound(
                java.util.List.of(football), "Karting"));
    }

    @Test
    void historicalImportMayUseClosedSportTaggedSeasonOnlyForMatchingSport() {
        var community = Community.builder().id(7L)
                .sports(java.util.List.of("Futebol", "Karting", "Ténis")).build();
        var archived = CommunitySeason.builder().id(55L).community(community)
                .sport("Ténis").active(false).build();
        assertSame(archived, CommunityRoundSeasonPolicy.validateImport(7L, archived, "TENIS"));
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validateImport(7L, archived, "Karting"))
                .getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> CommunityRoundSeasonPolicy.validateImport(8L, archived, "Ténis"))
                .getStatusCode());
    }

    @Test
    void importPreservesLegacySharedSeasonsAndSingleSportRequestCompatibility() {
        var single = Community.builder().id(7L).sport("Futebol").build();
        var season = CommunitySeason.builder().community(single)
                .sport("Futebol").active(false).build();
        assertSame(season, CommunityRoundSeasonPolicy.validateImport(7L, season, null));
        var shared = CommunitySeason.builder()
                .community(Community.builder().id(7L)
                        .sports(java.util.List.of("Futebol", "Karting")).build())
                .sport(null).active(false).build();
        assertSame(shared, CommunityRoundSeasonPolicy.validateImport(7L, shared, "Karting"));
    }

    @Test
    void olderClientsCanStillCreateUnassignedRounds() {
        assertNull(CommunityRoundSeasonPolicy.validate(7L, null, "Futsal", false));
    }
}
