package com.example.desporto24.controllers;

import com.example.desporto24.service.interfaces.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionManagementControllerTest {

    @Mock private SessionService sessionService;
    @Mock private Authentication authentication;
    @InjectMocks private SessionController controller;

    @Test
    void leaveUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        controller.leave(10L, authentication);

        verify(sessionService).leave(10L, "miguel");
    }

    @Test
    void cancelUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        controller.cancel(10L, authentication);

        verify(sessionService).cancel(10L, "miguel");
    }

    @Test
    void removeParticipantUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        controller.removeParticipant(10L, 55L, authentication);

        verify(sessionService).removeParticipant(10L, 55L, "miguel");
    }
}
