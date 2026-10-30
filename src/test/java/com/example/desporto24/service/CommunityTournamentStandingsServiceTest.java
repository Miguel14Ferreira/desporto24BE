package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentStandingsServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentParticipantRepository participants;
    @Mock CommunityTournamentMatchRepository matches;
    @InjectMocks CommunityTournamentStandingsService service;

    private final Profile viewer = profile(10L, "viewer");
    private final Profile ana = profile(1L, "ana");
    private final Profile bruno = profile(2L, "bruno");
    private final Profile carla = profile(3L, "carla");
    private final Profile diana = profile(4L, "diana");
    private final Community community = Community.builder().id(7L)
            .name("CNG").visibility(CommunityVisibility.PRIVATE).owner(viewer).build();
    private final CommunityTournament tournament = CommunityTournament.builder().id(9L)
            .community(community).name("Liga").status(TournamentStatus.IN_PROGRESS).build();

    private static Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private CommunityTournamentParticipant registered(Profile profile) {
        return CommunityTournamentParticipant.builder()
                .id(profile.getId()).tournament(tournament).profile(profile)
                .status(TournamentParticipantStatus.CONFIRMED).build();
    }

    private CommunityTournamentMatch result(Long id, Profile home, Profile away, int scored, int conceded) {
        return CommunityTournamentMatch.builder().id(id).tournament(tournament)
                .homeParticipant(registered(home)).awayParticipant(registered(away))
                .status(TournamentMatchStatus.COMPLETED)
                .homeScore(scored).awayScore(conceded).build();
    }

    private void memberAndTournament() {
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(members.existsByCommunityIdAndProfileId(7L, 10L)).thenReturn(true);
        when(tournaments.findById(9L)).thenReturn(Optional.of(tournament));
    }

    private void table(List<Profile> players, List<CommunityTournamentMatch> completed) {
        var playerPage = PageRequest.of(0, 33, Sort.by("id"));
        var matchPage = PageRequest.of(0, 497, Sort.by("id"));
        when(participants.findByTournamentIdAndStatus(
                9L, TournamentParticipantStatus.CONFIRMED, playerPage))
                .thenReturn(new SliceImpl<>(
                        players.stream().map(this::registered).toList(), playerPage, false));
        when(matches.findByTournamentIdAndStatus(
                9L, TournamentMatchStatus.COMPLETED, matchPage))
                .thenReturn(new SliceImpl<>(completed, matchPage, false));
    }

    @Test
    void allConfirmedPlayersAppearEvenBeforeFirstResult() {
        memberAndTournament();
        table(List.of(bruno, ana, carla), List.of());
        when(matches.countByTournamentId(9L)).thenReturn(3L);

        var result = service.standings(7L, 9L, "viewer");

        assertEquals(3, result.standings().size());
        assertEquals(List.of("ana", "bruno", "carla"),
                result.standings().stream().map(r -> r.username()).toList());
        assertEquals(List.of(1, 2, 3),
                result.standings().stream().map(r -> r.position()).toList());
        assertTrue(result.standings().stream().allMatch(r ->
                r.played() == 0 && r.points() == 0L));
        assertEquals(3L, result.totalMatches());
        assertEquals(0, result.completedMatches());
    }

    @Test
    void winsDrawsLossesGoalsAndPointsAreCalculatedFromCompletedMatches() {
        memberAndTournament();
        table(List.of(ana, bruno, carla), List.of(
                result(1L, ana, bruno, 2, 1),
                result(2L, ana, carla, 0, 0),
                result(3L, bruno, carla, 3, 0)
        ));
        when(matches.countByTournamentId(9L)).thenReturn(3L);

        var result = service.standings(7L, 9L, "viewer");

        assertEquals(3, result.completedMatches());
        var a = result.standings().get(0);
        assertEquals("ana", a.username());
        assertEquals(4L, a.points());
        assertEquals(2, a.played());
        assertEquals(1, a.won());
        assertEquals(1, a.drawn());
        assertEquals(0, a.lost());
        assertEquals(2, a.goalsFor());
        assertEquals(1, a.goalsAgainst());
        assertEquals(1, a.goalDifference());
        var b = result.standings().get(1);
        assertEquals("bruno", b.username());
        assertEquals(3L, b.points());
        assertEquals(4, b.goalsFor());
        assertEquals(2, b.goalsAgainst());
        assertEquals(-3, result.standings().get(2).goalDifference());
    }

    @Test
    void chessModeUsesHalfPointIntegerStorageAndWinsBeforeGoals() {
        memberAndTournament();
        tournament.setScoringMode(
                com.example.desporto24.enumeration.TournamentScoringMode.CHESS_HALF_POINTS);
        tournament.setWinPoints(2);
        tournament.setDrawPoints(1);
        tournament.setLossPoints(0);
        // ana: one win and one loss => 2 half-points = 1 point.
        // bruno: two draws => 2 half-points = 1 point.
        // Wins break the tie without inventing a chess 'goal difference'.
        table(List.of(ana, bruno, carla), List.of(
                result(1L, ana, carla, 1, 0),
                result(2L, bruno, carla, 0, 0),
                result(3L, bruno, ana, 0, 0)
        ));
        var table = service.standings(7L, 9L, "viewer");
        assertEquals("ana", table.standings().get(0).username());
        assertEquals(3L, table.standings().get(0).points()); // 1 win + 1 draw
        assertEquals("bruno", table.standings().get(1).username());
        assertEquals(2L, table.standings().get(1).points()); // two draws
    }

    @Test
    void customTournamentPointConfigurationIsRespected() {
        memberAndTournament();
        tournament.setWinPoints(5);
        tournament.setDrawPoints(2);
        tournament.setLossPoints(1);
        table(List.of(ana, bruno, carla), List.of(
                result(1L, ana, bruno, 2, 1),
                result(2L, ana, carla, 0, 0)
        ));

        var table = service.standings(7L, 9L, "viewer");
        assertEquals(7L, table.standings().get(0).points());
        assertEquals(1L, table.standings().stream()
                .filter(r -> r.username().equals("bruno")).findFirst().orElseThrow().points());
        assertEquals(2L, table.standings().stream()
                .filter(r -> r.username().equals("carla")).findFirst().orElseThrow().points());
        assertEquals(5, table.winPoints());
        assertEquals(2, table.drawPoints());
        assertEquals(1, table.lossPoints());
    }

    @Test
    void tiesUseGoalDifferenceThenGoalsForInsteadOfSignupOrder() {
        memberAndTournament();
        table(List.of(diana, carla, bruno, ana), List.of(
                result(1L, ana, bruno, 3, 1),
                result(2L, carla, diana, 2, 0)
        ));

        var result = service.standings(7L, 9L, "viewer");

        assertEquals(List.of("ana", "carla", "bruno", "diana"),
                result.standings().stream().map(r -> r.username()).toList());
    }

    @Test
    void pointTotalsUseLongToAvoidOverflowForConfiguredScoring() {
        memberAndTournament();
        tournament.setWinPoints(2_000_000_000);
        table(List.of(ana, bruno, carla), List.of(
                result(1L, ana, bruno, 1, 0),
                result(2L, ana, carla, 1, 0)
        ));

        var result = service.standings(7L, 9L, "viewer");

        assertEquals(4_000_000_000L, result.standings().get(0).points());
    }

    @Test
    void standingsAlwaysReflectTheLatestPersistedScoresWithoutCaching() {
        memberAndTournament();
        var playerPage = PageRequest.of(0, 33, Sort.by("id"));
        var matchPage = PageRequest.of(0, 497, Sort.by("id"));
        when(participants.findByTournamentIdAndStatus(
                9L, TournamentParticipantStatus.CONFIRMED, playerPage))
                .thenReturn(new SliceImpl<>(
                        List.of(registered(ana), registered(bruno)), playerPage, false));
        when(matches.findByTournamentIdAndStatus(
                9L, TournamentMatchStatus.COMPLETED, matchPage))
                .thenReturn(
                        new SliceImpl<>(List.of(result(1L, ana, bruno, 1, 0)), matchPage, false),
                        new SliceImpl<>(List.of(result(1L, ana, bruno, 0, 1)), matchPage, false));

        assertEquals("ana", service.standings(7L, 9L, "viewer").standings().get(0).username());
        assertEquals("bruno", service.standings(7L, 9L, "viewer").standings().get(0).username());
        verify(matches, times(2)).findByTournamentIdAndStatus(
                9L, TournamentMatchStatus.COMPLETED, matchPage);
    }

    @Test
    void outsiderCannotReadPrivateRosterOrStandings() {
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));

        assertThrows(AccessDeniedException.class, () -> service.standings(7L, 9L, "viewer"));

        verifyNoInteractions(tournaments, participants, matches);
    }

    @Test
    void cannotReadAnotherCommunitiesTournament() {
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(members.existsByCommunityIdAndProfileId(7L, 10L)).thenReturn(true);
        tournament.setCommunity(Community.builder().id(99L).build());
        when(tournaments.findById(9L)).thenReturn(Optional.of(tournament));

        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.standings(7L, 9L, "viewer")).getStatusCode().value());
        verifyNoInteractions(participants, matches);
    }

    @Test
    void moreThanThirtyTwoConfirmedPlayersCannotBeScored() {
        memberAndTournament();
        var page = PageRequest.of(0, 33, Sort.by("id"));
        var many = java.util.stream.LongStream.rangeClosed(1, 33)
                .mapToObj(id -> registered(profile(id, "player-" + id))).toList();
        when(participants.findByTournamentIdAndStatus(
                9L, TournamentParticipantStatus.CONFIRMED, page))
                .thenReturn(new SliceImpl<>(many, page, false));

        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.standings(7L, 9L, "viewer")).getStatusCode().value());
        verifyNoInteractions(matches);
    }

    @Test
    void inconsistentCompletedMatchIsNotSilentlyIncluded() {
        memberAndTournament();
        var incomplete = result(1L, ana, bruno, 3, 2);
        incomplete.setHomeScore(null);
        table(List.of(ana, bruno), List.of(incomplete));

        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.standings(7L, 9L, "viewer")).getStatusCode().value());
        verify(matches, never()).countByTournamentId(any());
    }
}
