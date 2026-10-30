package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommunitySessionServiceTest {

    @Test
    void nonMembersOnlySeeExplicitlyPublicSessions() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);

        Profile viewer = new Profile();
        viewer.setId(7L);
        Community community = Community.builder().id(10L)
                .visibility(CommunityVisibility.PUBLIC).build();
        Session publicSession = new Session();
        publicSession.setAudience(SessionAudience.PUBLIC);
        Session exclusiveSession = new Session();
        exclusiveSession.setAudience(SessionAudience.COMMUNITY_ONLY);
        Session legacySession = new Session();

        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L))
                .thenReturn(List.of(exclusiveSession, legacySession, publicSession));
        when(shares.sessionsSharedWithCommunity(10L)).thenReturn(List.of());

        CommunitySessionService service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);

        assertEquals(1, service.sessions(10L, "viewer").size());
        verify(mapper).toSummary(same(publicSession), isNull(), same(viewer));
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void membersStillSeeExclusiveAndUnspecifiedAudienceSessions() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);

        Profile viewer = new Profile();
        viewer.setId(7L);
        Community community = Community.builder().id(10L)
                .visibility(CommunityVisibility.PUBLIC).build();
        Session exclusiveSession = new Session();
        exclusiveSession.setAudience(SessionAudience.COMMUNITY_ONLY);
        Session legacySession = new Session();

        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(members.existsByCommunityIdAndProfileId(10L, 7L)).thenReturn(true);
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L))
                .thenReturn(List.of(exclusiveSession, legacySession));
        when(shares.sessionsSharedWithCommunity(10L)).thenReturn(List.of());

        CommunitySessionService service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);

        assertEquals(2, service.sessions(10L, "viewer").size());
        verify(mapper).toSummary(same(exclusiveSession), isNull(), same(viewer));
        verify(mapper).toSummary(same(legacySession), isNull(), same(viewer));
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void aPublicSessionSharedWithSeveralCommunitiesAppearsOncePerCommunity() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);
        Profile viewer = new Profile();
        viewer.setId(7L);
        Community group = Community.builder().id(10L)
                .visibility(CommunityVisibility.PRIVATE).build();
        Session publicGame = new Session();
        publicGame.setId(55L);
        publicGame.setAudience(SessionAudience.PUBLIC);
        publicGame.setPrivateGame(false);
        publicGame.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(communities.findById(10L)).thenReturn(Optional.of(group));
        when(members.existsByCommunityIdAndProfileId(10L, 7L)).thenReturn(true);
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L)).thenReturn(List.of());
        when(shares.sessionsSharedWithCommunity(10L)).thenReturn(List.of(publicGame));
        var service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);
        assertEquals(1,service.sessions(10L,"viewer").size());
        verify(mapper).toSummary(publicGame,null,viewer);
    }

    @Test
    void sharedMemberOnlySessionIsVisibleOnlyInsideItsCommunities() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);
        Profile user = new Profile();
        user.setId(7L);
        Community community = Community.builder().id(10L)
                .visibility(CommunityVisibility.PUBLIC).build();
        Session restricted = new Session();
        restricted.setId(55L);
        restricted.setAudience(SessionAudience.COMMUNITY_ONLY);
        restricted.setStatus(SessionStatus.OPEN);
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(user));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L)).thenReturn(List.of());
        when(shares.sessionsSharedWithCommunity(10L)).thenReturn(List.of(restricted));
        var service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);

        assertEquals(0, service.sessions(10L, "viewer").size());
        verifyNoInteractions(mapper);

        when(members.existsByCommunityIdAndProfileId(10L, 7L)).thenReturn(true);
        assertEquals(1, service.sessions(10L, "viewer").size());
        verify(mapper).toSummary(restricted, null, user);
    }

    @Test
    void cancelledSessionsAreExcludedFromOwnedAndCrossPostedCommunityListings() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);

        Profile viewer = new Profile();
        viewer.setId(7L);
        Community community = Community.builder().id(10L)
                .visibility(CommunityVisibility.PUBLIC).build();

        Session cancelledOwned = new Session();
        cancelledOwned.setId(100L);
        cancelledOwned.setStatus(SessionStatus.CANCELLED);
        cancelledOwned.setAudience(SessionAudience.COMMUNITY_ONLY);

        Session activeOwned = new Session();
        activeOwned.setId(101L);
        activeOwned.setStatus(SessionStatus.OPEN);
        activeOwned.setAudience(SessionAudience.COMMUNITY_ONLY);

        Session completedOwned = new Session();
        completedOwned.setId(102L);
        completedOwned.setStatus(SessionStatus.COMPLETED);
        completedOwned.setAudience(SessionAudience.COMMUNITY_ONLY);

        Session cancelledShared = new Session();
        cancelledShared.setId(103L);
        cancelledShared.setStatus(SessionStatus.CANCELLED);
        cancelledShared.setAudience(SessionAudience.PUBLIC);
        cancelledShared.setPrivateGame(false);

        Session activeShared = new Session();
        activeShared.setId(104L);
        activeShared.setStatus(SessionStatus.OPEN);
        activeShared.setAudience(SessionAudience.PUBLIC);
        activeShared.setPrivateGame(false);

        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(members.existsByCommunityIdAndProfileId(10L, 7L)).thenReturn(true);
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L))
                .thenReturn(List.of(cancelledOwned, activeOwned, completedOwned));
        when(shares.sessionsSharedWithCommunity(10L))
                .thenReturn(List.of(cancelledShared, activeShared));

        var service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);

        assertEquals(3, service.sessions(10L, "viewer").size());
        verify(mapper).toSummary(same(activeOwned), isNull(), same(viewer));
        verify(mapper).toSummary(same(completedOwned), isNull(), same(viewer));
        verify(mapper).toSummary(same(activeShared), isNull(), same(viewer));
        verify(mapper, never()).toSummary(same(cancelledOwned), isNull(), any(Profile.class));
        verify(mapper, never()).toSummary(same(cancelledShared), isNull(), any(Profile.class));
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void nonMembersCannotSeeCancelledPublicCommunitySessions() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);

        Profile viewer = new Profile();
        viewer.setId(7L);
        Community community = Community.builder().id(10L)
                .visibility(CommunityVisibility.PUBLIC).build();
        Session cancelled = new Session();
        cancelled.setId(200L);
        cancelled.setAudience(SessionAudience.PUBLIC);
        cancelled.setStatus(SessionStatus.CANCELLED);
        Session available = new Session();
        available.setId(201L);
        available.setAudience(SessionAudience.PUBLIC);
        available.setStatus(SessionStatus.OPEN);

        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(sessions.findByCommunityIdOrderByGameDateDesc(10L))
                .thenReturn(List.of(cancelled, available));
        when(shares.sessionsSharedWithCommunity(10L)).thenReturn(List.of());

        var service = new CommunitySessionService(
                communities, members, profiles, sessions, shares, mapper);
        assertEquals(1, service.sessions(10L, "viewer").size());
        verify(mapper).toSummary(same(available), isNull(), same(viewer));
        verifyNoMoreInteractions(mapper);
    }

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
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);

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
                    communities, members, profiles, sessions, shares, mapper);

            assertThrows(IllegalStateException.class,
                    () -> service.openToPublic(10L, 42L, "owner"));
            assertEquals(SessionAudience.COMMUNITY_ONLY, session.getAudience());
            verify(sessions, never()).save(any(Session.class));
        }
    }

    @Test
    void adminCanRestoreMembersOnlyEvenWhenGameIsFull() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        SessionViewMapper mapper = mock(SessionViewMapper.class);
        SessionCommunityShareRepository shares = mock(SessionCommunityShareRepository.class);
        Profile actor = new Profile();
        actor.setId(1L);
        Community group = Community.builder().id(10L).build();
        CommunityMember membership = CommunityMember.builder()
                .community(group).profile(actor).role(CommunityRole.ADMIN).build();
        Session game = new Session();
        game.setCommunity(group);
        game.setAudience(SessionAudience.PUBLIC);
        game.setStatus(SessionStatus.FULL);
        game.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        when(profiles.findUserByUsernameIgnoreCase("admin")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(10L,1L)).thenReturn(Optional.of(membership));
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(game));
        when(sessions.save(game)).thenReturn(game);

        new CommunitySessionService(communities,members,profiles,sessions,shares,mapper)
                .restrictToMembers(10L,42L,"admin");

        assertEquals(SessionAudience.COMMUNITY_ONLY,game.getAudience());
        verify(mapper).toSummary(same(game),isNull());
        verify(sessions).save(same(game));
    }

    @Test
    void restrictionDeniesRegularMembersAndUnrelatedCommunity() {
        Profile actor = new Profile();
        actor.setId(1L);
        Community group = Community.builder().id(10L).build();
        CommunityMember membership = CommunityMember.builder()
                .community(group).profile(actor).role(CommunityRole.MEMBER).build();
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionRepository sessions = mock(SessionRepository.class);
        when(profiles.findUserByUsernameIgnoreCase("actor")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(10L,1L)).thenReturn(Optional.of(membership));
        CommunitySessionService service = new CommunitySessionService(
                mock(CommunityRepository.class),members,profiles,sessions,mock(SessionCommunityShareRepository.class),mock(SessionViewMapper.class));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                ()->service.restrictToMembers(10L,42L,"actor"));
        verify(sessions,never()).save(any());

        when(members.findByCommunityIdAndProfileId(10L,1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(group).profile(actor).role(CommunityRole.OWNER).build()));
        Session other = new Session();
        other.setCommunity(Community.builder().id(11L).build());
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(other));
        assertThrows(IllegalArgumentException.class,
                ()->service.restrictToMembers(10L,42L,"actor"));
        verify(sessions,never()).save(any());
    }

    @Test
    void restrictionRejectsEndedAndPastSessions() {
        for (SessionStatus status : new SessionStatus[]{SessionStatus.CANCELLED,SessionStatus.COMPLETED,SessionStatus.OPEN}) {
            Profile actor = new Profile();actor.setId(2L);
            Community group = Community.builder().id(10L).build();
            CommunityMemberRepository members = mock(CommunityMemberRepository.class);
            ProfileRepository profiles = mock(ProfileRepository.class);
            SessionRepository sessions = mock(SessionRepository.class);
            when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
            when(members.findByCommunityIdAndProfileId(10L,2L)).thenReturn(Optional.of(
                    CommunityMember.builder().community(group).profile(actor).role(CommunityRole.OWNER).build()));
            Session game = new Session();
            game.setCommunity(group);
            game.setAudience(SessionAudience.PUBLIC);
            game.setStatus(status);
            game.setGameDate(Date.from(status==SessionStatus.OPEN
                    ? Instant.now().minusSeconds(60):Instant.now().plusSeconds(600)));
            when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(game));
            var service = new CommunitySessionService(
                    mock(CommunityRepository.class),members,profiles,sessions,
                    mock(com.example.desporto24.repository.session.SessionCommunityShareRepository.class),
                    mock(SessionViewMapper.class));
            assertThrows(IllegalStateException.class,
                    ()->service.restrictToMembers(10L,42L,"owner"));
            assertEquals(SessionAudience.PUBLIC,game.getAudience());
            verify(sessions,never()).save(any());
        }
    }

}
