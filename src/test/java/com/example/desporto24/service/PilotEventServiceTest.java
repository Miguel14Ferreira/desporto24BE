package com.example.desporto24.service;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.analytics.PilotEvent;
import com.example.desporto24.repository.analytics.PilotEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotEventServiceTest {

    @Mock
    private PilotEventRepository pilotEventRepository;

    private PilotEventService service;

    @BeforeEach
    void setUp() {
        service = new PilotEventService(pilotEventRepository);
    }

    @Test
    void returnsCountsForEveryPilotEventType() {
        when(pilotEventRepository.countByEventType(PilotEventType.ACCESS)).thenReturn(200L);
        when(pilotEventRepository.countByEventType(PilotEventType.REGISTRATION_COMPLETED)).thenReturn(100L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_SEARCHED)).thenReturn(70L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_CREATED)).thenReturn(20L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_JOINED)).thenReturn(60L);
        when(pilotEventRepository.countByEventType(PilotEventType.ATTENDANCE_CONFIRMED)).thenReturn(30L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_COMPLETED)).thenReturn(10L);

        var counts = service.eventCounts();

        assertEquals(200L, counts.access());
        assertEquals(100L, counts.registrationCompleted());
        assertEquals(70L, counts.sessionSearched());
        assertEquals(20L, counts.sessionCreated());
        assertEquals(60L, counts.sessionJoined());
        assertEquals(30L, counts.attendanceConfirmed());
        assertEquals(10L, counts.sessionCompleted());
    }

    @Test
    void recordsOnlyFirstAccessForDevice() {
        when(pilotEventRepository.existsByEventTypeAndDeviceId(
                PilotEventType.ACCESS,
                "device-123"
        )).thenReturn(false, true);

        service.recordFirstAccess("  device-123  ");
        service.recordFirstAccess("device-123");

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.ACCESS, event.getEventType());
        assertEquals("device-123", event.getDeviceId());
        assertNull(event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsRegistrationOnlyOncePerProfile() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.REGISTRATION_COMPLETED,
                42L
        )).thenReturn(false, true);

        service.recordRegistrationCompleted(profile);
        service.recordRegistrationCompleted(profile);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.REGISTRATION_COMPLETED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionSearchOnlyOncePerProfile() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.SESSION_SEARCHED,
                42L
        )).thenReturn(false, true);

        service.recordSessionSearched(profile);
        service.recordSessionSearched(profile);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_SEARCHED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionCreationOnlyOncePerSession() {
        Profile creator = new Profile();
        creator.setId(42L);
        Profile anotherProfile = new Profile();
        anotherProfile.setId(43L);

        when(pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_CREATED,
                99L
        )).thenReturn(false, true);

        service.recordSessionCreated(creator, 99L);
        service.recordSessionCreated(anotherProfile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_CREATED, event.getEventType());
        assertEquals(creator, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionJoinOnlyOncePerProfileAndSession() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.SESSION_JOINED,
                42L,
                99L
        )).thenReturn(false, true);

        service.recordSessionJoined(profile, 99L);
        service.recordSessionJoined(profile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_JOINED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsAttendanceOnlyOncePerProfileAndSession() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.ATTENDANCE_CONFIRMED,
                42L,
                99L
        )).thenReturn(false, true);

        service.recordAttendanceConfirmed(profile, 99L);
        service.recordAttendanceConfirmed(profile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.ATTENDANCE_CONFIRMED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionCompletionOnlyOncePerSession() {
        Profile creator = new Profile();
        creator.setId(42L);
        Profile anotherProfile = new Profile();
        anotherProfile.setId(43L);

        when(pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_COMPLETED,
                99L
        )).thenReturn(false, true);

        service.recordSessionCompleted(creator, 99L);
        service.recordSessionCompleted(anotherProfile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_COMPLETED, event.getEventType());
        assertEquals(creator, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsBusinessEventFromTrustedDomainService() {
        Profile profile = new Profile();
        profile.setId(42L);

        service.recordBusinessEvent(
                PilotEventType.SESSION_JOINED,
                profile,
                99L
        );

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_JOINED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void accessCannotBeRecordedAsBusinessEvent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recordBusinessEvent(PilotEventType.ACCESS, new Profile(), null)
        );

        verify(pilotEventRepository, never()).save(any());
    }
}
