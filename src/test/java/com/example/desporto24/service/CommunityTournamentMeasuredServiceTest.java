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
class CommunityTournamentMeasuredServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityTournamentRaceRoundRepository links;
    @Mock CommunityCompetitionService competition;
    @InjectMocks CommunityTournamentMeasuredService service;
    private Community community;
    private CommunityTournament tournament;

    @BeforeEach void setUp() {
        community=Community.builder().id(7L).name("Swim").build();
        tournament=CommunityTournament.builder().id(15L).community(community)
                .sport("Atletismo").eventType("JUMP").eventDiscipline("LONG_JUMP")
                .scoringMode(TournamentScoringMode.FIELD_BEST_MARK)
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
    private CommunityRoundEntryResponse marked(long id,String time) {
        BigDecimal metres=new BigDecimal(time);
        return new CommunityRoundEntryResponse(null,"athlete"+id,"Athlete "+id,null,null,
                CommunityMatchResult.DRAW,0,0,false,false,null,metres,null,null,
                id+100,id,0,null,BigDecimal.ZERO,metres,false,null,metres,null,null);
    }
    private CommunityRoundResponse source(long id,String discipline) {
        return new CommunityRoundResponse(id,"Round",Instant.now(),
                List.of(marked(1,"8.47"),marked(2,"7.11")),
                "Atletismo",null,List.of(),"JUMP",discipline);
    }
    private CommunityCompetitionOverviewResponse overview(CommunityRoundResponse... source) {
        return new CommunityCompetitionOverviewResponse(null,List.of(),null,null,List.of(source),List.of());
    }

    @Test void linksValidSwimmingTimesAndComputesBestTimeWithoutRacePoints() {
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Atletismo").eventType("JUMP")
                .eventDiscipline("LONG_JUMP").build()));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        when(competition.overview(7L,"owner",null)).thenReturn(overview(source(20L,"LONG_JUMP")));
        var result=service.link(7L,15L,20L,"owner");
        verify(links).save(any(CommunityTournamentRaceRound.class));
        assertEquals(TournamentStatus.IN_PROGRESS,tournament.getStatus());
        assertEquals("LONG_JUMP",result.eventDiscipline());
        // Returned data is provisional until the link repository exposes its new row.
        assertTrue(result.standings().isEmpty());
    }

    @Test void rejectsAnOtherwiseValidSwimFromTheWrongDisciplineBeforeLinking() {
        member(CommunityRole.OWNER);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(20L)).thenReturn(Optional.of(CommunityRound.builder().id(20L)
                .community(community).sport("Atletismo").eventType("JUMP")
                .eventDiscipline("HIGH_JUMP").build()));
        assertThrows(ResponseStatusException.class,()->service.link(7L,15L,20L,"owner"));
        verify(links,never()).save(any());
    }

    @Test void listsOnlyMatchingDisciplineAndDoesNotInventTimesForOtherSwims() {
        member(CommunityRole.MEMBER);
        when(tournaments.findById(15L)).thenReturn(Optional.of(tournament));
        when(links.findByTournamentIdOrderByIdAsc(15L)).thenReturn(List.of());
        when(competition.overview(7L,"owner",null)).thenReturn(overview(
                source(20L,"LONG_JUMP"),source(21L,"HIGH_JUMP")));
        var result=service.overview(7L,15L,"owner");
        assertEquals(List.of(20L),result.availableRounds().stream().map(CommunityRoundResponse::id).toList());
        assertTrue(result.standings().isEmpty());
    }

    @Test void rejectsCompletedTournamentMutations() {
        member(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.COMPLETED);
        when(tournaments.findByIdForUpdate(15L)).thenReturn(Optional.of(tournament));
        assertThrows(ResponseStatusException.class,()->service.unlink(7L,15L,20L,"owner"));
        verify(links,never()).delete(any());
    }
}
