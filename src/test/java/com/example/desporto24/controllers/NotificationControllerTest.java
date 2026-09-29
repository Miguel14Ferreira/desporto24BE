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
    void acceptFriendRequestUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.acceptMyFriendRequest(9L, authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService).acceptFriendRequest(9L, "miguel");
    }

    @Test
    void rejectFriendRequestUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.rejectMyFriendRequest(9L, authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(notificationService).rejectFriendRequest(9L, "miguel");
    }

    @Test
    void legacyNotificationListCannotChooseAnotherIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> controller.getNotifications("ana", authentication)
        );
    }

    @Test
    void legacyBlockAccountIsBoundToAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.blockAcc("miguel@example.com", authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService)
                .disableProfileAndSendEmail("miguel@example.com", "miguel");
    }

    @Test
    void blockOwnAccountUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.blockMyAccount(authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(notificationService).blockOwnAccount("miguel");
    }

    @Test
    void legacyDeleteUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        Notifications notification = new Notifications();
        notification.setId(9L);

        var response = controller.deleteNotification(notification, authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService).deleteNotification(9L, "miguel");
    }

    @Test
    void legacyFriendAcceptIgnoresClientTokenAndUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.acceptFriendRequest(
                9L,
                "client-supplied-token",
                authentication
        );

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService).acceptFriendRequest(9L, "miguel");
    }

    @Test
    void deleteAllNotificationsUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.deleteAllMyNotifications(authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(notificationService).deleteAllNotifications("miguel");
    }

    @Test
    void deleteNotificationUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.deleteMyNotification(9L, authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(notificationService).deleteNotification(9L, "miguel");
    }

    @Test
    void markReadUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.markRead(9L, authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(notificationService).markRead(9L, "miguel");
    }

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
