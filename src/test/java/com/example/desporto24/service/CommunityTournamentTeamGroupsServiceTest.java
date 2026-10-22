package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamGroupsServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentTeamRepository teams;
    @Mock CommunityTournamentTeamMatchRepository matches;
    @InjectMocks CommunityTournamentTeamGroupsService service;

    private CommunityTournament competition(int groups) {
        return CommunityTournament.builder().id(19L)
                .format(TournamentFormat.GROUP_KNOCKOUT)
                .groupCount(groups).entryType(TournamentEntryType.TEAM)
                .status(TournamentStatus.IN_PROGRESS)
                .winPoints(3).drawPoints(1).lossPoints(0).build();
    }
    private List<CommunityTournamentTeam> roster(CommunityTournament cup, int n) {
        return LongStream.rangeClosed(1, n)
                .mapToObj(id -> CommunityTournamentTeam.builder()
                        .id(id).name("Equipa " + id).tournament(cup).build()).toList();
    }

    @Test
    void twoGroupsOfFourGenerateExactlySixGamesEachWithNoRepeatedPair() {
        var cup = competition(2);
        var entrants = roster(cup, 8);
        var fixtures = service.draw(cup, entrants);
        assertEquals(12, fixtures.size());
        assertEquals(Set.of(1, 2), Set.copyOf(entrants.stream()
                .map(CommunityTournamentTeam::getGroupNumber).toList()));
        assertEquals(8, new HashSet<>(entrants.stream()
                .map(CommunityTournamentTeam::getGroupDrawOrder).toList()).size());
        Set<String> played = new HashSet<>();
        for (var game : fixtures) {
            assertEquals(game.getHomeTeam().getGroupNumber(), game.getAwayTeam().getGroupNumber());
            assertEquals(game.getGroupNumber(), game.getHomeTeam().getGroupNumber());
            long a = Math.min(game.getHomeTeam().getId(), game.getAwayTeam().getId());
            long b = Math.max(game.getHomeTeam().getId(), game.getAwayTeam().getId());
            assertTrue(played.add(game.getGroupNumber() + ":" + a + ":" + b));
            assertEquals(TournamentMatchStatus.SCHEDULED, game.getStatus());
            assertNull(game.getHomeScore());
            assertNull(game.getAwayScore());
        }
        verify(teams).saveAll(any());
    }

    @Test
    void fourGroupsOfThreeGenerateOneMatchPerPairAndNeverFakeResults() {
        var cup = competition(4);
        var fixtures = service.draw(cup, roster(cup, 12));
        assertEquals(12, fixtures.size());
        Map<Integer, Integer> counts = new HashMap<>();
        fixtures.forEach(game -> {
            counts.merge(game.getGroupNumber(), 1, Integer::sum);
            assertNull(game.getHomeScore());
            assertNull(game.getAwayScore());
        });
        assertEquals(Map.of(1, 3, 2, 3, 3, 3, 4, 3), counts);
    }

    @Test
    void incompleteGroupsCannotTriggerKnockoutAndCompletedGroupsSeedSemifinals() {
        var cup = competition(2);
        var entrants = roster(cup, 6);
        var fixtures = service.draw(cup, entrants);
        when(matches.existsByTournamentIdAndGroupNumberIsNull(19L)).thenReturn(false);
        when(matches.findByTournamentIdAndGroupNumberIsNotNull(eq(19L), any(PageRequest.class)))
                .thenAnswer(call -> new SliceImpl<>(fixtures, PageRequest.of(0, 993), false));
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(entrants);
        service.advanceAfterGroupResult(cup);
        verify(matches, never()).saveAll(any());

        for (var game : fixtures) {
            game.setStatus(TournamentMatchStatus.COMPLETED);
            game.setHomeScore(1);
            game.setAwayScore(0);
        }
        service.advanceAfterGroupResult(cup);
        @SuppressWarnings("unchecked")
        org.mockito.ArgumentCaptor<List<CommunityTournamentTeamMatch>> capture =
                org.mockito.ArgumentCaptor.forClass(List.class);
        verify(matches).saveAll(capture.capture());
        var semifinals = capture.getValue();
        assertEquals(2, semifinals.size());
        var picked = new HashSet<Long>();
        for (var game : semifinals) {
            assertEquals(1, game.getRoundNumber());
            assertNull(game.getGroupNumber());
            assertNotEquals(game.getHomeTeam().getGroupNumber(), game.getAwayTeam().getGroupNumber());
            assertTrue(picked.add(game.getHomeTeam().getId()));
            assertTrue(picked.add(game.getAwayTeam().getId()));
            assertNull(game.getHomeScore());
            assertNull(game.getAwayScore());
        }
        assertEquals(4, picked.size());
    }

    @Test
    void drawOrderIsLastTieBreakAfterGenuineResults() {
        var cup = competition(2);
        var entrants = roster(cup, 6);
        var fixtures = service.draw(cup, entrants);
        when(matches.existsByTournamentIdAndGroupNumberIsNull(19L)).thenReturn(false);
        when(matches.findByTournamentIdAndGroupNumberIsNotNull(eq(19L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(fixtures, PageRequest.of(0, 993), false));
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(entrants);
        fixtures.forEach(game -> {
            game.setStatus(TournamentMatchStatus.COMPLETED);
            game.setHomeScore(0);
            game.setAwayScore(0);
        });
        service.advanceAfterGroupResult(cup);
        @SuppressWarnings("unchecked")
        org.mockito.ArgumentCaptor<List<CommunityTournamentTeamMatch>> cap =
                org.mockito.ArgumentCaptor.forClass(List.class);
        verify(matches).saveAll(cap.capture());
        for (var game : cap.getValue()) {
            // The first two in each group must come from the stored initial random draw.
            var group = game.getHomeTeam().getGroupNumber();
            var order = entrants.stream().filter(team -> team.getGroupNumber().equals(group))
                    .map(CommunityTournamentTeam::getGroupDrawOrder).sorted().limit(2).toList();
            assertTrue(order.contains(game.getHomeTeam().getGroupDrawOrder()));
        }
    }

    @Test
    void upToThirtyTwoTeamsHaveEveryRequiredPairWithoutRoundConflicts() {
        for (int groups : new int[]{2, 4}) {
            for (int size = groups * 2; size <= 32; size++) {
                reset(teams);
                var cup = competition(groups);
                var fixtures = service.draw(cup, roster(cup, size));
                int base = size / groups, extra = size % groups;
                int expected = extra * (base + 1) * base / 2
                        + (groups - extra) * base * (base - 1) / 2;
                assertEquals(expected, fixtures.size(),
                        "Unexpected fixtures with " + size + " teams in " + groups + " groups");
                Set<String> pairs = new HashSet<>();
                Set<String> occupied = new HashSet<>();
                for (var fixture : fixtures) {
                    int group = fixture.getGroupNumber();
                    assertEquals(group, fixture.getHomeTeam().getGroupNumber());
                    assertEquals(group, fixture.getAwayTeam().getGroupNumber());
                    long low = Math.min(fixture.getHomeTeam().getId(), fixture.getAwayTeam().getId());
                    long high = Math.max(fixture.getHomeTeam().getId(), fixture.getAwayTeam().getId());
                    assertTrue(pairs.add(group + ":" + low + ":" + high),
                            "The same teams cannot play twice in their group");
                    assertTrue(occupied.add(group + ":" + fixture.getRoundNumber() + ":"
                            + fixture.getHomeTeam().getId()), "Home team plays twice in a round");
                    assertTrue(occupied.add(group + ":" + fixture.getRoundNumber() + ":"
                            + fixture.getAwayTeam().getId()), "Away team plays twice in a round");
                }
            }
        }
    }

    @Test
    void fourGroupsAutomaticallyCreateEightQualifiedTeamsOnlyAfterLastMatch() {
        var cup = competition(4);
        var entrants = roster(cup, 12);
        var fixtures = service.draw(cup, entrants);
        when(matches.existsByTournamentIdAndGroupNumberIsNull(19L))
                .thenReturn(false, false, true);
        when(matches.findByTournamentIdAndGroupNumberIsNotNull(eq(19L), any(PageRequest.class)))
                .thenAnswer(call -> new SliceImpl<>(fixtures, PageRequest.of(0, 993), false));
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(entrants);
        for (var game : fixtures) {
            game.setStatus(TournamentMatchStatus.COMPLETED);
            game.setHomeScore(1);
            game.setAwayScore(0);
        }
        var outstanding = fixtures.get(0);
        outstanding.setStatus(TournamentMatchStatus.SCHEDULED);
        outstanding.setHomeScore(null);
        outstanding.setAwayScore(null);
        service.advanceAfterGroupResult(cup);
        verify(matches, never()).saveAll(any());

        outstanding.setStatus(TournamentMatchStatus.COMPLETED);
        outstanding.setHomeScore(2);
        outstanding.setAwayScore(1);
        service.advanceAfterGroupResult(cup);
        @SuppressWarnings("unchecked")
        org.mockito.ArgumentCaptor<List<CommunityTournamentTeamMatch>> cap =
                org.mockito.ArgumentCaptor.forClass(List.class);
        verify(matches, times(1)).saveAll(cap.capture());
        var quarterfinals = cap.getValue();
        assertEquals(4, quarterfinals.size());
        Set<Long> qualified = new HashSet<>();
        for (var match : quarterfinals) {
            assertNull(match.getGroupNumber());
            assertEquals(1, match.getRoundNumber());
            assertNotEquals(match.getHomeTeam().getGroupNumber(),
                    match.getAwayTeam().getGroupNumber());
            assertTrue(qualified.add(match.getHomeTeam().getId()));
            assertTrue(qualified.add(match.getAwayTeam().getId()));
            assertNull(match.getHomeScore());
            assertNull(match.getAwayScore());
        }
        assertEquals(8, qualified.size());

        // A concurrent second callback must not publish another bracket.
        service.advanceAfterGroupResult(cup);
        verify(matches, times(1)).saveAll(any());
    }

    @Test
    void invalidGroupCountOrTooFewTeamsIsRejectedBeforePublishing() {
        var invalid = competition(3);
        var error = assertThrows(ResponseStatusException.class,
                () -> service.draw(invalid, roster(invalid, 10)));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        var valid = competition(4);
        var shortage = assertThrows(ResponseStatusException.class,
                () -> service.draw(valid, roster(valid, 7)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, shortage.getStatusCode());
        verify(teams, never()).saveAll(any());
    }
}
