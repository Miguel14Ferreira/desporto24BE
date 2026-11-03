package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTimedServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityTournamentRaceRoundRepository links;
    @Mock CommunityCompetitionService competition;
    @InjectMocks CommunityTournamentTimedService service;
    private Community community;
    private CommunityTournament tournament;

    @BeforeEach void setUp() {
        community=Community.builder().id(7L).name("Swim").build();
        tournament=CommunityTournament.builder().id(15L).community(community)
                .sport("Natação").eventType("SWIM_LCM").eventDiscipline("FREE_50")
                .scoringMode(TournamentScoringMode.SWIM_BEST_TIME)
                .entryType(TournamentEntryType.INDIVIDUAL)
                .format(TournamentFormat.LEAGUE).status(TournamentStatus.DRAFT).build();
    }
    private void member(CommunityRole role) {
        var actor=new Profile();
        actor.setId(1L);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
        if(role==CommunityRole.MEMBER)
            when(members.existsByCommunityIdAndProfileId(7L,1L)).thenReturn(true);
        else when(members.findByCommunityIdAndProfileId(7L,1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(actor).role(role).build()));
    }
    private CommunityRoundEntryResponse timed(long id,String time) {
        BigDecimal seconds=new BigDecimal(time);
        return new CommunityRoundEntryResponse(null,"swimmer"+id,"Swimmer "+id,null,null,
                CommunityMatchResult.DRAW,0,0,false,false,null,seconds,null,null,
                id+100,id,0,null,BigDecimal.ZERO,seconds,false,null,null,null,null);
    }
    private CommunityRoundResponse source(long id,String discipline) {
        return new CommunityRoundResponse(id,"Round",Instant.now(),
                List.of(timed(1,"28.47"),timed(2,"29.11")),
                "Natação",null,List.of(),"SWIM_LCM",discipline);
    }
    private CommunityCompetitionOverviewResponse overview(CommunityRoundResponse... source) {
        return new CommunityCompetitionOverviewResponse(null,List.of(),null,null,List.of(source),List.of());
    }

    @Test void linksValidSwimmingTimesAndComputesBestTimeWithoutRacePoints() {
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Natação").eventType("SWIM_LCM")
                .eventDiscipline("FREE_50").build()));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        when(competition.overview(7L,"owner",null)).thenReturn(overview(source(20L,"FREE_50")));
        var result=service.link(7L,15L,20L,"owner");
        verify(links).save(any(CommunityTournamentRaceRound.class));
        assertEquals(TournamentStatus.IN_PROGRESS,tournament.getStatus());
        assertEquals("FREE_50",result.eventDiscipline());
        // Returned data is provisional until the link repository exposes its new row.
        assertTrue(result.standings().isEmpty());
    }

    @Test void rejectsDuplicateIdentityAsTwoTimedParticipants() {
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Natação").eventType("SWIM_LCM")
                .eventDiscipline("FREE_50").build()));
        // Both rows belong to the same community player; one athlete is not a race.
        var duplicate=new CommunityRoundResponse(20L,"Duplicate",Instant.now(),
                List.of(timed(1,"28.47"),timed(1,"28.48")),
                "Natação",null,List.of(),"SWIM_LCM","FREE_50");
        when(competition.overview(7L,"owner",null)).thenReturn(overview(duplicate));
        assertEquals(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY,
                assertThrows(ResponseStatusException.class,
                        ()->service.link(7L,15L,20L,"owner")).getStatusCode());
        verify(links,never()).save(any());
    }

    @Test void refusesPartiallyInvalidLinkedK1ResultsAfterEditingTheSource() {
        tournament.setSport("Canoagem Velocidade");
        tournament.setEventType("CANOE_SPRINT");
        tournament.setEventDiscipline("K1_500");
        tournament.setScoringMode(TournamentScoringMode.CANOE_BEST_TIME);
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        var round=CommunityRound.builder().id(20L).community(community)
                .sport("Canoagem Velocidade").eventType("CANOE_SPRINT")
                .eventDiscipline("K1_500").build();
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(
                List.of(CommunityTournamentRaceRound.builder().tournament(tournament).round(round).build()));
        var stale=new CommunityRoundResponse(20L,"500m",Instant.now(),
                List.of(timed(1,"112.125")),
                "Canoagem Velocidade",null,List.of(),"CANOE_SPRINT","K1_500");
        when(competition.overview(7L,"owner",null)).thenReturn(overview(stale));
        assertEquals(org.springframework.http.HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        ()->service.overview(7L,15L,"owner")).getStatusCode());
        verify(links,never()).delete(any());
    }

    @Test void rejectsAnOtherwiseValidSwimFromTheWrongDisciplineBeforeLinking() {
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Natação").eventType("SWIM_LCM")
                .eventDiscipline("FREE_100").build()));
        assertThrows(ResponseStatusException.class,()->service.link(7L,15L,20L,"owner"));
        verify(links,never()).save(any());
    }

    @Test void listsOnlyMatchingDisciplineAndDoesNotInventTimesForOtherSwims() {
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        when(competition.overview(7L,"owner",null)).thenReturn(overview(
                source(20L,"FREE_50"),source(21L,"FREE_100")));
        var result=service.overview(7L,15L,"owner");
        assertEquals(List.of(20L),result.availableRounds().stream().map(CommunityRoundResponse::id).toList());
        assertTrue(result.standings().isEmpty());
    }

    @Test void rowing1xOnlyOffersMatchingBoatAndDistanceAtMillisecondPrecision() {
        tournament.setSport("Remo");
        tournament.setEventType("ROWING_SINGLE");
        tournament.setEventDiscipline("SINGLE_2000");
        tournament.setScoringMode(TournamentScoringMode.ROWING_BEST_TIME);
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        var correct = new CommunityRoundResponse(20L,"2000 m",Instant.now(),
                List.of(timed(1,"420.123"),timed(2,"422.456")),
                "Remo",null,List.of(),"ROWING_SINGLE","SINGLE_2000");
        var shorter = new CommunityRoundResponse(21L,"1000 m",Instant.now(),
                List.of(timed(1,"210.123"),timed(2,"211.456")),
                "Remo",null,List.of(),"ROWING_SINGLE","SINGLE_1000");
        var old = new CommunityRoundResponse(22L,"Antigo",Instant.now(),
                List.of(timed(1,"430.123"),timed(2,"431.456")),
                "Remo",null,List.of(),null,null);
        var crew = new CommunityRoundResponse(23L,"Tripulação",Instant.now(),
                List.of(timed(1,"430.123"),timed(2,"431.456")),
                "Remo",null,List.of(),"ROWING_SINGLE","DOUBLE_2000");
        when(competition.overview(7L,"owner",null)).thenReturn(overview(correct,shorter,old,crew));
        assertEquals(List.of(20L),service.overview(7L,15L,"owner").availableRounds()
                .stream().map(CommunityRoundResponse::id).toList());
    }

    @Test void canoeOnlyListsMatchingK1DistanceNeverOtherBoatsOrSports() {
        tournament.setSport("Canoagem Velocidade");
        tournament.setEventType("CANOE_SPRINT");
        tournament.setEventDiscipline("K1_500");
        tournament.setScoringMode(TournamentScoringMode.CANOE_BEST_TIME);
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        var k1_500 = new CommunityRoundResponse(20L,"500m",Instant.now(),
                List.of(timed(1,"112.125"),timed(2,"115.007")),
                "Canoagem Velocidade",null,List.of(),"CANOE_SPRINT","K1_500");
        var k1_200 = new CommunityRoundResponse(21L,"200m",Instant.now(),
                List.of(timed(1,"45.123"),timed(2,"46.555")),
                "Canoagem Velocidade",null,List.of(),"CANOE_SPRINT","K1_200");
        var untyped = new CommunityRoundResponse(22L,"Legacy",Instant.now(),
                List.of(timed(1,"100.125"),timed(2,"101.555")),
                "Canoagem Velocidade",null,List.of(),null,null);
        var swimming = new CommunityRoundResponse(23L,"Swim",Instant.now(),
                List.of(timed(1,"100.125"),timed(2,"101.555")),
                "Natação",null,List.of(),"SWIM_LCM","FREE_50");
        when(competition.overview(7L,"owner",null))
                .thenReturn(overview(k1_500,k1_200,untyped,swimming));
        var response=service.overview(7L,15L,"owner");
        assertEquals(List.of(20L),response.availableRounds().stream()
                .map(CommunityRoundResponse::id).toList());
        assertTrue(response.standings().isEmpty());
    }

    @Test void canoeRejectsLinkingARecordedRoundFromAnotherK1Distance() {
        tournament.setSport("Canoagem Velocidade");
        tournament.setEventType("CANOE_SPRINT");
        tournament.setEventDiscipline("K1_500");
        tournament.setScoringMode(TournamentScoringMode.CANOE_BEST_TIME);
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Canoagem Velocidade")
                .eventType("CANOE_SPRINT").eventDiscipline("K1_200").build()));
        assertThrows(ResponseStatusException.class, ()->service.link(7L,15L,20L,"owner"));
        verify(links,never()).save(any());
    }

    @Test void trackOnlyListsMatchingDistanceAndExcludesHistoricalUntypedRounds() {
        tournament.setSport("Atletismo");
        tournament.setEventType("TRACK");
        tournament.setEventDiscipline("TRACK_100");
        tournament.setScoringMode(TournamentScoringMode.TRACK_BEST_TIME);
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        var hundred=new CommunityRoundResponse(20L,"100 m",Instant.now(),
                List.of(timed(1,"10.123"),timed(2,"10.129")),
                "Atletismo",null,List.of(),"TRACK","TRACK_100");
        var twoHundred=new CommunityRoundResponse(21L,"200 m",Instant.now(),
                List.of(timed(1,"20.123"),timed(2,"20.129")),
                "Atletismo",null,List.of(),"TRACK","TRACK_200");
        var legacy=new CommunityRoundResponse(22L,"Legacy",Instant.now(),
                List.of(timed(1,"11.000"),timed(2,"12.000")),
                "Atletismo",null,List.of(),"TRACK",null);
        when(competition.overview(7L,"owner",null))
                .thenReturn(overview(hundred,twoHundred,legacy));
        var result=service.overview(7L,15L,"owner");
        assertEquals(List.of(20L),result.availableRounds().stream()
                .map(CommunityRoundResponse::id).toList());
    }

    @Test void hurdlesDoNotCombineFlat100mOr110mHurdleTimes() {
        tournament.setSport("Atletismo");
        tournament.setEventType("TRACK");
        tournament.setEventDiscipline("TRACK_HURDLES_100");
        tournament.setScoringMode(TournamentScoringMode.TRACK_BEST_TIME);
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        var hurdles100=new CommunityRoundResponse(30L,"100m barreiras",Instant.now(),
                List.of(timed(1,"12.531"),timed(2,"12.633")),
                "Atletismo",null,List.of(),"TRACK","TRACK_HURDLES_100");
        var flat100=new CommunityRoundResponse(31L,"100m planos",Instant.now(),
                List.of(timed(1,"11.231"),timed(2,"11.302")),
                "Atletismo",null,List.of(),"TRACK","TRACK_100");
        var hurdles110=new CommunityRoundResponse(32L,"110m barreiras",Instant.now(),
                List.of(timed(1,"13.231"),timed(2,"13.302")),
                "Atletismo",null,List.of(),"TRACK","TRACK_HURDLES_110");
        when(competition.overview(7L,"owner",null))
                .thenReturn(overview(hurdles100,flat100,hurdles110));
        var result=service.overview(7L,15L,"owner");
        assertEquals(List.of(30L),result.availableRounds().stream()
                .map(CommunityRoundResponse::id).toList());
    }

    @Test void linkingFlatRaceToHurdlesTournamentIsRejected() {
        tournament.setSport("Atletismo");
        tournament.setEventType("TRACK");
        tournament.setEventDiscipline("TRACK_HURDLES_100");
        tournament.setScoringMode(TournamentScoringMode.TRACK_BEST_TIME);
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(31L)).thenReturn(Optional.of(
                CommunityRound.builder().id(31L).community(community)
                .sport("Atletismo").eventType("TRACK")
                .eventDiscipline("TRACK_100").build()));
        assertThrows(ResponseStatusException.class,()->
                service.link(7L,15L,31L,"owner"));
        verify(links,never()).save(any());
    }

    @Test void rejectsCompletedTournamentMutations() {
        member(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.COMPLETED);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        assertThrows(ResponseStatusException.class,()->service.unlink(7L,15L,20L,"owner"));
        verify(links,never()).delete(any());
    }
}
