package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunitySessionService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.service.CommunityTournamentFinalizationService;
import com.example.desporto24.service.CommunityTournamentResultService;
import com.example.desporto24.service.CommunityTournamentScheduleService;
import com.example.desporto24.service.CommunityTournamentService;
import com.example.desporto24.service.CommunityTournamentStandingsService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Isolated, transactional JPA + real services + real controller + real exception
 * advice. Each test seeds throwaway H2 profiles, private communities, memberships,
 * individual tournaments and fixtures. The test transaction is rolled back.
 *
 * MockMvc standalone principals represent authenticated identities; this does
 * NOT test JWT signatures, the live Spring Security filter chain, or LIVE users.
 * It does test actual persisted ownership checks and HTTP 403 status mappings.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:community-tournament-role-http;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class CommunityTournamentIsolatedRoleHttpH2Test {

    @Autowired private EntityManager em;
    @Autowired private ProfileRepository profiles;
    @Autowired private CommunityRepository communities;
    @Autowired private CommunityMemberRepository members;
    @Autowired private CommunityJoinRequestRepository joinRequests;
    @Autowired private CommunityTournamentRepository tournaments;
    @Autowired private CommunityTournamentParticipantRepository participants;
    @Autowired private CommunityTournamentMatchRepository matches;
    @Autowired private CommunityTournamentTeamMatchRepository teamMatches;

    private MockMvc mvc;
    private Long sharedId;
    private Long exclusiveId;
    private Long draftId;
    private Long activeId;
    private Long activeMatchId;

    private static final String OWNER = "isolated-role-owner";
    private static final String ADMIN = "isolated-role-admin";
    private static final String MEMBER = "isolated-role-member";
    private static final String OUTSIDER = "isolated-role-outsider";

    @BeforeEach
    void prepareRealH2Fixture() {
        Profile owner = profile(OWNER);
        Profile admin = profile(ADMIN);
        Profile member = profile(MEMBER);
        profile(OUTSIDER);

        Community shared = community("Temporary private league", owner);
        Community exclusive = community("Owner-only secret fixture", owner);
        sharedId = shared.getId();
        exclusiveId = exclusive.getId();

        join(shared, owner, CommunityRole.OWNER);
        join(shared, admin, CommunityRole.ADMIN);
        join(shared, member, CommunityRole.MEMBER);
        join(exclusive, owner, CommunityRole.OWNER);

        CommunityTournament draft = tournament(shared, "Pending schedule", TournamentStatus.DRAFT);
        CommunityTournament active = tournament(shared, "Active competition", TournamentStatus.IN_PROGRESS);
        // A second, private-community tournament must remain invisible to the
        // member who belongs to the shared group, even when they guess its ID.
        tournament(exclusive, "Private unrelated competition", TournamentStatus.DRAFT);
        draftId = draft.getId();
        activeId = active.getId();

        enrolled(draft, owner);
        enrolled(draft, member);
        CommunityTournamentParticipant first = enrolled(active, owner);
        CommunityTournamentParticipant second = enrolled(active, member);
        CommunityTournamentMatch activeMatch = CommunityTournamentMatch.builder()
                .tournament(active).homeParticipant(first).awayParticipant(second)
                .roundNumber(1).build();
        em.persist(activeMatch);
        em.flush();
        activeMatchId = activeMatch.getId();
        em.clear();

        var management = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class),
                joinRequests, profiles, mock(NotificationsRepository.class),
                mock(ApplicationEventPublisher.class));
        var tournamentService = new CommunityTournamentService(
                communities, members, tournaments, participants, matches, teamMatches, profiles);
        var schedule = new CommunityTournamentScheduleService(
                profiles, members, tournaments, participants, matches);
        var result = new CommunityTournamentResultService(
                profiles, members, tournaments, matches);
        var standings = new CommunityTournamentStandingsService(
                profiles, members, tournaments, participants, matches);

        var communityController = new CommunityController(management,
                mock(CommunityCompetitionService.class),
                mock(CommunityHistoricalPlayerLinkService.class),
                mock(CommunityImportService.class),
                mock(CommunityStandingsArchiveService.class),
                mock(CommunityHistoricalMatchPreviewService.class),
                mock(CommunityHistoricalMatchArchiveService.class),
                mock(CommunitySessionService.class));
        var tournamentController = new CommunityTournamentController(
                tournamentService, schedule, result, standings,
                mock(CommunityTournamentFinalizationService.class));

        mvc = MockMvcBuilders.standaloneSetup(communityController, tournamentController)
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    @Test
    void ownerAdminMemberReadRealPrivateLeagueWhileOutsiderAndCrossCommunityReadsAreDenied() throws Exception {
        for (String actor : List.of(OWNER, ADMIN, MEMBER)) {
            mvc.perform(get("/api/communities/{id}", sharedId).principal(as(actor)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.myRole").value(
                            actor.equals(OWNER) ? "OWNER" : actor.equals(ADMIN) ? "ADMIN" : "MEMBER"));
            mvc.perform(get("/api/communities/{id}/tournaments", sharedId)
                            .principal(as(actor)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tournaments.length()").value(2));
            mvc.perform(get("/api/communities/{id}/tournaments/{tid}", sharedId, activeId)
                            .principal(as(actor)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }

        mvc.perform(get("/api/communities/{id}", exclusiveId).principal(as(MEMBER)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/communities/{id}", sharedId).principal(as(OUTSIDER)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));
        mvc.perform(get("/api/communities/{id}/tournaments", sharedId)
                        .principal(as(OUTSIDER)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/communities/{id}/tournaments/{tid}", sharedId, activeId)
                        .principal(as(OUTSIDER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void participantAndOutsiderCannotPublishScheduleOrChangeRealPersistedResult() throws Exception {
        String schedulePath = "/api/communities/{id}/tournaments/{tid}/schedule";
        String resultPath = "/api/communities/{id}/tournaments/{tid}/matches/{mid}/result";
        for (String actor : List.of(MEMBER, OUTSIDER)) {
            mvc.perform(post(schedulePath, sharedId, draftId).principal(as(actor)))
                    .andExpect(status().isForbidden());
            mvc.perform(put(resultPath, sharedId, activeId, activeMatchId)
                            .principal(as(actor))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"homeScore\":2,\"awayScore\":1}"))
                    .andExpect(status().isForbidden());
            mvc.perform(post("/api/communities/{id}/tournaments", sharedId)
                            .principal(as(actor))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Unauthorized tournament\"}"))
                    .andExpect(status().isForbidden());
        }
        em.flush();
        em.clear();
        assertFalse(matches.existsByTournamentId(draftId), "unauthorized schedule must not create fixtures");
        var unchanged = matches.findById(activeMatchId).orElseThrow();
        assertEquals(TournamentMatchStatus.SCHEDULED, unchanged.getStatus());
        assertNull(unchanged.getHomeScore());
        assertNull(unchanged.getAwayScore());
        assertEquals(TournamentStatus.DRAFT, tournaments.findById(draftId).orElseThrow().getStatus());
    }

    @Test
    void ownerPublishesOnlyTheirScheduleAndAdminScoresWhileMemberReadsThePersistedResult() throws Exception {
        mvc.perform(post("/api/communities/{id}/tournaments/{tid}/schedule", sharedId, draftId)
                        .principal(as(OWNER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mvc.perform(get("/api/communities/{id}/tournaments/{tid}/matches", sharedId, draftId)
                        .principal(as(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches.length()").value(1));

        mvc.perform(put("/api/communities/{id}/tournaments/{tid}/matches/{mid}/result",
                        sharedId, activeId, activeMatchId)
                        .principal(as(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"homeScore\":2,\"awayScore\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        em.flush();
        em.clear();
        var recorded = matches.findById(activeMatchId).orElseThrow();
        assertEquals(TournamentMatchStatus.COMPLETED, recorded.getStatus());
        assertEquals(2, recorded.getHomeScore());
        assertEquals(1, recorded.getAwayScore());

        mvc.perform(get("/api/communities/{id}/tournaments/{tid}/standings", sharedId, activeId)
                        .principal(as(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedMatches").value(1))
                .andExpect(jsonPath("$.standings.length()").value(2));
        mvc.perform(get("/api/communities/{id}/tournaments/{tid}/standings", sharedId, activeId)
                        .principal(as(OUTSIDER)))
                .andExpect(status().isForbidden());
    }

    private Profile profile(String username) {
        Profile p = new Profile();
        p.setUsername(username);
        em.persist(p);
        return p;
    }

    private Community community(String name, Profile owner) {
        Community c = Community.builder().name(name).owner(owner)
                .visibility(CommunityVisibility.PRIVATE)
                .rankingsEnabled(true).createdAt(Instant.now()).build();
        em.persist(c);
        return c;
    }

    private void join(Community c, Profile profile, CommunityRole role) {
        em.persist(CommunityMember.builder().community(c).profile(profile)
                .role(role).joinedAt(Instant.now()).build());
    }

    private CommunityTournament tournament(Community group, String name, TournamentStatus status) {
        CommunityTournament t = CommunityTournament.builder()
                .community(group).name(name).sport("Futebol")
                .status(status).createdAt(Instant.now()).build();
        em.persist(t);
        return t;
    }

    private CommunityTournamentParticipant enrolled(CommunityTournament tournament, Profile profile) {
        CommunityTournamentParticipant p = CommunityTournamentParticipant.builder()
                .tournament(tournament).profile(profile).joinedAt(Instant.now()).build();
        em.persist(p);
        return p;
    }

    private UsernamePasswordAuthenticationToken as(String username) {
        return new UsernamePasswordAuthenticationToken(username, null, List.of());
    }
}
