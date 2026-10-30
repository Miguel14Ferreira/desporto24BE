package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.NotificationPreferences;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationPreferencesRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationPreferencesServiceTest {
    @Mock NotificationPreferencesRepository preferences;
    @Mock ProfileRepository profiles;
    @InjectMocks NotificationPreferencesService service;

    @Test
    void missingPreferencesKeepExistingOptionalAlertsOnByDefault() {
        var defaults = service.get("ana");
        assertTrue(defaults.inAppEnabled());
        assertTrue(defaults.emailEnabled());
        assertTrue(defaults.disabledInAppTypes().isEmpty());
        assertTrue(defaults.disabledEmailTypes().isEmpty());
    }

    @Test
    void differentChannelsAndIndividualTypesRemainIndependent() {
        Profile profile = new Profile();
        profile.setUsername("ana");
        when(profiles.findUserByUsername("ana")).thenReturn(Optional.of(profile));
        when(preferences.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = service.update("ana", new NotificationPreferencesRequest(
                true, false, Set.of("CHAT_MESSAGE", "COMMUNITY_JOIN_REQUEST"),
                Set.of("COMMUNITY_INVITE")));
        assertTrue(response.inAppEnabled());
        assertFalse(response.emailEnabled());
        assertEquals(Set.of("CHAT_MESSAGE", "COMMUNITY_JOIN_REQUEST"),
                response.disabledInAppTypes());
        assertEquals(Set.of("COMMUNITY_INVITE"), response.disabledEmailTypes());

        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                true, false, "CHAT_MESSAGE,COMMUNITY_JOIN_REQUEST", "COMMUNITY_INVITE")));
        Notifications message = notification(NotificationType.CHAT_MESSAGE);
        assertFalse(service.allowsInApp("ana", message));
        assertFalse(service.allowsOptionalEmail("ana", NotificationType.COMMUNITY_INVITE));
        assertTrue(service.allowsInApp("ana", notification(NotificationType.SESSION_CANCELLED)));
    }

    @Test
    void legacyFriendRequestsHaveIndependentOptOut() {
        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                true, true, "FRIEND_REQUEST", "")));
        Notifications old = notification(null);
        old.setFriendRequest(true);
        assertEquals("FRIEND_REQUEST", NotificationPreferencesService.category(old));
        assertFalse(service.allowsInApp("ana", old));
        assertTrue(service.allowsInApp("ana", notification(NotificationType.SESSION_INVITE)));
    }

    @Test
    void accountSecurityIsMandatoryButOldProfileUpdatesRemainOptional() {
        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                false, true, "PROFILE_UPDATE", "")));
        Notifications routine = notification(null);
        routine.setUpdateProfile(true);
        routine.setSubject("Perfil atualizado");
        Notifications email = notification(null);
        email.setUpdateProfile(true);
        email.setSubject("Alteração de email");
        Notifications password = notification(null);
        password.setSubject("Palavra-passe alterada");

        assertFalse(NotificationPreferencesService.isAccountSecurityAlert(routine));
        assertFalse(service.allowsInApp("ana", routine));
        assertTrue(NotificationPreferencesService.isAccountSecurityAlert(email));
        assertTrue(service.allowsInApp("ana", email));
        assertTrue(service.allowsInApp("ana", password));
    }

    @Test
    void globalOptOutOverridesIndividualChoices() {
        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                false, true, "", "")));
        assertFalse(service.allowsInApp("ana", notification(NotificationType.SESSION_INVITE)));
    }

    @Test
    void rejectsUnknownKindsAndNullPreferenceFieldsWithoutWriting() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update("ana", new NotificationPreferencesRequest(
                        true, true, Set.of("NOT_A_REAL_TYPE"), Set.of())));
        assertThrows(IllegalArgumentException.class,
                () -> service.update("ana", new NotificationPreferencesRequest(
                        true, true, null, Set.of())));
        verify(preferences, never()).save(any());
    }

    @Test
    void optionalFriendRequestEmailRespectsTypeAndGlobalOptOut() {
        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                true, true, "", "FRIEND_REQUEST")));
        assertFalse(service.allowsOptionalEmailCategory("ana", NotificationPreferencesService.FRIEND_REQUEST));
        assertTrue(service.allowsOptionalEmail("ana", NotificationType.COMMUNITY_INVITE));

        when(preferences.findById("ana")).thenReturn(Optional.of(saved(
                true, false, "", "")));
        assertFalse(service.allowsOptionalEmailCategory("ana", NotificationPreferencesService.FRIEND_REQUEST));
    }

    @Test
    void registeredRecipientsCannotBypassInviteEmailOptOutWithDifferentCasing() {
        Profile recipient = new Profile();
        recipient.setUsername("ana");
        when(profiles.findUserByEmailIgnoreCase("ANA@EXAMPLE.TEST"))
                .thenReturn(Optional.of(recipient));
        when(preferences.findById("ana"))
                .thenReturn(Optional.of(saved(true, true, "", "COMMUNITY_INVITE")));

        assertFalse(service.allowsOptionalEmailAddress("ANA@EXAMPLE.TEST",
                NotificationType.COMMUNITY_INVITE));
    }

    @Test
    void externalRecipientsWithoutAnAccountHaveNoStoredPreferences() {
        when(profiles.findUserByEmailIgnoreCase("guest@external.example")).thenReturn(Optional.empty());
        assertTrue(service.allowsOptionalEmailAddress("guest@external.example",
                NotificationType.COMMUNITY_INVITE));
    }

    private NotificationPreferences saved(boolean inApp, boolean email,
                                           String inAppTypes, String emailTypes) {
        var result = new NotificationPreferences();
        result.setUsername("ana");
        result.setInAppEnabled(inApp);
        result.setEmailEnabled(email);
        result.setDisabledInAppTypes(inAppTypes);
        result.setDisabledEmailTypes(emailTypes);
        return result;
    }

    private Notifications notification(NotificationType type) {
        var result = new Notifications();
        result.setType(type);
        result.setUsername("ana");
        return result;
    }
}
