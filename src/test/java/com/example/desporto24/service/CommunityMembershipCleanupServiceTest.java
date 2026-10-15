package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityMembershipCleanupServiceTest {

    @Test
    void removesFutureCommunityParticipationAndWaitlistThenOffersPlaceToNextPlayer() {
        SessionRepository sessions = mock(SessionRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        SessionWaitlistRepository waitlists = mock(SessionWaitlistRepository.class);
        WaitlistPromotionService promotions = mock(WaitlistPromotionService.class);
        Session session = new Session();
        session.setId(42L);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        session.setStatus(SessionStatus.FULL);
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        SessionParticipant participant = new SessionParticipant();
        participant.setState(ParticipantState.ACTIVE);
        participant.setTeamCode("A");
        participant.setAttendanceStatus(AttendanceStatus.PENDING);
        SessionParticipant teammate = new SessionParticipant();
        teammate.setState(ParticipantState.ACTIVE);
        teammate.setTeamCode("B");
        SessionWaitlist queued = new SessionWaitlist();
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L)).thenReturn(List.of(session));
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(session));
        when(participants.findBySessionIdAndProfileIdAndState(
                42L, 7L, ParticipantState.ACTIVE)).thenReturn(Optional.of(participant));
        when(waitlists.findBySessionIdAndProfileId(42L, 7L)).thenReturn(Optional.of(queued));
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE)).thenReturn(List.of(teammate));

        new CommunityMembershipCleanupService(sessions, participants, waitlists, promotions)
                .onMembershipEnded(new CommunityMembershipEndedEvent(10L, 7L));

        assertEquals(ParticipantState.REMOVED, participant.getState());
        assertNotNull(participant.getExitedAt());
        verify(waitlists).delete(queued);
        verify(participants).save(participant);
        verify(promotions).promoteNext(eq(session), any(Instant.class));
        assertNull(participant.getTeamCode());
        assertNull(teammate.getTeamCode());
        verify(participants).saveAll(List.of(teammate));
    }

    @Test
    void leavesPastAndPublicSessionsUntouched() {
        SessionRepository sessions = mock(SessionRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        SessionWaitlistRepository waitlists = mock(SessionWaitlistRepository.class);
        WaitlistPromotionService promotions = mock(WaitlistPromotionService.class);
        Session past = new Session();
        past.setId(1L);
        past.setAudience(SessionAudience.COMMUNITY_ONLY);
        past.setStatus(SessionStatus.COMPLETED);
        past.setGameDate(Date.from(Instant.now().minusSeconds(3600)));
        Session openedToPublic = new Session();
        openedToPublic.setId(2L);
        openedToPublic.setAudience(SessionAudience.PUBLIC);
        openedToPublic.setStatus(SessionStatus.OPEN);
        openedToPublic.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L))
                .thenReturn(List.of(past, openedToPublic));

        new CommunityMembershipCleanupService(sessions, participants, waitlists, promotions)
                .onMembershipEnded(new CommunityMembershipEndedEvent(10L, 7L));

        verify(sessions, never()).findByIdForUpdate(anyLong());
        verifyNoInteractions(participants, waitlists, promotions);
    }
}
