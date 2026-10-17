package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamScoringTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentTeamMatchRepository matches;
    @Mock CommunityTournamentTeamRepository teams;
    @Mock CommunityTournamentTeamKnockoutService knockout;
    @InjectMocks CommunityTournamentTeamResultService scoring;
    @InjectMocks CommunityTournamentTeamStandingsService standings;

    private Profile actor;
    private CommunityTournament competition;
    private CommunityTournamentTeam blue;
    private CommunityTournamentTeam green;
    private CommunityTournamentTeam red;
    private CommunityTournamentTeamMatch fixture;

    @BeforeEach
    void setup() {
        actor = new Profile();
        actor.setId(1L);
        actor.setUsername("owner");
        competition = CommunityTournament.builder().id(11L)
                .community(Community.builder().id(7L).build())
                .entryType(TournamentEntryType.TEAM).status(TournamentStatus.IN_PROGRESS)
                .winPoints(3).drawPoints(1).lossPoints(0).build();
        blue = CommunityTournamentTeam.builder().id(20L).name("Azuis").build();
        green = CommunityTournamentTeam.builder().id(21L).name("Verdes").build();
        red = CommunityTournamentTeam.builder().id(22L).name("Vermelhos").build();
        fixture = game(50L, blue, green, null, null);
    }

    private CommunityTournamentTeamMatch game(Long id, CommunityTournamentTeam home,
            CommunityTournamentTeam away, Integer scoreHome, Integer scoreAway) {
        return CommunityTournamentTeamMatch.builder().id(id).tournament(competition)
                .roundNumber(1).homeTeam(home).awayTeam(away)
                .status(scoreHome == null ? TournamentMatchStatus.SCHEDULED : TournamentMatchStatus.COMPLETED)
                .homeScore(scoreHome).awayScore(scoreAway).build();
    }

    private void actor() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
    }

    private void owner() {
        actor();
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.OWNER).build()));
    }

    private void tournamentForWrite() {
        when(tournaments.findByIdForUpdate(11L)).thenReturn(Optional.of(competition));
    }

    @Test
    void resultCreatesAndCorrectsScoreWithoutChangingCompletionTime() {
        owner();
        tournamentForWrite();
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        when(matches.save(any())).thenAnswer(i -> i.getArgument(0));
        var first = scoring.record(7L, 11L, 50L, "owner",
                new RecordCommunityTournamentMatchResultRequest(2, 0));
        assertEquals(TournamentMatchStatus.COMPLETED, first.status());
        assertEquals("Verdes", first.awayTeamName());
        assertEquals(2, first.homeScore());
        assertNotNull(fixture.getCompletedAt());
        var completion = fixture.getCompletedAt();
        var correction = scoring.record(7L, 11L, 50L, "owner",
                new RecordCommunityTournamentMatchResultRequest(1, 1));
        assertEquals(1, correction.awayScore());
        assertEquals(completion, fixture.getCompletedAt());
        verify(matches, times(2)).save(fixture);
    }

    @Test
    void duplicateScoreIsIdempotent() {
        owner();
        tournamentForWrite();
        fixture.setStatus(TournamentMatchStatus.COMPLETED);
        fixture.setHomeScore(3);
        fixture.setAwayScore(2);
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        var result = scoring.record(7L, 11L, 50L, "owner",
                new RecordCommunityTournamentMatchResultRequest(3, 2));
        assertEquals(3, result.homeScore());
        verify(matches, never()).save(any());
    }

    @Test
    void invalidScoresRejectedBeforeDatabaseAccess() {
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(-1, 0))).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1000, 0))).getStatusCode());
        verifyNoInteractions(tournaments, matches);
    }

    @Test
    void memberCannotWriteScore() {
        actor();
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        assertThrows(AccessDeniedException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)));
        verifyNoInteractions(tournaments, matches);
    }

    @Test
    void foreignCommunityFixtureAndDraftTournamentAreRejected() {
        owner();
        tournamentForWrite();
        competition.setCommunity(Community.builder().id(8L).build());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(2, 0))).getStatusCode());
        competition.setCommunity(Community.builder().id(7L).build());
        competition.setStatus(TournamentStatus.DRAFT);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(2, 0))).getStatusCode());
        verifyNoInteractions(matches);
    }

    @Test
    void fixtureFromAnotherTournamentIsNeverScored() {
        owner();
        tournamentForWrite();
        fixture.setTournament(CommunityTournament.builder().id(99L).build());
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0))).getStatusCode());
        verify(matches, never()).save(any());
    }

    @Test
    void finalizedTournamentRejectsScoreCorrections() {
        owner();
        tournamentForWrite();
        competition.setStatus(TournamentStatus.COMPLETED);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0))).getStatusCode());
        verifyNoInteractions(matches);
    }

    @Test
    void knockoutRejectsDrawAndDoesNotSaveOrAdvance() {
        competition.setFormat(TournamentFormat.KNOCKOUT);
        owner();
        tournamentForWrite();
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        var failure = assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(2, 2)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, failure.getStatusCode());
        verify(matches, never()).saveAndFlush(any());
        verifyNoInteractions(knockout);
    }

    @Test
    void knockoutScoreIsFlushedAndBracketAdvancesUnderTournamentLock() {
        competition.setFormat(TournamentFormat.KNOCKOUT);
        owner();
        tournamentForWrite();
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        when(matches.findByTournamentId(eq(11L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(List.of(fixture), PageRequest.of(0, 33), false));
        var saved = scoring.record(7L, 11L, 50L, "owner",
                new RecordCommunityTournamentMatchResultRequest(3, 1));
        assertEquals(3, saved.homeScore());
        verify(matches).saveAndFlush(fixture);
        verify(knockout).advanceAfterResult(competition);
        verify(matches, never()).save(fixture);
    }

    @Test
    void knockoutPreviousRoundCannotBeCorrectedAfterNextRoundStarts() {
        competition.setFormat(TournamentFormat.KNOCKOUT);
        owner();
        tournamentForWrite();
        fixture.setStatus(TournamentMatchStatus.COMPLETED);
        fixture.setHomeScore(3);
        fixture.setAwayScore(1);
        when(matches.findById(50L)).thenReturn(Optional.of(fixture));
        var finalRound = game(70L, blue, red, null, null);
        finalRound.setRoundNumber(2);
        when(matches.findByTournamentId(eq(11L), any(PageRequest.class)))
                .thenReturn(new SliceImpl<>(List.of(finalRound), PageRequest.of(0, 33), false));
        var error = assertThrows(ResponseStatusException.class,
                () -> scoring.record(7L, 11L, 50L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(matches, never()).saveAndFlush(any());
        verifyNoInteractions(knockout);
    }

    @Test
    void standingsOnlyCountCompletedFixturesAndRespectPointsAndTieBreakers() {
        actor();
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(11L)).thenReturn(Optional.of(competition));
        when(teams.findByTournamentIdOrderByIdAsc(11L)).thenReturn(List.of(blue, green, red));
        when(matches.findByTournamentIdAndStatus(eq(11L), eq(TournamentMatchStatus.COMPLETED),
                any(PageRequest.class))).thenReturn(new SliceImpl<>(List.of(
                        game(51L, blue, green, 2, 0),
                        game(52L, green, red, 1, 1)), PageRequest.of(0, 497), false));
        when(matches.countByTournamentId(11L)).thenReturn(3L);
        var table = standings.standings(7L, 11L, "owner");
        assertEquals(3L, table.totalMatches());
        assertEquals(2, table.completedMatches());
        assertEquals("Azuis", table.standings().get(0).teamName());
        assertEquals(3L, table.standings().get(0).points());
        assertEquals("Vermelhos", table.standings().get(1).teamName());
        assertEquals(1L, table.standings().get(1).points());
        assertEquals("Verdes", table.standings().get(2).teamName());
        assertEquals(2, table.standings().get(2).played());
        assertEquals(1, table.standings().get(2).lost());
    }

    @Test
    void teamPriorityChangesOrderWhenPointsEqualWithoutChangingResults() {
        actor();
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(11L)).thenReturn(Optional.of(competition));
        when(teams.findByTournamentIdOrderByIdAsc(11L)).thenReturn(List.of(blue, green, red));
        // All teams win once; blue has better goal difference, green scores more.
        var fixtures = List.of(game(51L, blue, green, 2, 0),
                game(52L, red, blue, 1, 0),
                game(53L, green, red, 6, 4));
        when(matches.findByTournamentIdAndStatus(eq(11L), eq(TournamentMatchStatus.COMPLETED),
                any(PageRequest.class))).thenReturn(
                        new SliceImpl<>(fixtures, PageRequest.of(0, 993), false));
        when(matches.countByTournamentId(11L)).thenReturn(3L);

        assertEquals("Azuis", standings.standings(7L, 11L, "owner")
                .standings().get(0).teamName());

        competition.setTeamTieBreakPriority(TournamentTeamTieBreakPriority.GOALS_SCORED_FIRST);
        var byScored = standings.standings(7L, 11L, "owner");
        assertEquals("Verdes", byScored.standings().get(0).teamName());
        assertEquals(3, byScored.standings().get(0).points());
        // No result was mutated by selecting another ordering.
        verify(matches, never()).save(any());
    }

    @Test
    void winsFirstUsesActualWinsBeforeGoalDifference() {
        actor();
        var white = CommunityTournamentTeam.builder().id(23L).name("Brancos").build();
        competition.setWinPoints(2);
        competition.setDrawPoints(2);
        competition.setTeamTieBreakPriority(TournamentTeamTieBreakPriority.WINS_FIRST);
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(11L)).thenReturn(Optional.of(competition));
        when(teams.findByTournamentIdOrderByIdAsc(11L))
                .thenReturn(List.of(blue, green, red, white));
        when(matches.findByTournamentIdAndStatus(eq(11L), eq(TournamentMatchStatus.COMPLETED),
                any(PageRequest.class))).thenReturn(new SliceImpl<>(List.of(
                        game(51L, blue, green, 1, 0),
                        game(52L, blue, red, 1, 0),
                        game(53L, white, blue, 5, 0),
                        game(54L, green, red, 10, 0),
                        game(55L, green, white, 0, 0),
                        game(56L, red, white, 0, 0)
                ), PageRequest.of(0, 993), false));
        when(matches.countByTournamentId(11L)).thenReturn(6L);
        var table = standings.standings(7L, 11L, "owner");
        int bluePosition = table.standings().stream()
                .filter(row -> row.teamId().equals(blue.getId())).findFirst().orElseThrow().position();
        int greenPosition = table.standings().stream()
                .filter(row -> row.teamId().equals(green.getId())).findFirst().orElseThrow().position();
        assertTrue(bluePosition < greenPosition);
        competition.setTeamTieBreakPriority(TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST);
        var byDifference = standings.standings(7L, 11L, "owner");
        int defaultBlue = byDifference.standings().stream()
                .filter(row -> row.teamId().equals(blue.getId())).findFirst().orElseThrow().position();
        int defaultGreen = byDifference.standings().stream()
                .filter(row -> row.teamId().equals(green.getId())).findFirst().orElseThrow().position();
        assertTrue(defaultGreen < defaultBlue);
    }

    @Test
    void standingsRejectOutsidersWithoutExposingTeams() {
        actor();
        assertThrows(AccessDeniedException.class,
                () -> standings.standings(7L, 11L, "owner"));
        verifyNoInteractions(tournaments, teams, matches);
    }
}
