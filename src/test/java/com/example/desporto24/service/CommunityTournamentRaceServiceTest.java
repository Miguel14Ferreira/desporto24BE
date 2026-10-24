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
                List.of(first, second), sport, null, List.of(), "RACE");
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
                .sport("Karting").eventType("RACE").build();
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
                .id(50L).community(community).sport("Karting").eventType("RACE").build()));
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
                .id(50L).community(community).sport("Futebol").eventType("RACE").build()));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                assertThrows(ResponseStatusException.class,
                        () -> service.link(7L, 19L, 50L, "owner")).getStatusCode());
        verify(links, never()).save(any());
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
