package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommunitySessionServiceTest {

    @Test
    void cannotPublishCancelledCompletedOrStartedSessions() {
        for (SessionStatus status : new SessionStatus[] {
                SessionStatus.CANCELLED, SessionStatus.COMPLETED, SessionStatus.OPEN
        }) {
            CommunityRepository communities = mock(CommunityRepository.class);
            CommunityMemberRepository members = mock(CommunityMemberRepository.class);
            ProfileRepository profiles = mock(ProfileRepository.class);
            SessionRepository sessions = mock(SessionRepository.class);
            SessionViewMapper mapper = mock(SessionViewMapper.class);

            Profile actor = new Profile();
            actor.setId(1L);
            actor.setUsername("owner");
            Community community = Community.builder().id(10L).build();
            CommunityMember membership = CommunityMember.builder()
                    .community(community).profile(actor).role(CommunityRole.OWNER).build();
            Session session = new Session();
            session.setCommunity(community);
            session.setStatus(status);
            session.setAudience(SessionAudience.COMMUNITY_ONLY);
            session.setGameDate(Date.from(status == SessionStatus.OPEN
                    ? Instant.now().minusSeconds(60) : Instant.now().plusSeconds(3600)));

            when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
            when(members.findByCommunityIdAndProfileId(10L, 1L))
                    .thenReturn(Optional.of(membership));
            when(sessions.findById(42L)).thenReturn(Optional.of(session));

            CommunitySessionService service = new CommunitySessionService(
                    communities, members, profiles, sessions, mapper);

            assertThrows(IllegalStateException.class,
                    () -> service.openToPublic(10L, 42L, "owner"));
            assertEquals(SessionAudience.COMMUNITY_ONLY, session.getAudience());
            verify(sessions, never()).save(any(Session.class));
        }
    }
}
