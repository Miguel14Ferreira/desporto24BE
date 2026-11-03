package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentRelayServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentTeamRepository teams;
    @Mock CommunityTournamentTeamPlayerRepository roster;
    @Mock CommunityTournamentRelayHeatRepository heats;
    @Mock CommunityTournamentRelayFinalistRepository finalists;
    @Mock CommunityTournamentRelayDecisionRepository decisions;
    @Mock CommunityTournamentTeamService teamService;
    @InjectMocks CommunityTournamentRelayService service;
    Community community;CommunityTournament competition;CommunityTournamentTeam team;

    @BeforeEach void init(){
        community=Community.builder().id(7L).name("Atletismo").build();
        competition=CommunityTournament.builder().id(20L).community(community)
                .sport("Atletismo").eventType("TRACK_RELAY").eventDiscipline("RELAY_4X100")
                .entryType(TournamentEntryType.TEAM).playersPerTeam(4)
                .format(TournamentFormat.LEAGUE).scoringMode(TournamentScoringMode.RELAY_BEST_TIME)
                .status(TournamentStatus.DRAFT).build();
        team=CommunityTournamentTeam.builder().id(30L).name("Equipa A")
                .tournament(competition).build();
    }
    void manager(){
        var actor=new Profile();actor.setId(1L);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(7L,1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(actor)
                        .role(CommunityRole.OWNER).build()));
        when(tournaments.findByIdForUpdate(20L)).thenReturn(Optional.of(competition));
    }
    void runners(){
        List<CommunityTournamentTeamPlayer> players=new ArrayList<>();
        for(long id=11;id<=14;id++){
            var profile=new Profile();profile.setId(id);
            players.add(CommunityTournamentTeamPlayer.builder().team(team).profile(profile).build());
        }
        when(roster.findByTournamentIdOrderByTeamIdAscIdAsc(20L)).thenReturn(players);
    }
    @Test void recordsFourDistinctLegsAndStartsTournamentWithoutMatchResults(){
        manager();runners();
        when(teams.findById(30L)).thenReturn(Optional.of(team));
        when(heats.findByTournamentIdAndTeamIdAndStageAndHeatNumber(20L,30L,"QUALIFICATION",1))
                .thenReturn(Optional.empty());
        when(heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(20L)).thenReturn(List.of());
        when(teamService.list(7L,20L,"owner")).thenReturn(List.of());
        service.record(7L,20L,1,30L,
                new CommunityRelayHeatRequest(new BigDecimal("43.123"),List.of(11L,12L,13L,14L)),
                "owner");
        var captured=ArgumentCaptor.forClass(CommunityTournamentRelayHeat.class);
        verify(heats).saveAndFlush(captured.capture());
        assertEquals(new BigDecimal("43.123"),captured.getValue().getElapsedSeconds());
        assertEquals(11L,captured.getValue().getLeg1ProfileId());
        assertEquals(14L,captured.getValue().getLeg4ProfileId());
        assertEquals(TournamentStatus.IN_PROGRESS,competition.getStatus());
        verify(tournaments).saveAndFlush(competition);
    }
    @Test void refusesDuplicateRunnersWithoutTouchingTeamResults(){
        manager();
        assertThrows(ResponseStatusException.class,()->service.record(7L,20L,1,30L,
                new CommunityRelayHeatRequest(new BigDecimal("43.123"),List.of(11L,11L,12L,13L)),
                "owner"));
        verify(heats,never()).saveAndFlush(any());
    }
    @Test void rejectsRunnersMissingFromRegisteredFourPersonRoster(){
        manager();runners();
        when(teams.findById(30L)).thenReturn(Optional.of(team));
        assertThrows(ResponseStatusException.class,()->service.record(7L,20L,1,30L,
                new CommunityRelayHeatRequest(new BigDecimal("43.123"),List.of(11L,12L,13L,99L)),
                "owner"));
        verify(heats,never()).saveAndFlush(any());
    }
    @Test void qualifyingResultsCannotChangeAfterFinalPublication(){
        manager();
        competition.setRelayFinalPublishedAt(java.time.Instant.now());
        assertThrows(ResponseStatusException.class,()->service.record(7L,20L,1,30L,
                new CommunityRelayHeatRequest(new BigDecimal("43.100"),List.of(11L,12L,13L,14L)),
                "owner"));
        verify(heats,never()).saveAndFlush(any());
    }
    @Test void onlyPublishedFinalistsCanRecordFinalHeats(){
        manager(); competition.setStatus(TournamentStatus.IN_PROGRESS);
        competition.setRelayFinalPublishedAt(java.time.Instant.now());
        when(teams.findById(30L)).thenReturn(Optional.of(team));
        var request=new CommunityRelayHeatRequest(new BigDecimal("42.123"),List.of(11L,12L,13L,14L));
        assertThrows(ResponseStatusException.class,()->service.recordFinal(7L,20L,1,30L,request,"owner"));
        verify(heats,never()).saveAndFlush(any());
    }
    @Test void publishesAtMostEightFrozenFinalistsFromValidQualificationTimes(){
        manager(); competition.setStatus(TournamentStatus.IN_PROGRESS);
        var teamsList=new ArrayList<CommunityTournamentTeam>();
        var raceResults=new ArrayList<CommunityTournamentRelayHeat>();
        for(long n=1;n<=9;n++){
            var t=CommunityTournamentTeam.builder().id(n).name("Team "+n)
                    .tournament(competition).build();
            teamsList.add(t);
            raceResults.add(CommunityTournamentRelayHeat.builder()
                    .id(n).team(t).tournament(competition).stage("QUALIFICATION")
                    .decisionStatus("VALID").heatNumber(1)
                    .elapsedSeconds(new BigDecimal(40+n))
                    .leg1ProfileId(11L).leg2ProfileId(12L)
                    .leg3ProfileId(13L).leg4ProfileId(14L).build());
        }
        when(heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(20L))
                .thenReturn(raceResults);
        when(teams.findByTournamentIdOrderByIdAsc(20L)).thenReturn(teamsList);
        when(finalists.findByTournamentIdOrderBySeedAsc(20L)).thenReturn(List.of());
        when(teamService.list(7L,20L,"owner")).thenReturn(List.of());
        var response=service.publishFinal(7L,20L,"owner");
        var captured=ArgumentCaptor.forClass(List.class);
        verify(finalists).saveAllAndFlush(captured.capture());
        assertEquals(8,captured.getValue().size());
        assertNotNull(competition.getRelayFinalPublishedAt());
        assertTrue(response.finalPublished());
        assertTrue(response.finalStandings().isEmpty());
        assertEquals(9,response.qualifyingStandings().size());
    }
    @Test void disqualificationKeepsTimedHeatAndAppendsJuryAudit(){
        manager(); competition.setStatus(TournamentStatus.IN_PROGRESS);
        var result=CommunityTournamentRelayHeat.builder().id(99L)
                .tournament(competition).team(team).stage("QUALIFICATION")
                .decisionStatus("VALID").heatNumber(1)
                .elapsedSeconds(new BigDecimal("43.123"))
                .leg1ProfileId(11L).leg2ProfileId(12L)
                .leg3ProfileId(13L).leg4ProfileId(14L).build();
        when(heats.findById(99L)).thenReturn(Optional.of(result));
        when(heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(20L)).thenReturn(List.of(result));
        when(decisions.findByTournamentIdOrderByIdAsc(20L)).thenReturn(List.of());
        when(teamService.list(7L,20L,"owner")).thenReturn(List.of());
        var response=service.decide(7L,20L,99L,
                new CommunityRelayDecisionRequest("DISQUALIFIED","Passagem do testemunho irregular"),
                "owner");
        assertEquals("DISQUALIFIED",result.getDecisionStatus());
        assertTrue(response.standings().isEmpty());
        var action=ArgumentCaptor.forClass(CommunityTournamentRelayDecision.class);
        verify(decisions).saveAndFlush(action.capture());
        assertEquals("VALID",action.getValue().getPreviousStatus());
        assertEquals("DISQUALIFIED",action.getValue().getNewStatus());
        assertEquals(99L,action.getValue().getHeat().getId());
    }
    @Test void rejectsWrongEventTypeEvenWithCorrectTeamAndRoster(){
        manager();
        competition.setEventDiscipline("TRACK_100");
        assertThrows(ResponseStatusException.class,()->service.record(7L,20L,1,30L,
                new CommunityRelayHeatRequest(new BigDecimal("43.123"),List.of(11L,12L,13L,14L)),
                "owner"));
        verify(heats,never()).saveAndFlush(any());
    }
}
