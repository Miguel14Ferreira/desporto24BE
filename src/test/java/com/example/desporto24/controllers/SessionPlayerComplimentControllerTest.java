package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.PlayerComplimentType;
import com.example.desporto24.model.dto.session.PlayerComplimentRequest;
import com.example.desporto24.model.dto.session.PlayerComplimentResponse;
import com.example.desporto24.service.SessionPlayerComplimentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionPlayerComplimentControllerTest {
    @Mock SessionPlayerComplimentService service;
    @Mock Authentication authentication;
    @InjectMocks SessionPlayerComplimentController controller;

    @Test
    void usesTheAuthenticatedSenderForPositiveFeedback() {
        var req = new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY);
        var reply = new PlayerComplimentResponse(12L, 13L, PlayerComplimentType.FAIR_PLAY, Instant.now());
        when(authentication.getName()).thenReturn("player");
        when(service.give(7L, 13L, "player", req)).thenReturn(reply);
        assertSame(reply, controller.compliment(7L, 13L, req, authentication));
    }

    @Test
    void onlyTheLoggedInPlayerMayReadSentOrReceivedCompliments() {
        when(authentication.getName()).thenReturn("player");
        when(service.sent(7L, "player")).thenReturn(List.of());
        when(service.received(7L, "player")).thenReturn(List.of());
        assertTrue(controller.sent(7L, authentication).isEmpty());
        assertTrue(controller.received(7L, authentication).isEmpty());
        verify(service).sent(7L, "player");
        verify(service).received(7L, "player");
    }
}
