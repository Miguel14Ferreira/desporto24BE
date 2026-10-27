package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunitySessionResultRequest;
import com.example.desporto24.model.dto.community.CreateCommunityRoundRequest;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunitySessionResultServiceTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository memberships;
    @Mock ProfileRepository profiles;
    @Mock SessionRepository sessions;
    @Mock SessionParticipantRepository participants;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityCompetitionService competition;
    @InjectMocks CommunitySessionResultService service;

    private final long communityId = 8L;
    private final long sessionId = 63L;

    private CommunitySessionResultRequest winnerA() {
        return new CommunitySessionResultRequest(CommunitySessionResultRequest.Outcome.A, List.of(
                new CommunitySessionResultRequest.Player("ana", CommunitySessionResultRequest.Team.A),
                new CommunitySessionResultRequest.Player("bea", CommunitySessionResultRequest.Team.B)));
    }

    private void eligible(CommunityRole role, boolean rankingsEnabled) {
        var community = Community.builder().id(communityId).rankingsEnabled(rankingsEnabled).build();
        var owner = new Profile(); owner.setId(90L);
        var member = CommunityMember.builder().role(role).build();
        when(communities.findById(communityId)).thenReturn(Optional.of(community));
        when(profiles.findUserByUsernameIgnoreCase("manager")).thenReturn(Optional.of(owner));
        when(memberships.findByCommunityIdAndProfileId(communityId, 90L)).thenReturn(Optional.of(member));
    }

    private void pastSession(SessionStatus status) {
        var session = new Session();
        session.setId(sessionId);
        session.setSport("Futebol");
        session.setStatus(status);
        session.setGameDate(Date.from(Instant.now().minusSeconds(86400)));
        when(sessions.lockForCommunityResult(communityId, sessionId)).thenReturn(Optional.of(session));
    }

    private void players() {
        var ana = new Profile(); ana.setUsername("ana");
        var bea = new Profile(); bea.setUsername("bea");
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(sessionId, ParticipantState.ACTIVE))
                .thenReturn(List.of(SessionParticipant.builder().profile(ana).build(),
                        SessionParticipant.builder().profile(bea).build()));
    }

    @Test
    void createsRankedRoundWithPlayerResultsAndUniqueSessionReference() {
        eligible(CommunityRole.ADMIN, true);
        pastSession(SessionStatus.COMPLETED);
        players();
        var response = new CommunityRoundResponse(25L,"Sessão #63", Instant.now(), List.of(),
                "Futebol", null, null, null);
        var round = new CommunityRound(); round.setId(25L);
        when(competition.createRound(eq(communityId), eq("manager"), any(CreateCommunityRoundRequest.class)))
                .thenReturn(response);
        when(rounds.findById(25L)).thenReturn(Optional.of(round));

        assertEquals(25L, service.record(communityId, sessionId, "manager", winnerA()).id());
        var captor = ArgumentCaptor.forClass(CreateCommunityRoundRequest.class);
        verify(competition).createRound(eq(communityId), eq("manager"), captor.capture());
        assertEquals(2, captor.getValue().entries().size());
        assertEquals("WIN", captor.getValue().entries().get(0).result().name());
        assertEquals("LOSS", captor.getValue().entries().get(1).result().name());
        assertEquals(0, captor.getValue().entries().get(0).goals());
        assertEquals(sessionId, round.getSourceSessionId());
        verify(rounds).saveAndFlush(round);
    }

    @Test
    void rejectsDisabledRankingsWithoutTouchingSessionsOrStandings() {
        eligible(CommunityRole.ADMIN, false);
        assertThrows(ResponseStatusException.class,
                () -> service.record(communityId, sessionId, "manager", winnerA()));
        verifyNoInteractions(sessions, competition, rounds);
    }

    @Test
    void rejectsNonManagers() {
        eligible(CommunityRole.MEMBER, true);
        assertThrows(AccessDeniedException.class,
                () -> service.record(communityId, sessionId, "manager", winnerA()));
        verifyNoInteractions(sessions, competition);
    }

    @Test
    void refusesCancelledSessions() {
        eligible(CommunityRole.ADMIN, true);
        pastSession(SessionStatus.CANCELLED);
        assertThrows(ResponseStatusException.class,
                () -> service.record(communityId, sessionId, "manager", winnerA()));
        verifyNoInteractions(competition);
    }

    @Test
    void refusesDuplicateSessionBeforeCreatingAnyRound() {
        eligible(CommunityRole.ADMIN, true);
        pastSession(SessionStatus.COMPLETED);
        when(rounds.existsByCommunityIdAndSourceSessionId(communityId, sessionId)).thenReturn(true);
        assertThrows(ResponseStatusException.class,
                () -> service.record(communityId, sessionId, "manager", winnerA()));
        verifyNoInteractions(competition);
    }

    @Test
    void refusesInventedPlayersAndUnbalancedTeams() {
        eligible(CommunityRole.ADMIN, true);
        pastSession(SessionStatus.COMPLETED);
        players();
        assertThrows(IllegalArgumentException.class, () -> service.record(
                communityId, sessionId, "manager",
                new CommunitySessionResultRequest(CommunitySessionResultRequest.Outcome.A, List.of(
                    new CommunitySessionResultRequest.Player("ana", CommunitySessionResultRequest.Team.A),
                    new CommunitySessionResultRequest.Player("impostor", CommunitySessionResultRequest.Team.B)))));
        assertThrows(IllegalArgumentException.class, () -> service.record(
                communityId, sessionId, "manager",
                new CommunitySessionResultRequest(CommunitySessionResultRequest.Outcome.DRAW, List.of(
                    new CommunitySessionResultRequest.Player("ana", CommunitySessionResultRequest.Team.A),
                    new CommunitySessionResultRequest.Player("bea", CommunitySessionResultRequest.Team.A)))));
        verifyNoInteractions(competition);
    }
}
