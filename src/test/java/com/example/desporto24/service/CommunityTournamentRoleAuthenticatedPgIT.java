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
import java.util.List;
import java.util.ArrayList;

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

    /**
     * All paths below are served by the real random-port HTTP server, using
     * signed JWTs and the disposable localhost PostgreSQL 16 database.
     * Every test creates its own community so the original role-matrix fixture
     * and its exact tournament counts remain independent of JUnit test order.
     */
    private Community separateCompetition(String name) {
        Profile owner = profiles.findUserByUsernameIgnoreCase("isolated_pg_owner").orElseThrow();
        Profile admin = profiles.findUserByUsernameIgnoreCase("isolated_pg_admin").orElseThrow();
        Community group = community(name, owner);
        addMember(group, owner, CommunityRole.OWNER);
        addMember(group, admin, CommunityRole.ADMIN);
        return group;
    }

    private List<Profile> fictionalCompetitors(Community group, String prefix, int count) {
        List<Profile> result = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Profile athlete = profile(prefix + i);
            addMember(group, athlete, CommunityRole.MEMBER);
            result.add(athlete);
        }
        return result;
    }

    @Test
    void eightIndividualEntrantsCompleteActualHttpLeagueWithLockedChampion() throws Exception {
        Community group = separateCompetition("Synthetic eight-player individual season");
        List<Profile> athletes = fictionalCompetitors(group, "ind_full_pg_player_", 8);
        String root = "/api/communities/" + group.getId() + "/tournaments";
        JsonNode created = api("POST", root, ownerJwt,
                Map.of("name", "Ténis de teste: oito individuais",
                        "sport", "Ténis", "entryType", "INDIVIDUAL", "format", "LEAGUE"), 201);
        String tournament = root + "/" + created.path("id").asLong();
        assertEquals("DRAFT", created.path("status").asText());

        String playerJwt = token(athletes.get(0));
        api("GET", tournament, playerJwt, null, 200);
        api("GET", tournament, outsiderJwt, null, 403);
        api("GET", root, playerJwt, null, 200);
        api("POST", root, playerJwt, Map.of("name", "Unauthorized"), 403);

        // Registration is opt-in and not an organizer's unilateral enrollment.
        for (Profile athlete : athletes) {
            JsonNode pending = api("PUT", tournament + "/participants/me",
                    token(athlete), null, 200);
            assertEquals("PENDING", pending.path("status").asText());
            api("PUT", tournament + "/participants/" + athlete.getId() + "/confirm",
                    playerJwt, null, 403);
            JsonNode confirmed = api("PUT",
                    tournament + "/participants/" + athlete.getId() + "/confirm",
                    adminJwt, null, 200);
            assertEquals("CONFIRMED", confirmed.path("status").asText());
        }
        assertEquals(8, api("GET", tournament + "/participants",
                playerJwt, null, 200).path("participants").size());
        api("POST", tournament + "/schedule", playerJwt, null, 403);
        JsonNode schedule = api("POST", tournament + "/schedule", ownerJwt, null, 200);
        assertEquals(8, schedule.path("participantCount").asInt());
        assertEquals(7, schedule.path("roundCount").asInt());
        assertEquals(28, schedule.path("matchCount").asInt());
        api("POST", tournament + "/schedule", ownerJwt, null, 409);
        api("POST", tournament + "/complete", ownerJwt, null, 409);

        JsonNode fixtures = api("GET", tournament + "/matches",
                playerJwt, null, 200).path("matches");
        assertEquals(28, fixtures.size());
        long champion = athletes.get(0).getId();
        for (JsonNode fixture : fixtures) {
            long homeId = fixture.path("homeProfileId").asLong();
            long awayId = fixture.path("awayProfileId").asLong();
            int home = homeId == champion ? 2 : awayId == champion ? 0 : 1;
            int away = awayId == champion ? 2 : 0;
            String result = tournament + "/matches/" + fixture.path("id").asLong() + "/result";
            api("PUT", result, playerJwt, Map.of("homeScore", home, "awayScore", away), 403);
            assertEquals("COMPLETED", api("PUT", result, ownerJwt,
                    Map.of("homeScore", home, "awayScore", away), 200)
                    .path("status").asText());
        }
        JsonNode standings = api("GET", tournament + "/standings", playerJwt, null, 200);
        assertEquals(28, standings.path("totalMatches").asInt());
        assertEquals(28, standings.path("completedMatches").asInt());
        assertEquals(8, standings.path("standings").size());
        assertEquals(champion, standings.path("standings").get(0).path("profileId").asLong());

        JsonNode finished = api("POST", tournament + "/complete", ownerJwt, null, 200);
        assertEquals("COMPLETED", finished.path("status").asText());
        assertEquals(champion, finished.path("championProfileId").asLong());
        assertFalse(finished.path("championTied").asBoolean());
        assertEquals(finished.path("completedAt").asText(),
                api("POST", tournament + "/complete", ownerJwt, null, 200)
                        .path("completedAt").asText());
        api("PUT", tournament + "/matches/" + fixtures.get(0).path("id").asLong()
                + "/result", adminJwt, Map.of("homeScore", 5, "awayScore", 4), 409);
    }

    @Test
    void fourRealTeamsCompleteHomeAwayLeagueWithResultAndChampionSnapshots() throws Exception {
        Community group = separateCompetition("Synthetic four-team home-away league");
        List<Profile> athletes = fictionalCompetitors(group, "team_league_pg_player_", 8);
        String root = "/api/communities/" + group.getId() + "/tournaments";
        JsonNode created = api("POST", root, ownerJwt,
                Map.of("name", "Liga equipas HTTP", "sport", "Futebol",
                        "entryType", "TEAM", "format", "LEAGUE", "roundRobinLegs", 2,
                        "playersPerTeam", 2), 201);
        String tournament = root + "/" + created.path("id").asLong();
        String teamsPath = tournament + "/teams";
        String memberJwt = token(athletes.get(0));
        List<Long> teamIds = new ArrayList<>();
        for (int team = 0; team < 4; team++) {
            JsonNode added = api("POST", teamsPath, adminJwt,
                    Map.of("name", "Equipa HTTP " + team), 201);
            long teamId = added.path("id").asLong();
            teamIds.add(teamId);
            for (int slot = 0; slot < 2; slot++) {
                Profile member = athletes.get(team * 2 + slot);
                api("POST", teamsPath + "/" + teamId + "/players", ownerJwt,
                        Map.of("profileId", member.getId()), 201);
            }
        }
        assertEquals(4, api("GET", teamsPath, memberJwt, null, 200).size());
        api("GET", teamsPath, outsiderJwt, null, 403);
        api("POST", teamsPath, memberJwt, Map.of("name", "Unauthorized team"), 403);
        api("POST", teamsPath + "/schedule", memberJwt, null, 403);
        JsonNode schedule = api("POST", teamsPath + "/schedule", ownerJwt, null, 200);
        assertEquals(4, schedule.path("teamCount").asInt());
        assertEquals(6, schedule.path("roundCount").asInt());
        assertEquals(12, schedule.path("matchCount").asInt());
        api("POST", teamsPath + "/schedule", ownerJwt, null, 409);
        api("POST", teamsPath + "/complete", ownerJwt, null, 409);

        JsonNode fixtures = api("GET", teamsPath + "/matches",
                memberJwt, null, 200).path("matches");
        assertEquals(12, fixtures.size());
        long champion = teamIds.get(0);
        for (JsonNode fixture : fixtures) {
            long homeId = fixture.path("homeTeamId").asLong();
            long awayId = fixture.path("awayTeamId").asLong();
            int home = homeId == champion ? 2 : awayId == champion ? 0 : 1;
            int away = awayId == champion ? 2 : 0;
            String resultPath = teamsPath + "/matches/" + fixture.path("id").asLong() + "/result";
            api("POST", resultPath, memberJwt,
                    Map.of("homeScore", home, "awayScore", away), 403);
            api("POST", resultPath, adminJwt,
                    Map.of("homeScore", home, "awayScore", away), 200);
        }
        JsonNode standings = api("GET", teamsPath + "/standings", memberJwt, null, 200);
        assertEquals(12, standings.path("completedMatches").asInt());
        assertEquals(4, standings.path("standings").size());
        assertEquals(champion, standings.path("standings").get(0).path("teamId").asLong());

        JsonNode completed = api("POST", teamsPath + "/complete", ownerJwt, null, 200);
        assertEquals("COMPLETED", completed.path("status").asText());
        assertEquals(champion, completed.path("championTeamId").asLong());
        assertEquals(completed.path("completedAt").asText(),
                api("POST", teamsPath + "/complete", adminJwt, null, 200)
                        .path("completedAt").asText());
        api("POST", teamsPath + "/matches/" + fixtures.get(0).path("id").asLong()
                + "/result", ownerJwt, Map.of("homeScore", 7, "awayScore", 3), 409);
        api("GET", teamsPath + "/standings", outsiderJwt, null, 403);
    }

    @Test
    void cupsAndGroupPlayoffsAdvanceOnlyFromRealSavedHttpMatchResults() throws Exception {
        Community group = separateCompetition("Synthetic team cups and groups");
        List<Profile> athletes = fictionalCompetitors(group, "team_cup_pg_player_", 4);
        String root = "/api/communities/" + group.getId() + "/tournaments";
        String memberJwt = token(athletes.get(0));
        for (String format : List.of("KNOCKOUT", "GROUP_KNOCKOUT")) {
            Map<String,Object> request = new java.util.LinkedHashMap<>();
            request.put("name", "Taça HTTP " + format);
            request.put("sport", "Futebol");
            request.put("entryType", "TEAM");
            request.put("format", format);
            request.put("playersPerTeam", 1);
            if ("GROUP_KNOCKOUT".equals(format)) request.put("groupCount", 2);
            JsonNode created = api("POST", root, ownerJwt, request, 201);
            String tournament = root + "/" + created.path("id").asLong();
            String teamsPath = tournament + "/teams";
            for (int i = 0; i < 4; i++) {
                long teamId = api("POST", teamsPath, ownerJwt,
                        Map.of("name", "Clube " + format + " " + i), 201).path("id").asLong();
                api("POST", teamsPath + "/" + teamId + "/players", adminJwt,
                        Map.of("profileId", athletes.get(i).getId()), 201);
            }
            api("POST", teamsPath + "/schedule", memberJwt, null, 403);
            JsonNode started = api("POST", teamsPath + "/schedule", ownerJwt, null, 200);
            assertEquals("IN_PROGRESS", started.path("status").asText());
            if ("GROUP_KNOCKOUT".equals(format)) {
                assertEquals(2, api("GET", teamsPath + "/groups",
                        memberJwt, null, 200).path("groups").size());
            }
            int scored = 0;
            int roundsObserved = 0;
            for (int step = 0; step < 8; step++) {
                JsonNode tournamentState = api("GET", tournament, memberJwt, null, 200);
                if ("COMPLETED".equals(tournamentState.path("status").asText())) break;
                JsonNode fixtures = api("GET", teamsPath + "/matches",
                        memberJwt, null, 200).path("matches");
                int pending = 0;
                for (JsonNode fixture : fixtures) {
                    if (!"SCHEDULED".equals(fixture.path("status").asText())) continue;
                    pending++;
                    String result = teamsPath + "/matches/" + fixture.path("id").asLong() + "/result";
                    if ("KNOCKOUT".equals(format) || fixture.path("groupNumber").isNull()) {
                        api("POST", result, ownerJwt,
                                Map.of("homeScore", 1, "awayScore", 1), 422);
                    }
                    api("POST", result, adminJwt,
                            Map.of("homeScore", 2, "awayScore", 0), 200);
                    scored++;
                }
                assertTrue(pending > 0, format + " cannot stall before completion");
                roundsObserved++;
            }
            JsonNode finished = api("GET", tournament, memberJwt, null, 200);
            assertEquals("COMPLETED", finished.path("status").asText(), format);
            assertTrue(finished.path("championTeamId").asLong() > 0, format);
            assertFalse(finished.path("championTied").asBoolean(), format);
            assertEquals("KNOCKOUT".equals(format) ? 3 : 5, scored, format);
            assertTrue(roundsObserved >= 2);
            assertEquals(scored, api("GET", teamsPath + "/matches",
                    memberJwt, null, 200).path("matches").size());
            api("POST", teamsPath + "/matches/" +
                    api("GET", teamsPath + "/matches", memberJwt, null, 200)
                            .path("matches").get(0).path("id").asLong() + "/result",
                    ownerJwt, Map.of("homeScore", 8, "awayScore", 0), 409);
        }
    }

}
