package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamScheduleServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentTeamRepository teams;
    @Mock CommunityTournamentTeamPlayerRepository players;
    @Mock CommunityTournamentTeamMatchRepository matches;
    @Mock CommunityTournamentTeamKnockoutService knockout;
    @InjectMocks CommunityTournamentTeamScheduleService service;

    private final Profile owner = profile(1, "owner");
    private final Community community = Community.builder().id(7L).name("CNG").build();
    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(19L).community(community).entryType(TournamentEntryType.TEAM)
            .playersPerTeam(2).status(TournamentStatus.DRAFT).build();

    static Profile profile(long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private void manager(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(owner).role(role).build()));
    }

    private void locked() {
        when(tournaments.findByIdForUpdate(19L)).thenReturn(Optional.of(tournament));
    }

    private List<CommunityTournamentTeam> roster(int count) {
        List<CommunityTournamentTeam> result = new ArrayList<>();
        List<CommunityTournamentTeamPlayer> assigned = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var team = CommunityTournamentTeam.builder().id((long) i + 20)
                    .tournament(tournament).name("Equipa " + i)
                    .normalizedName("equipa " + i).createdAt(Instant.now()).build();
            result.add(team);
            for (int j = 0; j < 2; j++) {
                assigned.add(CommunityTournamentTeamPlayer.builder()
                        .tournament(tournament).team(team).profile(profile(100 + i * 2 + j,
                                "player-" + i + "-" + j)).assignedAt(Instant.now()).build());
            }
        }
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(result);
        when(players.findByTournamentIdOrderByTeamIdAscIdAsc(19L)).thenReturn(assigned);
        for (var p : assigned) {
            when(members.existsByCommunityIdAndProfileId(7L, p.getProfile().getId()))
                    .thenReturn(true);
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<CommunityTournamentTeamMatch> captured() {
        ArgumentCaptor<List> arg = ArgumentCaptor.forClass(List.class);
        verify(matches).saveAll(arg.capture());
        return (List<CommunityTournamentTeamMatch>) arg.getValue();
    }

    @Test
    void generatesEveryPairOnceWithFourTeamsAndNoDoubleBookingsWithinRound() {
        manager(CommunityRole.OWNER);
        locked();
        roster(4);
        var response = service.generate(7L, 19L, "owner");
        assertEquals(4, response.teamCount());
        assertEquals(3, response.roundCount());
        assertEquals(6, response.matchCount());
        assertEquals(TournamentStatus.IN_PROGRESS, response.status());
        var games = captured();
        assertEquals(6, games.size());
        Set<String> pairings = new HashSet<>();
        for (var game : games) {
            assertNotEquals(game.getHomeTeam().getId(), game.getAwayTeam().getId());
            String pair = List.of(game.getHomeTeam().getId(), game.getAwayTeam().getId())
                    .stream().sorted().map(Object::toString).collect(Collectors.joining("-"));
            assertTrue(pairings.add(pair), "Duplicate pairing");
            long sameRound = games.stream()
                    .filter(g -> g.getRoundNumber() == game.getRoundNumber())
                    .filter(g -> g != game &&
                            (g.getHomeTeam().getId().equals(game.getHomeTeam().getId())
                            || g.getAwayTeam().getId().equals(game.getHomeTeam().getId())
                            || g.getHomeTeam().getId().equals(game.getAwayTeam().getId())
                            || g.getAwayTeam().getId().equals(game.getAwayTeam().getId())))
                    .count();
            assertEquals(0, sameRound);
        }
        assertEquals(6, pairings.size());
        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        assertNotNull(tournament.getStartedAt());
    }

    @Test
    void knockoutPublishesOnlyOpeningRoundWithValidatedFullSquads() {
        tournament.setFormat(TournamentFormat.KNOCKOUT);
        manager(CommunityRole.OWNER);
        locked();
        var entrants = roster(5);
        var first = List.of(CommunityTournamentTeamMatch.builder()
                .tournament(tournament).homeTeam(entrants.get(0))
                .awayTeam(entrants.get(1)).roundNumber(1).build());
        when(knockout.firstRound(tournament, entrants)).thenReturn(first);

        var response = service.generate(7L, 19L, "owner");

        assertEquals(TournamentStatus.IN_PROGRESS, response.status());
        assertEquals(5, response.teamCount());
        assertEquals(3, response.roundCount());
        assertEquals(1, response.matchCount());
        assertEquals(first, captured());
        assertNotNull(tournament.getStartedAt());
    }

    @Test
    void twoLegLeagueMirrorsEveryPairOnLaterRounds() {
        tournament.setRoundRobinLegs(2);
        manager(CommunityRole.OWNER);
        locked();
        roster(4);
        var response = service.generate(7L, 19L, "owner");
        assertEquals(6, response.roundCount());
        assertEquals(12, response.matchCount());
        var fixtures = captured();
        assertEquals(12, fixtures.size());
        var unique = new HashSet<String>();
        for (var fixture : fixtures) {
            String directed = fixture.getHomeTeam().getId() + ":" + fixture.getAwayTeam().getId();
            assertTrue(unique.add(directed), "Repeated home/away match");
            if (fixture.getRoundNumber() > 3) {
                assertTrue(fixtures.stream().anyMatch(other ->
                        other.getRoundNumber() == fixture.getRoundNumber() - 3
                        && other.getHomeTeam().getId().equals(fixture.getAwayTeam().getId())
                        && other.getAwayTeam().getId().equals(fixture.getHomeTeam().getId())));
            }
        }
        assertEquals(12, unique.size());
    }

    @Test
    void twoLegLeagueWithOddTeamCountPreservesByes() {
        tournament.setRoundRobinLegs(2);
        manager(CommunityRole.ADMIN);
        locked();
        roster(3);
        var summary = service.generate(7L, 19L, "owner");
        assertEquals(6, summary.roundCount());
        assertEquals(6, summary.matchCount());
        var games = captured();
        for (long id = 20; id <= 22; id++) {
            final long teamId = id;
            assertEquals(4, games.stream().filter(g ->
                    g.getHomeTeam().getId() == teamId || g.getAwayTeam().getId() == teamId).count());
            assertEquals(2, games.stream().filter(g -> g.getHomeTeam().getId() == teamId).count());
            assertEquals(2, games.stream().filter(g -> g.getAwayTeam().getId() == teamId).count());
        }
    }

    @Test
    void oddTeamCountHasByesAndEachTeamPlaysExactlyTwice() {
        manager(CommunityRole.ADMIN);
        locked();
        roster(3);
        var summary = service.generate(7L, 19L, "owner");
        assertEquals(3, summary.roundCount());
        assertEquals(3, summary.matchCount());
        var games = captured();
        assertEquals(3, games.size());
        Map<Long, Long> counts = new HashMap<>();
        for (var game : games) {
            counts.merge(game.getHomeTeam().getId(), 1L, Long::sum);
            counts.merge(game.getAwayTeam().getId(), 1L, Long::sum);
        }
        assertEquals(Set.of(20L, 21L, 22L), counts.keySet());
        assertTrue(counts.values().stream().allMatch(x -> x == 2));
    }

    @Test
    void rejectsTournamentWithIncompleteSquadsWithoutStartingIt() {
        manager(CommunityRole.OWNER);
        locked();
        var teamA = CommunityTournamentTeam.builder().id(20L).build();
        var teamB = CommunityTournamentTeam.builder().id(21L).build();
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(teamA, teamB));
        when(players.findByTournamentIdOrderByTeamIdAscIdAsc(19L)).thenReturn(List.of());
        var failure = assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner"));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, failure.getStatusCode());
        verify(matches, never()).saveAll(any());
        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
    }

    @Test
    void publishesFlexibleLeagueWithDifferentNonEmptyRosterSizes() {
        tournament.setPlayersPerTeam(null);
        manager(CommunityRole.OWNER);
        locked();
        var first = CommunityTournamentTeam.builder().id(20L).name("Verdes").build();
        var second = CommunityTournamentTeam.builder().id(21L).name("Azuis").build();
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(first, second));
        var registrations = List.of(
                CommunityTournamentTeamPlayer.builder()
                        .team(first).profile(profile(100L, "a")).build(),
                CommunityTournamentTeamPlayer.builder()
                        .team(second).profile(profile(101L, "b")).build(),
                CommunityTournamentTeamPlayer.builder()
                        .team(second).profile(profile(102L, "c")).build());
        when(players.findByTournamentIdOrderByTeamIdAscIdAsc(19L)).thenReturn(registrations);
        for (var registration : registrations) {
            when(members.existsByCommunityIdAndProfileId(7L, registration.getProfile().getId()))
                    .thenReturn(true);
        }

        var result = service.generate(7L, 19L, "owner");
        assertEquals(TournamentStatus.IN_PROGRESS, result.status());
        assertEquals(1, result.matchCount());
        assertEquals(1, captured().size());
    }

    @Test
    void rejectsFlexibleCompetitionWithAnEmptyTeam() {
        tournament.setPlayersPerTeam(null);
        manager(CommunityRole.OWNER);
        locked();
        var first = CommunityTournamentTeam.builder().id(20L).build();
        var second = CommunityTournamentTeam.builder().id(21L).build();
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(first, second));
        var registration = CommunityTournamentTeamPlayer.builder()
                .team(first).profile(profile(100L, "a")).build();
        when(players.findByTournamentIdOrderByTeamIdAscIdAsc(19L))
                .thenReturn(List.of(registration));
        when(members.existsByCommunityIdAndProfileId(7L, 100L)).thenReturn(true);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner")).getStatusCode());
        verify(matches, never()).saveAll(any());
        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
    }

    @Test
    void rejectsPlayersWhoAreNoLongerCommunityMembers() {
        manager(CommunityRole.OWNER);
        locked();
        var teamA = CommunityTournamentTeam.builder().id(20L).build();
        var teamB = CommunityTournamentTeam.builder().id(21L).build();
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(teamA, teamB));
        when(players.findByTournamentIdOrderByTeamIdAscIdAsc(19L))
                .thenReturn(List.of(CommunityTournamentTeamPlayer.builder()
                        .team(teamA).profile(profile(100L, "ex-member")).build()));
        var failure = assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner"));
        assertEquals(HttpStatus.CONFLICT, failure.getStatusCode());
        verify(matches, never()).saveAll(any());
    }

    @Test
    void cannotPublishTwiceEvenIfSomeoneRetriesSimultaneously() {
        manager(CommunityRole.OWNER);
        locked();
        when(matches.existsByTournamentId(19L)).thenReturn(true);
        var failure = assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner"));
        assertEquals(HttpStatus.CONFLICT, failure.getStatusCode());
        verify(teams, never()).findByTournamentIdOrderByIdAsc(anyLong());
    }

    @Test
    void ordinaryMemberCannotPublishTheCalendar() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class,
                () -> service.generate(7L, 19L, "owner"));
        verify(tournaments, never()).findByIdForUpdate(anyLong());
    }

    @Test
    void scopeIsolationPreventsUsingAnotherCommunityTournament() {
        manager(CommunityRole.OWNER);
        var another = Community.builder().id(8L).build();
        tournament.setCommunity(another);
        locked();
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner")).getStatusCode());
    }

    @Test
    void refusesIndividualTournamentRoutes() {
        manager(CommunityRole.OWNER);
        tournament.setEntryType(TournamentEntryType.INDIVIDUAL);
        locked();
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 19L, "owner")).getStatusCode());
    }

    @Test
    void pagedFixturesShowRealTeamIdsAndNamesToMembers() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));
        var left = CommunityTournamentTeam.builder().id(20L).name("Azuis").build();
        var right = CommunityTournamentTeam.builder().id(21L).name("Verdes").build();
        var fixture = CommunityTournamentTeamMatch.builder().id(70L).tournament(tournament)
                .roundNumber(1).homeTeam(left).awayTeam(right).build();
        when(matches.findByTournamentId(eq(19L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(List.of(fixture), PageRequest.of(0, 50), false));
        var page = service.matches(7L, 19L, "owner", 0);
        assertFalse(page.hasMore());
        assertEquals("Azuis", page.matches().get(0).homeTeamName());
        assertEquals(21L, page.matches().get(0).awayTeamId());
        assertEquals("Verdes", page.matches().get(0).awayTeamName());
    }

    @Test
    void rejectsNegativePageBeforeAnyQuery() {
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> service.matches(7L, 19L, "owner", -1)).getStatusCode());
        verifyNoInteractions(tournaments);
    }
}
