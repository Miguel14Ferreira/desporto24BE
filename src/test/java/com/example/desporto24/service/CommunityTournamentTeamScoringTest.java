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
    void standingsRejectOutsidersWithoutExposingTeams() {
        actor();
        assertThrows(AccessDeniedException.class,
                () -> standings.standings(7L, 11L, "owner"));
        verifyNoInteractions(tournaments, teams, matches);
    }
}
