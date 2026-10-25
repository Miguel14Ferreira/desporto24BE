package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Opt-in authenticated HTTP smoke journey against a disposable PostgreSQL 16.
 * Starts the ACTUAL Spring Boot app, security filter, controllers, JPA and DB.
 * Never points at Render; no real accounts or seed records are used.
 *
 * This class ends in IT, so the normal Maven unit test suite does not run it.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "spring.liquibase.enabled=false",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.open-in-view=true",
      "spring.datasource.hikari.schema=desporto24",
      "spring.jpa.properties.hibernate.default_schema=desporto24",
      "springdoc.api-docs.enabled=false",
      "desporto.app.jwtSecret=isolated_authentication_key_only_for_test_2026_speed",
      "desporto.cors.allowed-origins=http://127.0.0.1:4200",
      "UI_APP_URL=http://127.0.0.1:4200",
      "VERIFY_EMAIL_HOST=http://127.0.0.1:8080",
      "AES_ENCRYPTION_KEY=ciKey1",
      "desporto.media.provider=local",
      "POSTGRES_SCHEMA=desporto24"
    })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityInlineSpeedAuthenticatedPgIT {
    private static final String SPORT = "Patinagem de Velocidade";
    private static final String FIRST = "INLINE_TRACK_500D_FIRST_ROUND";
    private static final String SEMI = "INLINE_TRACK_500D_SEMI_HEAT";
    private static final String FINAL = "INLINE_TRACK_500D_FINAL_A";
    private static final String[] NAMES = {
      "Ana Exemplo", "Bruno Exemplo", "Carla Exemplo", "Diogo Exemplo",
      "Eva Exemplo", "Filipe Exemplo", "Gabriela Exemplo", "Hugo Exemplo",
      "Ines Exemplo", "Joao Exemplo", "Lara Exemplo", "Marco Exemplo"
    };

    /** Resolve an unrelated admin importer executor ambiguity only in this QA context. */
    @TestConfiguration
    static class IsolatedExecutorConfiguration {
        @Bean(name = "applicationTaskExecutor")
        @Primary
        TaskExecutor isolatedFixtureExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @DynamicPropertySource
    static void fixtureProperties(DynamicPropertyRegistry registry) {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        if (url == null || !url.matches(
                "^jdbc:postgresql://(127\\.0\\.0\\.1|localhost):5432/speed_http_qa$")) {
            throw new IllegalStateException("Only disposable loopback speed_http_qa is allowed");
        }
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> System.getenv("JYNLO_TEST_PG_USERNAME"));
        registry.add("spring.datasource.password", () -> System.getenv("JYNLO_TEST_PG_PASSWORD"));
    }

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    private int port;
    @Autowired private ProfileRepository profiles;
    @Autowired private CommunityRepository communities;
    @Autowired private CommunityMemberRepository members;
    @Autowired private CommunityRoundRepository rounds;
    @Autowired private CommunityRoundEntryRepository entries;
    @Autowired private CommunityInlineSpeedDecisionRepository decisions;
    @Autowired private CommunityInlineSpeedHeatRepository links;
    @Autowired private JWTokenProvider jwt;
    @Autowired private ObjectMapper mapper;
    private final HttpClient http = HttpClient.newHttpClient();
    private final List<String> handles = new ArrayList<>();
    private Long communityId;
    private String ownerToken;
    private String outsiderToken;

    @BeforeAll
    void fixtures() {
        Profile owner = profile("qa_owner_speed", "Organizador Ficticio");
        var outsider = profile("qa_outsider_speed", "Pessoa Fora da Comunidade");
        ownerToken = jwt.generateJwtToken(new ProfilePrincipal(owner));
        outsiderToken = jwt.generateJwtToken(new ProfilePrincipal(outsider));

        var community = Community.builder()
            .name("Comunidade QA - Patinagem")
            .sport("Futebol") // competition may use a different catalogue sport
            .visibility(CommunityVisibility.PRIVATE)
            .owner(owner).rankingsEnabled(false)
            .individualTournamentsEnabled(true)
            .createdAt(Instant.now()).build();
        communityId = communities.saveAndFlush(community).getId();

        addMember(owner, CommunityRole.OWNER);
        for (int i = 0; i < NAMES.length; i++) {
            String handle = "qa_skater_" + (i + 1);
            handles.add(handle);
            addMember(profile(handle, NAMES[i]), CommunityRole.MEMBER);
        }
    }

    private Profile profile(String username, String displayName) {
        Profile p = new Profile();
        p.setUsername(username);
        p.setFullName(displayName);
        p.setEmail(username + "@example.invalid");
        p.setEnabled(true);
        p.setNotLocked(true);
        p.setMFA(false);
        p.setRole("ROLE_USER");
        p.setAuthorities(new String[] {"ROLE_USER"});
        return profiles.saveAndFlush(p);
    }

    private void addMember(Profile p, CommunityRole role) {
        members.saveAndFlush(CommunityMember.builder()
            .community(communities.findById(communityId).orElseThrow())
            .profile(p).role(role).joinedAt(Instant.now()).build());
    }

    private JsonNode request(String method, String path, String token,
                             Object body, int expected) throws Exception {
        String url = "http://127.0.0.1:" + port + path;
        var builder = HttpRequest.newBuilder(URI.create(url))
            .header("Accept", "application/json");
        if (token != null) builder.header("Authorization", "Bearer " + token);
        if (body != null) {
            builder.header("Content-Type", "application/json");
            builder.method(method, HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        HttpResponse<String> response = http.send(builder.build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(expected, response.statusCode(),
            method + " " + path + " returned " + response.statusCode() + " " + response.body());
        return response.body() == null || response.body().isBlank()
            ? mapper.createObjectNode() : mapper.readTree(response.body());
    }

    private Map<String, Object> entrant(int index, Integer place, Double seconds) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("username", handles.get(index));
        row.put("result", "DRAW");
        if (place != null) row.put("finishPosition", place);
        if (seconds != null) row.put("elapsedSeconds", seconds);
        return row;
    }

    private long record(String name, String type, int... entrants) throws Exception {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int j = 0; j < entrants.length; j++) {
            int skater = entrants[j];
            rows.add(entrant(skater, FIRST.equals(type) ? null : j + 1,
                FIRST.equals(type) ? 30.0 + skater / 100.0 : null));
        }
        JsonNode saved = request("POST", "/api/communities/" + communityId + "/rounds",
          ownerToken, Map.of("name", name, "playedAt", Instant.now().toString(),
              "sport", SPORT, "eventType", type, "entries", rows), 200);
        assertTrue(saved.path("id").asLong() > 0, saved.toString());
        return saved.path("id").asLong();
    }

    private JsonNode overview(long tournamentId) throws Exception {
        return request("GET", speed(tournamentId), ownerToken, null, 200);
    }

    private String speed(long tournamentId) {
        return "/api/communities/" + communityId + "/tournaments/" +
            tournamentId + "/inline-speed";
    }

    @Test
    void genuineJwtAndHttpWithPostgresMoveFictionalSkatersToFinal() throws Exception {
        // Security uses the real JWT filter, token signature and repository lookup.
        request("GET", "/api/communities/" + communityId + "/competition",
            null, null, 403);
        request("GET", "/api/communities/" + communityId + "/competition",
            outsiderToken, null, 403);

        JsonNode season = request("POST", "/api/communities/" + communityId + "/seasons",
            ownerToken, Map.of("name", "Epoca QA 2026"), 200);
        assertTrue(season.path("id").asLong() > 0);

        JsonNode tournament = request("POST",
            "/api/communities/" + communityId + "/tournaments", ownerToken,
            Map.of("name", "Campeonato de Patinagem 2026 (QA)",
                "sport", SPORT, "entryType", "INDIVIDUAL",
                "winPoints", 0, "drawPoints", 0, "lossPoints", 0), 201);
        long tournamentId = tournament.path("id").asLong();
        assertTrue(tournamentId > 0, tournament.toString());

        // First timed stage: 12 distinct skaters and eight provisional qualifiers.
        long first = record("Primeira fase 500m +D",
            FIRST, 0,1,2,3,4,5,6,7,8,9,10,11);
        request("PUT", speed(tournamentId) + "/heats/" + first + "?heatNumber=1",
            outsiderToken, null, 403);
        JsonNode linked = request("PUT",
            speed(tournamentId) + "/heats/" + first + "?heatNumber=1",
            ownerToken, null, 200);
        assertEquals("SEMI", linked.path("proposals").get(0).path("targetStage").asText());
        assertEquals(8, linked.path("proposals").get(0).path("playerIds").size());
        assertEquals(0, linked.path("decisions").size());

        List<Long> eight = new ArrayList<>();
        for (var id : linked.path("proposals").get(0).path("playerIds")) eight.add(id.asLong());
        request("PUT", speed(tournamentId) + "/qualifications/FIRST",
            ownerToken, Map.of("playerIds", eight, "reason", ""), 422);
        JsonNode confirmed = request("PUT", speed(tournamentId) + "/qualifications/FIRST",
            ownerToken, Map.of("playerIds", eight,
                "reason", "O juiz confirma os doze tempos desta primeira fase"), 200);
        assertEquals(1, confirmed.path("decisions").size());

        // A non-qualifier cannot be linked to the semifinal, even if it has a result.
        long invalidSemi = record("Semi invalida",
            SEMI, 0,1,2,9);
        request("PUT", speed(tournamentId) + "/heats/" + invalidSemi + "?heatNumber=1",
            ownerToken, null, 409);

        long semiA = record("Meia-final A", SEMI, 0,1,2,3);
        long semiB = record("Meia-final B", SEMI, 4,5,6,7);
        request("PUT", speed(tournamentId) + "/heats/" + semiA + "?heatNumber=1",
            ownerToken, null, 200);
        JsonNode semis = request("PUT",
            speed(tournamentId) + "/heats/" + semiB + "?heatNumber=2",
            ownerToken, null, 200);
        List<Long> four = new ArrayList<>();
        for (var id : semis.path("proposals")) {
            if ("SEMI".equals(id.path("sourceStage").asText()))
                id.path("playerIds").forEach(item -> four.add(item.asLong()));
        }
        assertEquals(4, four.size());
        request("PUT", speed(tournamentId) + "/qualifications/SEMI",
            ownerToken, Map.of("playerIds", four, "reason", "Decisao confirmada pelo juiz"), 200);

        long finalRound = record("Final A", FINAL, 0,4,1,5);
        JsonNode finalOverview = request("PUT",
            speed(tournamentId) + "/heats/" + finalRound + "?heatNumber=1",
            ownerToken, null, 200);
        assertEquals(4, finalOverview.path("linkedHeats").size());
        assertEquals(2, finalOverview.path("decisions").size());
        assertFalse(communities.findById(communityId).orElseThrow().isRankingsEnabled());
        assertEquals(4, links.findByTournamentIdOrderByIdAsc(tournamentId).size());
        assertEquals(2, decisions.findByTournamentIdOrderByIdAsc(tournamentId).size());
        assertTrue(rounds.findById(first).isPresent());
        assertEquals(12, entries.findByRoundIdOrderByIdAsc(first).size());
        assertTrue(overview(tournamentId).path("decisions").get(0).has("confirmedBy"));
    }
}
