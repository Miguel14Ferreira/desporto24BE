package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionNotificationServiceTest {

    private NotificationsRepository notificationsRepository;
    private SessionParticipantRepository participantRepository;
    private SessionWaitlistRepository waitlistRepository;
    private SessionNotificationService service;

    @BeforeEach
    void setUp() {
        notificationsRepository = mock(NotificationsRepository.class);
        participantRepository = mock(SessionParticipantRepository.class);
        waitlistRepository = mock(SessionWaitlistRepository.class);
        service = new SessionNotificationService(
                notificationsRepository,
                participantRepository,
                waitlistRepository
        );
    }

    @Test
    void waitlistPromotionCreatesTypedUnreadNotification() {
        Profile profile = profile(2L, "miguel");
        Session session = session(10L, profile(1L, "creator"));

        service.notifyWaitlistPromoted(session, profile);

        ArgumentCaptor<Notifications> captor = ArgumentCaptor.forClass(Notifications.class);
        verify(notificationsRepository).save(captor.capture());
        Notifications saved = captor.getValue();

        assertEquals(NotificationType.WAITLIST_PROMOTED, saved.getType());
        assertEquals(10L, saved.getSessionId());
        assertEquals("miguel", saved.getUsername());
        assertFalse(saved.isRead());
    }

    @Test
    void removalCreatesSessionRemovedNotification() {
        Profile profile = profile(2L, "miguel");
        Session session = session(10L, profile(1L, "creator"));

        service.notifyParticipantRemoved(session, profile);

        ArgumentCaptor<Notifications> captor = ArgumentCaptor.forClass(Notifications.class);
        verify(notificationsRepository).save(captor.capture());
        assertEquals(NotificationType.SESSION_REMOVED, captor.getValue().getType());
    }

    @Test
    void cancellationNotifiesActiveParticipantsAndWaitlistOnceButNotCreator() {
        Profile creator = profile(1L, "creator");
        Profile active = profile(2L, "active");
        Profile waiting = profile(3L, "waiting");
        Session session = session(10L, creator);

        when(participantRepository.findBySessionIdAndStateOrderByJoinedAtAsc(
                10L, ParticipantState.ACTIVE))
                .thenReturn(List.of(
                        participant(session, creator),
                        participant(session, active)
                ));
        when(waitlistRepository.findBySessionIdOrderByPriorityTierAscCreatedAtAsc(10L))
                .thenReturn(List.of(
                        waitlist(session, active),
                        waitlist(session, waiting)
                ));

        service.notifySessionCancelled(session);

        ArgumentCaptor<Notifications> captor = ArgumentCaptor.forClass(Notifications.class);
        verify(notificationsRepository, times(2)).save(captor.capture());

        List<Notifications> saved = captor.getAllValues();
        assertEquals(List.of("active", "waiting"),
                saved.stream().map(Notifications::getUsername).sorted().toList());
        assertTrue(saved.stream().allMatch(n -> n.getType() == NotificationType.SESSION_CANCELLED));
        assertTrue(saved.stream().allMatch(n -> n.getSessionId().equals(10L)));
        assertTrue(saved.stream().noneMatch(n -> n.getUsername().equals("creator")));
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Session session(Long id, Profile creator) {
        Session session = new Session();
        session.setId(id);
        session.setProfile(creator);
        return session;
    }

    private SessionParticipant participant(Session session, Profile profile) {
        return SessionParticipant.builder()
                .session(session)
                .profile(profile)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(Instant.now())
                .build();
    }

    private SessionWaitlist waitlist(Session session, Profile profile) {
        return SessionWaitlist.builder()
                .session(session)
                .profile(profile)
                .priorityTier(0)
                .createdAt(Instant.now())
                .build();
    }
}
