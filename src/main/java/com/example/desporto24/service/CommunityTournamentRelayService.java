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
import java.util.stream.Collectors;

/**
 * Relay team results: qualifying heats, a frozen final field, final heats,
 * and append-only jury decisions. No federation result is inferred from a provisional time.
 */
@Service @RequiredArgsConstructor @Transactional
public class CommunityTournamentRelayService {
    private static final String QUALIFICATION="QUALIFICATION", FINAL="FINAL";
    private static final String VALID="VALID", DISQUALIFIED="DISQUALIFIED";
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamPlayerRepository roster;
    private final CommunityTournamentRelayHeatRepository heats;
    private final CommunityTournamentRelayFinalistRepository finalists;
    private final CommunityTournamentRelayDecisionRepository decisions;
    private final CommunityTournamentTeamService teamService;

    @Transactional(readOnly=true)
    public CommunityRelayOverviewResponse overview(Long communityId,Long tournamentId,String username) {
        requireMember(communityId,username);
        return calculate(communityId,typed(communityId,tournamentId,false),username);
    }

    /** Backward compatible endpoint: previously stored times are always qualifying heats. */
    public CommunityRelayOverviewResponse record(Long communityId,Long tournamentId,
            int heatNumber,Long teamId,CommunityRelayHeatRequest request,String username) {
        return recordStage(communityId,tournamentId,QUALIFICATION,heatNumber,teamId,request,username);
    }

    public CommunityRelayOverviewResponse recordFinal(Long communityId,Long tournamentId,
            int heatNumber,Long teamId,CommunityRelayHeatRequest request,String username) {
        return recordStage(communityId,tournamentId,FINAL,heatNumber,teamId,request,username);
    }

