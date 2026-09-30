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
