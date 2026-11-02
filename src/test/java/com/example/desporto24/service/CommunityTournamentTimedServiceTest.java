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

    @Test void rejectsCompletedTournamentMutations() {
        member(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.COMPLETED);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        assertThrows(ResponseStatusException.class,()->service.unlink(7L,15L,20L,"owner"));
        verify(links,never()).delete(any());
    }
}
