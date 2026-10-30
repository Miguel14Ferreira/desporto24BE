package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueEditSuggestion;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.venue.VenueEditSuggestionRepository;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Real Spring HTTP/JWT/JPA/PG16 contract, opt-in, loopback only, no external notifications. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=true",
        "spring.datasource.hikari.schema=desporto24",
        "spring.jpa.properties.hibernate.default_schema=desporto24",
        "springdoc.api-docs.enabled=false",
        "desporto.app.jwtSecret=isolated_jynlo_venue_moderation_contract_key_2026",
        "desporto.cors.allowed-origins=http://127.0.0.1:4200",
        "UI_APP_URL=http://127.0.0.1:4200",
        "VERIFY_EMAIL_HOST=http://127.0.0.1:8080",
        "AES_ENCRYPTION_KEY=ciKey1",
        "desporto.media.provider=local",
        "POSTGRES_SCHEMA=desporto24"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VenueModerationAuthenticatedPgIT {
    private static final String BASE = "/api/admin/venue-edit-suggestions";
    private static final String SOURCE = "https://example.org/qa/municipal-pool";

    @TestConfiguration
    static class Fixtures {
        @Bean(name = "applicationTaskExecutor")
        @Primary
        TaskExecutor executor() { return new SyncTaskExecutor(); }

        // Real service and database, but absolutely no notification/email sends.
        @Bean(name = "isolatedVenueDecisionNotifications")
        @Primary
        VenueEditDecisionNotificationService suppressedNotifications() {
            return mock(VenueEditDecisionNotificationService.class);
        }
    }

    @DynamicPropertySource
    static void disposableDatabase(DynamicPropertyRegistry registry) {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        if (!"jdbc:postgresql://127.0.0.1:5432/venue_moderation_qa".equals(url))
            throw new IllegalStateException("Only disposable loopback venue_moderation_qa allowed");
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> System.getenv("JYNLO_TEST_PG_USERNAME"));
        registry.add("spring.datasource.password", () -> System.getenv("JYNLO_TEST_PG_PASSWORD"));
    }

    @Value("${local.server.port}") int port;
    @Autowired ProfileRepository profiles;
    @Autowired VenueRepository venues;
    @Autowired VenueEditSuggestionRepository submissions;
    @Autowired VenueEditDecisionNotificationService suppressedNotifications;
    @Autowired JWTokenProvider jwt;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private String memberToken;
    private String secondMemberToken;
    private String secondAdminToken;
    private long venueA, venueB;

    @BeforeAll
    void setup() {
        adminToken = jwt.generateJwtToken(new ProfilePrincipal(profile("venueqa_moderator", "ROLE_ADMIN")));
        memberToken = jwt.generateJwtToken(new ProfilePrincipal(profile("venueqa_member", "ROLE_USER")));
        secondMemberToken = jwt.generateJwtToken(new ProfilePrincipal(profile("venueqa_other", "ROLE_USER")));
        secondAdminToken = jwt.generateJwtToken(new ProfilePrincipal(profile("venueqa_moderator2", "ROLE_ADMIN")));
        venueA = venue("Recinto QA Alfa").getId();
        venueB = venue("Recinto QA Beta").getId();
    }

    private Profile profile(String username, String role) {
        Profile p = new Profile();
        p.setUsername(username);
        p.setFullName("Only Synthetic QA");
        p.setEmail(username + "@example.invalid");
        p.setRole(role);
        p.setAuthorities(new String[]{role});
        p.setEnabled(true);
        p.setNotLocked(true);
        p.setMFA(false);
        return profiles.saveAndFlush(p);
    }

    private Venue venue(String name) {
        return venues.saveAndFlush(Venue.builder()
                .name(name).city("Localidade QA").district("Lisboa")
                .address("Rua de Testes — sem instalação real")
                .sports("Futebol").createdAt(Instant.now()).build());
    }

    private JsonNode api(String method, String path, String token, Object body, int expected)
            throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(25)).header("Accept", "application/json");
        if (token != null) builder.header("Authorization", "Bearer " + token);
        if (body == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
        else builder.header("Content-Type", "application/json").method(method,
                HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
        var reply = http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(expected, reply.statusCode(),
                method + " " + path + " status=" + reply.statusCode() + " body=" + reply.body());
        return reply.body().isBlank() ? mapper.createObjectNode() : mapper.readTree(reply.body());
    }


    /**
     * Start two HTTP requests against the same fixture row in parallel.
     * Both requests use genuinely signed JWTs and independent DB transactions.
     */
    private List<Integer> parallelSubmissions(long venueId, String firstJwt, String secondJwt)
            throws Exception {
        String url = "http://127.0.0.1:" + port +
                "/api/venues/" + venueId + "/edit-suggestions";
        String body = mapper.writeValueAsString(Map.of(
                "changes", Map.of("contact", "220000123"),
                "reason", "Contacto totalmente fictício num teste concorrente",
                "sourceUrl", SOURCE));
        var executor = Executors.newFixedThreadPool(2);
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (String token : List.of(firstJwt, secondJwt)) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    if (!start.await(12, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Timed out before parallel HTTP requests");
                    }
                    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                            .timeout(Duration.ofSeconds(25))
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(body)).build();
                    return http.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
                }));
            }
            assertTrue(ready.await(12, TimeUnit.SECONDS));
            start.countDown();
            List<Integer> statuses = new ArrayList<>();
            for (Future<Integer> future : futures) {
                statuses.add(future.get(40, TimeUnit.SECONDS));
            }
            statuses.sort(Integer::compareTo);
            return statuses;
        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }

    @Test
    void concurrentSameUserRequestsCreateOnlyOnePendingProposal() throws Exception {
        long fixture = venue("Recinto QA Concorrência Mesmo Jogador").getId();
        assertEquals(List.of(202, 409),
                parallelSubmissions(fixture, memberToken, memberToken));
        long rows = submissions.findAll().stream()
                .filter(row -> row.getVenue().getId().equals(fixture))
                .count();
        assertEquals(1L, rows);
        assertEquals(VenueEditSuggestion.Status.PENDING,
                submissions.findFirstByVenue_IdAndSubmittedByUsernameIgnoreCaseOrderBySubmittedAtDesc(
                        fixture, "venueqa_member").orElseThrow().getStatus());
        assertNull(venues.findById(fixture).orElseThrow().getContact());
    }

    @Test
    void concurrentDifferentUsersMayBothSuggestSameVenueWithoutPublishing() throws Exception {
        long fixture = venue("Recinto QA Concorrência Dois Jogadores").getId();
        assertEquals(List.of(202, 202),
                parallelSubmissions(fixture, memberToken, secondMemberToken));
        long rows = submissions.findAll().stream()
                .filter(row -> row.getVenue().getId().equals(fixture))
                .count();
        assertEquals(2L, rows);
        assertNull(venues.findById(fixture).orElseThrow().getContact());
    }


    /**
     * Competing authenticated moderator decisions must serialize on the
     * proposal row. Exactly one decision and one notification may persist.
     */
    @Test
    void concurrentApproveAndRejectNeverPublishConflictingDecisions() throws Exception {
        long fixture = venue("Recinto QA Dupla Moderação").getId();
        long id = api("POST", "/api/venues/" + fixture + "/edit-suggestions",
                memberToken, Map.of(
                        "changes", Map.of("contact", "220000777"),
                        "reason", "Fonte fictícia da proposta de concorrência administrativa",
                        "sourceUrl", SOURCE), 202).path("id").asLong();
        assertTrue(id > 0);
        var executor = Executors.newFixedThreadPool(2);
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        clearInvocations(suppressedNotifications);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (String action : List.of("approve", "reject")) {
                final String token = "approve".equals(action) ? adminToken : secondAdminToken;
                final Map<String, ?> payload = "approve".equals(action)
                        ? Map.of("sourceReviewed", true, "note", "Fonte QA revista pelo moderador")
                        : Map.of("note", "Fonte QA rejeitada pelo segundo moderador");
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    if (!start.await(12, TimeUnit.SECONDS))
                        throw new IllegalStateException("Moderation race did not start");
                    HttpRequest req = HttpRequest.newBuilder(URI.create(
                            "http://127.0.0.1:" + port + BASE + "/" + id + "/" + action))
                            .timeout(Duration.ofSeconds(25))
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    mapper.writeValueAsString(payload))).build();
                    return http.send(req, HttpResponse.BodyHandlers.ofString()).statusCode();
                }));
            }
            assertTrue(ready.await(12, TimeUnit.SECONDS));
            start.countDown();
            List<Integer> statuses = new ArrayList<>();
            for (Future<Integer> future : futures)
                statuses.add(future.get(40, TimeUnit.SECONDS));
            statuses.sort(Integer::compareTo);
            assertEquals(List.of(200, 409), statuses);
        } finally {
            start.countDown();
            executor.shutdownNow();
        }

        var stored = submissions.findById(id).orElseThrow();
        assertNotEquals(VenueEditSuggestion.Status.PENDING, stored.getStatus());
        assertNotNull(stored.getReviewedAt());
        assertEquals(SOURCE, stored.getSourceUrl());
        assertEquals(1L, submissions.findAll().stream()
                .filter(p -> p.getId().equals(id)).count());

        String liveContact = venues.findById(fixture).orElseThrow().getContact();
        if (stored.getStatus() == VenueEditSuggestion.Status.APPROVED) {
            assertEquals("220000777", liveContact);
            assertEquals("venueqa_moderator", stored.getReviewedByUsername());
            assertEquals(stored.getReviewedAt(), stored.getSourceReviewConfirmedAt());
            verify(suppressedNotifications, times(1)).decision(
                    eq(id), eq(fixture), anyString(), eq("venueqa_member"),
                    eq(true), anyString());
        } else {
            assertEquals(VenueEditSuggestion.Status.REJECTED, stored.getStatus());
            assertNull(liveContact);
            assertEquals("venueqa_moderator2", stored.getReviewedByUsername());
            assertNull(stored.getSourceReviewConfirmedAt());
            verify(suppressedNotifications, times(1)).decision(
                    eq(id), eq(fixture), anyString(), eq("venueqa_member"),
                    eq(false), anyString());
        }

        for (JsonNode item : api("GET", BASE + "?page=0", adminToken, null, 200)
                .path("suggestions"))
            assertNotEquals(id, item.path("id").asLong());
        boolean inHistory = false;
        for (JsonNode item : api("GET", BASE + "/history?page=0", adminToken, null, 200)
                .path("suggestions")) {
            if (item.path("id").asLong() == id) {
                inHistory = true;
                assertEquals(stored.getStatus().name(), item.path("status").asText());
            }
        }
        assertTrue(inHistory);
    }

    @Test
    void signedJwtRequiresExplicitModerationAndPersistsSourceReview() throws Exception {
        String submit = "/api/venues/" + venueA + "/edit-suggestions";
        api("GET", BASE + "/history?page=0", null, null, 403);
        api("GET", BASE + "/history?page=0", memberToken, null, 403);
        api("GET", BASE + "?page=0", memberToken, null, 403);

        var proposed = api("POST", submit, memberToken, Map.of(
                "changes", Map.of("contact", "220000001"),
                "reason", "Telefone em fonte totalmente fictícia", "sourceUrl", SOURCE), 202);
        long id = proposed.path("id").asLong();
        assertTrue(id > 0);
        assertEquals("PENDING", proposed.path("status").asText());
        assertTrue(proposed.path("sourceReviewConfirmedAt").isNull());
        assertNull(venues.findById(venueA).orElseThrow().getContact());
        // Each concurrent fixture owns its own pending row; inspect this
        // proposal specifically instead of asserting a global queue size.
        boolean visiblePending = false;
        for (JsonNode item : api("GET", BASE + "?page=0", adminToken, null, 200)
                .path("suggestions")) {
            if (item.path("id").asLong() == id) {
                visiblePending = true;
                assertEquals("PENDING", item.path("status").asText());
            }
        }
        assertTrue(visiblePending);
        // The other test may already have created a REJECTED legacy fixture:
        // history must exclude THIS still-pending record, not be globally empty.
        for (JsonNode item : api("GET", BASE + "/history?page=0", adminToken, null, 200)
                .path("suggestions")) {
            assertNotEquals(id, item.path("id").asLong());
            assertNotEquals("PENDING", item.path("status").asText());
        }

        String approve = BASE + "/" + id + "/approve";
        api("POST", approve, memberToken, Map.of("sourceReviewed", true, "note", "QA"), 403);
        api("POST", approve, adminToken, Map.of("note", "QA"), 400);
        api("POST", approve, adminToken, Map.of("sourceReviewed", false, "note", "QA"), 400);
        assertEquals(VenueEditSuggestion.Status.PENDING,
                submissions.findById(id).orElseThrow().getStatus());
        assertNull(venues.findById(venueA).orElseThrow().getContact());

        var approved = api("POST", approve, adminToken,
                Map.of("sourceReviewed", true, "note", "Fonte QA fictícia verificada"), 200);
        assertEquals("APPROVED", approved.path("status").asText());
        assertEquals("venueqa_moderator", approved.path("reviewedByUsername").asText());
        assertEquals(SOURCE, approved.path("sourceUrl").asText());
        assertFalse(approved.path("sourceReviewConfirmedAt").isNull());
        assertEquals(approved.path("reviewedAt").asText(),
                approved.path("sourceReviewConfirmedAt").asText());
        assertEquals("220000001", venues.findById(venueA).orElseThrow().getContact());
        assertEquals(id, api("GET", BASE + "/history?page=0", adminToken, null, 200)
                .path("suggestions").get(0).path("id").asLong());
        for (JsonNode item : api("GET", BASE + "?page=0", adminToken, null, 200)
                .path("suggestions")) {
            assertNotEquals(id, item.path("id").asLong(),
                    "Approved record must leave the pending queue");
        }
        api("POST", approve, adminToken, Map.of("sourceReviewed", true), 409);
        api("GET", BASE + "/history?page=-1", adminToken, null, 400);
        api("GET", BASE + "/history?page=1001", adminToken, null, 400);
        verify(suppressedNotifications).decision(eq(id), eq(venueA),
                anyString(), eq("venueqa_member"), eq(true), anyString());
    }

    @Test
    void legacyNoSourceCannotBeApprovedButCanBeRejected() throws Exception {
        String submit = "/api/venues/" + venueB + "/edit-suggestions";
        api("POST", submit, memberToken, Map.of(
                "changes", Map.of("contact", "220000002"),
                "reason", "Fonte ainda não foi comprovada"), 400);
        assertNull(venues.findById(venueB).orElseThrow().getContact());

        // Legacy fixture created in disposable PostgreSQL, never a real venue.
        var record = submissions.saveAndFlush(VenueEditSuggestion.builder()
                .venue(venues.findById(venueB).orElseThrow())
                .status(VenueEditSuggestion.Status.PENDING)
                .beforeJson("{\"contact\":null}")
                .changesJson("{\"contact\":\"220000002\"}")
                .reason("Contacto anterior ao campo de fonte")
                .submittedByUsername("venueqa_member")
                .submittedAt(Instant.now()).build());
        long id = record.getId();
        api("POST", BASE + "/" + id + "/approve", adminToken,
                Map.of("sourceReviewed", true, "note", "Sem fonte"), 400);
        var rejected = api("POST", BASE + "/" + id + "/reject", adminToken,
                Map.of("note", "Contacto sem fonte pública verificável"), 200);
        assertEquals("REJECTED", rejected.path("status").asText());
        assertTrue(rejected.path("sourceReviewConfirmedAt").isNull());
        assertNull(venues.findById(venueB).orElseThrow().getContact());
        assertEquals(VenueEditSuggestion.Status.REJECTED,
                submissions.findById(id).orElseThrow().getStatus());
    }
}
