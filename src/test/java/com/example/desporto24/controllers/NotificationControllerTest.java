package com.example.desporto24.controllers;

import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.service.interfaces.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock private NotificationService notificationService;
    @Mock private Authentication authentication;
    @InjectMocks private NotificationController controller;

    @Test
    void myNotificationsUsesAuthenticatedIdentity() {
        Notifications notification = new Notifications();
        when(authentication.getName()).thenReturn("miguel");
        when(notificationService.getNotificationsFromProfile("miguel"))
                .thenReturn(List.of(notification));

        var response = controller.getMyNotifications(authentication);

        assertEquals(1, response.getBody().size());
        verify(notificationService).getNotificationsFromProfile("miguel");
    }
}
