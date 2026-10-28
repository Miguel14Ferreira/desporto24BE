package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Regression for BOLA / IDOR on legacy community sessions whose audience was
 * never backfilled from older schema versions.
 */
@ExtendWith(MockitoExtension.class)
class SessionLegacyCommunityAuthorizationTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityMemberRepository communityMemberRepository;
    @Mock private com.example.desporto24.repository.session.SessionParticipantRepository participantRepository;
    @Mock private com.example.desporto24.repository.session.SessionWaitlistRepository waitlistRepository;
    @Mock private com.example.desporto24.repository.session.SessionCommunityShareRepository sessionCommunityShareRepository;
    @Mock private SessionViewMapper sessionViewMapper;
    @InjectMocks private SessionServiceImpl sessionService;

    private final Profile outsider = profile(44L);
    private final Session legacy = communitySession();

    @Test
    void outsiderCannotReadLegacyCommunitySessionDetails() {
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(88L, 44L))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> sessionService.getById(77L, "outsider"));
        verifyNoInteractions(sessionViewMapper);
    }

    @Test
    void outsiderCannotReadLegacyCommunitySessionSeries() {
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(88L, 44L))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> sessionService.series(77L, "outsider"));
        verifyNoInteractions(sessionViewMapper);
    }

    @Test
    void outsiderCannotJoinLegacyCommunitySession() {
        when(sessionRepository.findByIdForUpdate(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(88L, 44L))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> sessionService.join(77L, new JoinSessionRequest(null), "outsider"));
        verifyNoInteractions(sessionViewMapper);
    }

    @Test
    void activeCommunityMemberMayStillViewLegacySession() {
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(88L, 44L))
                .thenReturn(true);
        SessionDetailResponse expected = mock(SessionDetailResponse.class);
        when(sessionViewMapper.toDetail(legacy, outsider)).thenReturn(expected);

        assertSame(expected, sessionService.getById(77L, "outsider"));
    }

    @Test
    void explicitlyPublicCommunitySessionRemainsAccessible() {
        legacy.setAudience(SessionAudience.PUBLIC);
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        SessionDetailResponse expected = mock(SessionDetailResponse.class);
        when(sessionViewMapper.toDetail(legacy, outsider)).thenReturn(expected);

        assertSame(expected, sessionService.getById(77L, "outsider"));
        verifyNoInteractions(communityMemberRepository);
    }

    @Test
    void alreadyConfirmedPublicRegistrantRetainsDetailAccessAfterRestriction() {
        legacy.setAudience(SessionAudience.COMMUNITY_ONLY);
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(88L,44L)).thenReturn(false);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                77L,44L,com.example.desporto24.enumeration.ParticipantState.ACTIVE)).thenReturn(true);
        SessionDetailResponse expected=mock(SessionDetailResponse.class);
        when(sessionViewMapper.toDetail(legacy,outsider)).thenReturn(expected);
        assertSame(expected,sessionService.getById(77L,"outsider"));
    }

    @Test
    void outsidersWithoutExistingRegistrationStillCannotSeeRestrictedDetails() {
        legacy.setAudience(SessionAudience.COMMUNITY_ONLY);
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        assertThrows(AccessDeniedException.class,()->sessionService.getById(77L,"outsider"));
        verifyNoInteractions(sessionViewMapper);
    }

    @Test
    void malformedCommunityOnlyAudienceWithoutCommunityFailsClosed() {
        legacy.setCommunity(null);
        legacy.setAudience(SessionAudience.COMMUNITY_ONLY);
        when(sessionRepository.findById(77L)).thenReturn(Optional.of(legacy));
        when(profileRepository.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));

        assertThrows(AccessDeniedException.class,
                () -> sessionService.getById(77L, "outsider"));
        verifyNoInteractions(communityMemberRepository, sessionViewMapper);
    }

    private Profile profile(Long id) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername("outsider");
        return profile;
    }

    private Session communitySession() {
        Session session = new Session();
        session.setId(77L);
        session.setProfile(profile(1L));
        session.setCommunity(community());
        session.setAudience(null);
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        session.setStatus(SessionStatus.OPEN);
        return session;
    }

    private Community community() {
        Community result = new Community();
        result.setId(88L);
        return result;
    }
}
