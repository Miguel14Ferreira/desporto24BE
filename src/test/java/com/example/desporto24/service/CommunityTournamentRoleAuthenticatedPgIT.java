package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Actual signed JWT -> Spring Security -> HTTP -> real services -> real JPA ->
 * disposable PostgreSQL 16. Fictional users only, no Render or production.
 * Opt-in *PgIT excluded from default Maven unit test suite.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=true",
        "spring.datasource.hikari.schema=desporto24",
        "spring.jpa.properties.hibernate.default_schema=desporto24",
        "springdoc.api-docs.enabled=false",
        "desporto.app.jwtSecret=isolated_jynlo_community_roles_pg16_auth_contract_key_2026",
        "desporto.cors.allowed-origins=http://127.0.0.1:4200",
        "UI_APP_URL=http://127.0.0.1:4200",
        "VERIFY_EMAIL_HOST=http://127.0.0.1:8080",
        "AES_ENCRYPTION_KEY=ciKey1",
        "desporto.media.provider=local",
        "POSTGRES_SCHEMA=desporto24"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityTournamentRoleAuthenticatedPgIT {
    @TestConfiguration
    static class IsolatedDispatch {
        @Bean(name = "applicationTaskExecutor")
        @Primary
        TaskExecutor synchronousFixtureExecutor() { return new SyncTaskExecutor(); }

        @Bean(name = "fixtureNoExternalEmail")
        @Primary
        EmailSender stubOutboundEmail() { return mock(EmailSender.class); }
    }

    @DynamicPropertySource
    static void onlyDisposableLoopbackDatabase(DynamicPropertyRegistry registry) {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        if (!"jdbc:postgresql://127.0.0.1:5432/community_role_auth_qa".equals(url))
            throw new IllegalStateException("Only local disposable community_role_auth_qa is allowed");
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> System.getenv("JYNLO_TEST_PG_USERNAME"));
        registry.add("spring.datasource.password", () -> System.getenv("JYNLO_TEST_PG_PASSWORD"));
    }

    @Value("${local.server.port}") int port;
    @Autowired ProfileRepository profiles;
    @Autowired CommunityRepository communities;
    @Autowired CommunityMemberRepository members;
    @Autowired CommunityTournamentRepository tournaments;
    @Autowired CommunityTournamentParticipantRepository participants;
    @Autowired CommunityTournamentMatchRepository matches;
    @Autowired JWTokenProvider jwt;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private String ownerJwt, adminJwt, memberJwt, outsiderJwt;
    private Long groupId, privateGroupId, draftId, activeId, matchId;

    @BeforeAll
    void createDisposableRoleFixtures() {
        var owner = profile("isolated_pg_owner");
        var admin = profile("isolated_pg_admin");
        var member = profile("isolated_pg_member");
        var outsider = profile("isolated_pg_outsider");
        ownerJwt = token(owner);
        adminJwt = token(admin);
        memberJwt = token(member);
        outsiderJwt = token(outsider);

        var shared = community("Synthetic shared private league", owner);
        var exclusive = community("Synthetic owner-only league", owner);
        groupId = shared.getId();
        privateGroupId = exclusive.getId();
        addMember(shared, owner, CommunityRole.OWNER);
        addMember(shared, admin, CommunityRole.ADMIN);
        addMember(shared, member, CommunityRole.MEMBER);
        addMember(exclusive, owner, CommunityRole.OWNER);

        var draft = tournament(shared, "Draft QA", TournamentStatus.DRAFT);
        var active = tournament(shared, "Active QA", TournamentStatus.IN_PROGRESS);
        tournament(exclusive, "Owner-only QA tournament", TournamentStatus.DRAFT);
        draftId = draft.getId();
        activeId = active.getId();
        enrolled(draft, owner);
        enrolled(draft, member);
        var home = enrolled(active, owner);
        var away = enrolled(active, member);
        matchId = matches.saveAndFlush(CommunityTournamentMatch.builder()
                .tournament(active).homeParticipant(home).awayParticipant(away)
                .roundNumber(1).build()).getId();
    }

    private Profile profile(String username) {
        var p = new Profile();
        p.setUsername(username);
        p.setFullName("Fictional QA");
        p.setEmail(username + "@example.invalid");
        p.setEnabled(true);
        p.setNotLocked(true);
        p.setRole("ROLE_USER");
        p.setAuthorities(new String[]{"ROLE_USER"});
        return profiles.saveAndFlush(p);
    }

    private String token(Profile p) {
        return jwt.generateJwtToken(new ProfilePrincipal(p));
    }

    private Community community(String name, Profile owner) {
        return communities.saveAndFlush(Community.builder().name(name)
                .owner(owner).visibility(CommunityVisibility.PRIVATE)
                .rankingsEnabled(true).createdAt(Instant.now()).build());
    }

    private void addMember(Community c, Profile p, CommunityRole role) {
        members.saveAndFlush(CommunityMember.builder().community(c)
                .profile(p).role(role).joinedAt(Instant.now()).build());
    }

    private CommunityTournament tournament(Community c, String name, TournamentStatus status) {
        return tournaments.saveAndFlush(CommunityTournament.builder()
                .community(c).name(name).sport("Futebol").status(status)
                .createdAt(Instant.now()).build());
    }

    private CommunityTournamentParticipant enrolled(CommunityTournament t, Profile p) {
        return participants.saveAndFlush(CommunityTournamentParticipant.builder()
                .tournament(t).profile(p).joinedAt(Instant.now()).build());
    }

    private JsonNode api(String method, String path, String token, Object body, int expected)
            throws Exception {
        assertTrue(path.startsWith("/api/communities/"), "Isolated QA URL scope");
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(25)).header("Accept", "application/json");
        if (token != null) request.header("Authorization", "Bearer " + token);
        if (body == null) request.method(method, HttpRequest.BodyPublishers.noBody());
        else request.header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
        var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(expected, response.statusCode(), method + " " + path + " returned " + response.statusCode());
        return response.body().isBlank() ? mapper.createObjectNode() : mapper.readTree(response.body());
    }

    @Test
    void realSignedJwtHttpAndPostgres16EnforceRoleMatrixWithoutAnyProductionWrites()
            throws Exception {
        String shared = "/api/communities/" + groupId;
        String exclusive = "/api/communities/" + privateGroupId;
        String active = shared + "/tournaments/" + activeId;
        String draft = shared + "/tournaments/" + draftId;
        String result = active + "/matches/" + matchId + "/result";

        api("GET", shared, null, null, 403);
        api("GET", shared, outsiderJwt, null, 403);
        api("GET", exclusive, memberJwt, null, 403);
        api("GET", active, outsiderJwt, null, 403);
        api("GET", shared + "/tournaments", outsiderJwt, null, 403);

        assertEquals("OWNER", api("GET", shared, ownerJwt, null, 200).path("myRole").asText());
        assertEquals("ADMIN", api("GET", shared, adminJwt, null, 200).path("myRole").asText());
        assertEquals("MEMBER", api("GET", shared, memberJwt, null, 200).path("myRole").asText());
        assertEquals("IN_PROGRESS", api("GET", active, memberJwt, null, 200).path("status").asText());
        assertEquals(2, api("GET", shared + "/tournaments", memberJwt, null, 200)
                .path("tournaments").size());

        // Denial is checked against the actual HTTP server and persisted rows.
        api("POST", draft + "/schedule", memberJwt, null, 403);
        api("POST", draft + "/schedule", outsiderJwt, null, 403);
        api("PUT", result, memberJwt, Map.of("homeScore", 2, "awayScore", 1), 403);
        api("PUT", result, outsiderJwt, Map.of("homeScore", 2, "awayScore", 1), 403);
        api("POST", shared + "/tournaments", memberJwt,
                Map.of("name", "Unapproved QA"), 403);

        assertEquals(TournamentStatus.DRAFT, tournaments.findById(draftId).orElseThrow().getStatus());
        assertFalse(matches.existsByTournamentId(draftId));
        assertEquals(TournamentMatchStatus.SCHEDULED, matches.findById(matchId).orElseThrow().getStatus());
        assertNull(matches.findById(matchId).orElseThrow().getHomeScore());

        // These writes happen ONLY on the disposable PostgreSQL fixture.
        assertEquals("IN_PROGRESS", api("POST", draft + "/schedule", ownerJwt, null, 200)
                .path("status").asText());
        assertEquals(1, api("GET", draft + "/matches", memberJwt, null, 200)
                .path("matches").size());
        assertEquals("COMPLETED", api("PUT", result, adminJwt,
                Map.of("homeScore", 2, "awayScore", 1), 200).path("status").asText());
        assertEquals(1, api("GET", active + "/standings", memberJwt, null, 200)
                .path("completedMatches").asInt());
        api("GET", active + "/standings", outsiderJwt, null, 403);
        assertEquals(TournamentMatchStatus.COMPLETED, matches.findById(matchId).orElseThrow().getStatus());
        assertEquals(2, matches.findById(matchId).orElseThrow().getHomeScore());
        assertEquals(1, matches.findById(matchId).orElseThrow().getAwayScore());

        // Opt-in real Angular on localhost, still reading these exact
        // persisted JWT/PG16 fixtures while Spring's random-port server is up.
        // The browser blocks ALL tournament mutations before they reach Spring.
        if ("true".equals(System.getenv("JYNLO_TOURNAMENT_ROLE_BROWSER_QA"))) {
            var process = new ProcessBuilder(
                    "node", "scripts/qa/community-tournament-role-browser-real.cjs")
                    .inheritIO();
            var env = process.environment();
            env.put("JYNLO_ROLES_SPRING_PORT", Integer.toString(port));
            env.put("JYNLO_ROLES_COMMUNITY_ID", groupId.toString());
            env.put("JYNLO_ROLES_TOURNAMENT_ID", activeId.toString());
            env.put("JYNLO_ROLES_OWNER_JWT", ownerJwt);
            env.put("JYNLO_ROLES_MEMBER_JWT", memberJwt);
            env.put("JYNLO_ROLES_OUTSIDER_JWT", outsiderJwt);
            var child = process.start();
            if (!child.waitFor(175, java.util.concurrent.TimeUnit.SECONDS)) {
                child.destroyForcibly();
                fail("Real Angular tournament role browser exceeded its time budget");
            }
            assertEquals(0, child.exitValue(),
                    "Chromium must render authorized tournament read from real signed JWT and PG16");
        }
    }
}
