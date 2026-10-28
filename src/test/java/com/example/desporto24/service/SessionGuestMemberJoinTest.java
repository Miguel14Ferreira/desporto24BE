package com.example.desporto24.service;

import com.example.desporto24.enumeration.JoinState;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.repository.session.SessionOrganizerExclusionRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionGuestMemberJoinTest {
    @Mock private SessionRepository sessions;
    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository communityMembers;
    @Mock private SessionParticipantRepository participants;
    @Mock private SessionWaitlistRepository waitlist;
    @Mock private SessionOrganizerExclusionRepository exclusions;
    @Mock private SessionViewMapper mapper;
    @Mock private ReliabilityService reliabilityService;
    @Mock private PilotEventService pilotEvents;
    @InjectMocks private SessionServiceImpl service;

    private static final String TOKEN="a".repeat(43);

    private Session session() throws Exception {
        Profile creator=new Profile();
        creator.setId(1L);
        Session session=new Session();
        session.setId(77L);
        session.setProfile(creator);
        Community community=new Community();
        community.setId(88L);
        session.setCommunity(community);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        session.setStatus(SessionStatus.OPEN);
        session.setEnabled(true);
        session.setPrivateGame(false);
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        session.setMaxParticipants(3);
        session.setGuestLinkHash(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(TOKEN.getBytes(StandardCharsets.UTF_8))));
        return session;
    }

    @Test
    void signedCommunityInviteAdmitsAuthenticatedNonMemberAndUnlocksDetails() throws Exception {
        Session session=session();
        Profile user=new Profile();user.setId(44L);user.setUsername("guestAccount");
        when(sessions.findByIdForUpdate(77L)).thenReturn(Optional.of(session));
        when(profiles.findUserByUsername("guestAccount")).thenReturn(Optional.of(user));
        when(participants.countBySessionIdAndState(77L,ParticipantState.ACTIVE)).thenReturn(1L);
        when(participants.findBySessionIdAndProfileId(77L,44L)).thenReturn(Optional.empty());
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(77L,ParticipantState.ACTIVE))
                .thenReturn(List.of());
        assertEquals(JoinState.PARTICIPANT,service.joinWithGuestToken(77L,TOKEN,"guestAccount").state());
        verify(participants).save(argThat(p->p.getSession()==session
                && p.getProfile()==user && p.getState()==ParticipantState.ACTIVE));
        verify(communityMembers,never()).existsByCommunityIdAndProfileId(88L,44L);

        when(sessions.findById(77L)).thenReturn(Optional.of(session));
        when(communityMembers.existsByCommunityIdAndProfileId(88L,44L)).thenReturn(false);
        when(participants.existsBySessionIdAndProfileIdAndState(77L,44L,ParticipantState.ACTIVE))
                .thenReturn(true);
        SessionDetailResponse detail=mock(SessionDetailResponse.class);
        when(mapper.toDetail(session,user)).thenReturn(detail);
        assertSame(detail,service.getById(77L,"guestAccount"));
    }

    @Test
    void signedCrosspostedInviteLetsAccountHolderJoinWithoutMembership() throws Exception {
        Session session=session();
        session.setCommunity(null);
        Profile user=new Profile();user.setId(44L);user.setUsername("invited");
        when(sessions.findByIdForUpdate(77L)).thenReturn(Optional.of(session));
        when(profiles.findUserByUsername("invited")).thenReturn(Optional.of(user));
        when(participants.countBySessionIdAndState(77L,ParticipantState.ACTIVE)).thenReturn(1L);
        when(participants.findBySessionIdAndProfileId(77L,44L)).thenReturn(Optional.empty());
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(77L,ParticipantState.ACTIVE))
                .thenReturn(List.of());
        assertEquals(JoinState.PARTICIPANT,
                service.joinWithGuestToken(77L,TOKEN,"invited").state());
        verify(participants).save(argThat(p->p.getProfile()==user));
    }

    @Test
    void invalidGuestTokenDoesNotBypassRestrictedCommunityAccess() throws Exception {
        Session session=session();
        Profile user=new Profile();user.setId(44L);
        when(sessions.findByIdForUpdate(77L)).thenReturn(Optional.of(session));
        when(profiles.findUserByUsername("outsider")).thenReturn(Optional.of(user));
        assertThrows(ResponseStatusException.class,
                ()->service.joinWithGuestToken(77L,"b".repeat(43),"outsider"));
        assertThrows(ResponseStatusException.class,
                ()->service.joinWithGuestToken(77L,"bad","outsider"));
        verify(participants,never()).save(any());
        verifyNoInteractions(reliabilityService);
    }

    @Test
    void signedInvitationNeverBypassesPrivateGameOrOrganizerExclusions() throws Exception {
        Session session=session();
        Profile user=new Profile();user.setId(44L);
        when(sessions.findByIdForUpdate(77L)).thenReturn(Optional.of(session));
        when(profiles.findUserByUsername("outsider")).thenReturn(Optional.of(user));
        session.setPrivateGame(true);
        assertThrows(ResponseStatusException.class,
                ()->service.joinWithGuestToken(77L,TOKEN,"outsider"));
        session.setPrivateGame(false);
        when(exclusions.existsByOrganizerIdAndExcludedId(1L,44L)).thenReturn(true);
        assertThrows(AccessDeniedException.class,
                ()->service.joinWithGuestToken(77L,TOKEN,"outsider"));
        verify(participants,never()).save(any());
    }
}
