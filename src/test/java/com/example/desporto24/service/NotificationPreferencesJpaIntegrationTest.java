package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.repository.NotificationPreferencesRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import jakarta.persistence.EntityManager;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proves that the same preferences used to suppress optional notifications
 * survive a real JPA flush/clear and remain account-specific. Fictional users
 * and an isolated in-memory DB; never touches the live Render database.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(NotificationPreferencesService.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:notification-settings-qa;MODE=LEGACY;NON_KEYWORDS=SESSION;INIT=CREATE SCHEMA IF NOT EXISTS desporto24;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.hikari.schema=PUBLIC",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
class NotificationPreferencesJpaIntegrationTest {
    @Autowired private NotificationPreferencesService service;
    @Autowired private NotificationPreferencesRepository preferences;
    @Autowired private ProfileRepository profiles;
    @Autowired private EntityManager entityManager;

    @Test
    void savesAndReloadsEachAccountIndependentlyAndActuallyBlocksOptionalChannels() {
        profiles.saveAndFlush(account("notif-qa-ana", "notif-qa-ana@example.test"));
        profiles.saveAndFlush(account("notif-qa-bob", "notif-qa-bob@example.test"));

        assertTrue(service.get("notif-qa-ana").inAppEnabled());
        assertTrue(service.get("notif-qa-bob").emailEnabled());

        service.update("notif-qa-ana", new NotificationPreferencesRequest(
                true, true, Set.of("CHAT_MESSAGE", "VENUE_EDIT_DECISION"),
                Set.of("FRIEND_REQUEST", "COMMUNITY_INVITE")));
        preferences.flush();
        entityManager.clear();

        var loaded = service.get("notif-qa-ana");
        assertEquals(Set.of("CHAT_MESSAGE", "VENUE_EDIT_DECISION"), loaded.disabledInAppTypes());
        assertEquals(Set.of("FRIEND_REQUEST", "COMMUNITY_INVITE"), loaded.disabledEmailTypes());
        assertFalse(service.allowsInApp("notif-qa-ana", NotificationType.CHAT_MESSAGE));
        assertTrue(service.allowsInApp("notif-qa-ana", NotificationType.SESSION_INVITE));
        assertFalse(service.allowsOptionalEmailCategory("notif-qa-ana",
                NotificationPreferencesService.FRIEND_REQUEST));
        assertFalse(service.allowsOptionalEmail("notif-qa-ana", NotificationType.COMMUNITY_INVITE));
        assertTrue(service.allowsOptionalEmail("notif-qa-ana", NotificationType.SESSION_INVITE));

        // A different account is not affected by Ana's selections.
        assertTrue(service.allowsInApp("notif-qa-bob", NotificationType.CHAT_MESSAGE));
        assertTrue(service.allowsOptionalEmail("notif-qa-bob", NotificationType.COMMUNITY_INVITE));

        // Change the saved global settings and confirm a fresh DB read still respects them.
        service.update("notif-qa-ana", new NotificationPreferencesRequest(
                false, false, Set.of("CHAT_MESSAGE"), Set.of("FRIEND_REQUEST")));
        preferences.flush();
        entityManager.clear();

        assertFalse(service.get("notif-qa-ana").inAppEnabled());
        assertFalse(service.get("notif-qa-ana").emailEnabled());
        assertFalse(service.allowsInApp("notif-qa-ana", NotificationType.SESSION_INVITE));
        assertFalse(service.allowsOptionalEmail("notif-qa-ana", NotificationType.SESSION_INVITE));
        assertTrue(service.get("notif-qa-bob").inAppEnabled());
        assertTrue(service.get("notif-qa-bob").emailEnabled());
    }

    private static Profile account(String username, String email) {
        Profile profile = new Profile();
        profile.setUserId(username + "-id");
        profile.setUsername(username);
        profile.setEmail(email);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        return profile;
    }
}
