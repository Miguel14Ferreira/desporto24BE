package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.model.Session;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServicePublicPreviewTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @InjectMocks private SessionServiceImpl service;

    private Session publicSession() {
        var session = new Session();
        session.setId(42L);
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(false);
        session.setEnabled(true);
        session.setSport("Futebol");
        session.setCity("Lisboa");
        session.setGameDate(Date.from(Instant.parse("2099-10-20T18:00:00Z")));
        session.setMaxParticipants(10);
        session.setExternalConfirmedParticipants(7);
        session.setStatus(SessionStatus.OPEN);
        return session;
    }

    @Test
    void publicPreviewCountsActiveAndExternalConfirmedWithoutExposingIdentities() {
        var session = publicSession();
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(session));
        when(participantRepository.countBySessionIdAndState(42L, ParticipantState.ACTIVE))
                .thenReturn(1L);

        var preview = service.publicPreview(42L);

        assertEquals(42L, preview.id());
        assertEquals(10, preview.maxParticipants());
        assertEquals(8, preview.participantCount());
        assertEquals(2, preview.maxParticipants() - preview.participantCount());
        verify(participantRepository).countBySessionIdAndState(42L, ParticipantState.ACTIVE);
    }

    @Test
    void privateSessionDoesNotRevealItsParticipantCount() {
        var session = publicSession();
        session.setPrivateGame(true);
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(session));

        assertThrows(SessionNotFound.class, () -> service.publicPreview(42L));

        verifyNoInteractions(participantRepository);
    }

    @Test
    void communityOnlySessionDoesNotRevealItsParticipantCount() {
        var session = publicSession();
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(session));

        assertThrows(SessionNotFound.class, () -> service.publicPreview(42L));

        verifyNoInteractions(participantRepository);
    }

    @Test
    void previewTreatsLegacyNullExternalConfirmedAsZero() {
        var session = publicSession();
        session.setExternalConfirmedParticipants(null);
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(session));
        when(participantRepository.countBySessionIdAndState(42L, ParticipantState.ACTIVE))
                .thenReturn(3L);

        assertEquals(3, service.publicPreview(42L).participantCount());
    }
}
