package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.*;
import org.springframework.test.context.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Authenticated HTTP + real Spring + JPA + PG16. All riders, times, outcomes
 * and rankings are synthetic; no Render endpoint, credentials or data are used.
 * Opt-in IT excluded from default Maven unit suite.
 */
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties={
      "spring.liquibase.enabled=false",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.open-in-view=true",
      "spring.datasource.hikari.schema=desporto24",
      "spring.jpa.properties.hibernate.default_schema=desporto24",
      "springdoc.api-docs.enabled=false",
      "desporto.app.jwtSecret=isolated_authentication_key_only_for_test_2026_sprint",
      "desporto.cors.allowed-origins=http://127.0.0.1:4200",
      "UI_APP_URL=http://127.0.0.1:4200",
      "VERIFY_EMAIL_HOST=http://127.0.0.1:8080",
      "AES_ENCRYPTION_KEY=ciKey1",
      "desporto.media.provider=local",
      "POSTGRES_SCHEMA=desporto24"
    })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityTrackSprintAuthenticatedPgIT {
    @TestConfiguration
    static class IsolatedExecutor {
        @Bean(name="applicationTaskExecutor") @Primary
        TaskExecutor fixtureExecutor() {return new SyncTaskExecutor();}
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry reg) {
        String url=System.getenv("JYNLO_TEST_PG_JDBC_URL");
        if(url==null||!url.matches("^jdbc:postgresql://(127\\.0\\.0\\.1|localhost):5432/sprint_http_qa$"))
            throw new IllegalStateException("Only loopback disposable sprint_http_qa is allowed");
        reg.add("spring.datasource.url",()->url);
        reg.add("spring.datasource.username",()->System.getenv("JYNLO_TEST_PG_USERNAME"));
        reg.add("spring.datasource.password",()->System.getenv("JYNLO_TEST_PG_PASSWORD"));
    }
    @Value("${local.server.port}") int port;
    @Autowired ProfileRepository profiles;
    @Autowired CommunityRepository communities;
    @Autowired CommunityMemberRepository members;
    @Autowired CommunitySeasonRepository seasons;
    @Autowired CommunityRoundRepository rounds;
    @Autowired CommunityRoundEntryRepository entries;
    @Autowired CommunityTrackSprintBracketRepository brackets;
    @Autowired JWTokenProvider jwt;
    final ObjectMapper mapper=new ObjectMapper();
    final HttpClient client=HttpClient.newHttpClient();
    final List<String> riders=new ArrayList<>();
    final String sport="Ciclismo de Pista";
    Long group,closedSeason,foreignSeason,otherGroup,activeSeason;
    String ownerJwt,memberJwt,outsiderJwt;
    int savedRounds;

    @BeforeAll
    void init() {
        Profile owner=profile("fixture_sprint_owner","Fixture Owner");
        Profile member=profile("fixture_sprint_member","Fixture Member");
        Profile outsider=profile("fixture_sprint_outsider","Fixture Outsider");
        ownerJwt=jwt.generateJwtToken(new ProfilePrincipal(owner));
        memberJwt=jwt.generateJwtToken(new ProfilePrincipal(member));
        outsiderJwt=jwt.generateJwtToken(new ProfilePrincipal(outsider));
        Community c=communities.saveAndFlush(Community.builder().name("Isolated Olympic QA")
            .sport(sport).visibility(CommunityVisibility.PRIVATE).owner(owner)
            .rankingsEnabled(true).createdAt(Instant.now()).build());
        group=c.getId();
        add(c,owner,CommunityRole.OWNER);
        add(c,member,CommunityRole.MEMBER);
        for(int i=1;i<=24;i++) {
            String handle=String.format(Locale.ROOT,"fixture_track_%02d",i);
            riders.add(handle);
            add(c,profile(handle,"Fictional Rider "+i),CommunityRole.MEMBER);
        }
        closedSeason=seasons.saveAndFlush(CommunitySeason.builder()
            .community(c).name("Historical independent QA season")
            .startsAt(Instant.parse("2025-01-01T00:00:00Z"))
            .endedAt(Instant.parse("2025-12-31T00:00:00Z"))
            .createdAt(Instant.now()).active(false).build()).getId();
        Profile otherOwner=profile("fixture_sprint_other_owner","Other Owner");
        Community other=communities.saveAndFlush(Community.builder()
            .name("Other private community").sport(sport)
            .visibility(CommunityVisibility.PRIVATE).owner(otherOwner)
            .rankingsEnabled(true).createdAt(Instant.now()).build());
        otherGroup=other.getId();
        add(other,otherOwner,CommunityRole.OWNER);
        foreignSeason=seasons.saveAndFlush(CommunitySeason.builder()
            .community(other).name("Other community season")
            .startsAt(Instant.now()).createdAt(Instant.now()).active(true).build()).getId();
    }
    Profile profile(String username,String name) {
        Profile p=new Profile();
        p.setUsername(username);p.setFullName(name);
        p.setEmail(username+"@example.invalid");p.setEnabled(true);p.setNotLocked(true);
        p.setMFA(false);p.setRole("ROLE_USER");p.setAuthorities(new String[]{"ROLE_USER"});
        return profiles.saveAndFlush(p);
    }
    void add(Community c,Profile p,CommunityRole role) {
        members.saveAndFlush(CommunityMember.builder()
            .community(c).profile(p).role(role).joinedAt(Instant.now()).build());
    }
    HttpResponse<String> send(String method,String path,String token,Object body) throws Exception {
        var req=HttpRequest.newBuilder(URI.create("http://127.0.0.1:"+port+path))
            .timeout(Duration.ofSeconds(25)).header("Accept","application/json");
        if(token!=null)req.header("Authorization","Bearer "+token);
        if(body!=null) {
            req.header("Content-Type","application/json");
            req.method(method,HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
        } else req.method(method,HttpRequest.BodyPublishers.noBody());
        return client.send(req.build(),HttpResponse.BodyHandlers.ofString());
    }
    JsonNode api(String method,String path,String token,Object body,int status) throws Exception {
        var res=send(method,path,token,body);
        assertEquals(status,res.statusCode(),method+" "+path+" -> "+res.statusCode()+" "+res.body());
        return res.body().isBlank()?mapper.createObjectNode():mapper.readTree(res.body());
    }
    String community() {return "/api/communities/"+group;}
    String brackets(Long season) {return community()+"/seasons/"+season+"/track-sprint-brackets";}
    JsonNode overview() throws Exception {
        JsonNode value=api("GET",brackets(activeSeason),ownerJwt,null,200);
        assertEquals(1,value.size());
        return value.get(0);
    }
    JsonNode match(String slot) throws Exception {
        for(JsonNode m:overview().path("matches"))
            if(slot.equals(m.path("slot").asText()))return m;
        fail("Missing slot "+slot);
        return mapper.createObjectNode();
    }
    Map<String,Object> rider(String username,String result) {
        return Map.of("username",username,"result",result,"mvp",false,"cleanSheet",false);
    }
    long record(String name,String type,String phase,String key,Integer number,
                List<Map<String,Object>> participants) throws Exception {
        Map<String,Object> req=new LinkedHashMap<>();
        req.put("name",name);req.put("playedAt",Instant.now().toString());
        req.put("sport",sport);req.put("eventType",type);req.put("entries",participants);
        if(phase!=null)req.put("eventDiscipline",phase);
        if(key!=null)req.put("sprintSeriesKey",key);
        if(number!=null)req.put("sprintHeatNumber",number);
        JsonNode saved=api("POST",community()+"/rounds",ownerJwt,req,200);
        long id=saved.path("id").asLong();
        assertTrue(id>0,saved.toString());
        savedRounds++;
        return id;
    }
    long heat(String slot,int heatNumber,String winner) throws Exception {
        JsonNode m=match(slot);
        assertTrue(List.of("READY","IN_PROGRESS").contains(m.path("status").asText()),m.toString());
        List<String> names=new ArrayList<>();
        for(String field:List.of("riderA","riderB","riderC"))
            if(m.hasNonNull(field))names.add(m.path(field).asText());
        assertTrue(names.contains(winner),"Fixture winner not in "+slot);
        List<Map<String,Object>> rows=new ArrayList<>();
        for(String name:names)rows.add(rider(name,name.equals(winner)?"WIN":"LOSS"));
        String type=slot.startsWith("U")||slot.startsWith("V")||slot.startsWith("W")
            ?"TRACK_SPRINT_REPECHAGE":"TRACK_SPRINT_HEAT";
        return record("Fictional "+slot+" #"+heatNumber,type,m.path("phase").asText(),
            m.path("seriesKey").asText(),heatNumber,rows);
    }
    void stage(String prefix,int count) throws Exception {
        for(int i=1;i<=count;i++) {
            String slot=prefix+i;
            JsonNode m=match(slot);
            assertEquals("READY",m.path("status").asText(),m.toString());
            // U1 deliberately uses its third rider; all fixtures synthetic.
            String winner=prefix.equals("U")&&i==1
                ?m.path("riderC").asText():m.path("riderA").asText();
            heat(slot,1,winner);
            assertEquals("FINISHED",match(slot).path("status").asText(),slot);
        }
    }
    void bestThree(String slot,boolean needsThird) throws Exception {
        JsonNode m=match(slot);
        assertEquals("READY",m.path("status").asText(),m.toString());
        String a=m.path("riderA").asText(),b=m.path("riderB").asText();
        heat(slot,1,a);
        assertEquals("IN_PROGRESS",match(slot).path("status").asText());
        heat(slot,2,needsThird?b:a);
        assertEquals(needsThird?"IN_PROGRESS":"FINISHED",match(slot).path("status").asText());
        if(needsThird)heat(slot,3,a);
        assertEquals("FINISHED",match(slot).path("status").asText());
        assertEquals(a,match(slot).path("winner").asText());
    }

    @Test
    void actualJwtHttpPostgresProgressesAllOlympicStagesWithoutTouchingHistory()
            throws Exception {
        api("GET",community()+"/competition",null,null,403);
        api("GET",community()+"/competition",outsiderJwt,null,403);
        activeSeason=api("POST",community()+"/seasons",ownerJwt,
            Map.of("name","Active fictitious Olympic season"),200).path("id").asLong();
        assertTrue(activeSeason>0);
        api("GET",brackets(activeSeason),null,null,403);
        api("GET",brackets(activeSeason),outsiderJwt,null,403);
        assertEquals(0,api("GET",brackets(activeSeason),memberJwt,null,200).size());
        assertEquals(0,api("GET",brackets(closedSeason),ownerJwt,null,200).size());
        api("GET","/api/communities/"+otherGroup+"/seasons/"+foreignSeason+
            "/track-sprint-brackets",ownerJwt,null,403);

        List<Map<String,Object>> timed=new ArrayList<>();
        for(int i=0;i<24;i++) {
            Map<String,Object> row=new LinkedHashMap<>(rider(riders.get(i),"DRAW"));
            row.put("finishPosition",i+1);
            row.put("elapsedSeconds",BigDecimal.valueOf(10_001L+i,3));
            timed.add(row);
        }
        long qual=record("Fictional 200m qualification","TRACK_FLYING_200",
            null,null,null,timed);
        Map<String,Object> newBracket=Map.of("name","Olympic 24 QA",
            "format","OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual);
        api("POST",brackets(activeSeason),memberJwt,newBracket,403);
        api("POST",brackets(activeSeason),outsiderJwt,newBracket,403);
        JsonNode created=api("POST",brackets(activeSeason),ownerJwt,newBracket,200);
        long id=created.path("id").asLong();
        assertTrue(id>0);
        assertEquals(24,created.path("qualifyingSeeds").size());
        assertEquals(45,created.path("matches").size());
        assertEquals(riders.get(0),created.path("qualifyingSeeds").get(0).asText());
        assertEquals("WAITING_PREVIOUS",match("U1").path("status").asText());
        assertEquals("WAITING_PREVIOUS",match("F58").path("status").asText());
        assertEquals(id,api("GET",brackets(activeSeason),memberJwt,null,200)
            .get(0).path("id").asLong());

        stage("A",12);
        stage("U",4); // Three riders per repechage, third rider wins U1
        stage("C",8);
        stage("V",4); // Two riders per repechage
        stage("E",6);
        stage("W",2); // Three riders per repechage
        assertEquals("READY",match("Q1").path("status").asText());
        JsonNode firstQuarter=match("Q1");
        long q1=heat("Q1",1,firstQuarter.path("riderA").asText());
        assertEquals("WAITING_PREVIOUS",match("F58").path("status").asText());
        heat("Q1",2,firstQuarter.path("riderB").asText());
        assertEquals("IN_PROGRESS",match("Q1").path("status").asText());
        heat("Q1",3,firstQuarter.path("riderA").asText());
        for(int i=2;i<=4;i++)bestThree("Q"+i,false);
        JsonNode f=match("F58");
        assertEquals("READY",f.path("status").asText());
        List<Map<String,Object>> rank=new ArrayList<>();
        int pos=5;
        for(String field:List.of("riderA","riderB","riderC","riderD")) {
            Map<String,Object> row=new LinkedHashMap<>(rider(f.path(field).asText(),"DRAW"));
            row.put("finishPosition",pos++);
            rank.add(row);
        }
        long ranking=record("Fictional 5th to 8th","TRACK_SPRINT_5_8",
            "SPRINT_PLACES_5_8",f.path("seriesKey").asText(),1,rank);
        JsonNode classified=match("F58");
        assertEquals("FINISHED",classified.path("status").asText());
        assertEquals(4,classified.path("rankings").size());
        assertEquals(5,classified.path("rankings").get(0).path("position").asInt());
        assertEquals(8,classified.path("rankings").get(3).path("position").asInt());
        bestThree("S1",false);bestThree("S2",false);
        bestThree("B",false);bestThree("G",false);
        assertEquals("FINISHED",match("G").path("status").asText());
        assertEquals("FINISHED",match("B").path("status").asText());

        api("DELETE",brackets(activeSeason)+"/"+id,memberJwt,null,403);
        api("POST",community()+"/rounds",memberJwt,Map.of(
            "name","Unauthorized heat","playedAt",Instant.now().toString(),
            "sport",sport,"eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline","SPRINT_GOLD_FINAL","sprintSeriesKey","SP"+id+"-G",
            "sprintHeatNumber",1,"entries",List.of(
                rider(riders.get(0),"WIN"),rider(riders.get(1),"LOSS"))),403);
        assertTrue(brackets.findById(id).isPresent());
        assertTrue(rounds.findById(qual).isPresent());
        assertTrue(rounds.findById(q1).isPresent());
        assertTrue(rounds.findById(ranking).isPresent());
        assertEquals(24,entries.findByRoundIdOrderByIdAsc(qual).size());
        assertEquals(4,entries.findByRoundIdOrderByIdAsc(ranking).size());
        assertEquals(55,rounds.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
            group,activeSeason).size());
        assertEquals(0,rounds.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
            group,closedSeason).size());
        assertEquals(0,api("GET",brackets(closedSeason),memberJwt,null,200).size());

        // Opt-in cross-repository browser verification reuses this exact real
        // PostgreSQL/JWT fixture while the Spring random-port server is alive.
        // Ordinary Java unit tests and the API-only integration workflow are unchanged.
        if ("true".equals(System.getenv("JYNLO_SPRINT_BROWSER_QA"))) {
            var process = new ProcessBuilder(
                "node", "scripts/qa/track-sprint-browser-real.cjs")
                .inheritIO();
            var env = process.environment();
            env.put("JYNLO_SPRINT_PORT", Integer.toString(port));
            env.put("JYNLO_SPRINT_COMMUNITY_ID", group.toString());
            env.put("JYNLO_SPRINT_ACTIVE_SEASON_ID", activeSeason.toString());
            env.put("JYNLO_SPRINT_CLOSED_SEASON_ID", closedSeason.toString());
            env.put("JYNLO_SPRINT_BRACKET_ID", Long.toString(id));
            env.put("JYNLO_SPRINT_OWNER_TOKEN", ownerJwt);
            env.put("JYNLO_SPRINT_MEMBER_TOKEN", memberJwt);
            var child = process.start();
            if (!child.waitFor(160, java.util.concurrent.TimeUnit.SECONDS)) {
                child.destroyForcibly();
                fail("Real Angular Chromium Sprint check exceeded its time budget");
            }
            assertEquals(0, child.exitValue(),
                "Real Angular Chromium must display genuine JWT/PG16 Sprint in PT and EN");
        }

        // Official dead-heat path: a distinct 200 m round in the same
        // disposable database, after the read-only Chromium checks above.
        // The existing completed Olympic fixture and historical season
        // MUST remain untouched by this additional bracket.
        List<Map<String,Object>> tied200=new ArrayList<>();
        for(int i=0;i<24;i++) {
            Map<String,Object> row=new LinkedHashMap<>(rider(riders.get(i),"DRAW"));
            row.put("finishPosition",i+1);
            row.put("elapsedSeconds",BigDecimal.valueOf(i==1?10001L:10001L+i,3));
            tied200.add(row);
        }
        long tiedRound=record("Fictional 200m dead heat", "TRACK_FLYING_200",
            null,null,null,tied200);
        Map<String,Object> withoutDecision=Map.of(
            "name","Tied QA rejected", "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
            "qualifyingRoundId",tiedRound);
        api("POST",brackets(activeSeason),ownerJwt,withoutDecision,400);
        var recordedDecision=Map.of(
            "method","LAST_100",
            "orderedUsernames",List.of(riders.get(1),riders.get(0)),
            "last100Seconds",Map.of(riders.get(0),5.111,riders.get(1),5.101),
            "reference","Signed fictitious commissaires sheet #41/2026");
        Map<String,Object> withDecision=Map.of(
            "name","Official 200m tie QA",
            "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
            "qualifyingRoundId",tiedRound,
            "officialTieDecisions",List.of(recordedDecision));
        api("POST",brackets(activeSeason),memberJwt,withDecision,403);
        JsonNode official=api("POST",brackets(activeSeason),ownerJwt,withDecision,200);
        long officialId=official.path("id").asLong();
        assertTrue(officialId>0);
        assertEquals(riders.get(1),official.path("qualifyingSeeds").get(0).asText());
        assertEquals(riders.get(0),official.path("qualifyingSeeds").get(1).asText());
        assertEquals(1,official.path("officialTieDecisions").size());
        assertEquals("LAST_100",official.path("officialTieDecisions").get(0)
            .path("method").asText());
        assertEquals("fixture_sprint_owner",
            official.path("tieDecisionRecordedBy").asText());
        assertNotNull(brackets.findById(officialId).orElseThrow().getTieDecisionsJson());
        JsonNode saved=api("GET",brackets(activeSeason),memberJwt,null,200);
        boolean discovered=false;
        for(JsonNode b:saved) {
            if(b.path("id").asLong()==officialId) {
                discovered=true;
                assertEquals(1,b.path("officialTieDecisions").size());
                assertEquals(24,b.path("qualifyingSeeds").size());
            }
        }
        assertTrue(discovered,"Witnessed decision and seeds must survive PostgreSQL roundtrip");
        assertEquals(0,api("GET",brackets(closedSeason),memberJwt,null,200).size());

        // Separate third bracket: real authenticated incident/decision history.
        // The existing completed Sprint and 200 m documents remain unchanged.
        JsonNode fresh=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional incident QA","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long br=fresh.path("id").asLong();
        String key="SP"+br+"-A1";
        Map<String,Object> incident=Map.of(
            "seriesKey",key,"heatNumber",1,"incidentType","PUNCTURE",
            "details","Synthetic flat tyre, stopped before an official result");
        String incidentPath=brackets(activeSeason)+"/"+br+"/incidents";
        api("POST",incidentPath,memberJwt,incident,403);
        api("POST",incidentPath,outsiderJwt,incident,403);
        JsonNode pending=api("POST",incidentPath,ownerJwt,incident,200);
        long incidentId=pending.path("id").asLong();
        assertTrue(incidentId>0);
        assertEquals("PENDING",pending.path("status").asText());
        api("POST",incidentPath,ownerJwt,incident,409); // one pending incident
        JsonNode list=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode reported=null;
        for(JsonNode b:list)if(b.path("id").asLong()==br)reported=b;
        assertNotNull(reported);
        assertEquals(1,reported.path("incidents").size());
        assertEquals("PENDING",reported.path("incidents").get(0).path("status").asText());
        assertEquals("READY",reported.path("matches").get(0).path("status").asText());
        // Incident alone never changes a sporting result.
        var deniedHeat=Map.of(
            "name","Blocked A1","playedAt",Instant.now().toString(),
            "sport",sport,"eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
            "sprintSeriesKey",key,"sprintHeatNumber",1,
            "entries",List.of(rider(riders.get(0),"WIN"),rider(riders.get(23),"LOSS")));
        api("POST",community()+"/rounds",ownerJwt,deniedHeat,409);
        String decisionPath=incidentPath+"/"+incidentId+"/decision";
        var decision=Map.of(
            "decisionType","RESTART_REQUIRED",
            "reference","Fixture commissaires document #2026/41",
            "reason","Synthetic recognized tyre puncture; commissioners authorised a rerun");
        api("PUT",decisionPath,memberJwt,decision,403);
        JsonNode signed=api("PUT",decisionPath,ownerJwt,decision,200);
        assertEquals("RESTART_PENDING",signed.path("status").asText());
        assertEquals("RESTART_REQUIRED",signed.path("decisionType").asText());
        assertEquals("fixture_sprint_owner",signed.path("decidedBy").asText());
        api("PUT",decisionPath,ownerJwt,decision,409); // immutable audit
        // An initial restart choice alone cannot open the heat.
        api("POST",community()+"/rounds",ownerJwt,deniedHeat,409);
        String neutralUrl=incidentPath+"/"+incidentId+"/neutral-restart";
        JsonNode first=reported.path("matches").get(0);
        var neutralRestart=Map.of(
            "cause","PUNCTURE","noFaultConfirmed",true,
            "orderedRiders",List.of(first.path("riderA").asText(),
                                    first.path("riderB").asText()),
            "reference","Second documented commissaires restart document 2026/42",
            "reason","Panel expressly confirms a neutral puncture restart, same original order");
        api("PUT",neutralUrl,memberJwt,neutralRestart,403);
        Map<String,Object> neutralBad=new HashMap<>(neutralRestart);
        neutralBad.put("cause","NO_FAULT_FALL");
        api("PUT",neutralUrl,ownerJwt,neutralBad,400);
        neutralBad.put("cause","PUNCTURE");
        neutralBad.put("noFaultConfirmed",false);
        api("PUT",neutralUrl,ownerJwt,neutralBad,400);
        neutralBad.put("noFaultConfirmed",true);
        neutralBad.put("orderedRiders",List.of(first.path("riderB").asText(),
                    first.path("riderA").asText()));
        api("PUT",neutralUrl,ownerJwt,neutralBad,400);
        var approved=api("PUT",neutralUrl,ownerJwt,neutralRestart,200);
        assertEquals("NEUTRAL_RESTART_OK",approved.path("status").asText());
        assertEquals("fixture_sprint_owner",approved.path("rerunAuthorizedBy").asText());
        assertEquals(2,approved.path("rerunRiders").size());
        api("PUT",neutralUrl,ownerJwt,neutralRestart,409);
        assertEquals("READY",reported.path("matches").get(0).path("status").asText());
        // A restart never creates a result: only a subsequently raced heat does.
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            group,activeSeason,key).isEmpty());
        api("POST",community()+"/rounds",ownerJwt,deniedHeat,200);
        JsonNode updated=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode recorded=null;
        for(JsonNode b:updated)if(b.path("id").asLong()==br)recorded=b;
        assertNotNull(recorded);
        assertEquals("FINISHED",recorded.path("matches").get(0).path("status").asText());
        assertEquals(1,recorded.path("incidents").size());
        assertEquals("NEUTRAL_RESTART_OK",
                recorded.path("incidents").get(0).path("status").asText());
        assertEquals(0,api("GET",brackets(closedSeason),memberJwt,null,200).size());

        // A no-fault fall uses the same narrow documented restart contract;
        // ratifying positions is still suspended until a separate contract.
        for(String incidentKind:List.of("FALL","BICYCLE_DEFECT")) {
            var nextBracket=api("POST",brackets(activeSeason),ownerJwt,Map.of(
                    "name","Fictional no-fault incident "+incidentKind,
                    "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
                    "qualifyingRoundId",qual),200);
            long nextId=nextBracket.path("id").asLong();
            String nextSeries="SP"+nextId+"-A1";
            String nextPath=brackets(activeSeason)+"/"+nextId+"/incidents";
            var reportedMishap=api("POST",nextPath,ownerJwt,Map.of(
                    "seriesKey",nextSeries,"heatNumber",1,
                    "incidentType",incidentKind,
                    "details","Fictional no-fault or essential mechanical mishap in first heat"),200);
            String mishapUrl=nextPath+"/"+reportedMishap.path("id").asLong();
            var panelDecision=Map.of("decisionType","RESTART_REQUIRED",
                    "reference","Synthetic panel first ruling "+incidentKind+" 2026/61",
                    "reason","Commissaires expressly decide to restart after recognised mishap");
            var declared=api("PUT",mishapUrl+"/decision",ownerJwt,panelDecision,200);
            assertEquals("RESTART_PENDING",declared.path("status").asText());
            String cause=incidentKind.equals("FALL")
                    ?"NO_FAULT_FALL":"ESSENTIAL_BICYCLE_BREAKAGE";
            var restart=Map.of("cause",cause,"noFaultConfirmed",true,
                    "orderedRiders",List.of(
                        nextBracket.path("matches").get(0).path("riderA").asText(),
                        nextBracket.path("matches").get(0).path("riderB").asText()),
                    "reference","Second signed no-fault restart instruction 2026/62",
                    "reason","The same two riders and original order are expressly documented");
            assertEquals("NEUTRAL_RESTART_OK",
                    api("PUT",mishapUrl+"/neutral-restart",ownerJwt,restart,200)
                    .path("status").asText());
            assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                    group,activeSeason,nextSeries).isEmpty());
            assertTrue(nextBracket.path("matches").get(0).path("winner").isNull());
        }
        var placementBracket=api("POST",brackets(activeSeason),ownerJwt,Map.of(
                "name","Synthetic non-automatic final positions",
                "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
                "qualifyingRoundId",qual),200);
        long placementId=placementBracket.path("id").asLong();
        String placementKey="SP"+placementId+"-A1";
        String placementPath=brackets(activeSeason)+"/"+placementId+"/incidents";
        var placementReport=api("POST",placementPath,ownerJwt,Map.of(
                "seriesKey",placementKey,"heatNumber",1,"incidentType","PUNCTURE",
                "details","Fictional puncture, panel opts for positions at moment of incident"),200);
        String placementUrl=placementPath+"/"+placementReport.path("id").asLong();
        var placement=api("PUT",placementUrl+"/decision",ownerJwt,Map.of(
                "decisionType","OFFICIAL_RESULT_TO_RECORD",
                "reference","Signed fictional final positions decision 2026/66",
                "reason","Panel chose incident-time positions, not a physically disputed heat"),200);
        assertEquals("POSITIONS_PENDING",placement.path("status").asText());
        api("POST",community()+"/rounds",ownerJwt,Map.of(
                "name","Invalid physical result in place of panel ratification",
                "playedAt",Instant.now().toString(),"sport",sport,
                "eventType","TRACK_SPRINT_HEAT",
                "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
                "sprintSeriesKey",placementKey,"sprintHeatNumber",1,
                "entries",List.of(
                    rider(placementBracket.path("matches").get(0).path("riderA").asText(),"WIN"),
                    rider(placementBracket.path("matches").get(0).path("riderB").asText(),"LOSS"))),409);

        // UCI 3.2.048: the panel may instead declare incident-time positions
        // final. A second official record, not a synthetic physical heat, is
        // mandatory; exactly the two original riders must be ranked.
        String positionsUrl=placementUrl+"/official-positions";
        var originalPlacings=List.of(
                placementBracket.path("matches").get(0).path("riderB").asText(),
                placementBracket.path("matches").get(0).path("riderA").asText());
        var finalPositions=Map.of(
                "orderedRiders",originalPlacings,
                "panelPositionsConfirmed",true,
                "reference","Second distinct signed panel positions record 2026/67",
                "reason","Commissaires attest B was ahead of A at instant of neutral puncture");
        api("PUT",positionsUrl,memberJwt,finalPositions,403);
        Map<String,Object> invalidPositions=new HashMap<>(finalPositions);
        invalidPositions.put("panelPositionsConfirmed",false);
        api("PUT",positionsUrl,ownerJwt,invalidPositions,400);
        invalidPositions.put("panelPositionsConfirmed",true);
        invalidPositions.put("orderedRiders",List.of(originalPlacings.get(0),
                originalPlacings.get(0)));
        api("PUT",positionsUrl,ownerJwt,invalidPositions,400);
        invalidPositions.put("orderedRiders",List.of(originalPlacings.get(0),"not_participating"));
        api("PUT",positionsUrl,ownerJwt,invalidPositions,400);
        invalidPositions.put("orderedRiders",originalPlacings);
        invalidPositions.put("reference","Signed fictional final positions decision 2026/66");
        api("PUT",positionsUrl,ownerJwt,invalidPositions,400);
        var documented=api("PUT",positionsUrl,ownerJwt,finalPositions,200);
        assertEquals("OFFICIAL_POSITIONS",documented.path("status").asText());
        assertEquals(originalPlacings.get(0),documented.path("awardWinner").asText());
        assertEquals("fixture_sprint_owner",documented.path("awardConfirmedBy").asText());
        assertEquals(originalPlacings.get(0),documented.path("officialPositions").get(0).asText());
        assertEquals(originalPlacings.get(1),documented.path("officialPositions").get(1).asText());
        assertEquals("OFFICIAL_RESULT_TO_RECORD",documented.path("decisionType").asText());
        assertEquals("Signed fictional final positions decision 2026/66",
                documented.path("decisionReference").asText());
        api("PUT",positionsUrl,ownerJwt,finalPositions,409);
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                group,activeSeason,placementKey).isEmpty(),
                "Final incident-time placing must not fabricate a raced heat");
        JsonNode placementMatch=null;
        for(JsonNode b:api("GET",brackets(activeSeason),memberJwt,null,200))
            if(b.path("id").asLong()==placementId)
                for(JsonNode m:b.path("matches"))
                    if("A1".equals(m.path("slot").asText()))placementMatch=m;
        assertNotNull(placementMatch);
        assertEquals("FINISHED",placementMatch.path("status").asText());
        assertEquals(originalPlacings.get(0),placementMatch.path("winner").asText());
        api("POST",community()+"/rounds",ownerJwt,Map.of(
                "name","Forbidden replacement heat after official placing",
                "playedAt",Instant.now().toString(),"sport",sport,
                "eventType","TRACK_SPRINT_HEAT",
                "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
                "sprintSeriesKey",placementKey,"sprintHeatNumber",1,
                "entries",List.of(
                    rider(originalPlacings.get(1),"WIN"),
                    rider(originalPlacings.get(0),"LOSS"))),400);

        // Unintentional-fault fall under UCI 3.2.048(1): the rider NOT
        // at fault chooses their own starting position. Neither the ruling
        // nor the selected position can fabricate a raced result.
        var faultBracket=api("POST",brackets(activeSeason),ownerJwt,Map.of(
                "name","Fictional unintentional fall start choice",
                "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
                "qualifyingRoundId",qual),200);
        long faultBracketId=faultBracket.path("id").asLong();
        String faultSeries="SP"+faultBracketId+"-A1";
        JsonNode faultMatch=faultBracket.path("matches").get(0);
        String faultRider=faultMatch.path("riderA").asText();
        String nonFaultRider=faultMatch.path("riderB").asText();
        String faultIncidents=brackets(activeSeason)+"/"+faultBracketId+"/incidents";
        var fallReported=api("POST",faultIncidents,ownerJwt,Map.of(
                "seriesKey",faultSeries,"heatNumber",1,"incidentType","FALL",
                "details","Fictional competitor unintentionally causes a fall during a bend"),200);
        String faultBase=faultIncidents+"/"+fallReported.path("id").asLong();
        var finding=Map.of("decisionType","UNINTENTIONAL_FALL",
                "reference","First panel ruling on unintended fall #2026-901",
                "reason","Panel documents accidental fault and orders a repeat of this two-up heat");
        api("PUT",faultBase+"/decision",memberJwt,finding,403);
        var faultDecided=api("PUT",faultBase+"/decision",ownerJwt,finding,200);
        assertEquals("FAULT_START_PENDING",faultDecided.path("status").asText());
        api("PUT",faultBase+"/decision",ownerJwt,finding,409);
        Map<String,Object> physicalAfterFault=Map.of(
            "name","Not yet authorized race after accidental fall",
            "playedAt",Instant.now().toString(),
            "sport",sport,"eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
            "sprintSeriesKey",faultSeries,"sprintHeatNumber",1,
            "entries",List.of(rider(faultRider,"WIN"),rider(nonFaultRider,"LOSS")));
        api("POST",community()+"/rounds",ownerJwt,physicalAfterFault,409);
        String faultStartUrl=faultBase+"/unintentional-fall-start";
        Map<String,Object> startChoice=new HashMap<>(Map.of(
                "faultRider",faultRider,"nonFaultRider",nonFaultRider,
                "nonFaultStartPosition","OUTSIDE","nonFaultChoiceConfirmed",true,
                "reference","Independent victim choice record #2026-902",
                "reason","Rider not at fault expressly chooses outside starting position"));
        api("PUT",faultStartUrl,memberJwt,startChoice,403);
        Map<String,Object> badChoice=new HashMap<>(startChoice);
        badChoice.put("nonFaultChoiceConfirmed",false);
        api("PUT",faultStartUrl,ownerJwt,badChoice,400);
        badChoice.put("nonFaultChoiceConfirmed",true);
        badChoice.put("nonFaultStartPosition","MIDDLE");
        api("PUT",faultStartUrl,ownerJwt,badChoice,400);
        badChoice.put("nonFaultStartPosition","INSIDE");
        badChoice.put("nonFaultRider",faultRider);
        api("PUT",faultStartUrl,ownerJwt,badChoice,400);
        badChoice.put("nonFaultRider",nonFaultRider);
        badChoice.put("faultRider","not_a_participant");
        api("PUT",faultStartUrl,ownerJwt,badChoice,400);
        badChoice.put("faultRider",faultRider);
        badChoice.put("reference","First panel ruling on unintended fall #2026-901");
        api("PUT",faultStartUrl,ownerJwt,badChoice,400);
        var authorised=api("PUT",faultStartUrl,ownerJwt,startChoice,200);
        assertEquals("FAULT_START_OK",authorised.path("status").asText());
        assertEquals(faultRider,authorised.path("fallFaultRider").asText());
        assertEquals(nonFaultRider,authorised.path("fallNonFaultRider").asText());
        assertEquals("OUTSIDE",authorised.path("fallChosenStartPosition").asText());
        assertEquals(faultRider,authorised.path("rerunRiders").get(0).asText());
        assertEquals(nonFaultRider,authorised.path("rerunRiders").get(1).asText());
        assertEquals("fixture_sprint_owner",authorised.path("rerunAuthorizedBy").asText());
        api("PUT",faultStartUrl,ownerJwt,startChoice,409);
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                group,activeSeason,faultSeries).isEmpty());
        JsonNode beforeFaultRace=null;
        for(JsonNode b:api("GET",brackets(activeSeason),memberJwt,null,200))
            if(b.path("id").asLong()==faultBracketId)
                for(JsonNode m:b.path("matches"))
                    if("A1".equals(m.path("slot").asText()))beforeFaultRace=m;
        assertNotNull(beforeFaultRace);
        assertEquals("READY",beforeFaultRace.path("status").asText());
        assertTrue(beforeFaultRace.path("winner").isNull());
        api("POST",community()+"/rounds",ownerJwt,physicalAfterFault,200);
        assertEquals(1,rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                group,activeSeason,faultSeries).size());


        // UCI 3.2.048(3): synthetic pre-sprint balance, opponent and barrier
        // incidents. No race is recorded before a second documented choice.
        for(String cause:List.of("PRE_SPRINT_BALANCE","PRE_SPRINT_OPPONENT_CONTACT",
                "PRE_SPRINT_BARRIER_CONTACT")) {
            var preBracket=api("POST",brackets(activeSeason),ownerJwt,Map.of(
                    "name","Synthetic pre sprint "+cause,
                    "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
                    "qualifyingRoundId",qual),200);
            long preId=preBracket.path("id").asLong();
            String preSeries="SP"+preId+"-A1";
            String involvedRider=preBracket.path("matches").get(0).path("riderA").asText();
            String innocentRider=preBracket.path("matches").get(0).path("riderB").asText();
            String preIncidents=brackets(activeSeason)+"/"+preId+"/incidents";
            var preReport=api("POST",preIncidents,ownerJwt,Map.of(
                    "seriesKey",preSeries,"heatNumber",1,"incidentType",cause,
                    "details","Fictional contact or balance before sprint, starter stops race"),200);
            String preBase=preIncidents+"/"+preReport.path("id").asLong();
            var preDecision=Map.of("decisionType","PRE_SPRINT_RESTART",
                    "reference","First official pre sprint report "+preId,
                    "reason","Starter stopped the race before sprint after a recorded incident");
            api("PUT",preBase+"/decision",memberJwt,preDecision,403);
            var prePending=api("PUT",preBase+"/decision",ownerJwt,preDecision,200);
            assertEquals("FAULT_START_PENDING",prePending.path("status").asText());
            var notYetRaced=Map.of("name","No synthetic pre-sprint result",
                    "playedAt",Instant.now().toString(),"sport",sport,
                    "eventType","TRACK_SPRINT_HEAT",
                    "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
                    "sprintSeriesKey",preSeries,"sprintHeatNumber",1,
                    "entries",List.of(rider(involvedRider,"WIN"),rider(innocentRider,"LOSS")));
            api("POST",community()+"/rounds",ownerJwt,notYetRaced,409);
            String preChoiceUrl=preBase+"/pre-sprint-start";
            Map<String,Object> preChoice=new HashMap<>(Map.of(
                    "incidentRider",involvedRider,"nonFaultRider",innocentRider,
                    "nonFaultStartPosition","INSIDE","nonFaultChoiceConfirmed",true,
                    "beforeSprintConfirmed",true,"starterStoppedConfirmed",true,
                    "reference","Independent official pre sprint choice "+preId,
                    "reason","Rider not at fault explicitly selects the inside starting place"));
            api("PUT",preChoiceUrl,memberJwt,preChoice,403);
            Map<String,Object> badPre=new HashMap<>(preChoice);
            badPre.put("beforeSprintConfirmed",false);
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("beforeSprintConfirmed",true);
            badPre.put("starterStoppedConfirmed",false);
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("starterStoppedConfirmed",true);
            badPre.put("nonFaultChoiceConfirmed",false);
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("nonFaultChoiceConfirmed",true);
            badPre.put("nonFaultStartPosition","CENTER");
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("nonFaultStartPosition","INSIDE");
            badPre.put("nonFaultRider",involvedRider);
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("nonFaultRider",innocentRider);
            badPre.put("incidentRider","unrelated_rider");
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            badPre.put("incidentRider",involvedRider);
            badPre.put("reference","First official pre sprint report "+preId);
            api("PUT",preChoiceUrl,ownerJwt,badPre,400);
            var preAccepted=api("PUT",preChoiceUrl,ownerJwt,preChoice,200);
            assertEquals("FAULT_START_OK",preAccepted.path("status").asText());
            assertEquals(involvedRider,preAccepted.path("preSprintIncidentRider").asText());
            assertEquals(innocentRider,preAccepted.path("preSprintNonFaultRider").asText());
            assertEquals("INSIDE",preAccepted.path("preSprintStartPosition").asText());
            assertTrue(preAccepted.path("preSprintBeforeStartConfirmed").asBoolean());
            assertTrue(preAccepted.path("preSprintStarterStoppedConfirmed").asBoolean());
            assertEquals(innocentRider,preAccepted.path("rerunRiders").get(0).asText());
            assertEquals(involvedRider,preAccepted.path("rerunRiders").get(1).asText());
            api("PUT",preChoiceUrl,ownerJwt,preChoice,409);
            assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                    group,activeSeason,preSeries).isEmpty(),
                    "Stoppage must not create a fictitious heat");
            api("POST",community()+"/rounds",ownerJwt,notYetRaced,200);
        }

        // Official disciplinary documentation must NEVER create a winner.
        // A warning is auditable and allows manual result entry; DQ/relegation
        // stays blocked because the entrants/bracket composition can change.
        JsonNode disciplinary=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional disciplinary fixture","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long disciplinaryBracket=disciplinary.path("id").asLong();
        String disciplinarySeries="SP"+disciplinaryBracket+"-A1";
        String disciplinaryIncident=brackets(activeSeason)+"/"+disciplinaryBracket+"/incidents";
        JsonNode report=api("POST",disciplinaryIncident,ownerJwt,Map.of(
            "seriesKey",disciplinarySeries,"heatNumber",1,
            "incidentType","RULE_INFRINGEMENT",
            "details","Fictional alleged interference reported by the race organiser"),200);
        String decidePath=disciplinaryIncident+"/"+report.path("id").asLong()+"/decision";
        Map<String,Object> invalid=Map.of(
            "decisionType","DISCIPLINARY_RULING",
            "sanctionType","DISQUALIFICATION",
            "sanctionedUsername","unrelated_person",
            "reference","Official fictional commissioners minutes #204",
            "reason","Disqualification for invented interference as documented in the fictional minutes");
        api("PUT",decidePath,ownerJwt,invalid,400);
        Map<String,Object> ruling=new HashMap<>(invalid);
        ruling.put("sanctionedUsername",riders.get(23));
        api("PUT",decidePath,memberJwt,ruling,403);
        JsonNode signedRuling=api("PUT",decidePath,ownerJwt,ruling,200);
        assertEquals("ADJUDICATION_REQUIRED",signedRuling.path("status").asText());
        assertEquals("DISQUALIFICATION",signedRuling.path("sanctionType").asText());
        assertEquals(riders.get(23),signedRuling.path("sanctionedUsername").asText());
        assertEquals("fixture_sprint_owner",signedRuling.path("decidedBy").asText());
        api("PUT",decidePath,ownerJwt,ruling,409);
        api("POST",disciplinaryIncident,ownerJwt,Map.of(
            "seriesKey",disciplinarySeries,"heatNumber",1,
            "incidentType","FALL","details","Second duplicate report after a DQ"),409);
        Map<String,Object> illegalResult=new HashMap<>(deniedHeat);
        illegalResult.put("sprintSeriesKey",disciplinarySeries);
        api("POST",community()+"/rounds",ownerJwt,illegalResult,409);
        JsonNode disciplineOverview=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode found=null;
        for(JsonNode b:disciplineOverview)
            if(b.path("id").asLong()==disciplinaryBracket)found=b;
        assertNotNull(found);
        assertEquals(1,found.path("incidents").size());
        assertEquals("ADJUDICATION_REQUIRED",found.path("incidents").get(0).path("status").asText());
        assertEquals("READY",found.path("matches").get(0).path("status").asText());
        // A warning has no automatic sporting effect and leaves room to
        // record an ordinary official result explicitly afterwards.
        JsonNode caution=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional caution fixture","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long warningBracket=caution.path("id").asLong();
        String warningSeries="SP"+warningBracket+"-A1";
        String warningIncidents=brackets(activeSeason)+"/"+warningBracket+"/incidents";
        JsonNode warningReport=api("POST",warningIncidents,ownerJwt,Map.of(
            "seriesKey",warningSeries,"heatNumber",1,
            "incidentType","RULE_INFRINGEMENT",
            "details","Synthetic warning for reported irregular rider movement"),200);
        String warningDecision=warningIncidents+"/"+warningReport.path("id").asLong()+"/decision";
        JsonNode warning=api("PUT",warningDecision,ownerJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING","sanctionType","WARNING",
            "sanctionedUsername",riders.get(0),
            "reference","Synthetic commissaires protocol warning #2026/42",
            "reason","Documented fictional yellow-flag warning with no change to starters"),200);
        assertEquals("DECIDED",warning.path("status").asText());
        assertEquals("WARNING",warning.path("sanctionType").asText());
        Map<String,Object> recordedWarningHeat=new HashMap<>(deniedHeat);
        recordedWarningHeat.put("sprintSeriesKey",warningSeries);
        JsonNode actualHeat=api("POST",community()+"/rounds",ownerJwt,
            recordedWarningHeat,200);
        assertTrue(actualHeat.path("id").asLong()>0);
        JsonNode warningVisible=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode cautionOverview=null;
        for(JsonNode b:warningVisible)
            if(b.path("id").asLong()==warningBracket)cautionOverview=b;
        assertNotNull(cautionOverview);
        assertEquals("FINISHED",cautionOverview.path("matches").get(0).path("status").asText());
        assertEquals("WARNING",cautionOverview.path("incidents").get(0)
            .path("sanctionType").asText());
        assertEquals("DECIDED",cautionOverview.path("incidents").get(0)
            .path("status").asText());
        assertEquals("fixture_sprint_owner",cautionOverview.path("incidents").get(0)
            .path("decidedBy").asText());
        // Real three-up repêchage: the commissaires explicitly order a
        // TWO-rider rerun after excluding one, then the organizer records
        // a genuine heat. No athlete or WIN/LOSS is ever invented by this API.
        JsonNode reduced=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional three-up UCI rerun","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long reducedId=reduced.path("id").asLong();
        for(int i=1;i<=12;i++) {
            JsonNode bracketList=api("GET",brackets(activeSeason),ownerJwt,null,200);
            JsonNode source=null;
            for(JsonNode item:bracketList)if(item.path("id").asLong()==reducedId)source=item;
            assertNotNull(source);
            JsonNode slot=null;
            for(JsonNode item:source.path("matches"))
                if(("A"+i).equals(item.path("slot").asText()))slot=item;
            assertNotNull(slot);
            record("Fictional UCI A"+i,"TRACK_SPRINT_HEAT",
                "SPRINT_THIRTY_SECOND_FINAL","SP"+reducedId+"-A"+i,1,
                List.of(rider(slot.path("riderA").asText(),"WIN"),
                        rider(slot.path("riderB").asText(),"LOSS")));
        }
        JsonNode full=api("GET",brackets(activeSeason),memberJwt,null,200), candidate=null;
        for(JsonNode b:full)if(b.path("id").asLong()==reducedId)candidate=b;
        assertNotNull(candidate);
        JsonNode trio=null;
        for(JsonNode m:candidate.path("matches"))
            if("U1".equals(m.path("slot").asText()))trio=m;
        assertNotNull(trio);
        assertEquals("READY",trio.path("status").asText());
        assertTrue(trio.hasNonNull("riderC"));
        String riderA=trio.path("riderA").asText(), riderB=trio.path("riderB").asText(),
            removed=trio.path("riderC").asText();
        String base=brackets(activeSeason)+"/"+reducedId+"/incidents";
        JsonNode newReport=api("POST",base,ownerJwt,Map.of(
            "seriesKey","SP"+reducedId+"-U1","heatNumber",1,
            "incidentType","RULE_INFRINGEMENT",
            "details","Synthetic irregular favouring of one athlete in a three-up heat"),200);
        String rulingUrl=base+"/"+newReport.path("id").asLong()+"/decision";
        api("PUT",rulingUrl,ownerJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING","sanctionType","RELEGATION",
            "sanctionedUsername",removed,
            "reference","Synthetic official commissaires document U1-2026",
            "reason","Documented fictional interference favouring another racer"),200);
        String rerunUrl=base+"/"+newReport.path("id").asLong()+"/rerun";
        Map<String,Object> rerun=Map.of(
            "orderedRiders",List.of(riderB,riderA),
            "reference","Separate official restart order U1-2026",
            "reason","Witnessed two-rider rerun following removal of the relegated rider");
        api("PUT",rerunUrl,memberJwt,rerun,403);
        api("PUT",rerunUrl,outsiderJwt,rerun,403);
        api("PUT",rerunUrl,ownerJwt,Map.of(
            "orderedRiders",List.of(riderA,removed),
            "reference","Invalid restart with sanctioned competitor",
            "reason","This deliberately tries to reinstate a disqualified rider"),400);
        JsonNode authorized=api("PUT",rerunUrl,ownerJwt,rerun,200);
        assertEquals("RESTART_AUTHORIZED",authorized.path("status").asText());
        assertEquals("RELEGATION",authorized.path("sanctionType").asText());
        assertEquals(List.of(riderB,riderA),List.of(
            authorized.path("rerunRiders").get(0).asText(),
            authorized.path("rerunRiders").get(1).asText()));
        assertEquals("fixture_sprint_owner",authorized.path("rerunAuthorizedBy").asText());
        api("PUT",rerunUrl,ownerJwt,rerun,409);
        api("POST",base,ownerJwt,Map.of(
            "seriesKey","SP"+reducedId+"-U1","heatNumber",1,
            "incidentType","FALL","details","Duplicate attempt to change this same rerun"),409);
        JsonNode rerunList=api("GET",brackets(activeSeason),memberJwt,null,200),
            rerunBracket=null,rerunMatch=null;
        for(JsonNode b:rerunList)if(b.path("id").asLong()==reducedId)rerunBracket=b;
        assertNotNull(rerunBracket);
        for(JsonNode m:rerunBracket.path("matches"))
            if("U1".equals(m.path("slot").asText()))rerunMatch=m;
        assertNotNull(rerunMatch);
        assertEquals("READY",rerunMatch.path("status").asText());
        assertEquals(riderB,rerunMatch.path("riderA").asText());
        assertEquals(riderA,rerunMatch.path("riderB").asText());
        assertTrue(rerunMatch.path("riderC").isNull());
        assertTrue(rerunMatch.path("winner").isNull());
        record("Fictional witnessed two-up U1 rerun",
            "TRACK_SPRINT_REPECHAGE","SPRINT_REPECHAGE_1_32",
            "SP"+reducedId+"-U1",1,
            List.of(rider(riderB,"WIN"),rider(riderA,"LOSS")));
        JsonNode rerunResultList=api("GET",brackets(activeSeason),memberJwt,null,200),
            resulting=null,finished=null;
        for(JsonNode b:rerunResultList)if(b.path("id").asLong()==reducedId)resulting=b;
        assertNotNull(resulting);
        for(JsonNode m:resulting.path("matches"))
            if("U1".equals(m.path("slot").asText()))finished=m;
        assertNotNull(finished);
        assertEquals("FINISHED",finished.path("status").asText());
        assertEquals(riderB,finished.path("winner").asText());
        assertEquals(1,resulting.path("incidents").size());
        assertEquals("RESTART_AUTHORIZED",resulting.path("incidents").get(0)
            .path("status").asText());
        // Official event-wide UCI 3.2.048 bis interruption on an untouched
        // independent 24-rider bracket. No round or ranking is manufactured.
        JsonNode interrupted=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional permanently interrupted sprint","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long interruptedId=interrupted.path("id").asLong();
        String interruptionPath=brackets(activeSeason)+"/"+interruptedId+"/interruption";
        var permanentClosureDecision=Map.of("reference","Fictional commissioners minutes #2026/79",
            "reason","Venue evacuation forced a permanent termination before quarterfinals");
        api("PUT",interruptionPath,memberJwt,permanentClosureDecision,403);
        api("PUT",interruptionPath,outsiderJwt,permanentClosureDecision,403);
        JsonNode stopped=api("PUT",interruptionPath,ownerJwt,permanentClosureDecision,200);
        assertEquals("BEFORE_QUARTERS",stopped.path("interruption").path("stage").asText());
        assertTrue(stopped.path("interruption").path("noFinalClassification").asBoolean());
        assertEquals(0,stopped.path("interruption").path("regulatoryPlacings").size());
        assertEquals("fixture_sprint_owner",
            stopped.path("interruption").path("recordedBy").asText());
        assertTrue(stopped.path("matches").get(0).path("winner").isNull());
        api("PUT",interruptionPath,ownerJwt,permanentClosureDecision,409);
        JsonNode closedRead=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode closedBracket=null;
        for(JsonNode b:closedRead)if(b.path("id").asLong()==interruptedId)closedBracket=b;
        assertNotNull(closedBracket);
        assertEquals("BEFORE_QUARTERS",closedBracket.path("interruption").path("stage").asText());
        api("DELETE",brackets(activeSeason)+"/"+interruptedId,ownerJwt,null,409);
        api("POST",community()+"/rounds",ownerJwt,Map.of(
            "name","Forbidden race after definitive stop",
            "playedAt",Instant.now().toString(),
            "sport",sport,"eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
            "sprintSeriesKey","SP"+interruptedId+"-A1","sprintHeatNumber",1,
            "entries",List.of(rider(riders.get(0),"WIN"),
                rider(riders.get(23),"LOSS"))),409);
        // UCI 3.2.048 intentional fall, only one-heat two-rider A1.
        // The official decision is a separate record, NOT a fake raced round.
        JsonNode twoUp=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional referee-awarded heat","format",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long awardBracket=twoUp.path("id").asLong();
        String awardSeries="SP"+awardBracket+"-A1";
        String incidents=brackets(activeSeason)+"/"+awardBracket+"/incidents";
        JsonNode fall=api("POST",incidents,ownerJwt,Map.of(
            "seriesKey",awardSeries,"heatNumber",1,
            "incidentType","FALL",
            "details","Fictional intentional fall alleged on first single heat"),200);
        long fallId=fall.path("id").asLong();
        String awardRulingUrl=incidents+"/"+fallId+"/decision";
        api("PUT",awardRulingUrl,ownerJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING",
            "sanctionType","DISQUALIFICATION",
            "sanctionedUsername",riders.get(23),
            "reference","Fictional signed DQ decision #2026/83",
            "reason","Commissaires declare documented intentional fall in the first sprint heat"),200);
        String awardUrl=incidents+"/"+fallId+"/official-award";
        var officialAward=Map.of(
            "winnerUsername",riders.get(0),
            "basis","INTENTIONAL_FALL",
            "reference","Separate fictional official result sheet #2026/84",
            "reason","Other rider declared winner by commissaires after intentional fall");
        api("PUT",awardUrl,memberJwt,officialAward,403);
        api("PUT",awardUrl,outsiderJwt,officialAward,403);
        api("PUT",awardUrl,ownerJwt,Map.of(
            "winnerUsername",riders.get(23),"basis","INTENTIONAL_FALL",
            "reference","Invalid fictitious award for sanctioned rider",
            "reason","A deliberate attempt to award victory to disqualified cyclist"),400);
        api("PUT",awardUrl,ownerJwt,Map.of(
            "winnerUsername",riders.get(0),"basis","UNRECORDED_INFRINGEMENT",
            "reference","Invalid fictitious legal basis",
            "reason","Unsupported reason must never advance the bracket"),400);
        JsonNode confirmed=api("PUT",awardUrl,ownerJwt,officialAward,200);
        assertEquals("OFFICIAL_HEAT_AWARDED",confirmed.path("status").asText());
        assertEquals(riders.get(0),confirmed.path("awardWinner").asText());
        assertEquals("fixture_sprint_owner",confirmed.path("awardConfirmedBy").asText());
        api("PUT",awardUrl,ownerJwt,officialAward,409);
        JsonNode after=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode chosen=null;
        for(JsonNode b:after)if(b.path("id").asLong()==awardBracket)chosen=b;
        assertNotNull(chosen);
        JsonNode a1=null;
        for(JsonNode m:chosen.path("matches"))
            if("A1".equals(m.path("slot").asText()))a1=m;
        assertNotNull(a1);
        assertEquals("FINISHED",a1.path("status").asText());
        assertEquals(riders.get(0),a1.path("winner").asText());
        assertEquals(1,chosen.path("incidents").size());
        assertEquals("OFFICIAL_HEAT_AWARDED",
            chosen.path("incidents").get(0).path("status").asText());
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            group,activeSeason,awardSeries).isEmpty(),
            "An awarded result must never manufacture a physical round");
        // The result is definitive; no organizer may record an extra A1 heat.
        api("POST",community()+"/rounds",ownerJwt,Map.of(
            "name","Invalid synthetic second match",
            "playedAt",Instant.now().toString(),"sport",sport,
            "eventType","TRACK_SPRINT_HEAT","eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
            "sprintSeriesKey",awardSeries,"sprintHeatNumber",1,
            "entries",List.of(rider(riders.get(0),"WIN"),rider(riders.get(23),"LOSS"))),400);
        // Guardrail for unresolved original TWO-rider sanctions: the U/W
        // three-up restart endpoint MUST NOT be usable to invent a winner,
        // remove a rider or bypass the held official decision.
        JsonNode originalTwo=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional two-rider disciplinary guard",
                "format","OLYMPIC_TWENTY_FOUR_REPECHAGE",
                "qualifyingRoundId",qual),200);
        long twoId=originalTwo.path("id").asLong();
        String twoSeries="SP"+twoId+"-A1";
        String twoIncidentPath=brackets(activeSeason)+"/"+twoId+"/incidents";
        JsonNode twoReport=api("POST",twoIncidentPath,ownerJwt,Map.of(
            "seriesKey",twoSeries,"heatNumber",1,
            "incidentType","RULE_INFRINGEMENT",
            "details","Fictitious original two-rider sprint incident requiring review"),200);
        String twoDecisionPath=twoIncidentPath+"/"+twoReport.path("id").asLong()+"/decision";
        api("PUT",twoDecisionPath,memberJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING","sanctionType","DISQUALIFICATION",
            "sanctionedUsername",riders.get(0),
            "reference","Fictional panel decision two-rider #81",
            "reason","Synthetic incident awaiting commissioner result"),403);
        JsonNode twoDecision=api("PUT",twoDecisionPath,ownerJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING","sanctionType","DISQUALIFICATION",
            "sanctionedUsername",riders.get(0),
            "reference","Fictional panel decision two-rider #81",
            "reason","Synthetic incident awaiting commissioner result"),200);
        assertEquals("ADJUDICATION_REQUIRED",twoDecision.path("status").asText());
        assertEquals("DISQUALIFICATION",twoDecision.path("sanctionType").asText());
        api("PUT",twoIncidentPath+"/"+twoReport.path("id").asLong()+"/rerun",
            ownerJwt,Map.of("orderedRiders",List.of(riders.get(23),riders.get(0)),
                "reference","Invalid two-rider restart #81",
                "reason","Cannot reuse the special three-up restart for an A1 match"),400);
        api("POST",twoIncidentPath,ownerJwt,Map.of(
            "seriesKey",twoSeries,"heatNumber",1,
            "incidentType","OTHER",
            "details","Second report incorrectly attempts to release blocked slot"),409);
        api("POST",community()+"/rounds",ownerJwt,Map.of(
            "name","Attempted winner without adjudication",
            "playedAt",Instant.now().toString(),"sport",sport,
            "eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline","SPRINT_THIRTY_SECOND_FINAL",
            "sprintSeriesKey",twoSeries,"sprintHeatNumber",1,
            "entries",List.of(rider(riders.get(23),"WIN"),
                rider(riders.get(0),"LOSS"))),409);
        JsonNode twoOverview=api("GET",brackets(activeSeason),memberJwt,null,200);
        JsonNode guarded=null;
        for(JsonNode b:twoOverview)if(b.path("id").asLong()==twoId)guarded=b;
        assertNotNull(guarded);
        assertTrue(guarded.path("matches").get(0).path("winner").isNull());
        assertEquals("READY",guarded.path("matches").get(0).path("status").asText());
        assertEquals("ADJUDICATION_REQUIRED",guarded.path("incidents").get(0)
            .path("status").asText());

        // Signed BO3 heat decisions: REAL JWT/HTTP + PostgreSQL16. All riders
        // and documents below are fictional in this disposable CI database.
        JsonNode bo3Bracket=api("POST",brackets(activeSeason),ownerJwt,
            Map.of("name","Fictional BO3 adjudication with prior rounds",
                "format","OLYMPIC_TWENTY_FOUR_REPECHAGE","qualifyingRoundId",qual),200);
        long bo3Id=bo3Bracket.path("id").asLong();
        for(String phase:new String[]{"A","U","C","V","E","W"}) {
            int count=switch(phase) {
                case "A" -> 12; case "U","C" -> phase.equals("C")?8:4;
                case "V" -> 4; case "E" -> 6; default -> 2;
            };
            for(int i=1;i<=count;i++) {
                String slot=phase+i;
                JsonNode matchInBracket=null;
                JsonNode all=api("GET",brackets(activeSeason),ownerJwt,null,200);
                for(JsonNode b:all)if(b.path("id").asLong()==bo3Id)
                    for(JsonNode m:b.path("matches"))
                        if(slot.equals(m.path("slot").asText()))matchInBracket=m;
                assertNotNull(matchInBracket,"Missing "+slot+" in BO3 fixture");
                assertEquals("READY",matchInBracket.path("status").asText(),slot);
                List<Map<String,Object>> lineup=new ArrayList<>();
                String winner=matchInBracket.path("riderA").asText();
                for(String field:List.of("riderA","riderB","riderC"))
                    if(matchInBracket.hasNonNull(field)) {
                        String name=matchInBracket.path(field).asText();
                        lineup.add(rider(name,name.equals(winner)?"WIN":"LOSS"));
                    }
                record("Actual synthetic "+slot,
                    phase.equals("U")||phase.equals("V")||phase.equals("W")
                        ?"TRACK_SPRINT_REPECHAGE":"TRACK_SPRINT_HEAT",
                    matchInBracket.path("phase").asText(),
                    matchInBracket.path("seriesKey").asText(),1,lineup);
            }
        }
        JsonNode bo3Q=null;
        JsonNode allBo3=api("GET",brackets(activeSeason),memberJwt,null,200);
        for(JsonNode b:allBo3)if(b.path("id").asLong()==bo3Id)
            for(JsonNode m:b.path("matches"))
                if("Q1".equals(m.path("slot").asText()))bo3Q=m;
        assertNotNull(bo3Q);
        assertEquals("READY",bo3Q.path("status").asText());
        String bo3Series="SP"+bo3Id+"-Q1";
        String officialA=bo3Q.path("riderA").asText();
        String officialB=bo3Q.path("riderB").asText();
        String adjudications=brackets(activeSeason)+"/"+bo3Id+"/incidents";
        for(int heatNumber=1;heatNumber<=2;heatNumber++) {
            JsonNode bo3HeatReport=api("POST",adjudications,ownerJwt,Map.of(
                "seriesKey",bo3Series,"heatNumber",heatNumber,
                "incidentType","FALL",
                "details","Synthetic intentional fall for numbered BO3 heat"),200);
            long recordedId=bo3HeatReport.path("id").asLong();
            api("PUT",adjudications+"/"+recordedId+"/decision",ownerJwt,Map.of(
                "decisionType","DISCIPLINARY_RULING",
                "sanctionType","RELEGATION",
                "sanctionedUsername",officialB,
                "reference","Fictional signed per-heat decision "+heatNumber+"/2026",
                "reason","Commissioners document an intentional fall and relegate the fictional rider"),200);
            var allowed=Map.of(
                "basis","INTENTIONAL_FALL","winnerUsername",officialA,
                "reference","Fictional second official per-heat record "+heatNumber+"/2026",
                "reason","Officially declared heat victory after documented intentional fall");
            String officialUrl=adjudications+"/"+recordedId+"/official-award";
            api("PUT",officialUrl,memberJwt,allowed,403);
            JsonNode credited=api("PUT",officialUrl,ownerJwt,allowed,200);
            assertEquals(heatNumber,credited.path("heatNumber").asInt());
            assertEquals("OFFICIAL_HEAT_AWARDED",credited.path("status").asText());
            api("PUT",officialUrl,ownerJwt,allowed,409);
            JsonNode overviewAfter=null;
            for(JsonNode b:api("GET",brackets(activeSeason),memberJwt,null,200))
                if(b.path("id").asLong()==bo3Id)overviewAfter=b;
            assertNotNull(overviewAfter);
            JsonNode q1After=null;
            for(JsonNode m:overviewAfter.path("matches"))
                if("Q1".equals(m.path("slot").asText()))q1After=m;
            assertNotNull(q1After);
            assertEquals(heatNumber, q1After.path("winsA").asInt());
            assertEquals(heatNumber==1?"IN_PROGRESS":"FINISHED",
                q1After.path("status").asText());
            if(heatNumber==1) assertTrue(q1After.path("winner").isNull());
            else assertEquals(officialA,q1After.path("winner").asText());
            assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                group,activeSeason,bo3Series).isEmpty(),
                "Signed rulings must not invent any physical rounds");
        }

        JsonNode q2=null;
        for(JsonNode b:api("GET",brackets(activeSeason),ownerJwt,null,200))
            if(b.path("id").asLong()==bo3Id)
                for(JsonNode m:b.path("matches"))
                    if("Q2".equals(m.path("slot").asText()))q2=m;
        assertNotNull(q2);
        String seriesDq=q2.path("seriesKey").asText();
        String surviving=q2.path("riderA").asText();
        JsonNode dq=api("POST",adjudications,ownerJwt,Map.of(
            "seriesKey",seriesDq,"heatNumber",1,"incidentType","FALL",
            "details","Fictional intentional fall during sprint"),200);
        String dqPath=adjudications+"/"+dq.path("id").asLong();
        api("PUT",dqPath+"/decision",ownerJwt,Map.of(
            "decisionType","DISCIPLINARY_RULING","sanctionType","DISQUALIFICATION",
            "sanctionedUsername",q2.path("riderB").asText(),
            "reference","Synthetic documented decision 2026/50",
            "reason","Jury excludes rider due to fictional intentional fall"),200);
        var verdict=Map.of("basis","INTENTIONAL_FALL","winnerUsername",surviving,
            "reference","Separate synthetic official series ruling 2026/51",
            "reason","Jury expressly declares the other rider the BO3 series winner");
        api("PUT",dqPath+"/official-series-verdict",memberJwt,verdict,403);
        assertEquals("OFFICIAL_SERIES_AWARDED",
            api("PUT",dqPath+"/official-series-verdict",ownerJwt,verdict,200)
            .path("status").asText());
        api("PUT",dqPath+"/official-series-verdict",ownerJwt,verdict,409);
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            group,activeSeason,seriesDq).isEmpty());
        // UCI 3.2.011: only a documented request by the president starts review.
        String reviewPath=dqPath+"/reviews";
        var request=Map.of("presidentName","Synthetic Commissaires President",
            "requestReference","Synthetic request record 2026/72",
            "reason","Panel president requests reconsideration of referee verdict",
            "originalJudgeRefereeDecision",true);
        api("POST",reviewPath,memberJwt,request,403);
        var start=api("POST",reviewPath,ownerJwt,request,200);
        assertEquals("REQUESTED",start.path("action").asText());
        api("POST",reviewPath,ownerJwt,request,409);
        assertEquals(1,api("GET",reviewPath,memberJwt,null,200).size());
        var uphold=Map.of("outcome","UPHELD",
            "presidentName","Synthetic Commissaires President",
            "panelReviewReference","Synthetic panel review record 2026/73",
            "presidentDecisionReference","Synthetic chair final decision 2026/74",
            "reason","The panel formally reviewed the evidence and maintained the ruling");
        assertEquals("UPHELD",api("PUT",reviewPath+"/decision",ownerJwt,uphold,200)
                .path("action").asText());
        api("PUT",reviewPath+"/decision",ownerJwt,uphold,409);
        api("POST",reviewPath,ownerJwt,request,200);
        var overturn=Map.of("outcome","OVERTURNED",
            "presidentName","Synthetic Commissaires President",
            "panelReviewReference","Synthetic panel review record 2026/75",
            "presidentDecisionReference","Synthetic chair revocation 2026/76",
            "reason","The panel reviewed and the president explicitly overturned the award");
        assertEquals("OVERTURNED",
            api("PUT",reviewPath+"/decision",ownerJwt,overturn,200).path("action").asText());
        assertEquals(4,api("GET",reviewPath,memberJwt,null,200).size());
        api("POST",reviewPath,ownerJwt,request,409);
        JsonNode q2After=null;
        for(JsonNode b:api("GET",brackets(activeSeason),ownerJwt,null,200))
            if(b.path("id").asLong()==bo3Id)
                for(JsonNode m:b.path("matches"))
                    if("Q2".equals(m.path("slot").asText()))q2After=m;
        assertNotNull(q2After);
        assertTrue(q2After.path("winner").isNull(),
            "Overturned BO3 award must not remain an effective qualification");
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            group,activeSeason,seriesDq).isEmpty(),
            "Review decisions cannot fabricate physical rounds");
        // A revocation does not permit recording any dependent heat.
        api("POST",community()+"/rounds",ownerJwt,Map.of(
            "name","Forbidden Q2 after chair revocation",
            "playedAt",Instant.now().toString(),"sport",sport,
            "eventType","TRACK_SPRINT_HEAT",
            "eventDiscipline",q2.path("phase").asText(),
            "sprintSeriesKey",seriesDq,"sprintHeatNumber",1,
            "entries",List.of(rider(q2.path("riderA").asText(),"WIN"),
                    rider(q2.path("riderB").asText(),"LOSS"))),409);
        String correctionPath=reviewPath+"/rectification";
        var correct=Map.of(
            "presidentName","Synthetic Commissaires President",
            "instructionReference","Fictional explicit continuation order 2026/77",
            "overturnedDecisionReference","Synthetic chair revocation 2026/76",
            "reason","The president expressly cancels the disqualification and orders both original riders to resume",
            "sanctionAnnulledByPresident",true,
            "orderedRiders",List.of(q2.path("riderA").asText(),q2.path("riderB").asText()),
            "nextHeatNumber",1);
        api("PUT",correctionPath,memberJwt,correct,403);
        Map<String,Object> invalidCorrection=new HashMap<>(correct);
        invalidCorrection.put("sanctionAnnulledByPresident",false);
        api("PUT",correctionPath,ownerJwt,invalidCorrection,400);
        invalidCorrection.put("sanctionAnnulledByPresident",true);
        invalidCorrection.put("orderedRiders",
                List.of(q2.path("riderB").asText(),q2.path("riderA").asText()));
        api("PUT",correctionPath,ownerJwt,invalidCorrection,400);
        invalidCorrection.put("orderedRiders",correct.get("orderedRiders"));
        invalidCorrection.put("nextHeatNumber",2);
        api("PUT",correctionPath,ownerJwt,invalidCorrection,409);
        invalidCorrection.put("nextHeatNumber",1);
        invalidCorrection.put("overturnedDecisionReference","Incorrect revocation document ref");
        api("PUT",correctionPath,ownerJwt,invalidCorrection,400);
        var continuation=api("PUT",correctionPath,ownerJwt,correct,200);
        assertEquals("RECTIFIED",continuation.path("action").asText());
        assertEquals("RESUME_SAME_RIDERS",continuation.path("remedyKind").asText());
        assertEquals(1,continuation.path("nextHeatNumber").asInt());
        assertEquals(2,continuation.path("orderedRiders").size());
        api("PUT",correctionPath,ownerJwt,correct,409);
        api("POST",reviewPath,ownerJwt,request,409);
        assertEquals(5,api("GET",reviewPath,memberJwt,null,200).size());
        // The historical official series award remains stored but is no longer
        // effective, and correction does not invent a physical heat.
        assertTrue(rounds.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            group,activeSeason,seriesDq).isEmpty());
        JsonNode q2Corrected=null;
        for(JsonNode b:api("GET",brackets(activeSeason),ownerJwt,null,200))
            if(b.path("id").asLong()==bo3Id)
                for(JsonNode m:b.path("matches"))
                    if("Q2".equals(m.path("slot").asText()))q2Corrected=m;
        assertNotNull(q2Corrected);
        assertTrue(q2Corrected.path("winner").isNull());
        assertEquals("READY",q2Corrected.path("status").asText());
        record("Fictional Q2 physical heat only after official continuation",
            "TRACK_SPRINT_HEAT",q2.path("phase").asText(),seriesDq,1,
            List.of(rider(q2.path("riderA").asText(),"WIN"),
                    rider(q2.path("riderB").asText(),"LOSS")));
        JsonNode q2Raced=null;
        for(JsonNode b:api("GET",brackets(activeSeason),memberJwt,null,200))
            if(b.path("id").asLong()==bo3Id)
                for(JsonNode m:b.path("matches"))
                    if("Q2".equals(m.path("slot").asText()))q2Raced=m;
        assertNotNull(q2Raced);
        assertEquals("IN_PROGRESS",q2Raced.path("status").asText());
        assertEquals(1,q2Raced.path("winsA").asInt());
        assertTrue(q2Raced.path("winner").isNull(),
            "One physical heat cannot infer the BO3 winner");


        assertEquals(0,api("GET",brackets(closedSeason),memberJwt,null,200).size());
    }
}
