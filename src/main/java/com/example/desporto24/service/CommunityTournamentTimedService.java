package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
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
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provisional best-time championships: swimming, athletics track and K1 canoe sprint.
 * Every source round must match the exact sport, type and discipline.
 * Results are NOT converted into positional race points or head-to-head wins.
 * Completed tournaments are intentionally unsupported until immutable snapshots exist.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentTimedService {
    private static final int MAX_ROUNDS=100;
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityRoundRepository rounds;
    private final CommunityTournamentRaceRoundRepository links;
    private final CommunityCompetitionService competition;

    @Transactional(readOnly=true)
    public CommunityTournamentTimedOverviewResponse overview(Long communityId,Long tournamentId,String username) {
        checkMember(communityId,username);
        CommunityTournament tournament=typed(communityId,tournamentId,false);
        return calculate(communityId,tournament,username);
    }

    public CommunityTournamentTimedOverviewResponse link(Long communityId,Long tournamentId,Long roundId,String username) {
        checkManager(communityId,username);
        CommunityTournament tournament=typed(communityId,tournamentId,true);
        requireOpen(tournament);
        CommunityRound source=rounds.findById(roundId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Jornada não encontrada"));
        if (!eligible(tournament,source.getCommunity().getId(),source.getSport(),
                source.getEventType(),source.getEventDiscipline(),communityId))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A jornada deve ser da mesma comunidade, modalidade e distância");
        if (links.existsByTournamentIdAndRoundId(tournamentId,roundId))
            return calculate(communityId,tournament,username);
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).size()>=MAX_ROUNDS)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Máximo de 100 jornadas por campeonato");
        var sourceRead=allRounds(communityId,username).stream()
                .filter(r->roundId.equals(r.id())).findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,
                        "Não foi possível confirmar os resultados desta jornada"));
        if (!eligible(tournament,communityId,sourceRead.sport(),sourceRead.eventType(),
                sourceRead.eventDiscipline(),communityId)
                || sourceRead.entries()==null
                || sourceRead.entries().stream().filter(e->CommunityTimedStandings.valid(e, maxDecimals(tournament))).count()<2)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A jornada deve conter tempos válidos de pelo menos dois participantes");
        links.save(CommunityTournamentRaceRound.builder().round(source).tournament(tournament).build());
        if (tournament.getStatus()==TournamentStatus.DRAFT) {
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
        }
        return calculate(communityId,tournament,username);
    }

    public CommunityTournamentTimedOverviewResponse unlink(Long communityId,Long tournamentId,Long roundId,String username) {
        checkManager(communityId,username);
        CommunityTournament tournament=typed(communityId,tournamentId,true);
        requireOpen(tournament);
        links.findByTournamentIdAndRoundId(tournamentId,roundId).ifPresent(links::delete);
        links.flush();
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).isEmpty()) {
            tournament.setStatus(TournamentStatus.DRAFT);
            tournament.setStartedAt(null);
            tournaments.save(tournament);
        }
        return calculate(communityId,tournament,username);
    }

    private CommunityTournamentTimedOverviewResponse calculate(Long communityId,
            CommunityTournament tournament,String username) {
        var saved=links.findByTournamentIdOrderByIdAsc(tournament.getId());
        if(saved.size()>MAX_ROUNDS) throw new ResponseStatusException(HttpStatus.CONFLICT,"Demasiadas jornadas associadas");
        var all=allRounds(communityId,username);
        Map<Long,CommunityRoundResponse> byId=all.stream()
                .collect(Collectors.toMap(CommunityRoundResponse::id,r->r,(a,b)->a));
        List<CommunityRoundResponse> linked=new ArrayList<>();
        for(var relation:saved) {
            var round=byId.get(relation.getRound().getId());
            // Do not silently drop historical linked records or drift to another discipline.
            if(round==null || !eligible(tournament,communityId,round.sport(),
                    round.eventType(),round.eventDiscipline(),communityId))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Jornada ligada indisponível ou prova alterada; verifica os resultados");
            linked.add(round);
        }
        Set<Long> selected=saved.stream().map(link->link.getRound().getId()).collect(Collectors.toSet());
        List<CommunityRoundResponse> available=all.stream()
                .filter(r->eligible(tournament,communityId,r.sport(),r.eventType(),r.eventDiscipline(),communityId))
                .filter(r->!selected.contains(r.id()))
                .filter(r->r.entries()!=null && r.entries().stream().filter(e->CommunityTimedStandings.valid(e, maxDecimals(tournament))).count()>=2)
                .limit(MAX_ROUNDS).toList();
        return new CommunityTournamentTimedOverviewResponse(
                tournament.getId(),tournament.getEventType(),tournament.getEventDiscipline(),
                List.copyOf(linked),available,
                CommunityTimedStandings.calculate(linked,maxDecimals(tournament)));
    }

    private List<CommunityRoundResponse> allRounds(Long communityId,String username) {
        return competition.overview(communityId,username,null).rounds();
    }

    private int maxDecimals(CommunityTournament tournament) {
        return tournament.getScoringMode()==TournamentScoringMode.TRACK_BEST_TIME
                || tournament.getScoringMode()==TournamentScoringMode.CANOE_BEST_TIME ? 3 : 2;
    }

    private boolean eligible(CommunityTournament tournament,Long roundCommunityId,
            String sport,String eventType,String discipline,Long communityId) {
        return Objects.equals(roundCommunityId,communityId)
                && Objects.equals(sport,tournament.getSport())
                && Objects.equals(eventType,tournament.getEventType())
                && Objects.equals(discipline,tournament.getEventDiscipline())
                && (("Canoagem Velocidade".equals(sport) && "CANOE_SPRINT".equals(eventType)
                        && tournament.getScoringMode()==TournamentScoringMode.CANOE_BEST_TIME)
                    || ("Natação".equals(sport) && "SWIM_LCM".equals(eventType)
                        && tournament.getScoringMode()==TournamentScoringMode.SWIM_BEST_TIME)
                    || ("Atletismo".equals(sport) && "TRACK".equals(eventType)
                        && tournament.getScoringMode()==TournamentScoringMode.TRACK_BEST_TIME));
    }

    private CommunityTournament typed(Long communityId,Long tournamentId,boolean lock) {
        CommunityTournament tournament=(lock
                ? tournaments.findByIdForUpdate(tournamentId)
                : tournaments.findById(tournamentId))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Campeonato não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)
                || !(tournament.getScoringMode()==TournamentScoringMode.SWIM_BEST_TIME
                        || tournament.getScoringMode()==TournamentScoringMode.TRACK_BEST_TIME
                        || tournament.getScoringMode()==TournamentScoringMode.CANOE_BEST_TIME)
                || tournament.getEntryType()!=TournamentEntryType.INDIVIDUAL
                || tournament.getFormat()!=TournamentFormat.LEAGUE
                || !(("Canoagem Velocidade".equals(tournament.getSport())
                        && "CANOE_SPRINT".equals(tournament.getEventType())
                        && tournament.getScoringMode()==TournamentScoringMode.CANOE_BEST_TIME)
                    || ("Natação".equals(tournament.getSport())
                        && "SWIM_LCM".equals(tournament.getEventType())
                        && tournament.getScoringMode()==TournamentScoringMode.SWIM_BEST_TIME)
                    || ("Atletismo".equals(tournament.getSport())
                        && "TRACK".equals(tournament.getEventType())
                        && tournament.getScoringMode()==TournamentScoringMode.TRACK_BEST_TIME))
                || tournament.getEventDiscipline()==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Campeonato cronometrado não encontrado");
        return tournament;
    }

    private void requireOpen(CommunityTournament tournament) {
        if (tournament.getStatus()==TournamentStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Campeonato concluído");
    }
    private void checkMember(Long communityId,String username) {
        var actor=profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new AccessDeniedException("Utilizador inválido"));
        if (!members.existsByCommunityIdAndProfileId(communityId,actor.getId()))
            throw new AccessDeniedException("Não pertences à comunidade");
    }
    private void checkManager(Long communityId,String username) {
        var actor=profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new AccessDeniedException("Utilizador inválido"));
        var membership=members.findByCommunityIdAndProfileId(communityId,actor.getId())
                .orElseThrow(()->new AccessDeniedException("Não pertences à comunidade"));
        if(membership.getRole()!=CommunityRole.OWNER && membership.getRole()!=CommunityRole.ADMIN)
            throw new AccessDeniedException("Só administradores podem gerir o campeonato");
    }
}
