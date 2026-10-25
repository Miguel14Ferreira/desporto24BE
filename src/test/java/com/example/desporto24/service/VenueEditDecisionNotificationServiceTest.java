package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueEditDecisionNotificationServiceTest {
    @Mock NotificationsRepository notifications;
    @Mock NotificationPreferencesService preferences;
    @Mock ProfileRepository profiles;
    @Mock EmailService email;
    @Mock ApplicationEventPublisher events;
    VenueEditDecisionNotificationService service;

    @BeforeEach
    void init() {
        service = new VenueEditDecisionNotificationService(
                notifications, preferences, profiles, email, events);
        ReflectionTestUtils.setField(service, "appUrl", "https://d24sports.com");
    }

    @Test
    void approvalCreatesLinkedInAppAlertAndOptionalEmailEvent() {
        when(preferences.allowsInApp("member", NotificationType.VENUE_EDIT_DECISION))
                .thenReturn(true);
        when(preferences.allowsOptionalEmail("member", NotificationType.VENUE_EDIT_DECISION))
                .thenReturn(true);

        service.decision(13L, 5L, "Pavilhão", "member", true, "Verificado");
        var saved = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications).save(saved.capture());
        assertEquals(NotificationType.VENUE_EDIT_DECISION, saved.getValue().getType());
        assertEquals(5L, saved.getValue().getVenueId());
        assertEquals(13L, saved.getValue().getVenueEditSuggestionId());
        assertEquals("member", saved.getValue().getUsername());
        assertFalse(saved.getValue().isRead());
        verify(events).publishEvent(any(VenueEditDecisionNotificationService.DecisionEmail.class));
        verifyNoInteractions(email);
    }

    @Test
    void optOutSuppressesBothDeliveryChannels() {
        service.decision(13, 5, "Pavilhão", "member", false, "Inválido");
        verifyNoInteractions(notifications, events, email);
    }

    @Test
    void inAppOptOutDoesNotBlockEmailIfUserKeptEmailEnabled() {
        when(preferences.allowsOptionalEmail("member", NotificationType.VENUE_EDIT_DECISION))
                .thenReturn(true);
        service.decision(13, 5, "Campo", "member", false, "Sem confirmação");
        verifyNoInteractions(notifications);
        verify(events).publishEvent(any(VenueEditDecisionNotificationService.DecisionEmail.class));
    }

    @Test
    void escapedReasonCannotInjectMarkupIntoEmail() {
        when(preferences.allowsOptionalEmail("member", NotificationType.VENUE_EDIT_DECISION))
                .thenReturn(true);
        var profile = new Profile();
        profile.setUsername("member");
        profile.setEmail("member@example.com");
        profile.setEnabled(true);
        when(profiles.findUserByUsernameIgnoreCase("member")).thenReturn(Optional.of(profile));
        service.sendOptionalEmail(new VenueEditDecisionNotificationService.DecisionEmail(
                "member", 5, "<Pavilhão>", false, "<script>alert('x')</script>"));
        var emailHtml = ArgumentCaptor.forClass(String.class);
        verify(email).send(eq("member@example.com"), contains("Jynlo"), emailHtml.capture());
        String html = emailHtml.getValue();
        assertFalse(html.contains("<script>"));
        assertTrue(html.contains("&lt;script&gt;"));
        assertTrue(html.contains("https://d24sports.com/menu/venues/5"));
    }

    @Test
    void emailPreferenceCheckedAgainAfterCommit() {
        service.sendOptionalEmail(new VenueEditDecisionNotificationService.DecisionEmail(
                "member", 5, "Campo", true, ""));
        verifyNoInteractions(email);
    }
}
