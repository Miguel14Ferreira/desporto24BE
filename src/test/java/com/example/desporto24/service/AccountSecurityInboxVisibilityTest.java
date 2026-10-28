package com.example.desporto24.service;

import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.service.impl.NotificationServiceImpl;
import com.example.desporto24.service.interfaces.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountSecurityInboxVisibilityTest {
    @Test
    void mandatorySecurityAlertStaysVisibleAndUnreadWhenOptionalInboxIsOff() {
        NotificationsRepository repo = mock(NotificationsRepository.class);
        NotificationPreferencesService prefs = mock(NotificationPreferencesService.class);
        NotificationServiceImpl service = new NotificationServiceImpl(
                repo,
                mock(EmailService.class),
                mock(ProfileService.class),
                mock(ConfirmationTokenService.class),
                mock(FriendRequestService.class),
                mock(FriendRepository.class),
                mock(ProfileRepository.class),
                mock(UserBlockService.class),
                mock(PostGameReminderService.class)
        );
        ReflectionTestUtils.setField(service, "preferenceService", prefs);

        Notifications ordinary = new Notifications();
        ordinary.setSubject("Perfil atualizado");
        ordinary.setUpdateProfile(true);
        ordinary.setRead(false);
        Notifications security = new Notifications();
        security.setSubject("Alteração de email");
        security.setUpdateProfile(true);
        security.setRead(false);
        when(repo.findByUsername("ana")).thenReturn(List.of(ordinary, security));

        when(prefs.get("ana")).thenReturn(new NotificationPreferencesRequest(
                false, true, Set.of("PROFILE_UPDATE"), Set.of()));
        assertEquals(List.of(security), service.getNotificationsFromProfile("ana"));
        assertEquals(1L, service.countUnreadNotifications("ana"));

        // Legacy profile notices remain opted out; security notices do not.
        when(prefs.get("ana")).thenReturn(new NotificationPreferencesRequest(
                true, true, Set.of("PROFILE_UPDATE"), Set.of()));
        assertEquals(List.of(security), service.getNotificationsFromProfile("ana"));
        assertEquals(1L, service.countUnreadNotifications("ana"));

        security.setRead(true);
        assertEquals(0L, service.countUnreadNotifications("ana"));
    }
}
