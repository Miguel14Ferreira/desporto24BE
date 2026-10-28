package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionRecurrence;
import com.example.desporto24.enumeration.RegistrationOpeningMode;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.session.SessionCommunityShare;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCreateCrosspostTest {
    @Mock SessionRepository sessions;
    @Mock ProfileRepository profiles;
    @Mock CommunityRepository communities;
    @Mock com.example.desporto24.repository.community.CommunitySeasonRepository seasonsDb;
    @Mock CommunityMemberRepository members;
    @Mock SessionParticipantRepository participants;
    @Mock com.example.desporto24.repository.session.SessionWaitlistRepository waitlist;
    @Mock SessionCommunityShareRepository shares;
    @Mock SessionViewMapper mapper;
    @Mock PilotEventService events;
    @InjectMocks SessionServiceImpl service;

    private CreateSessionRequest request(List<Long> groups) {
        return new CreateSessionRequest(
                "Futebol", Instant.now().plusSeconds(3600), "Cacém", "Rua do Campo",
                10, BigDecimal.ZERO, false, null, null, null,
                null, null, null, null, 6, null, null, null, null, null, groups);
    }

    @Test
    void oneSessionIsCreatedAndSharedInTwoCommunitiesWithoutDuplicatingAttendance() {
        Profile creator = new Profile(); creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        Community first = Community.builder().id(11L).build();
        Community second = Community.builder().id(12L).build();
        when(communities.findAllById(List.of(11L, 12L)))
                .thenReturn(List.of(first, second));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(members.findByCommunityIdAndProfileId(12L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session s=inv.getArgument(0);
            s.setId(42L);
            return s;
        });
        SessionDetailResponse details=mock(SessionDetailResponse.class);
        when(mapper.toDetail(any(Session.class),eq(creator))).thenReturn(details);

        assertSame(details,service.create(request(List.of(11L,12L)),"organizer"));
        verify(sessions,times(1)).save(argThat(session ->
                session.getAudience() == SessionAudience.COMMUNITY_ONLY
                && !Boolean.TRUE.equals(session.getPrivateGame())
                && session.getCommunity() == null));
        verify(participants,times(1)).save(any());
        verify(shares,times(2)).save(argThat(share ->
                share.getSession().getId().equals(42L)
                && (share.getCommunity().getId().equals(11L)
                    || share.getCommunity().getId().equals(12L))));
    }

    @Test
    void recurringSharedGamesArePrivateToMembersAndEachOccurrenceIsPostedToEveryCommunity() {
        Profile creator = new Profile(); creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        Community first = Community.builder().id(11L).build();
        Community second = Community.builder().id(12L).build();
        when(communities.findAllById(List.of(11L, 12L))).thenReturn(List.of(first, second));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(members.findByCommunityIdAndProfileId(12L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        java.util.concurrent.atomic.AtomicLong ids = new java.util.concurrent.atomic.AtomicLong(40L);
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session saved = inv.getArgument(0);
            saved.setId(ids.incrementAndGet());
            return saved;
        });
        when(mapper.toDetail(any(Session.class), eq(creator)))
                .thenReturn(mock(SessionDetailResponse.class));

        CreateSessionRequest recurring = new CreateSessionRequest(
                "Futebol", Instant.now().plusSeconds(3600), "Cacém", "Rua do Campo",
                10, BigDecimal.ZERO, false, null, null, null,
                null, null, SessionRecurrence.WEEKLY, 2, 0,
                RegistrationOpeningMode.MANUAL, null, null, "Europe/Lisbon",
                null, List.of(11L, 12L));

        service.create(recurring, "organizer");

        var sessionsCreated = org.mockito.ArgumentCaptor.forClass(Session.class);
        verify(sessions, times(2)).save(sessionsCreated.capture());
        assertTrue(sessionsCreated.getAllValues().stream()
                .allMatch(s -> s.getAudience() == SessionAudience.COMMUNITY_ONLY));
        String seriesId = sessionsCreated.getAllValues().get(0).getRecurrenceSeriesId();
        assertNotNull(seriesId);
        assertEquals(seriesId, sessionsCreated.getAllValues().get(1).getRecurrenceSeriesId());
        verify(participants, times(2)).save(any());
        verify(shares, times(4)).save(any(SessionCommunityShare.class));
    }

    @Test
    void nonMembersCannotReadOrJoinSharedCommunitySessionsEvenWithInviteToken() {
        Profile outsider = new Profile(); outsider.setId(77L);
        Session session = new Session();
        session.setId(42L);
        session.setCommunity(null);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        session.setStatus(com.example.desporto24.enumeration.SessionStatus.OPEN);
        session.setGameDate(java.util.Date.from(Instant.now().plusSeconds(3600)));
        when(profiles.findUserByUsername("outsider")).thenReturn(Optional.of(outsider));
        when(sessions.findById(42L)).thenReturn(Optional.of(session));
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(session));

        assertThrows(AccessDeniedException.class, () -> service.getById(42L, "outsider"));
        assertThrows(AccessDeniedException.class, () ->
                service.join(42L, new com.example.desporto24.model.dto.session.JoinSessionRequest(null),
                        "outsider"));
        assertThrows(AccessDeniedException.class, () ->
                service.joinWithGuestToken(42L, "a".repeat(43), "outsider"));
        verify(participants, never()).save(any());
    }


    private CreateSessionRequest seasonRequest(List<Long> groups, long destination, boolean confirm) {
        return new CreateSessionRequest(
                "Futebol", Instant.now().plusSeconds(3600), "Cacém", "Rua do Campo",
                10, BigDecimal.ZERO, false, null, null, null,
                null, null, null, null, 0, null, null, null, null, null,
                groups, destination, confirm);
    }

    @Test
    void anyMemberCanEnableStandingsWithExplicitConsentAndCreateDefaultSeason() {
        Profile organizer = new Profile(); organizer.setId(7L);
        Community group = Community.builder().id(11L).rankingsEnabled(false).build();
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(communities.findAllById(List.of(11L))).thenReturn(List.of(group));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(seasonRequest(List.of(11L), 11L, false), "organizer"));
        verify(communities, never()).save(any());
        verify(sessions, never()).save(any());

        when(seasonsDb.findByCommunityIdAndActiveTrue(11L)).thenReturn(Optional.empty());
        when(seasonsDb.save(any(CommunitySeason.class))).thenAnswer(inv -> {
            CommunitySeason created = inv.getArgument(0);
            created.setId(99L);
            return created;
        });
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session session = inv.getArgument(0);
            session.setId(42L);
            return session;
        });
        when(mapper.toDetail(any(Session.class), eq(organizer)))
                .thenReturn(mock(SessionDetailResponse.class));

        service.create(seasonRequest(List.of(11L), 11L, true), "organizer");

        assertTrue(group.isRankingsEnabled());
        verify(communities).save(group);
        verify(seasonsDb).save(argThat(season -> season.isActive()
                && season.getCommunity() == group && season.getName().startsWith("Época ")));
        verify(sessions).save(argThat(session ->
                session.getSeasonCompetition() != null
                && session.getSeasonCompetition().getId().equals(99L)));
    }

    @Test
    void anyMemberCanUseExistingSeasonWithoutChangingSettings() {
        Profile organizer = new Profile(); organizer.setId(7L);
        Community group = Community.builder().id(11L).rankingsEnabled(true).build();
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(communities.findAllById(List.of(11L))).thenReturn(List.of(group));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        CommunitySeason active = CommunitySeason.builder().id(99L)
                .community(group).active(true).build();
        when(seasonsDb.findByCommunityIdAndActiveTrue(11L)).thenReturn(Optional.of(active));
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session session = inv.getArgument(0);
            session.setId(42L);
            return session;
        });
        when(mapper.toDetail(any(Session.class), eq(organizer)))
                .thenReturn(mock(SessionDetailResponse.class));

        service.create(seasonRequest(List.of(11L), 11L, false), "organizer");
        verify(sessions).save(argThat(session -> session.getSeasonCompetition() == active));
        verify(communities, never()).save(any());
        verify(seasonsDb, never()).save(any());
    }

    @Test
    void aGameSharedWithTwoCommunitiesCannotAffectEitherSeason() {
        Profile organizer = new Profile(); organizer.setId(7L);
        Community first = Community.builder().id(11L).rankingsEnabled(true).build();
        Community second = Community.builder().id(12L).rankingsEnabled(true).build();
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(communities.findAllById(List.of(11L, 12L)))
                .thenReturn(List.of(first, second));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(members.findByCommunityIdAndProfileId(12L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(seasonRequest(List.of(11L, 12L), 11L, false), "organizer"));
        assertThrows(IllegalArgumentException.class,
                () -> service.create(seasonRequest(List.of(11L, 12L), 12L, true), "organizer"));
        verify(sessions, never()).save(any());
        verify(seasonsDb, never()).save(any());
        verify(communities, never()).save(any());
    }

    @Test
    void aMultiCommunitySessionWithoutSeasonRemainsUnranked() {
        Profile organizer = new Profile(); organizer.setId(7L);
        Community first = Community.builder().id(11L).build();
        Community second = Community.builder().id(12L).build();
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(communities.findAllById(List.of(11L, 12L)))
                .thenReturn(List.of(first, second));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(members.findByCommunityIdAndProfileId(12L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session session = inv.getArgument(0);
            session.setId(42L);
            return session;
        });
        when(mapper.toDetail(any(Session.class), eq(organizer)))
                .thenReturn(mock(SessionDetailResponse.class));

        service.create(request(List.of(11L, 12L)), "organizer");
        verify(sessions).save(argThat(session -> session.getSeasonCompetition() == null));
        verifyNoInteractions(seasonsDb);
    }

    @Test
    void seasonDestinationMustBelongToSelectedCommunities() {
        Profile organizer = new Profile(); organizer.setId(7L);
        Community group = Community.builder().id(11L).build();
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(communities.findAllById(List.of(11L))).thenReturn(List.of(group));
        when(members.findByCommunityIdAndProfileId(11L, 7L)).thenReturn(
                Optional.of(CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        assertThrows(IllegalArgumentException.class,
                () -> service.create(seasonRequest(List.of(11L), 12L, true), "organizer"));
        verify(sessions, never()).save(any());
        verify(seasonsDb, never()).save(any());
    }

    @Test
    void refusesToPostToCommunityTheCreatorHasNotJoined() {
        Profile creator = new Profile(); creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        when(communities.findAllById(List.of(11L)))
                .thenReturn(List.of(Community.builder().id(11L).build()));
        assertThrows(AccessDeniedException.class,
                () -> service.create(request(List.of(11L)), "organizer"));
        verify(sessions,never()).save(any());
        verify(shares,never()).save(any());
    }

    @Test
    void duplicatesOrMoreThanFiveTargetsCannotCreateAnySession() {
        Profile creator = new Profile();creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        assertThrows(IllegalArgumentException.class,() ->
                service.create(request(List.of(11L,11L)), "organizer"));
        assertThrows(IllegalArgumentException.class,() ->
                service.create(request(List.of(1L,2L,3L,4L,5L,6L)), "organizer"));
        verify(sessions,never()).save(any());
    }
}
