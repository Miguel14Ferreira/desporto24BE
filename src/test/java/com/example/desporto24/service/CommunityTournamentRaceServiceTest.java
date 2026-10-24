package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import tools.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentRaceServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityTournamentRaceRoundRepository links;
    @Mock CommunityCompetitionService competition;
    @Mock ObjectMapper json;
    @InjectMocks CommunityTournamentRaceService service;

    private Community community;
    private CommunityTournament tournament;
    private Profile owner;

    @BeforeEach
    void setUp() {
        community = Community.builder().id(7L).name("CNG").build();
        tournament = CommunityTournament.builder()
                .id(19L).community(community).sport("Karting")
                .entryType(TournamentEntryType.INDIVIDUAL)
                .format(TournamentFormat.LEAGUE)
                .scoringMode(TournamentScoringMode.RACE_POINTS)
                .racePointsByPlace("25,18,15")
                .status(TournamentStatus.DRAFT).build();
        owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
    }

    private void admin() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.OWNER).build()));
        when(tournaments.findByIdForUpdate(19L)).thenReturn(Optional.of(tournament));
    }

    private CommunityRoundResponse race(Long id, String sport) {
        CommunityRoundEntryResponse first = entry(10L, 20L, "Ana", 1, false);
        CommunityRoundEntryResponse second = entry(11L, 21L, "Rui", 2, false);
        return new CommunityRoundResponse(id, "Corrida " + id, Instant.now(),
                List.of(first, second), sport, null, List.of(), null);
    }

    private CommunityRoundEntryResponse entry(Long playerId, Long profileId,
                                               String name, Integer place, boolean disqualified) {
        return new CommunityRoundEntryResponse(profileId, name.toLowerCase(), name,
                null, null, CommunityMatchResult.DRAW, 0, 0, false, false,
                place, new BigDecimal("120.500"), new BigDecimal("60.000"), 2,
                playerId + 100, playerId, 0, null, BigDecimal.ZERO,
                new BigDecimal("120.500"), disqualified, null, null, null, null);
    }

    private CommunityCompetitionOverviewResponse overview(CommunityRoundResponse... rounds) {
        return new CommunityCompetitionOverviewResponse(null, List.of(), null, null,
                List.of(rounds), List.of());
    }

    @Test
    void calculatesPointsFromSelectedRacesUsingTheExistingCommunityRules() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));
        var recorded = CommunityRound.builder().id(50L).community(community)
                .sport("Karting").eventType(null).build();
        when(links.findByTournamentIdOrderByIdAsc(19L))
                .thenReturn(List.of(CommunityTournamentRaceRound.builder()
                        .tournament(tournament).round(recorded).build()));
        when(competition.overview(7L, "owner", null))
                .thenReturn(overview(race(50L, "Karting"), race(51L, "Karting")));

        var result = service.overview(7L, 19L, "owner");

        assertEquals(1, result.linkedRaces().size());
        assertEquals(51L, result.availableRaces().get(0).id());
        assertEquals(List.of(25, 18, 15), result.pointsByPlace());
        assertEquals(2, result.standings().size());
        assertEquals("Ana", result.standings().get(0).fullName());
        assertEquals(25, result.standings().get(0).championshipPoints());
        assertEquals(18, result.standings().get(1).championshipPoints());
    }

    @Test
    void linksRecordedKartingOnlyAndStartsTheCompetition() {
        admin();
        when(rounds.findById(50L)).thenReturn(Optional.of(CommunityRound.builder()
                .id(50L).community(community).sport("Karting").eventType(null).build()));
        when(competition.overview(7L, "owner", null)).thenReturn(overview(race(50L, "Karting")));
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of());

        service.link(7L, 19L, 50L, "owner");

        verify(links).save(argThat(link -> link.getRound().getId().equals(50L)
                && link.getTournament().getId().equals(19L)));
        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        assertNotNull(tournament.getStartedAt());
    }

    @Test
    void blocksOtherSportsAndUnrelatedCommunitiesWithoutChangingAnything() {
        admin();
        when(rounds.findById(50L)).thenReturn(Optional.of(CommunityRound.builder()
                .id(50L).community(community).sport("Futebol").eventType(null).build()));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                assertThrows(ResponseStatusException.class,
                        () -> service.link(7L, 19L, 50L, "owner")).getStatusCode());
        verify(links, never()).save(any());
    }

    @Test
    void bmxRacingAcceptsOnlyRaceRoundsFromSameSportAndCommunity() {
        tournament.setSport("BMX");
        admin();
        var good = CommunityRound.builder().id(50L).community(community)
                .sport("BMX").eventType("RACE").build();
        when(rounds.findById(50L)).thenReturn(Optional.of(good));
        when(competition.overview(7L, "owner", null))
                .thenReturn(overview(new CommunityRoundResponse(50L, "BMX corrida",
                        Instant.now(), race(50L, "BMX").entries(),
                        "BMX", null, List.of(), "RACE")));
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of());

        var result = service.link(7L, 19L, 50L, "owner");
        assertEquals(2, result.standings().size());
        assertEquals(25, result.standings().get(0).championshipPoints());
        verify(links).save(argThat(link -> link.getRound().getId().equals(50L)));
    }

    @Test
    void bmxFreestyleAndOtherSportsCannotBeAddedToBmxRacingChampionships() {
        tournament.setSport("BMX");
        admin();
        var freestyle = CommunityRound.builder().id(50L).community(community)
                .sport("BMX").eventType("FREESTYLE").build();
        when(rounds.findById(50L)).thenReturn(Optional.of(freestyle));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                assertThrows(ResponseStatusException.class,
                        () -> service.link(7L, 19L, 50L, "owner")).getStatusCode());
        freestyle.setEventType("RACE");
        freestyle.setSport("Karting");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                assertThrows(ResponseStatusException.class,
                        () -> service.link(7L, 19L, 50L, "owner")).getStatusCode());
        verifyNoInteractions(competition);
        verify(links, never()).save(any());
    }

    @Test
    void bmxChampionshipReadModelExcludesFreestyleAndKarting() {
        tournament.setSport("BMX");
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));
        var raceRound = new CommunityRoundResponse(50L, "BMX corrida", Instant.now(),
                race(50L, "BMX").entries(), "BMX", null, List.of(), "RACE");
        var freestyle = new CommunityRoundResponse(51L, "BMX freestyle", Instant.now(),
                race(50L, "BMX").entries(), "BMX", null, List.of(), "FREESTYLE");
        when(competition.overview(7L, "owner", null))
                .thenReturn(overview(raceRound, freestyle, race(52L, "Karting")));
        var result = service.overview(7L, 19L, "owner");
        assertEquals(List.of(50L), result.availableRaces().stream()
                .map(CommunityRoundResponse::id).toList());
    }

    @Test
    void completesAndArchivesWinnerAndReadModelInOneTransaction() throws Exception {
        admin();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        var recorded = CommunityRound.builder().id(50L).community(community)
                .sport("Karting").build();
        var link = CommunityTournamentRaceRound.builder()
                .tournament(tournament).round(recorded).build();
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(link));
        when(competition.overview(7L, "owner", null))
                .thenReturn(overview(race(50L, "Karting")));
        when(json.writeValueAsString(any(CommunityTournamentRaceOverviewResponse.class)))
                .thenReturn("{\"frozen\":true}");
        when(tournaments.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var completed = service.complete(7L, 19L, "owner");

        assertEquals(TournamentStatus.COMPLETED, completed.status());
        assertEquals(20L, completed.championProfileId());
        assertEquals("Ana", completed.championDisplayName());
        assertEquals(25L, completed.championPoints());
        assertFalse(completed.championTied());
        assertNotNull(completed.completedAt());
        assertEquals("{\"frozen\":true}", tournament.getRaceFinalSnapshotJson());
        var saved = org.mockito.ArgumentCaptor.forClass(CommunityTournamentRaceOverviewResponse.class);
        verify(json).writeValueAsString(saved.capture());
        assertTrue(saved.getValue().availableRaces().isEmpty());
        assertEquals(1, saved.getValue().linkedRaces().size());
        assertEquals(2, saved.getValue().standings().size());
    }

    @Test
    void archivedTableSurvivesFutureRoundCorrectionsAndCannotBeEdited() throws Exception {
        admin();
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournament.setRaceFinalSnapshotJson("{\"frozen\":true}");
        var archived = new CommunityTournamentRaceOverviewResponse(19L, List.of(25, 18),
                List.of(race(50L, "Karting")), List.of(),
                List.of(new CommunityRaceStandingResponse(1, 10L, 20L, "ana", "Ana",
                        null, 1, 1, 1, 1, 25, 0)));
        when(json.readValue("{\"frozen\":true}", CommunityTournamentRaceOverviewResponse.class))
                .thenReturn(archived);
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));

        var got = service.overview(7L, 19L, "owner");

        assertSame(archived, got);
        verifyNoInteractions(competition, links);
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> service.link(7L, 19L, 50L, "owner")).getStatusCode());
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> service.unlink(7L, 19L, 50L, "owner")).getStatusCode());
        assertEquals(TournamentStatus.COMPLETED,
                service.complete(7L, 19L, "owner").status());
        verify(tournaments, never()).save(any());
    }

    @Test
    void realJsonArchiveRoundTripsAndNeverReadsEditedJornadasAfterClosing() throws Exception {
        // Earlier tests mock JSON serialization. This verifies the actual
        // Jackson 3 mapping of nested race entries, Instant and BigDecimal.
        admin();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        var recorded = CommunityRound.builder().id(50L).community(community)
                .sport("Karting").build();
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(
                List.of(CommunityTournamentRaceRound.builder()
                        .tournament(tournament).round(recorded).build()));
        when(competition.overview(7L, "owner", null))
                .thenReturn(overview(race(50L, "Karting")));
        when(tournaments.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var realService = new CommunityTournamentRaceService(profiles, members,
                tournaments, rounds, links, competition, new ObjectMapper());
        var result = realService.complete(7L, 19L, "owner");

        assertEquals(TournamentStatus.COMPLETED, result.status());
        assertNotNull(tournament.getRaceFinalSnapshotJson());
        assertTrue(tournament.getRaceFinalSnapshotJson().contains("Corrida 50"));
        assertEquals(20L, result.championProfileId());
        assertEquals(25L, result.championPoints());

        // The original round could be edited or deleted afterwards.
        // A completed championship must deserialize the stored JSON, not
        // rebuild the table or retrieve the mutable community round.
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));
        clearInvocations(competition, links);

        var archived = realService.overview(7L, 19L, "owner");
        assertEquals(List.of(25, 18, 15), archived.pointsByPlace());
        assertEquals(1, archived.linkedRaces().size());
        assertEquals("Corrida 50", archived.linkedRaces().get(0).name());
        assertEquals(2, archived.linkedRaces().get(0).entries().size());
        assertNotNull(archived.linkedRaces().get(0).playedAt());
        assertEquals(new BigDecimal("120.500"),
                archived.linkedRaces().get(0).entries().get(0).elapsedSeconds());
        assertTrue(archived.availableRaces().isEmpty());
        assertEquals(2, archived.standings().size());
        assertEquals("Ana", archived.standings().get(0).fullName());
        assertEquals(25, archived.standings().get(0).championshipPoints());
        assertEquals(18, archived.standings().get(1).championshipPoints());
        verifyNoInteractions(competition, links);
    }

    @Test
    void refusesClosingDraftOrChampionshipWithoutLinkedRaces() {
        admin();
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> service.complete(7L, 19L, "owner")).getStatusCode());
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of());
        when(competition.overview(7L, "owner", null)).thenReturn(overview());
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> service.complete(7L, 19L, "owner")).getStatusCode());
        verify(tournaments, never()).save(any());
    }

    @Test
    void jointlyFirstDriversDoNotReceiveAnInventedSoleChampion() throws Exception {
        admin();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        var r1 = CommunityRound.builder().id(50L).community(community).sport("Karting").build();
        var r2 = CommunityRound.builder().id(51L).community(community).sport("Karting").build();
        var link1 = CommunityTournamentRaceRound.builder().tournament(tournament).round(r1).build();
        var link2 = CommunityTournamentRaceRound.builder().tournament(tournament).round(r2).build();
        when(links.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(link1, link2));
        var first = race(50L, "Karting");
        var swapped = new CommunityRoundResponse(51L, "Corrida 51", Instant.now(),
                List.of(entry(10L, 20L, "Ana", 2, false),
                        entry(11L, 21L, "Rui", 1, false)), "Karting", null, List.of(), null);
        when(competition.overview(7L, "owner", null)).thenReturn(overview(first, swapped));
        when(json.writeValueAsString(any(CommunityTournamentRaceOverviewResponse.class)))
                .thenReturn("{\"tied\":true}");
        when(tournaments.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var closed = service.complete(7L, 19L, "owner");

        assertTrue(closed.championTied());
        assertNull(closed.championProfileId());
        assertNull(closed.championUsername());
        assertNull(closed.championPoints());
    }

    @Test
    void ordinaryMemberCanReadFinalSnapshotButCannotMutateKartingChampionship() throws Exception {
        var member = new Profile();
        member.setId(2L);
        member.setUsername("member");
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournament.setRaceFinalSnapshotJson("{\"final\":true}");
        var archived = new CommunityTournamentRaceOverviewResponse(19L, List.of(25, 18),
                List.of(race(50L, "Karting")), List.of(),
                List.of(new CommunityRaceStandingResponse(1, 10L, 20L, "ana", "Ana",
                        null, 1, 1, 1, 1, 25, 0)));
        when(profiles.findUserByUsernameIgnoreCase("member")).thenReturn(Optional.of(member));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament));
        when(json.readValue("{\"final\":true}", CommunityTournamentRaceOverviewResponse.class))
                .thenReturn(archived);
        when(members.findByCommunityIdAndProfileId(7L, 2L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.MEMBER).build()));

        assertSame(archived, service.overview(7L, 19L, "member"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.complete(7L, 19L, "member"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.link(7L, 19L, 50L, "member"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.unlink(7L, 19L, 50L, "member"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.changePoints(7L, 19L, "member", "25,18"));

        verify(tournaments, never()).findByIdForUpdate(anyLong());
        verify(tournaments, never()).save(any());
        verifyNoInteractions(links, rounds, competition);
    }

    @Test
    void outsiderCannotReadKartingArchiveOrReachTournamentRepository() {
        var outsider = new Profile();
        outsider.setId(99L);
        outsider.setUsername("outsider");
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournament.setRaceFinalSnapshotJson("{\"final\":true}");
        when(profiles.findUserByUsernameIgnoreCase("outsider")).thenReturn(Optional.of(outsider));
        when(members.existsByCommunityIdAndProfileId(7L, 99L)).thenReturn(false);

        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.overview(7L, 19L, "outsider"));
        verifyNoInteractions(tournaments, json, links, rounds, competition);
    }

    @Test
    void preventsChangingPointsAfterTheFirstRace() {
        admin();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> service.changePoints(7L, 19L, "owner", "25,18,15")).getStatusCode());
        verify(tournaments, never()).save(any());
    }
}
