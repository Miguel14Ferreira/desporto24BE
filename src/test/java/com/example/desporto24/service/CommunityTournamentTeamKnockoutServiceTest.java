package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.community.*;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamKnockoutServiceTest {
    @Mock CommunityTournamentTeamRepository teams;
    @Mock CommunityTournamentTeamMatchRepository matches;
    @Mock CommunityTournamentRepository tournaments;
    @InjectMocks CommunityTournamentTeamKnockoutService service;

    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(19L).format(TournamentFormat.KNOCKOUT).entryType(TournamentEntryType.TEAM)
            .status(TournamentStatus.IN_PROGRESS).build();

    private List<CommunityTournamentTeam> roster(int count) {
        return LongStream.range(1, count + 1)
                .mapToObj(id -> CommunityTournamentTeam.builder().id(id)
                        .name("Equipa " + id).tournament(tournament).build()).toList();
    }

    @Test
    void openingDrawUsesExactlyOneMatchPerFirstRoundParticipantAndLeavesByes() {
        for (int size = 2; size <= 32; size++) {
            var entrants = roster(size);
            var first = service.firstRound(tournament, entrants);
            assertEquals(size - Integer.highestOneBit(size - 1), first.size());
            HashSet<Long> seen = new HashSet<>();
            for (var match : first) {
                assertEquals(1, match.getRoundNumber());
                assertTrue(seen.add(match.getHomeTeam().getId()));
                assertTrue(seen.add(match.getAwayTeam().getId()));
                assertEquals(TournamentMatchStatus.SCHEDULED, match.getStatus());
            }
            assertEquals(first.size() * 2, seen.size());
        }
    }

    @Test
    void knockoutForFiveTeamsAdvancesByesThenWinnersAndSnapshotsChampion() {
        var entrants = roster(5);
        var fixtures = new ArrayList<>(service.firstRound(tournament, entrants));
        when(teams.findByTournamentIdOrderByIdAsc(19L)).thenReturn(entrants);
        when(matches.findByTournamentId(eq(19L), any(PageRequest.class)))
                .thenAnswer(invocation -> new SliceImpl<>(List.copyOf(fixtures),
                        PageRequest.of(0, 32), false));
        when(matches.saveAll(any())).thenAnswer(invocation -> {
            List<CommunityTournamentTeamMatch> games = invocation.getArgument(0);
            fixtures.addAll(games);
            return games;
        });

        var first = fixtures.get(0);
        first.setStatus(TournamentMatchStatus.COMPLETED);
        first.setHomeScore(3);
        first.setAwayScore(0);
        service.advanceAfterResult(tournament);
        assertEquals(3, fixtures.size());
        assertEquals(2, fixtures.stream().filter(game -> game.getRoundNumber() == 2).count());

        for (var game : fixtures.stream().filter(g -> g.getRoundNumber() == 2).toList()) {
            game.setStatus(TournamentMatchStatus.COMPLETED);
            game.setHomeScore(2);
            game.setAwayScore(1);
        }
        service.advanceAfterResult(tournament);
        assertEquals(4, fixtures.size());
        var finalMatch = fixtures.get(3);
        assertEquals(3, finalMatch.getRoundNumber());
        finalMatch.setStatus(TournamentMatchStatus.COMPLETED);
        finalMatch.setHomeScore(0);
        finalMatch.setAwayScore(1);
        service.advanceAfterResult(tournament);
        assertEquals(TournamentStatus.COMPLETED, tournament.getStatus());
        assertEquals(finalMatch.getAwayTeam().getId(), tournament.getChampionTeamId());
        assertEquals(finalMatch.getAwayTeam().getName(), tournament.getChampionTeamName());
        assertFalse(tournament.isChampionTied());
        assertNotNull(tournament.getCompletedAt());
        verify(tournaments).save(tournament);
    }

    @Test
    void incompleteRoundDoesNotCreateAnotherRound() {
        var first = new ArrayList<>(service.firstRound(tournament, roster(4)));
        first.get(0).setStatus(TournamentMatchStatus.COMPLETED);
        first.get(0).setHomeScore(1);
        first.get(0).setAwayScore(0);
        when(matches.findByTournamentId(eq(19L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(first, PageRequest.of(0, 32), false));

        service.advanceAfterResult(tournament);
        verify(matches, never()).saveAll(any());
        verify(tournaments, never()).save(any());
    }

    @Test
    void drawCannotAdvanceBracket() {
        var first = new ArrayList<>(service.firstRound(tournament, roster(2)));
        first.get(0).setStatus(TournamentMatchStatus.COMPLETED);
        first.get(0).setHomeScore(1);
        first.get(0).setAwayScore(1);
        when(matches.findByTournamentId(eq(19L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(first, PageRequest.of(0, 32), false));

        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.advanceAfterResult(tournament)).getStatusCode());
        verify(tournaments, never()).save(any());
    }
}
