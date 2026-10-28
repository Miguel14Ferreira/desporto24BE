package com.example.desporto24.service;

import com.example.desporto24.controllers.NotificationController;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.NotificationPreferencesRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.impl.NotificationServiceImpl;
import com.example.desporto24.service.interfaces.ProfileService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Authenticated-principal MVC contract over real JPA repositories and isolated H2.
 * Tests the HTTP binding and persistence, not production JWT filters, accounts or email.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(NotificationPreferencesService.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:notification-preferences-http-qa;MODE=LEGACY;NON_KEYWORDS=SESSION;INIT=CREATE SCHEMA IF NOT EXISTS desporto24;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.hikari.schema=PUBLIC",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
class NotificationPreferencesAuthenticatedHttpJpaTest {
    @Autowired private NotificationPreferencesService preferencesService;
    @Autowired private NotificationPreferencesRepository preferences;
    @Autowired private NotificationsRepository notifications;
    @Autowired private ProfileRepository profiles;
    @Autowired private EntityManager entityManager;

    @Test
    void authenticatedPreferencesImmediatelyFilterInboxAndBadgeAcrossReloadsAndAccounts()
            throws Exception {
        profiles.saveAndFlush(account("notif-http-ana", "notif-http-ana@example.test"));
        profiles.saveAndFlush(account("notif-http-bob", "notif-http-bob@example.test"));

        var anaChat = notifications.saveAndFlush(alert("notif-http-ana", NotificationType.CHAT_MESSAGE));
        notifications.saveAndFlush(alert("notif-http-ana", NotificationType.SESSION_INVITE));
        notifications.saveAndFlush(alert("notif-http-bob", NotificationType.CHAT_MESSAGE));

        MockMvc mvc = mvc();
        var ana = principal("notif-http-ana");
        var bob = principal("notif-http-bob");

        mvc.perform(get("/api/notifications/me/preferences").principal(ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inAppEnabled").value(true))
                .andExpect(jsonPath("$.emailEnabled").value(true));
        mvc.perform(get("/api/notifications/me/unread-count").principal(ana))
                .andExpect(status().isOk()).andExpect(content().string("2"));
        mvc.perform(get("/api/notifications/me").principal(ana))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

        // Disabling in-app chat must not disable email or unrelated session alerts.
        mvc.perform(put("/api/notifications/me/preferences").principal(ana)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inAppEnabled":true,"emailEnabled":true,
                         "disabledInAppTypes":["CHAT_MESSAGE"],
                         "disabledEmailTypes":["COMMUNITY_INVITE"]}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabledInAppTypes[0]").value("CHAT_MESSAGE"))
                .andExpect(jsonPath("$.disabledEmailTypes[0]").value("COMMUNITY_INVITE"));

        preferences.flush();
        notifications.flush();
        entityManager.clear();

        mvc.perform(get("/api/notifications/me/preferences").principal(ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabledInAppTypes[0]").value("CHAT_MESSAGE"));
        mvc.perform(get("/api/notifications/me").principal(ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("SESSION_INVITE"));
        mvc.perform(get("/api/notifications/me/unread-count").principal(ana))
                .andExpect(status().isOk()).andExpect(content().string("1"));

        // Opt-outs belong to the authenticated account, not the requester's input.
        mvc.perform(get("/api/notifications/me/preferences").principal(bob))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabledInAppTypes.length()").value(0));
        mvc.perform(get("/api/notifications/me/unread-count").principal(bob))
                .andExpect(status().isOk()).andExpect(content().string("1"));

        // Muting every in-app category hides the historical unread alerts too.
        mvc.perform(put("/api/notifications/me/preferences").principal(ana)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inAppEnabled":false,"emailEnabled":false,
                         "disabledInAppTypes":["CHAT_MESSAGE"],
                         "disabledEmailTypes":["COMMUNITY_INVITE"]}
                        """))
                .andExpect(status().isOk());
        mvc.perform(get("/api/notifications/me").principal(ana))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get("/api/notifications/me/unread-count").principal(ana))
                .andExpect(status().isOk()).andExpect(content().string("0"));

        // Re-enable, mark a real row read and verify the remaining count.
        mvc.perform(put("/api/notifications/me/preferences").principal(ana)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inAppEnabled":true,"emailEnabled":true,
                         "disabledInAppTypes":[],"disabledEmailTypes":[]}
                        """))
                .andExpect(status().isOk());
        assertTrue(preferencesService.allowsInApp("notif-http-ana", NotificationType.CHAT_MESSAGE));
        mvc.perform(put("/api/notifications/" + anaChat.getId() + "/read").principal(ana))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/notifications/me/unread-count").principal(ana))
                .andExpect(status().isOk()).andExpect(content().string("1"));
        mvc.perform(get("/api/notifications/me/unread-count").principal(bob))
                .andExpect(status().isOk()).andExpect(content().string("1"));
    }

    private MockMvc mvc() {
        NotificationServiceImpl service = new NotificationServiceImpl(
                notifications,
                mock(EmailService.class),
                mock(ProfileService.class),
                mock(ConfirmationTokenService.class),
                mock(FriendRequestService.class),
                mock(FriendRepository.class),
                profiles,
                mock(UserBlockService.class),
                mock(PostGameReminderService.class));
        ReflectionTestUtils.setField(service, "preferenceService", preferencesService);
        return MockMvcBuilders.standaloneSetup(
                new NotificationController(service, preferencesService,
                    mock(ConversationNotificationMuteService.class), mock(InboxSummaryService.class),
                    mock(PrivateGroupService.class))).build();
    }

    private UsernamePasswordAuthenticationToken principal(String username) {
        return new UsernamePasswordAuthenticationToken(username, null);
    }

    private Profile account(String username, String email) {
        Profile profile = new Profile();
        profile.setUserId(username + "-id");
        profile.setUsername(username);
        profile.setEmail(email);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        return profile;
    }

    private Notifications alert(String username, NotificationType type) {
        Notifications notification = new Notifications();
        notification.setUsername(username);
        notification.setType(type);
        notification.setSubject("QA notification");
        notification.setMessage("Isolated fictional QA");
        notification.setGreeting("");
        notification.setSignature("");
        notification.setCreatedAt("2026-10-25T17:00:00Z");
        notification.setRead(false);
        return notification;
    }
}
