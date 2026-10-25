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
      "Ines Exemplo", "Joao Exemplo", "Lara Exemplo", "Marco Exemplo",
      "Nuno Exemplo", "Olga Exemplo", "Paulo Exemplo", "Rita Exemplo",
      "Sara Exemplo", "Tiago Exemplo", "Vera Exemplo", "Zeca Exemplo"
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
    // Spring Boot 4 uses Jackson 3 for MVC; Jackson 2 is used locally to read
    // standard JSON in this client, without depending on an application bean.
    private final ObjectMapper mapper = new ObjectMapper();
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
        row.put("mvp", false);
        row.put("cleanSheet", false);
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

    private long recordMultiwayTiedHeat(int heatNumber) throws Exception {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int index = (heatNumber - 1) * 5; index < heatNumber * 5; index++) {
            // Three skaters at the 16th-place boundary have equal original times.
            int timed = index >= 15 && index <= 17 ? 15 : index;
            rows.add(entrant(index, null, 30.0 + timed / 100.0));
        }
        JsonNode saved = request("POST", "/api/communities/" + communityId + "/rounds",
            ownerToken, Map.of("name", "Manga de desempate fictícia " + heatNumber,
                "playedAt", Instant.now().toString(), "sport", SPORT,
                "eventType", FIRST, "entries", rows), 200);
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
        long first = record("Primeira manga da primeira fase 500m +D",
            FIRST, 0,1,2,3,4,5);
        long firstTwo = record("Segunda manga da primeira fase 500m +D",
            FIRST, 6,7,8,9,10,11);
        request("PUT", speed(tournamentId) + "/heats/" + first + "?heatNumber=1",
            outsiderToken, null, 403);
        request("PUT", speed(tournamentId) + "/heats/" + first + "?heatNumber=1",
            ownerToken, null, 200);
        JsonNode linked = request("PUT",
            speed(tournamentId) + "/heats/" + firstTwo + "?heatNumber=2",
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
        assertEquals(5, finalOverview.path("linkedHeats").size());
        assertEquals(2, finalOverview.path("decisions").size());
        assertFalse(communities.findById(communityId).orElseThrow().isRankingsEnabled());
        assertEquals(5, links.findByTournamentIdOrderByIdAsc(tournamentId).size());
        assertEquals(2, decisions.findByTournamentIdOrderByIdAsc(tournamentId).size());
        assertTrue(rounds.findById(first).isPresent());
        assertEquals(6, entries.findByRoundIdOrderByIdAsc(first).size());
        assertEquals(6, entries.findByRoundIdOrderByIdAsc(firstTwo).size());
        assertTrue(overview(tournamentId).path("decisions").get(0).has("confirmedBy"));

        // A separate 20-skater competition must take the 2026 quarter-final route.
        JsonNode largeTournament = request("POST",
            "/api/communities/" + communityId + "/tournaments", ownerToken,
            Map.of("name", "Campeonato de 20 Patinadores (QA)",
                "sport", SPORT, "entryType", "INDIVIDUAL"), 201);
        long largeId = largeTournament.path("id").asLong();
        assertTrue(largeId > 0);
        JsonNode largeOverview = null;
        for (int heat = 0; heat < 4; heat++) {
            int base = heat * 5;
            long roundId = record("20 patinadores - qualificacao " + (heat + 1),
                FIRST, base,base+1,base+2,base+3,base+4);
            largeOverview = request("PUT",
                speed(largeId) + "/heats/" + roundId + "?heatNumber=" + (heat + 1),
                ownerToken, null, 200);
        }
        assertNotNull(largeOverview);
        assertEquals("QUARTER",
            largeOverview.path("proposals").get(0).path("targetStage").asText());
        assertEquals(16, largeOverview.path("proposals").get(0).path("playerIds").size());
        List<Long> sixteen = new ArrayList<>();
        largeOverview.path("proposals").get(0).path("playerIds")
            .forEach(id -> sixteen.add(id.asLong()));

        // Never approve 15; an exceptional 17th without a time tie
        // requires the jury's separate detailed attestation.
        request("PUT", speed(largeId) + "/qualifications/FIRST",
            ownerToken, Map.of("playerIds", sixteen.subList(0, 15),
                "reason", "Juri presente e manga completa"), 422);
        List<Long> seventeen = new ArrayList<>(sixteen);
        JsonNode last = largeOverview.path("linkedHeats").get(3)
            .path("skaters").get(1);
        long extraId = last.path("playerId").asLong();
        if (sixteen.contains(extraId)) {
            extraId = largeOverview.path("linkedHeats").get(3)
                .path("skaters").get(4).path("playerId").asLong();
        }
        assertFalse(sixteen.contains(extraId));
        seventeen.add(extraId);
        request("PUT", speed(largeId) + "/qualifications/FIRST",
            ownerToken, Map.of("playerIds", seventeen,
                "reason", "Sem provas"), 422);

        request("PUT", speed(largeId) + "/qualifications/FIRST", ownerToken,
            Map.of("playerIds", sixteen,
                "reason", "Os vinte tempos foram verificados e a fase esta concluida"), 200);

        JsonNode quarterOverview = null;
        for (int heat = 0; heat < 4; heat++) {
            int base = heat * 4;
            long roundId = record("Quarto " + (heat + 1), "INLINE_TRACK_500D_QUARTER_HEAT",
                base,base+1,base+2,base+3);
            quarterOverview = request("PUT",
                speed(largeId) + "/heats/" + roundId + "?heatNumber=" + (heat + 1),
                ownerToken, null, 200);
        }
        assertNotNull(quarterOverview);
        List<Long> eightQuarter = new ArrayList<>();
        for (var p : quarterOverview.path("proposals"))
            if ("QUARTER".equals(p.path("sourceStage").asText()))
                p.path("playerIds").forEach(id -> eightQuarter.add(id.asLong()));
        assertEquals(8, eightQuarter.size());
        request("PUT", speed(largeId) + "/qualifications/QUARTER",
            ownerToken, Map.of("playerIds", eightQuarter), 200);

        long semifinal1 = record("20 skaters semifinal 1", SEMI, 0,1,4,5);
        long semifinal2 = record("20 skaters semifinal 2", SEMI, 8,9,12,13);
        request("PUT", speed(largeId) + "/heats/" + semifinal1 + "?heatNumber=1",
            ownerToken, null, 200);
        JsonNode semifinalOverview = request("PUT",
            speed(largeId) + "/heats/" + semifinal2 + "?heatNumber=2",
            ownerToken, null, 200);
        List<Long> fourFinal = new ArrayList<>();
        for (var p : semifinalOverview.path("proposals"))
            if ("SEMI".equals(p.path("sourceStage").asText()))
                p.path("playerIds").forEach(id -> fourFinal.add(id.asLong()));
        assertEquals(4, fourFinal.size());
        request("PUT", speed(largeId) + "/qualifications/SEMI",
            ownerToken, Map.of("playerIds", fourFinal), 200);

        long largeFinal = record("Final dos 20", FINAL, 0,8,1,9);
        JsonNode finalTwenty = request("PUT",
            speed(largeId) + "/heats/" + largeFinal + "?heatNumber=1",
            ownerToken, null, 200);
        assertEquals(11, finalTwenty.path("linkedHeats").size());
        assertEquals(3, finalTwenty.path("decisions").size());
        assertEquals(11, links.findByTournamentIdOrderByIdAsc(largeId).size());
        assertFalse(communities.findById(communityId).orElseThrow().isRankingsEnabled());

        // Independent competition with three skaters tied at place 16.
        var tieTournament = request("POST",
            "/api/communities/" + communityId + "/tournaments", ownerToken,
            Map.of("name", "Desempate fictício oficial", "sport", SPORT,
                   "entryType", "INDIVIDUAL"), 201);
        long tieId = tieTournament.path("id").asLong();
        JsonNode tieState = null;
        for (int heat = 1; heat <= 4; heat++) {
            long roundId = recordMultiwayTiedHeat(heat);
            tieState = request("PUT",
                speed(tieId) + "/heats/" + roundId + "?heatNumber=" + heat,
                ownerToken, null, 200);
        }
        assertNotNull(tieState);
        assertFalse(tieState.path("proposals").get(0).path("ready").asBoolean());
        assertTrue(tieState.path("proposals").get(0).path("warning").asText()
                .contains("Empate múltiplo"));
        List<JsonNode> ordered = new ArrayList<>();
        for (var heat : tieState.path("linkedHeats"))
            for (var skater : heat.path("skaters")) ordered.add(skater);
        ordered.sort(Comparator.comparingDouble(s -> s.path("seconds").asDouble()));
        List<Long> sixteenChosen = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            sixteenChosen.add(ordered.get(i).path("playerId").asLong());
        // Choose one of the three tied at the cutoff, based on a recorded jury ruling.
        sixteenChosen.add(ordered.get(17).path("playerId").asLong());
        String reason = "A equipa de juízes confirmou o desempate com a prova oficial";
        request("PUT", speed(tieId) + "/tiebreaks/FIRST", outsiderToken,
                Map.of("playerIds", sixteenChosen, "reason", reason), 403);
        request("PUT", speed(tieId) + "/tiebreaks/FIRST", ownerToken,
                Map.of("playerIds", sixteenChosen, "reason", "Curto"), 422);
        request("PUT", speed(tieId) + "/tiebreaks/FIRST", ownerToken,
                Map.of("playerIds", sixteenChosen, "reason", reason,
                        "evidenceType", "FAKE_METHOD",
                        "evidenceReference", "Ata 2026-001"), 422);
        request("PUT", speed(tieId) + "/tiebreaks/FIRST", ownerToken,
                Map.of("playerIds", sixteenChosen, "reason", reason,
                        "evidenceType", "OFFICIAL_MINUTES"), 422);
        JsonNode resolved = request("PUT", speed(tieId) + "/tiebreaks/FIRST",
                ownerToken, Map.of("playerIds", sixteenChosen, "reason", reason,
                        "evidenceType", "OFFICIAL_MINUTES",
                        "evidenceReference", "Ata oficial de júri 2026-001"), 200);
        assertEquals("OFFICIAL_MINUTES", resolved.path("tieBreaks").get(0)
                .path("evidenceType").asText());
        assertEquals("Ata oficial de júri 2026-001", resolved.path("tieBreaks").get(0)
                .path("revisions").get(0).path("evidenceReference").asText());
        assertTrue(resolved.path("proposals").get(0).path("ready").asBoolean());
        assertEquals(1, resolved.path("tieBreaks").size());
        assertEquals("qa_owner_speed", resolved.path("tieBreaks").get(0).path("confirmedBy").asText());
        assertEquals(0, resolved.path("decisions").size(),
                "Tie-break must never ratify the qualification automatically");
        assertEquals(1, resolved.path("tieBreaks").get(0).path("revisions").size());

        // A correction before qualification must preserve the original decision.
        List<Long> correction = new ArrayList<>(sixteenChosen);
        correction.set(15, ordered.get(16).path("playerId").asLong());
        String correctedReason = "A segunda revisão oficial escolheu outra atleta empatada";
        JsonNode amended = request("PUT", speed(tieId) + "/tiebreaks/FIRST",
                ownerToken, Map.of("playerIds", correction,
                        "reason", correctedReason, "evidenceType", "VIDEO_REVIEW",
                        "evidenceReference", "Vídeo oficial de chegada 2026-002"), 200);
        assertEquals("VIDEO_REVIEW", amended.path("tieBreaks").get(0)
                .path("evidenceType").asText());
        assertEquals("OFFICIAL_MINUTES", amended.path("tieBreaks").get(0)
                .path("revisions").get(0).path("evidenceType").asText());
        assertEquals(2, amended.path("tieBreaks").get(0).path("revisions").size());
        assertEquals(reason, amended.path("tieBreaks").get(0).path("revisions").get(0).path("reason").asText());
        assertEquals(correctedReason, amended.path("tieBreaks").get(0).path("revisions").get(1).path("reason").asText());
        assertEquals(0, amended.path("decisions").size());

        JsonNode restored = request("PUT", speed(tieId) + "/tiebreaks/FIRST",
                ownerToken, Map.of("playerIds", sixteenChosen, "reason", reason,
                        "evidenceType", "OFFICIAL_MINUTES",
                        "evidenceReference", "Ata oficial de júri 2026-003"), 200);
        assertEquals(3, restored.path("tieBreaks").get(0).path("revisions").size());
        assertEquals("qa_owner_speed", restored.path("tieBreaks").get(0)
                .path("revisions").get(2).path("confirmedBy").asText());
        JsonNode ratified = request("PUT", speed(tieId) + "/qualifications/FIRST",
                ownerToken, Map.of("playerIds", sixteenChosen,
                        "reason", "O juiz confirma os 16 apurados depois do desempate oficial"), 200);
        assertEquals(1, ratified.path("decisions").size());
        assertEquals(1, ratified.path("tieBreaks").size());
        assertEquals(3, ratified.path("tieBreaks").get(0).path("revisions").size());

        // Real PostgreSQL/JPA source edit: after the jury has ratified a
        // tie-break, changing even a non-cutoff time must invalidate both
        // rulings rather than silently continuing with the old qualifier list.
        long originalRound = tieState.path("linkedHeats").get(0).path("roundId").asLong();
        var originalResult = entries.findByRoundIdOrderByIdAsc(originalRound).get(0);
        var originalSeconds = originalResult.getElapsedSeconds();
        originalResult.setElapsedSeconds(originalSeconds.add(new java.math.BigDecimal("0.01")));
        entries.saveAndFlush(originalResult);
        JsonNode staleOverview = overview(tieId);
        assertTrue(staleOverview.path("tieBreaks").get(0).path("stale").asBoolean(),
                "The saved tie-break must show as stale after editing source timing");
        assertTrue(staleOverview.path("decisions").get(0).path("stale").asBoolean(),
                "Previously ratified qualifiers must also show as stale");
        assertFalse(staleOverview.path("proposals").get(0).path("ready").asBoolean(),
                "A changed multiway cutoff must require a new official tie-break");

        // Alteration of a source time does not delete the old ruling: it must
        // remain visible for the organizer's audit of what was decided.
        assertEquals(reason, staleOverview.path("tieBreaks").get(0).path("reason").asText());
        assertEquals(1, staleOverview.path("tieBreaks").size());
    }
}
