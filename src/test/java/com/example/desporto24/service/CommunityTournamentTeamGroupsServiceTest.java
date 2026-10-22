package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityTournamentTeamGroupsServiceTest {
    private final CommunityTournamentTeamMatchRepository matches =
            mock(CommunityTournamentTeamMatchRepository.class);
    private final CommunityTournamentTeamGroupsService groups = new CommunityTournamentTeamGroupsService(
            mock(ProfileRepository.class), mock(CommunityMemberRepository.class),
            mock(CommunityTournamentRepository.class), matches);

    private CommunityTournament tournament() {
        return CommunityTournament.builder().id(11L).format(TournamentFormat.GROUP_KNOCKOUT)
                .status(TournamentStatus.IN_PROGRESS).winPoints(3).drawPoints(1)
                .lossPoints(0).build();
    }

    private List<CommunityTournamentTeam> teams(int count) {
        return IntStream.rangeClosed(1, count).mapToObj(index ->
                CommunityTournamentTeam.builder().id((long) index)
                        .name("Equipa " + index).build()).toList();
    }

    @Test
    void formatsOfFourSixEightTwelveSixteenTwentyFourAndThirtyTwoWork() {
        assertEquals(Set.of(4, 6, 8, 12, 16, 24, 32),
                IntStream.rangeClosed(2, 32)
                        .filter(CommunityTournamentTeamGroupsService::supportsCount)
                        .boxed().collect(java.util.stream.Collectors.toSet()));
        for (int count : List.of(4, 6, 8, 12, 16, 24, 32)) {
            var draw = groups.draw(tournament(), teams(count));
            int groupSize = count % 4 == 0 ? 4 : 3;
            assertEquals(count * (groupSize - 1) / 2, draw.size());
            Map<Integer, Set<String>> playedPairs = new HashMap<>();
            Map<String, Set<Long>> playedInRound = new HashMap<>();
            for (var game : draw) {
                assertNotNull(game.getGroupNumber());
                assertTrue(game.getGroupNumber() > 0);
                assertTrue(game.getRoundNumber() >= 1 && game.getRoundNumber() <= 3);
                assertNotEquals(game.getHomeTeam().getId(), game.getAwayTeam().getId());
                var pair = Set.of(game.getHomeTeam().getId(), game.getAwayTeam().getId());
                var key = game.getGroupNumber() + "/" + game.getRoundNumber();
                Set<Long> roundPlayers = playedInRound.computeIfAbsent(key, unused -> new HashSet<>());
                assertTrue(roundPlayers.add(game.getHomeTeam().getId()), "home plays twice");
                assertTrue(roundPlayers.add(game.getAwayTeam().getId()), "away plays twice");
                assertTrue(playedPairs.computeIfAbsent(game.getGroupNumber(),
                        unused -> new HashSet<>()).add(pair.toString()));
            }
        }
    }

    @Test
    void groupsCannotProgressUntilAllGamesFinishThenBracketIsCreatedOnce() {
        var tournament = tournament();
        var fixtures = new ArrayList<>(groups.draw(tournament, teams(8)));
        when(matches.findByTournamentId(eq(11L), any(Pageable.class)))
                .thenAnswer(invocation -> new SliceImpl<>(List.copyOf(fixtures)));
        groups.advanceWhenGroupsComplete(tournament);
        verify(matches, never()).saveAll(any());

        fixtures.forEach(game -> {
            game.setStatus(TournamentMatchStatus.COMPLETED);
            // Deterministic sporting winners are not required to test that
            // the bracket contains exactly two teams from each group.
            game.setHomeScore(1);
            game.setAwayScore(0);
        });
        groups.advanceWhenGroupsComplete(tournament);
        @SuppressWarnings("unchecked")
        org.mockito.ArgumentCaptor<List<CommunityTournamentTeamMatch>> captor =
                org.mockito.ArgumentCaptor.forClass(List.class);
        verify(matches).saveAll(captor.capture());
        var firstRound = captor.getValue();
        assertEquals(2, firstRound.size());
        assertTrue(firstRound.stream().allMatch(g -> g.getGroupNumber() == null
                && g.getRoundNumber() == CommunityTournamentTeamGroupsService.FIRST_KNOCKOUT_ROUND));
        assertEquals(4, firstRound.stream()
                .flatMap(game -> java.util.stream.Stream.of(
                        game.getHomeTeam().getId(), game.getAwayTeam().getId()))
                .distinct().count());
        fixtures.addAll(firstRound);
        groups.advanceWhenGroupsComplete(tournament);
        verify(matches, times(1)).saveAll(any());
    }
}
