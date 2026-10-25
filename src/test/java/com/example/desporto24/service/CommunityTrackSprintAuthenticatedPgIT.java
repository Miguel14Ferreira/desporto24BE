package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
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
    }
}
