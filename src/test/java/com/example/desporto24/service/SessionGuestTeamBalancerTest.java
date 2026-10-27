package com.example.desporto24.service;

import com.example.desporto24.model.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.session.*;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionGuestTeamBalancerTest {
    private final CommunityStandingsArchiveRepository archives=mock(CommunityStandingsArchiveRepository.class);
    private final CommunityStandingsArchiveRowRepository rows=mock(CommunityStandingsArchiveRowRepository.class);
    private final CommunityPlayerRepository players=mock(CommunityPlayerRepository.class);
    private final SessionGuestTeamBalancer balancer=new SessionGuestTeamBalancer(archives,rows,players);

    @Test
    void approvedHistoricalGuestsAndRealAccountsShareTheSameExcelTeams() {
        var session=new Session();
        var community=new Community();community.setId(71L);session.setCommunity(community);
        var archive=new CommunityStandingsArchive();archive.setId(93L);
        when(archives.findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(71L,"INDIVIDUAL"))
                .thenReturn(Optional.of(archive));
        List<CommunityStandingsArchiveRow> standings=new ArrayList<>();
        for(int i=0;i<4;i++){
            var row=new CommunityStandingsArchiveRow();row.setId((long)i+1);
            row.setNormalizedName(List.of("rui","ana","afonso salvador","beto").get(i));
            row.setPoints((i+1)*10);row.setWins(i);row.setPlayed(6);
            standings.add(row);
        }
        when(rows.findByArchiveIdOrderByPositionAscIdAsc(93L)).thenReturn(standings);
        when(players.findByCommunityIdOrderByDisplayNameAsc(71L)).thenReturn(List.of());
        var registered=new ArrayList<SessionParticipant>();
        for(String name:List.of("rui","ana")){
            Profile p=new Profile();p.setId((long)name.length());p.setUsername(name);
            SessionParticipant member=new SessionParticipant();member.setProfile(p);
            registered.add(member);
        }
        List<SessionGuestRequest> guests=new ArrayList<>();
        for(int i=0;i<2;i++){
            var guest=new SessionGuestRequest();guest.setId((long)i+15);
            guest.setDisplayName(i==0?"Afonso Salvador":"Beto");
            guest.setNormalizedName(i==0?"afonso salvador":"beto");
            guest.setStatus(SessionGuestRequest.State.APPROVED);
            guests.add(guest);
        }
        var result=balancer.suggest(session,registered,guests);
        assertEquals(2,result.teamA().size());assertEquals(2,result.teamB().size());
        assertEquals(4,result.totalPlayers());
        assertEquals(4,result.playersWithStats());
        assertEquals(100.0,result.teamAScore()+result.teamBScore(),.001);
        var all=new ArrayList<>(result.teamA());all.addAll(result.teamB());
        assertEquals(2,all.stream().filter(p->p.guest()).count());
        assertTrue(all.stream().anyMatch(p->p.guest()&&p.displayName().equals("Afonso Salvador")
                &&p.username().equals("guest:15")&&p.strengthScore()==30.0));
        assertTrue(all.stream().noneMatch(p->p.guest()&&p.username().startsWith("@")));
        assertEquals(result,balancer.suggest(session,registered,guests));
    }

    @Test
    void unrankedGuestReceivesKnownAverageWithoutClaimingSomebodyElsesExcelPoints(){
        var session=new Session();var community=new Community();community.setId(71L);
        session.setCommunity(community);
        var archive=new CommunityStandingsArchive();archive.setId(93L);
        when(archives.findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(71L,"INDIVIDUAL"))
                .thenReturn(Optional.of(archive));
        var row=new CommunityStandingsArchiveRow();row.setId(1L);
        row.setNormalizedName("rui");row.setPoints(80);
        when(rows.findByArchiveIdOrderByPositionAscIdAsc(93L)).thenReturn(List.of(row));
        when(players.findByCommunityIdOrderByDisplayNameAsc(71L)).thenReturn(List.of());
        var p=new Profile();p.setId(1L);p.setUsername("rui");
        var member=new SessionParticipant();member.setProfile(p);
        var guest=new SessionGuestRequest();guest.setId(42L);
        guest.setDisplayName("Nome Novo");guest.setNormalizedName("nome novo");
        var result=balancer.suggest(session,List.of(member),List.of(guest));
        var all=new ArrayList<>(result.teamA());all.addAll(result.teamB());
        assertEquals(1,result.playersWithStats());
        var unranked=all.stream().filter(g->g.guest()).findFirst().orElseThrow();
        assertEquals(80.0,unranked.strengthScore());
        assertFalse(unranked.statsAvailable());
    }
}