    private CommunityRelayOverviewResponse recordStage(Long cid,Long tid,String stage,
            int heatNumber,Long teamId,CommunityRelayHeatRequest request,String username) {
        requireManager(cid,username);
        var tournament=typed(cid,tid,true);
        if(tournament.getStatus()==TournamentStatus.COMPLETED)
            throw conflict("O campeonato já terminou");
        if(QUALIFICATION.equals(stage) && tournament.getRelayFinalPublishedAt()!=null)
            throw conflict("A qualificação está fechada desde a publicação dos finalistas");
        if(FINAL.equals(stage) && tournament.getRelayFinalPublishedAt()==null)
            throw conflict("Publica os finalistas antes de registar tempos da final");
        if(heatNumber<1 || heatNumber>20)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Prova fora do intervalo 1–20");
        if(request==null || request.elapsedSeconds()==null || request.runnerProfileIds()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indica tempo e quatro corredores");
        BigDecimal time=request.elapsedSeconds().stripTrailingZeros();
        if(time.signum()<=0 || time.scale()>3 || time.precision()-time.scale()>8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tempo coletivo positivo com até três casas decimais");
        var legs=request.runnerProfileIds();
        if(legs.size()!=4 || legs.stream().anyMatch(Objects::isNull)
                || legs.stream().anyMatch(id->id<=0)
                || new HashSet<>(legs).size()!=4)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Seleciona quatro corredores distintos na ordem correta");
        var team=teams.findById(teamId).orElseThrow(()->notFound("Equipa não encontrada"));
        if(!team.getTournament().getId().equals(tid))
            throw notFound("Equipa não encontrada");
        if(FINAL.equals(stage) && !finalists.existsByTournamentIdAndTeamId(tid,teamId))
            throw conflict("Esta equipa não está entre os finalistas publicados");
        var athletes=roster.findByTournamentIdOrderByTeamIdAscIdAsc(tid).stream()
                .filter(p->p.getTeam().getId().equals(teamId))
                .map(p->p.getProfile().getId()).toList();
        if(athletes.size()!=4 || !new HashSet<>(athletes).equals(new HashSet<>(legs)))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A equipa precisa dos seus quatro membros confirmados");
        var result=heats.findByTournamentIdAndTeamIdAndStageAndHeatNumber(tid,teamId,stage,heatNumber)
                .orElseGet(()->CommunityTournamentRelayHeat.builder()
                        .tournament(tournament).team(team).stage(stage).heatNumber(heatNumber).build());
        if(DISQUALIFIED.equals(result.getDecisionStatus()))
            throw conflict("O tempo está desqualificado; revê primeiro a decisão de júri");
        result.setElapsedSeconds(time);
        result.setDecisionStatus(VALID);
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
        return calculate(cid,tournament,username);
    }

    /**
     * Freeze the finalist field and their best qualification marks. Maximum eight teams.
     * A tie across the eighth-place cutoff needs a jury resolution, not an ID tie-break.
     */
    public CommunityRelayOverviewResponse publishFinal(Long cid,Long tid,String username){
        requireManager(cid,username);
        var tournament=typed(cid,tid,true);
        if(tournament.getRelayFinalPublishedAt()!=null || tournament.getStatus()!=TournamentStatus.IN_PROGRESS)
            throw conflict("A final já foi publicada ou não há qualificação em curso");
        var qualifying=qualifyingStandings(heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(tid));
        if(qualifying.size()<2)throw conflict("São necessárias duas equipas com tempos válidos");
        final int limit=Math.min(8,qualifying.size());
        if(qualifying.size()>limit && qualifying.get(limit-1).bestSeconds()
                .compareTo(qualifying.get(limit).bestSeconds())==0)
            throw conflict("Empate no acesso à final: é necessária uma decisão desportiva");
        var byId=teams.findByTournamentIdOrderByIdAsc(tid).stream()
                .collect(Collectors.toMap(CommunityTournamentTeam::getId,x->x));
        Instant now=Instant.now();
        List<CommunityTournamentRelayFinalist> saved=new ArrayList<>();
        for(int i=0;i<limit;i++){
            var row=qualifying.get(i);
            var team=byId.get(row.teamId());
            if(team==null)throw conflict("Uma equipa qualificada deixou de existir");
            saved.add(CommunityTournamentRelayFinalist.builder()
                    .tournament(tournament).team(team).seed(i+1)
                    .qualifyingSeconds(row.bestSeconds()).publishedAt(now).build());
        }
        finalists.saveAllAndFlush(saved);
        tournament.setRelayFinalPublishedAt(now);
        tournaments.saveAndFlush(tournament);
        return calculate(cid,tournament,username);
    }

    /**
     * Disqualification and reinstatement never erase the original heat or past jury actions.
     * Qualification decisions become immutable after the final field is published.
     */
    public CommunityRelayOverviewResponse decide(Long cid,Long tid,Long heatId,
            CommunityRelayDecisionRequest request,String username) {
        Profile official=requireManager(cid,username);
        var tournament=typed(cid,tid,true);
        if(tournament.getStatus()==TournamentStatus.COMPLETED)
            throw conflict("O campeonato já terminou");
        if(request==null || request.status()==null || request.reason()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indica decisão e fundamento");
        String status=request.status().trim(),reason=request.reason().trim();
        if(!(VALID.equals(status) || DISQUALIFIED.equals(status))
                || reason.length()<5 || reason.length()>500)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Decisão inválida ou fundamento incompleto (5–500 caracteres)");
        var heat=heats.findById(heatId).orElseThrow(()->notFound("Prova não encontrada"));
        if(!heat.getTournament().getId().equals(tid))
            throw notFound("Prova não encontrada");
        String stage=heat.getStage()==null?QUALIFICATION:heat.getStage();
        if(QUALIFICATION.equals(stage) && tournament.getRelayFinalPublishedAt()!=null)
            throw conflict("As decisões da qualificação estão fechadas após publicar finalistas");
        if(!QUALIFICATION.equals(stage) && !FINAL.equals(stage))
            throw conflict("Fase de estafetas inválida");
        String previous=heat.getDecisionStatus()==null?VALID:heat.getDecisionStatus();
        if(previous.equals(status))
            throw conflict("O tempo já tem esse estado; não foi criada nova decisão");
        Instant now=Instant.now();
        heat.setDecisionStatus(status);
        heats.saveAndFlush(heat);
        decisions.saveAndFlush(CommunityTournamentRelayDecision.builder()
                .tournament(tournament).heat(heat).previousStatus(previous).newStatus(status)
                .reason(reason).actorProfileId(official.getId()).decidedAt(now).build());
        return calculate(cid,tournament,username);
    }

    private List<CommunityRelayStandingResponse> qualifyingStandings(
            List<CommunityTournamentRelayHeat> records) {
        return CommunityRelayStandings.calculate(records.stream()
                .filter(h->QUALIFICATION.equals(h.getStage()) || h.getStage()==null)
                .map(this::toHeat).toList());
    }

    private CommunityRelayOverviewResponse calculate(Long cid,CommunityTournament tournament,String username){
        var teamsList=teamService.list(cid,tournament.getId(),username);
        var records=heats.findByTournamentIdOrderByHeatNumberAscTeamIdAsc(tournament.getId());
        if(records.size()>1280)throw conflict("Demasiadas provas de estafetas");
        var result=records.stream().map(this::toHeat).toList();
        var qualifying=CommunityRelayStandings.calculate(result.stream()
                .filter(h->QUALIFICATION.equals(h.stage())).toList());
        boolean published=tournament.getRelayFinalPublishedAt()!=null;
        var finals=published?finalists.findByTournamentIdOrderBySeedAsc(tournament.getId()):List.<CommunityTournamentRelayFinalist>of();
        var finalistsResponse=finals.stream().map(f->new CommunityRelayFinalistResponse(
                f.getTeam().getId(),f.getTeam().getName(),f.getSeed(),f.getQualifyingSeconds())).toList();
        var finalRanks=published?CommunityRelayStandings.calculate(result.stream()
                .filter(h->FINAL.equals(h.stage())).toList()):List.<CommunityRelayStandingResponse>of();
        var jury=records.isEmpty()?List.<CommunityRelayDecisionResponse>of()
                :decisions.findByTournamentIdOrderByIdAsc(tournament.getId()).stream()
                  .map(d->new CommunityRelayDecisionResponse(d.getId(),d.getHeat().getId(),
                      d.getPreviousStatus(),d.getNewStatus(),d.getReason(),
                      d.getActorProfileId(),d.getDecidedAt())).toList();
        return new CommunityRelayOverviewResponse(tournament.getId(),
                tournament.getEventDiscipline(),teamsList,result,
                published?finalRanks:qualifying,published,finalistsResponse,qualifying,finalRanks,jury);
    }

    private CommunityRelayHeatResponse toHeat(CommunityTournamentRelayHeat h){
        return new CommunityRelayHeatResponse(h.getId(),h.getTeam().getId(),
                h.getTeam().getName(),h.getHeatNumber(),h.getElapsedSeconds(),
                List.of(h.getLeg1ProfileId(),h.getLeg2ProfileId(),h.getLeg3ProfileId(),
                        h.getLeg4ProfileId()),h.getRecordedAt(),
                h.getStage()==null?QUALIFICATION:h.getStage(),
                h.getDecisionStatus()==null?VALID:h.getDecisionStatus());
    }

    private CommunityTournament typed(Long cid,Long id,boolean lock){
        var tournament=(lock?tournaments.findByIdForUpdate(id):tournaments.findById(id))
                .orElseThrow(()->notFound("Campeonato não encontrado"));
        if(!tournament.getCommunity().getId().equals(cid)
                || tournament.getEntryType()!=TournamentEntryType.TEAM
                || tournament.getScoringMode()!=TournamentScoringMode.RELAY_BEST_TIME
                || tournament.getFormat()!=TournamentFormat.LEAGUE
                || !Integer.valueOf(4).equals(tournament.getPlayersPerTeam())
                || !"Atletismo".equals(tournament.getSport())
                || !"TRACK_RELAY".equals(tournament.getEventType())
                || !("RELAY_4X100".equals(tournament.getEventDiscipline())
                    || "RELAY_4X400".equals(tournament.getEventDiscipline())))
            throw notFound("Campeonato de estafetas não encontrado");
        return tournament;
    }
    private Profile actor(String username){
        return profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new AccessDeniedException("Utilizador inválido"));
    }
    private void requireMember(Long cid,String username){
        var user=actor(username);
        if(!members.existsByCommunityIdAndProfileId(cid,user.getId()))
            throw new AccessDeniedException("Não pertences à comunidade");
    }
    private Profile requireManager(Long cid,String username){
        var user=actor(username);
        var membership=members.findByCommunityIdAndProfileId(cid,user.getId())
                .orElseThrow(()->new AccessDeniedException("Não pertences à comunidade"));
        if(membership.getRole()!=CommunityRole.OWNER && membership.getRole()!=CommunityRole.ADMIN)
            throw new AccessDeniedException("Só os organizadores podem alterar tempos");
        return user;
    }
    private ResponseStatusException notFound(String message){
        return new ResponseStatusException(HttpStatus.NOT_FOUND,message);
    }
    private ResponseStatusException conflict(String message){
        return new ResponseStatusException(HttpStatus.CONFLICT,message);
    }
}
