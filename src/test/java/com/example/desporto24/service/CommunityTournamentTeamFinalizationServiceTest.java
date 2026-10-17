package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamFinalizationServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentTeamMatchRepository matches;
    @Mock CommunityTournamentTeamStandingsService standingsService;
    @InjectMocks CommunityTournamentTeamFinalizationService service;

    private Profile actor;
    private CommunityTournament tournament;
    private CommunityTournamentTeam blue, green, red;

    @BeforeEach
    void setup() {
        actor = new Profile();
        actor.setId(1L);
        actor.setUsername("owner");
        tournament = CommunityTournament.builder().id(19L)
                .community(Community.builder().id(7L).build())
                .entryType(TournamentEntryType.TEAM).format(TournamentFormat.LEAGUE)
                .status(TournamentStatus.IN_PROGRESS).build();
        blue = CommunityTournamentTeam.builder().id(20L).name("Azuis").build();
        green = CommunityTournamentTeam.builder().id(21L).name("Verdes").build();
        red = CommunityTournamentTeam.builder().id(22L).name("Vermelhos").build();
    }

    private void manager(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(CommunityMember.builder().role(role).build()));
    }

    private void locked() {
        when(tournaments.findByIdForUpdate(19L)).thenReturn(Optional.of(tournament));
    }

    private CommunityTournamentTeamMatch match(long id, CommunityTournamentTeam home,
                                               CommunityTournamentTeam away) {
        return CommunityTournamentTeamMatch.builder().id(id).tournament(tournament)
                .homeTeam(home).awayTeam(away).roundNumber(1)
                .status(TournamentMatchStatus.COMPLETED).homeScore(1).awayScore(0)
                .completedAt(Instant.now()).build();
    }

    private CommunityTournamentTeamStandingRowResponse row(int position, CommunityTournamentTeam team,
                                                            int played, int wins, int goals,
                                                            int conceded, long points) {
        return new CommunityTournamentTeamStandingRowResponse(position,
                team.getId(), team.getName(), played, wins, 0, played - wins,
                goals, conceded, goals - conceded, points);
    }

    private void standingTable(List<CommunityTournamentTeamStandingRowResponse> rows,
                               long total, int completed) {
        when(standingsService.standings(7L, 19L, "owner"))
                .thenReturn(new CommunityTournamentTeamStandingsResponse(
                        19L, TournamentStatus.IN_PROGRESS, total, completed,
                        3, 1, 0, rows));
    }

    private void fixtures(List<CommunityTournamentTeamMatch> games) {
        when(matches.findByTournamentId(eq(19L), any(Pageable.class))).thenReturn(
                new SliceImpl<>(games, org.springframework.data.domain.PageRequest.of(0, 497), false));
    }

    @Test
    void closesCompleteLeagueWithImmutableTeamSnapshot() {
        manager(CommunityRole.OWNER);
        locked();
        standingTable(List.of(row(1, blue, 2, 2, 4, 1, 6),
                row(2, green, 2, 1, 1, 2, 3),
                row(3, red, 2, 0, 0, 2, 0)), 3, 3);
        fixtures(List.of(match(1, blue, green), match(2, blue, red), match(3, green, red)));
        when(tournaments.save(tournament)).thenReturn(tournament);

        var result = service.complete(7L, 19L, "owner");

        assertEquals(TournamentStatus.COMPLETED, result.status());
        assertEquals(20L, result.championTeamId());
        assertEquals("Azuis", result.championTeamName());
        assertEquals(6L, result.championPoints());
        assertNull(result.championProfileId());
        assertFalse(result.championTied());
        assertNotNull(result.completedAt());
    }

    @Test
    void exactSportingTieIsNotResolvedUsingTeamId() {
        manager(CommunityRole.ADMIN);
        locked();
        standingTable(List.of(row(1, blue, 1, 0, 0, 0, 1),
                row(2, green, 1, 0, 0, 0, 1)), 1, 1);
        fixtures(List.of(match(1, blue, green)));
        when(tournaments.save(tournament)).thenReturn(tournament);

        var result = service.complete(7L, 19L, "owner");

        assertTrue(result.championTied());
        assertNull(result.championTeamId());
        assertNull(result.championTeamName());
        assertNull(result.championPoints());
    }

    @Test
    void repeatDoesNotReplaceChampionOrCompletionTime() {
        manager(CommunityRole.OWNER);
        locked();
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournament.setChampionTeamId(20L);
        tournament.setChampionTeamName("Azuis");
        tournament.setChampionPoints(9L);
        tournament.setCompletedAt(Instant.parse("2026-10-17T12:00:00Z"));
        var result = service.complete(7L, 19L, "owner");
        assertEquals("Azuis", result.championTeamName());
        assertEquals(9L, result.championPoints());
        assertEquals(Instant.parse("2026-10-17T12:00:00Z"), result.completedAt());
        verifyNoInteractions(matches, standingsService);
        verify(tournaments, never()).save(any());
    }

    @Test
    void incompleteScoresCannotFinalize() {
        manager(CommunityRole.OWNER);
        locked();
        standingTable(List.of(row(1, blue, 2, 1, 2, 1, 3),
                row(2, green, 1, 0, 0, 1, 0),
                row(3, red, 1, 0, 1, 1, 1)), 3, 2);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 19L, "owner")).getStatusCode());
        verifyNoInteractions(matches);
        verify(tournaments, never()).save(any());
    }

    @Test
    void duplicatedPairDoesNotHideMissingOpponent() {
        manager(CommunityRole.OWNER);
        locked();
        standingTable(List.of(row(1, blue, 2, 2, 4, 1, 6),
                row(2, green, 2, 1, 1, 2, 3),
                row(3, red, 2, 0, 0, 2, 0)), 3, 3);
        fixtures(List.of(match(1, blue, green), match(2, green, blue), match(3, blue, red)));
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 19L, "owner")).getStatusCode());
        verify(tournaments, never()).save(any());
    }

    @Test
    void onlyOrganizersCanFinalize() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class,
                () -> service.complete(7L, 19L, "owner"));
        verifyNoInteractions(tournaments, matches, standingsService);
    }

    @Test
    void rejectsForeignCommunityWithoutLeakingResults() {
        manager(CommunityRole.OWNER);
        locked();
        tournament.setCommunity(Community.builder().id(8L).build());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 19L, "owner")).getStatusCode());
        verifyNoInteractions(matches, standingsService);
    }

    @Test
    void draftAndIndividualCompetitionsAreNotEligible() {
        manager(CommunityRole.OWNER);
        locked();
        tournament.setStatus(TournamentStatus.DRAFT);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 19L, "owner")).getStatusCode());
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setEntryType(TournamentEntryType.INDIVIDUAL);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.complete(7L, 19L, "owner")).getStatusCode());
        verifyNoInteractions(matches, standingsService);
    }
}
