package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentResultServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentMatchRepository matches;
    @InjectMocks CommunityTournamentResultService service;

    private final Profile owner = profile(1L, "owner");
    private final Profile visitor = profile(2L, "visitor");
    private final Community community = Community.builder()
            .id(7L).name("Liga").owner(owner).build();
    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(9L).name("Torneio").community(community)
            .status(TournamentStatus.IN_PROGRESS).build();
    private final CommunityTournamentMatch match = CommunityTournamentMatch.builder()
            .id(21L).tournament(tournament).roundNumber(1)
            .homeParticipant(participant(11L, owner))
            .awayParticipant(participant(12L, visitor))
            .status(TournamentMatchStatus.SCHEDULED).build();

    private static Profile profile(long id, String username) {
        var p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private CommunityTournamentParticipant participant(long id, Profile profile) {
        return CommunityTournamentParticipant.builder()
                .id(id).tournament(tournament).profile(profile).joinedAt(Instant.now()).build();
    }

    private void manager(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(owner).role(role).build()));
    }

    private void tournamentLocked() {
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
    }

    private void matchFound() {
        when(matches.findById(21L)).thenReturn(Optional.of(match));
    }

    private void saving() {
        when(matches.save(any(CommunityTournamentMatch.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void organizerRecordsWinAndFirstCompletionTime() {
        manager(CommunityRole.OWNER);
        tournamentLocked();
        matchFound();
        saving();

        var result = service.recordResult(7L, 9L, 21L, "owner",
                new RecordCommunityTournamentMatchResultRequest(2, 1));

        assertEquals(TournamentMatchStatus.COMPLETED, result.status());
        assertEquals(2, result.homeScore());
        assertEquals(1, result.awayScore());
        assertEquals("owner", result.homeUsername());
        assertEquals("visitor", result.awayUsername());
        assertNotNull(match.getCompletedAt());
        verify(tournaments).findByIdForUpdate(9L);
        verify(matches, times(1)).save(match);
    }

    @Test
    void adminRecordsNilNilDraw() {
        manager(CommunityRole.ADMIN);
        tournamentLocked();
        matchFound();
        saving();
        var result = service.recordResult(7L, 9L, 21L, "owner",
                new RecordCommunityTournamentMatchResultRequest(0, 0));
        assertEquals(0, result.homeScore());
        assertEquals(0, result.awayScore());
        assertEquals(TournamentMatchStatus.COMPLETED, match.getStatus());
    }

    @Test
    void newChessLeagueAcceptsOnlyWinLossOrDrawOutcomes() {
        tournament.setScoringMode(
                com.example.desporto24.enumeration.TournamentScoringMode.CHESS_HALF_POINTS);
        manager(CommunityRole.OWNER);
        tournamentLocked();
        for (var invalid : java.util.List.of(
                new RecordCommunityTournamentMatchResultRequest(1, 1),
                new RecordCommunityTournamentMatchResultRequest(2, 0),
                new RecordCommunityTournamentMatchResultRequest(2, 1))) {
            assertEquals(400, assertThrows(ResponseStatusException.class, () ->
                    service.recordResult(7L, 9L, 21L, "owner", invalid))
                    .getStatusCode().value());
        }
        verifyNoInteractions(matches);
        matchFound();
        saving();
        var draw = service.recordResult(7L, 9L, 21L, "owner",
                new RecordCommunityTournamentMatchResultRequest(0, 0));
        assertEquals(0, draw.homeScore());
        assertEquals(0, draw.awayScore());
        assertEquals(TournamentMatchStatus.COMPLETED, draw.status());
    }

    @Test
    void correctionPreservesInitialCompletionTime() {
        manager(CommunityRole.ADMIN);
        tournamentLocked();
        matchFound();
        match.setStatus(TournamentMatchStatus.COMPLETED);
        match.setHomeScore(1);
        match.setAwayScore(1);
        Instant previous = Instant.parse("2026-10-15T10:00:00Z");
        match.setCompletedAt(previous);
        saving();

        var result = service.recordResult(7L, 9L, 21L, "owner",
                new RecordCommunityTournamentMatchResultRequest(3, 2));

        assertEquals(3, result.homeScore());
        assertEquals(2, result.awayScore());
        assertEquals(previous, match.getCompletedAt());
        verify(matches).save(match);
    }

    @Test
    void repeatingSameResultIsIdempotent() {
        manager(CommunityRole.OWNER);
        tournamentLocked();
        matchFound();
        match.setStatus(TournamentMatchStatus.COMPLETED);
        match.setHomeScore(2);
        match.setAwayScore(1);
        Instant previous = Instant.parse("2026-10-14T08:00:00Z");
        match.setCompletedAt(previous);

        var result = service.recordResult(7L, 9L, 21L, "owner",
                new RecordCommunityTournamentMatchResultRequest(2, 1));

        assertEquals(TournamentMatchStatus.COMPLETED, result.status());
        assertEquals(previous, match.getCompletedAt());
        verify(matches, never()).save(any());
    }

    @Test
    void ordinaryMemberCannotEnterResult() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)));
        verifyNoInteractions(tournaments, matches);
    }

    @Test
    void outsiderCannotEnterResult() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        assertThrows(AccessDeniedException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)));
        verifyNoInteractions(tournaments, matches);
    }

    @Test
    void draftCannotReceiveResult() {
        manager(CommunityRole.OWNER);
        tournamentLocked();
        tournament.setStatus(TournamentStatus.DRAFT);
        assertEquals(409, assertThrows(ResponseStatusException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)))
                .getStatusCode().value());
        verifyNoInteractions(matches);
    }

    @Test
    void completedTournamentCannotHaveScoresChanged() {
        manager(CommunityRole.ADMIN);
        tournamentLocked();
        tournament.setStatus(TournamentStatus.COMPLETED);
        assertEquals(409, assertThrows(ResponseStatusException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)))
                .getStatusCode().value());
        verifyNoInteractions(matches);
    }

    @Test
    void matchBelongingToAnotherTournamentIsInvisible() {
        manager(CommunityRole.OWNER);
        tournamentLocked();
        matchFound();
        match.setTournament(CommunityTournament.builder().id(999L).build());
        assertEquals(404, assertThrows(ResponseStatusException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)))
                .getStatusCode().value());
        verify(matches, never()).save(any());
    }

    @Test
    void otherCommunityTournamentIsInvisibleToManager() {
        manager(CommunityRole.ADMIN);
        tournamentLocked();
        tournament.setCommunity(Community.builder().id(998L).build());
        assertEquals(404, assertThrows(ResponseStatusException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)))
                .getStatusCode().value());
        verifyNoInteractions(matches);
    }

    @Test
    void missingMatchReturns404() {
        manager(CommunityRole.OWNER);
        tournamentLocked();
        assertEquals(404, assertThrows(ResponseStatusException.class, () ->
                service.recordResult(7L, 9L, 21L, "owner",
                        new RecordCommunityTournamentMatchResultRequest(1, 0)))
                .getStatusCode().value());
        verify(matches, never()).save(any());
    }

    @Test
    void invalidScoreIsRejectedBeforeAnyDatabaseCall() {
        for (var score : new Integer[]{-1, 1000, null}) {
            assertEquals(400, assertThrows(ResponseStatusException.class, () ->
                    service.recordResult(7L, 9L, 21L, "owner",
                            new RecordCommunityTournamentMatchResultRequest(score, 0)))
                    .getStatusCode().value());
            assertEquals(400, assertThrows(ResponseStatusException.class, () ->
                    service.recordResult(7L, 9L, 21L, "owner",
                            new RecordCommunityTournamentMatchResultRequest(0, score)))
                    .getStatusCode().value());
        }
        verifyNoInteractions(profiles, members, tournaments, matches);
    }
}
