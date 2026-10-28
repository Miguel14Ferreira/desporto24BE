package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.session.SessionCommunityShare;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.interfaces.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionPostGameCommunityControllerTest {
    private final SessionRepository sessions=mock(SessionRepository.class);
    private final SessionParticipantRepository participants=mock(SessionParticipantRepository.class);
    private final SessionCommunityShareRepository shares=mock(SessionCommunityShareRepository.class);
    private final ProfileRepository profiles=mock(ProfileRepository.class);
    private final CommunityMemberRepository members=mock(CommunityMemberRepository.class);
    private final CommunityJoinRequestRepository requests=mock(CommunityJoinRequestRepository.class);
    private final CommunityManagementService management=mock(CommunityManagementService.class);
    private final SessionService access=mock(SessionService.class);
    private final Authentication authentication=mock(Authentication.class);
    private final SessionPostGameCommunityController controller=
            new SessionPostGameCommunityController(sessions,participants,shares,profiles,
                    members,requests,management,access);

    private final Session session=new Session();
    private final Profile profile=new Profile();
    private final SessionParticipant attendance=new SessionParticipant();
    private final Community community=new Community();

    @BeforeEach void setup() {
        profile.setId(15L);profile.setUsername("jogador");
        community.setId(22L);community.setName("Futsal da Parede");
        community.setVisibility(CommunityVisibility.PRIVATE);
        session.setId(31L);session.setCommunity(community);
        session.setStatus(SessionStatus.COMPLETED);
        session.setGameDate(Date.from(Instant.parse("2026-10-01T19:00:00Z")));
        attendance.setSession(session);attendance.setProfile(profile);
        attendance.setState(ParticipantState.ACTIVE);
        attendance.setAttendanceStatus(AttendanceStatus.ATTENDED);
        attendance.setAttendanceRecordedAt(Instant.parse("2026-10-01T21:00:00Z"));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("jogador");
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(profile));
        when(sessions.findById(31L)).thenReturn(Optional.of(session));
        when(participants.findBySessionIdAndProfileIdAndState(31L,15L,ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attendance));
    }

    @Test void attendedPlayerCanChooseToRequestPrivateCommunityMembership() {
        var result=controller.suggestions(31L,authentication);
        assertEquals(1,result.size());
        assertEquals("Futsal da Parede",result.getFirst().name());
        assertTrue(result.getFirst().requiresApproval());
        assertEquals("REQUESTED",controller.join(31L,22L,authentication).state());
        verify(management).requestJoin(22L,"jogador");
        verify(management,never()).joinPublic(anyLong(),anyString());
        verify(access,atLeastOnce()).getById(31L,"jogador");
    }

    @Test void publicCommunityCanBeJoinedWithoutAdministratorApproval() {
        community.setVisibility(CommunityVisibility.PUBLIC);
        assertFalse(controller.suggestions(31L,authentication).getFirst().requiresApproval());
        assertEquals("JOINED",controller.join(31L,22L,authentication).state());
        verify(management).joinPublic(22L,"jogador");
    }

    @Test void crosspostedCommunitiesCanBeChosenIndividually() {
        Community other=new Community();other.setId(25L);other.setName("Racket Friends");
        other.setVisibility(CommunityVisibility.PUBLIC);
        when(shares.findBySession_IdOrderByIdAsc(31L))
                .thenReturn(List.of(new SessionCommunityShare(session,community),
                        new SessionCommunityShare(session,other)));
        var result=controller.suggestions(31L,authentication);
        assertEquals(List.of(22L,25L),result.stream().map(
                SessionPostGameCommunityController.Suggestion::id).toList());
        assertEquals("JOINED",controller.join(31L,25L,authentication).state());
        verify(management).joinPublic(25L,"jogador");
    }

    @Test void pendingAndRejectedRequestsNeverRepeatTheSuggestion() {
        when(requests.findByCommunityIdAndRequesterIdAndStatus(22L,15L,CommunityInviteStatus.PENDING))
                .thenReturn(Optional.of(new com.example.desporto24.model.community.CommunityJoinRequest()));
        assertTrue(controller.suggestions(31L,authentication).isEmpty());
        assertEquals(409,assertThrows(ResponseStatusException.class,
                ()->controller.join(31L,22L,authentication)).getStatusCode().value());
        verifyNoInteractions(management);
    }

    @Test void notYetCompletedOrNoShowOrUnverifiedAttendanceCannotJoin() {
        session.setStatus(SessionStatus.OPEN);
        assertEquals(403,assertThrows(ResponseStatusException.class,
                ()->controller.suggestions(31L,authentication)).getStatusCode().value());
        session.setStatus(SessionStatus.COMPLETED);
        attendance.setAttendanceStatus(AttendanceStatus.NO_SHOW);
        assertEquals(403,assertThrows(ResponseStatusException.class,
                ()->controller.join(31L,22L,authentication)).getStatusCode().value());
        attendance.setAttendanceStatus(AttendanceStatus.ATTENDED);
        attendance.setAttendanceRecordedAt(null);
        assertEquals(403,assertThrows(ResponseStatusException.class,
                ()->controller.join(31L,22L,authentication)).getStatusCode().value());
        verifyNoInteractions(management);
    }

    @Test void memberOrUnrelatedCommunityOrAnonymousNeverReceivesAnOffer() {
        when(members.existsByCommunityIdAndProfileId(22L,15L)).thenReturn(true);
        assertTrue(controller.suggestions(31L,authentication).isEmpty());
        assertEquals(409,assertThrows(ResponseStatusException.class,
                ()->controller.join(31L,22L,authentication)).getStatusCode().value());
        assertEquals(404,assertThrows(ResponseStatusException.class,
                ()->controller.join(31L,999L,authentication)).getStatusCode().value());
        assertEquals(401,assertThrows(ResponseStatusException.class,
                ()->controller.suggestions(31L,null)).getStatusCode().value());
    }

    @Test void sessionAccessCheckPreventsCommunityInformationLeaking() {
        when(access.getById(31L,"jogador")).thenThrow(
                new org.springframework.security.access.AccessDeniedException("unauthorized"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                ()->controller.suggestions(31L,authentication));
        verifyNoInteractions(shares,management);
    }
}
