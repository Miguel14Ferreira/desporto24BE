package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingRowResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingsResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentFinalizationServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentStandingsService standingsService;
    @InjectMocks CommunityTournamentFinalizationService service;

    private final Profile manager = profile(1L, "owner");
    private final Community community = Community.builder().id(7L)
            .name("Liga").owner(manager).build();
    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(9L).community(community).name("Liga de amigos")
            .status(TournamentStatus.IN_PROGRESS).startedAt(Instant.now()).build();

    private static Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private void actingAs(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(manager));
        when(members.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(CommunityMember.builder()
                        .profile(manager).community(community).role(role).build()));
    }

    private void locked() {
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
    }

    private CommunityTournamentStandingRowResponse row(Long id, String username,
            int played, int won, int drawn, int lost, int gf, int ga, long points) {
        return new CommunityTournamentStandingRowResponse(
                id.intValue(), id, username, username + " Silva", played,
                won, drawn, lost, gf, ga, gf - ga, points);
    }

    private void standings(long totalMatches, int completed, List<CommunityTournamentStandingRowResponse> rows) {
        when(standingsService.standings(7L, 9L, "owner")).thenReturn(
                new CommunityTournamentStandingsResponse(
                        9L, TournamentStatus.IN_PROGRESS, totalMatches, completed,
                        3, 1, 0, rows));
    }

    @Test
    void ownerClosesCompleteLeagueAndFreezesWinningProfileAndPoints() {
        actingAs(CommunityRole.OWNER);
        locked();
        standings(3, 3, List.of(
                row(5L, "ana", 2, 2, 0, 0, 4, 1, 6),
                row(6L, "rui", 2, 1, 0, 1, 3, 2, 3),
                row(7L, "joao", 2, 0, 0, 2, 1, 5, 0)));
        when(tournaments.save(tournament)).thenReturn(tournament);

        var finalized = service.complete(7L, 9L, "owner");

        assertEquals(TournamentStatus.COMPLETED, finalized.status());
        assertEquals(5L, finalized.championProfileId());
        assertEquals("ana", finalized.championUsername());
        assertEquals("ana Silva", finalized.championDisplayName());
        assertEquals(6L, finalized.championPoints());
        assertFalse(finalized.championTied());
        assertNotNull(finalized.completedAt());
        verify(tournaments).findByIdForUpdate(9L);
        verify(tournaments).save(tournament);
    }

    @Test
    void realSportingTieClosesWithoutChoosingChampionByProfileId() {
        actingAs(CommunityRole.ADMIN);
        locked();
        standings(1, 1, List.of(
                row(5L, "ana", 1, 0, 1, 0, 0, 0, 1),
                row(6L, "rui", 1, 0, 1, 0, 0, 0, 1)));
        when(tournaments.save(tournament)).thenReturn(tournament);

        var finalized = service.complete(7L, 9L, "owner");

        assertEquals(TournamentStatus.COMPLETED, finalized.status());
        assertTrue(finalized.championTied());
        assertNull(finalized.championProfileId());
        assertNull(finalized.championUsername());
        assertNull(finalized.championPoints());
        assertNotNull(finalized.completedAt());
    }

    @Test
    void repeatedCompletionDoesNotOverwriteSnapshot() {
        actingAs(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournament.setCompletedAt(Instant.parse("2026-10-16T12:00:00Z"));
        tournament.setChampionProfileId(5L);
        tournament.setChampionUsername("ana");
        tournament.setChampionPoints(100L);
        locked();

        var response = service.complete(7L, 9L, "owner");

        assertEquals(5L, response.championProfileId());
        assertEquals(100L, response.championPoints());
        assertEquals(Instant.parse("2026-10-16T12:00:00Z"), response.completedAt());
        verifyNoInteractions(standingsService);
        verify(tournaments, never()).save(any());
    }

    @Test
    void normalMemberCannotFinalize() {
        actingAs(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () -> service.complete(7L, 9L, "owner"));
        verifyNoInteractions(tournaments, standingsService);
    }

    @Test
    void outsiderCannotFinalize() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(manager));
        assertThrows(AccessDeniedException.class, () -> service.complete(7L, 9L, "owner"));
        verifyNoInteractions(tournaments, standingsService);
    }

    @Test
    void guessedTournamentFromAnotherCommunityIsNotRevealed() {
        actingAs(CommunityRole.ADMIN);
        tournament.setCommunity(Community.builder().id(99L).build());
        locked();
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verifyNoInteractions(standingsService);
    }

    @Test
    void draftCannotBeFinalized() {
        actingAs(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.DRAFT);
        locked();
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verifyNoInteractions(standingsService);
    }

    @Test
    void unfinishedMatchPreventsFinalization() {
        actingAs(CommunityRole.OWNER);
        locked();
        standings(3, 2, List.of(
                row(5L, "ana", 2, 1, 1, 0, 3, 2, 4),
                row(6L, "rui", 1, 0, 1, 0, 1, 1, 1),
                row(7L, "joao", 1, 0, 0, 1, 0, 1, 0)));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        verify(tournaments, never()).save(any());
    }

    @Test
    void missingRoundCannotBeHiddenByAllExistingMatchesBeingComplete() {
        actingAs(CommunityRole.OWNER);
        locked();
        standings(2, 2, List.of(
                row(5L, "ana", 2, 1, 1, 0, 3, 2, 4),
                row(6L, "rui", 2, 0, 1, 1, 1, 2, 1),
                row(7L, "joao", 2, 0, 2, 0, 1, 1, 2)));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verify(tournaments, never()).save(any());
    }

    @Test
    void noPlayersCannotFinalize() {
        actingAs(CommunityRole.OWNER);
        locked();
        standings(0, 0, List.of());
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verify(tournaments, never()).save(any());
    }

    @Test
    void impossibleNumberOfGamesPerPlayerCannotFinalize() {
        actingAs(CommunityRole.OWNER);
        locked();
        standings(1, 1, List.of(
                row(5L, "ana", 0, 0, 0, 0, 0, 0, 0),
                row(6L, "rui", 1, 1, 0, 0, 1, 0, 3)));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verify(tournaments, never()).save(any());
    }

    @Test
    void noAuthorizationOnNonexistentTournament() {
        actingAs(CommunityRole.OWNER);
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 9L, "owner")).getStatusCode().value());
        verifyNoInteractions(standingsService);
    }
}
