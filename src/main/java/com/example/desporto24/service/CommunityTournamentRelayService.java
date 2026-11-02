package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Relay results belong to a registered TEAM and snapshot exactly four leg runners.
 * This is a provisional best-time table, never a W/D/L fixture or official baton-change validation.
 */
@Service @RequiredArgsConstructor @Transactional
public class CommunityTournamentRelayService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamPlayerRepository roster;
    private final CommunityTournamentRelayHeatRepository heats;
    private final CommunityTournamentTeamService teamService;

    @Transactional(readOnly=true)
    public CommunityRelayOverviewResponse overview(Long communityId,Long tournamentId,String username) {
        requireMember(communityId,username);
        var tournament=typed(communityId,tournamentId,false);
        return calculate(communityId,tournament,username);
    }

    public CommunityRelayOverviewResponse record(Long communityId,Long tournamentId,
            int heatNumber,Long teamId,CommunityRelayHeatRequest request,String username) {
        requireManager(communityId,username);
        var tournament=typed(communityId,tournamentId,true);
        if(tournament.getStatus()==TournamentStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"O campeonato já terminou");
        if(heatNumber<1||heatNumber>20)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indica uma prova entre 1 e 20");
        if(request==null || request.elapsedSeconds()==null || request.runnerProfileIds()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indica tempo e quatro corredores");
        BigDecimal time=request.elapsedSeconds().stripTrailingZeros();
        if(time.signum()<=0 || time.scale()>3 || time.precision()-time.scale()>8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tempo coletivo positivo com até três casas decimais");
        List<Long> legs=request.runnerProfileIds();
        if(legs.size()!=4 || legs.stream().anyMatch(Objects::isNull)
                || legs.stream().anyMatch(id->id<=0)
                || new HashSet<>(legs).size()!=4)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Seleciona quatro atletas distintos, por ordem da estafeta");
        var team=teams.findById(teamId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Equipa não encontrada"));
        if(!team.getTournament().getId().equals(tournamentId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Equipa não encontrada");
        var athletes=roster.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId).stream()
                .filter(p->p.getTeam().getId().equals(teamId))
                .map(p->p.getProfile().getId()).toList();
        if(athletes.size()!=4 || !new HashSet<>(athletes).equals(new HashSet<>(legs)))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A equipa precisa de quatro membros confirmados e todos devem correr");
        var result=heats.findByTournamentIdAndTeamIdAndHeatNumber(tournamentId,teamId,heatNumber)
                .orElseGet(()->CommunityTournamentRelayHeat.builder().tournament(tournament)
                        .team(team).heatNumber(heatNumber).build());
        result.setElapsedSeconds(time);
        result.setLeg1ProfileId(legs.get(0));
        result.setLeg2ProfileId(legs.get(1));
        result.setLeg3ProfileId(legs.get(2));
        result.setLeg4ProfileId(legs.get(3));
        result.setRecordedAt(Instant.now());
        heats.saveAndFlush(result);
        if(tournament.getStatus()==TournamentStatus.DRAFT){
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.saveAndFlush(tournament);
        }
        return calculate(communityId,tournament,username);
    }

    private CommunityRelayOverviewResponse calculate(Long cid,CommunityTournament tournament,String username) {
        var teamsList=teamService.list(cid,tournament.getId(),username);
        var records=heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(tournament.getId());
        if(records.size()>640)throw new ResponseStatusException(HttpStatus.CONFLICT,"Demasiadas provas");
        var result=records.stream().map(h->new CommunityRelayHeatResponse(h.getId(),
                h.getTeam().getId(),h.getTeam().getName(),h.getHeatNumber(),
                h.getElapsedSeconds(),List.of(h.getLeg1ProfileId(),h.getLeg2ProfileId(),
                        h.getLeg3ProfileId(),h.getLeg4ProfileId()),h.getRecordedAt())).toList();
        return new CommunityRelayOverviewResponse(tournament.getId(),
                tournament.getEventDiscipline(),teamsList,result,
                CommunityRelayStandings.calculate(result));
    }

    private CommunityTournament typed(Long cid,Long id,boolean lock){
        var tournament=(lock?tournaments.findByIdForUpdate(id):tournaments.findById(id))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Campeonato não encontrado"));
        if(!tournament.getCommunity().getId().equals(cid)
                || tournament.getEntryType()!=TournamentEntryType.TEAM
                || tournament.getScoringMode()!=TournamentScoringMode.RELAY_BEST_TIME
                || tournament.getFormat()!=TournamentFormat.LEAGUE
                || !Integer.valueOf(4).equals(tournament.getPlayersPerTeam())
                || !"Atletismo".equals(tournament.getSport())
                || !"TRACK_RELAY".equals(tournament.getEventType())
                || !("RELAY_4X100".equals(tournament.getEventDiscipline())
                    || "RELAY_4X400".equals(tournament.getEventDiscipline())))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Campeonato de estafetas não encontrado");
        return tournament;
    }

    private Profile actor(String username){
        return profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new AccessDeniedException("Utilizador inválido"));
    }
    private void requireMember(Long cid,String username){
        var actor=actor(username);
        if(!members.existsByCommunityIdAndProfileId(cid,actor.getId()))
            throw new AccessDeniedException("Não pertences à comunidade");
    }
    private void requireManager(Long cid,String username){
        var actor=actor(username);
        var membership=members.findByCommunityIdAndProfileId(cid,actor.getId())
                .orElseThrow(()->new AccessDeniedException("Não pertences à comunidade"));
        if(membership.getRole()!=CommunityRole.OWNER && membership.getRole()!=CommunityRole.ADMIN)
            throw new AccessDeniedException("Só os organizadores podem registar tempos");
    }
}
