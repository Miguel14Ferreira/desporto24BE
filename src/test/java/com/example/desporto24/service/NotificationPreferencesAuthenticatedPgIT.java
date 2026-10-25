package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Locale;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Full HTTP -> signed JWT -> security filter -> controller -> JPA -> PostgreSQL 16.
 * Opt-in isolated fixture: no production database, Resend calls or real accounts.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=true",
        "spring.datasource.hikari.schema=desporto24",
        "spring.jpa.properties.hibernate.default_schema=desporto24",
        "springdoc.api-docs.enabled=false",
        "desporto.app.jwtSecret=isolated_jynlo_notification_jwt_contract_key_2026",
        "desporto.cors.allowed-origins=http://127.0.0.1:4200",
        "UI_APP_URL=http://127.0.0.1:4200",
        "VERIFY_EMAIL_HOST=http://127.0.0.1:8080",
        "AES_ENCRYPTION_KEY=ciKey1",
        "desporto.media.provider=local",
        "POSTGRES_SCHEMA=desporto24"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationPreferencesAuthenticatedPgIT {
    @TestConfiguration
    static class IsolatedExecutor {
        @Bean(name = "applicationTaskExecutor")
        @Primary
        TaskExecutor fixtureExecutor() {
            return new SyncTaskExecutor();
        }

        // No optional message can reach Resend from this isolated test context.
        @Bean(name = "fixtureOptionalEmailSender")
        @Primary
        EmailSender fixtureOptionalEmailSender() {
            return mock(EmailSender.class);
        }
    }

    @DynamicPropertySource
    static void localOnlyDatabase(DynamicPropertyRegistry registry) {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        if (!"jdbc:postgresql://127.0.0.1:5432/notification_auth_qa".equals(url)) {
            throw new IllegalStateException("Only local disposable notification_auth_qa is allowed");
        }
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> System.getenv("JYNLO_TEST_PG_USERNAME"));
        registry.add("spring.datasource.password", () -> System.getenv("JYNLO_TEST_PG_PASSWORD"));
    }

    @Value("${local.server.port}") int port;
    @Autowired ProfileRepository profiles;
    @Autowired NotificationsRepository notifications;
    @Autowired JWTokenProvider jwt;
    @Autowired NotificationPreferencesService preferenceService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired EmailSender optionalEmailSender;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private String anaToken;
    private String bobToken;
    private long anaChatId;

    @BeforeAll
    void createIsolatedAccounts() {
        Profile ana = profile("notif_jwt_ana");
        Profile bob = profile("notif_jwt_bob");
        anaToken = jwt.generateJwtToken(new ProfilePrincipal(ana));
        bobToken = jwt.generateJwtToken(new ProfilePrincipal(bob));
        anaChatId = notification("notif_jwt_ana", NotificationType.CHAT_MESSAGE).getId();
        notification("notif_jwt_ana", NotificationType.SESSION_INVITE);
        notification("notif_jwt_bob", NotificationType.CHAT_MESSAGE);
    }

    private Profile profile(String username) {
        Profile p = new Profile();
        p.setUsername(username);
        p.setFullName("Fictional Notification QA");
        p.setEmail(username + "@example.invalid");
        p.setEnabled(true);
        p.setNotLocked(true);
        p.setRole("ROLE_USER");
        p.setAuthorities(new String[]{"ROLE_USER"});
        return profiles.saveAndFlush(p);
    }

    private Notifications notification(String username, NotificationType type) {
        Notifications alert = new Notifications();
        alert.setUsername(username);
        alert.setType(type);
        alert.setSubject("QA synthetic notification");
        alert.setMessage("Isolated notification fixture");
        alert.setGreeting("");
        alert.setSignature("");
        alert.setCreatedAt(Instant.now().toString());
        alert.setRead(false);
        return notifications.saveAndFlush(alert);
    }

    private HttpResponse<String> send(String method, String path, String token, Object body)
            throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(25))
                .header("Accept", "application/json");
        if (token != null) request.header("Authorization", "Bearer " + token);
        if (body != null) {
            request.header("Content-Type", "application/json");
            request.method(method, HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
        } else {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode api(String method, String path, String token, Object body, int expectedStatus)
            throws Exception {
        var response = send(method, path, token, body);
        assertEquals(expectedStatus, response.statusCode(),
                method + " " + path + " -> " + response.statusCode() + " " + response.body());
        return response.body().isBlank() ? mapper.createObjectNode() : mapper.readTree(response.body());
    }

    @Test
    void committedCommunityInviteEmailsFollowPersistedOptOutWithoutCallingResend() {
        String username = "notif_jwt_invitee";
        String address = username + "@example.invalid";
        // Keep this fixture independent from the Ana/Bob JWT scenario.
        profile(username);
        reset(optionalEmailSender);

        // A committed invitation must not send if its recipient disabled this type.
        preferenceService.update(username, new NotificationPreferencesRequest(
                true, true, Set.of(), Set.of("COMMUNITY_INVITE")));
        publishCommittedInvite(991L, address.toUpperCase(Locale.ROOT));
        verifyNoInteractions(optionalEmailSender);

        // Re-enable and verify a single bilingual email, with the real committed
        // event listener querying persisted preferences by case-insensitive email.
        preferenceService.update(username, new NotificationPreferencesRequest(
                true, true, Set.of(), Set.of()));
        publishCommittedInvite(992L, address.toUpperCase(Locale.ROOT));
        verify(optionalEmailSender).send(eq(address.toUpperCase(Locale.ROOT)),
                contains("Jynlo"), argThat(html -> html.contains("view=mine&amp;invite=992") && html.contains("Community invitation")));
        verifyNoMoreInteractions(optionalEmailSender);

        // The global email switch is independent of in-app notifications.
        reset(optionalEmailSender);
        preferenceService.update(username, new NotificationPreferencesRequest(
                true, false, Set.of(), Set.of()));
        publishCommittedInvite(993L, address);
        verifyNoInteractions(optionalEmailSender);

        // Sender outage after commit does not fail or undo the invitation event.
        preferenceService.update(username, new NotificationPreferencesRequest(
                true, true, Set.of(), Set.of()));
        doThrow(new IllegalStateException("Fixture email provider unavailable"))
                .when(optionalEmailSender).send(anyString(), anyString(), anyString());
        assertDoesNotThrow(() -> publishCommittedInvite(994L, address));
        verify(optionalEmailSender).send(eq(address), anyString(), anyString());
    }

    private void publishCommittedInvite(Long id, String email) {
        // @TransactionalEventListener(AFTER_COMMIT) is exercised by a real DB
        // transaction rather than invoking the listener method directly.
        new TransactionTemplate(transactionManager).executeWithoutResult(status ->
                publisher.publishEvent(new CommunityInviteCreatedEvent(
                        id, email, "fixture_owner", "Fictional Sports Community")));
    }

    @Test
    void signedJwtControlsIdentityAndPersistedPreferencesFilterBothInboxAndUnreadBadge()
            throws Exception {
        String pref = "/api/notifications/me/preferences";
        String inbox = "/api/notifications/me";
        String badge = "/api/notifications/me/unread-count";

        // Authentication is exercised by the real server's Spring Security filter.
        api("GET", pref, null, null, 403);
        api("PUT", pref, null, Map.of("inAppEnabled", false), 403);
        api("GET", badge, "malformed.fake.jwt", null, 403);

        assertTrue(api("GET", pref, anaToken, null, 200).path("inAppEnabled").asBoolean());
        assertEquals(2, api("GET", badge, anaToken, null, 200).asInt());
        assertEquals(2, api("GET", inbox, anaToken, null, 200).size());
        assertEquals(1, api("GET", inbox, bobToken, null, 200).size());

        var chatMuted = Map.of(
                "inAppEnabled", true, "emailEnabled", true,
                "disabledInAppTypes", Set.of("CHAT_MESSAGE"),
                "disabledEmailTypes", Set.of("COMMUNITY_INVITE"));
        var saved = api("PUT", pref, anaToken, chatMuted, 200);
        assertTrue(saved.path("disabledInAppTypes").toString().contains("CHAT_MESSAGE"));
        assertTrue(saved.path("disabledEmailTypes").toString().contains("COMMUNITY_INVITE"));

        // The next real HTTP request uses a separate persistence context.
        var loaded = api("GET", pref, anaToken, null, 200);
        assertTrue(loaded.path("disabledInAppTypes").toString().contains("CHAT_MESSAGE"));
        assertEquals(1, api("GET", badge, anaToken, null, 200).asInt());
        var visible = api("GET", inbox, anaToken, null, 200);
        assertEquals(1, visible.size());
        assertEquals("SESSION_INVITE", visible.get(0).path("type").asText());

        // Neither GET nor PUT accepts a target username: the authenticated
        // JWT subject chooses whose settings and notifications are returned.
        assertTrue(api("GET", pref, bobToken, null, 200)
                .path("disabledInAppTypes").isEmpty());
        assertEquals(1, api("GET", badge, bobToken, null, 200).asInt());
        api("GET", "/api/notifications/notif_jwt_ana", bobToken, null, 403);
        api("PUT", "/api/notifications/" + anaChatId + "/read", bobToken, null, 403);

        var globallyMuted = Map.of(
                "inAppEnabled", false, "emailEnabled", false,
                "disabledInAppTypes", Set.of("CHAT_MESSAGE"),
                "disabledEmailTypes", Set.of("COMMUNITY_INVITE"));
        api("PUT", pref, anaToken, globallyMuted, 200);
        assertEquals(0, api("GET", badge, anaToken, null, 200).asInt());
        assertEquals(0, api("GET", inbox, anaToken, null, 200).size());
        assertEquals(1, api("GET", badge, bobToken, null, 200).asInt());

        // Opting back in restores historical unread records; marking a real
        // notification read only changes the authenticated owner's count.
        api("PUT", pref, anaToken, Map.of(
                "inAppEnabled", true, "emailEnabled", true,
                "disabledInAppTypes", Set.of(), "disabledEmailTypes", Set.of()), 200);
        assertEquals(2, api("GET", badge, anaToken, null, 200).asInt());
        api("PUT", "/api/notifications/" + anaChatId + "/read", anaToken, null, 204);
        assertEquals(1, api("GET", badge, anaToken, null, 200).asInt());
        assertEquals(1, api("GET", badge, bobToken, null, 200).asInt());
    }
}
