package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityHistoricalPlayerLink;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.community.CommunityTrackSprintBracket;
import com.example.desporto24.model.community.CommunityTrackSprintIncident;
import com.example.desporto24.model.community.CommunityTrackSprintReview;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityHistoricalPlayerLinkRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunityTrackSprintBracketRepository;
import com.example.desporto24.repository.community.CommunityTrackSprintIncidentRepository;
import com.example.desporto24.repository.community.CommunityTrackSprintReviewRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityRacketRuleRepository;
import com.example.desporto24.repository.community.CommunityRaceRuleRepository;
import com.example.desporto24.model.community.CommunityRaceRule;
import com.example.desporto24.model.community.CommunityRacketRule;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRowRepository;
import com.example.desporto24.model.community.CommunityStandingsArchive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityCompetitionService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityPlayerRepository playerRepository;
    private final CommunityHistoricalPlayerLinkRepository historicalPlayerLinkRepository;
    private final CommunityRoundRepository roundRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTrackSprintBracketRepository trackSprintBracketRepository;
    private final CommunityTrackSprintIncidentRepository trackSprintIncidentRepository;
    private final CommunityTrackSprintReviewRepository sprintReviewRepository;
    private final CommunityRacketRuleRepository racketRuleRepository;
    private final CommunityRaceRuleRepository raceRuleRepository;
    private final CommunityRoundEntryRepository entryRepository;
    private final CommunitySeasonRepository seasonRepository;
    private final CommunityStandingsArchiveRepository standingsArchiveRepository;
    private final CommunityStandingsArchiveRowRepository standingsArchiveRowRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public CommunityCompetitionOverviewResponse overview(Long communityId, String username) {
        return overview(communityId, username, null);
    }

    @Transactional(readOnly = true)
    public CommunityCompetitionOverviewResponse overview(
            Long communityId,
            String username,
            Long seasonId) {
        Profile actor = profile(username);
        Community community = community(communityId);
        requireAccess(community, actor);

        if (seasonId != null) {
            CommunitySeason selected = seasonRepository.findById(seasonId)
                    .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
            if (!selected.getCommunity().getId().equals(communityId)) {
                throw new IllegalArgumentException("A época não pertence a esta comunidade");
            }
        }

        Long activeSeasonId = seasonRepository.findByCommunityIdAndActiveTrue(communityId)
                .map(CommunitySeason::getId)
                .orElse(null);

        List<CommunityRound> rounds = seasonId == null
                ? roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(communityId)
                : roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                        communityId, seasonId);

        AcceptedAliases confirmedAliases = acceptedAliasProfiles(communityId);
        List<CommunityRacketRuleResponse> racketRules = seasonId == null
                ? List.of()
                : racketRuleRepository.findBySeasonId(seasonId).stream()
                        .map(this::racketRuleResponse).toList();
        List<CommunityRaceRuleResponse> raceRules = seasonId == null
                ? List.of() : raceRuleRepository.findBySeasonId(seasonId).stream()
                .map(rule -> new CommunityRaceRuleResponse(
                        rule.getSport(), CommunityRaceStandings.parsePoints(rule.getPointsByPlace())))
                .toList();
        List<CommunityRoundResponse> roundResponses = rounds.stream()
                .map(round -> roundResponse(round, confirmedAliases)).toList();
        return new CommunityCompetitionOverviewResponse(
                settings(community),
                seasonRepository.findByCommunityIdOrderByCreatedAtDesc(communityId)
                        .stream()
                        .map(season -> seasonResponse(community, season))
                        .toList(),
                seasonId,
                activeSeasonId,
                roundResponses,
                standings(community, seasonId, confirmedAliases),
                CommunityRacketStandings.calculate(roundResponses, racketRules),
                CommunityRaceStandings.calculate(roundResponses, raceRules)
        );
    }

    /** Register a finals-eight template, without claiming qualifying positions
     * or assigning riders. Quarter-final pairings remain organizer-entered.
     */
    public CommunityTrackSprintBracketDtos.Overview createTrackSprintBracket(
            Long communityId, Long seasonId, String username,
            CommunityTrackSprintBracketDtos.Create request) {
        requireAdmin(communityId, profile(username));
        Community community = community(communityId);
        if (!community.isRankingsEnabled())
            throw new IllegalArgumentException("Ativa primeiro a classificação da comunidade");
        CommunityRoundSport.resolve(community, "Ciclismo de Pista"); // existing catalogue only
        CommunitySeason season = seasonRepository.lockByIdAndCommunityId(seasonId, communityId)
                .orElseThrow(() -> new IllegalArgumentException("Época indisponível nesta comunidade"));
        if (!season.isActive())
            throw new IllegalStateException("Não podes criar um quadro numa época encerrada");
        String name = request.name() == null ? "" : request.name().trim();
        if (name.isBlank() || name.length() > 80)
            throw new IllegalArgumentException("Indica um nome válido para o quadro");
        String format = request.format() == null || request.format().isBlank()
                ? "EIGHT_RIDER_FINALS" : request.format();
        if (!Set.of("EIGHT_RIDER_FINALS", "SIXTEEN_RIDER_1_8", "TWENTY_EIGHT_RIDER_1_16",
                "OLYMPIC_TWENTY_FOUR_REPECHAGE").contains(format))
            throw new IllegalArgumentException("Formato de Sprint não suportado");
        CommunityRound qualification = null;
        List<String> seeds = List.of();
        if (!"EIGHT_RIDER_FINALS".equals(format)) {
            if (request.qualifyingRoundId() == null)
                throw new IllegalArgumentException("Seleciona a qualificação real de 200 m");
            qualification = roundRepository.findById(request.qualifyingRoundId())
                    .orElseThrow(() -> new IllegalArgumentException("Qualificação não encontrada"));
            if (!qualification.getCommunity().getId().equals(communityId)
                    || qualification.getSeason() == null
                    || !qualification.getSeason().getId().equals(seasonId))
                throw new IllegalArgumentException("Qualificação de outra comunidade ou época");
            seeds = CommunityTrackSprintQualifying16.seeds(
                    roundResponse(qualification, acceptedAliasProfiles(communityId)),
                    "TWENTY_EIGHT_RIDER_1_16".equals(format) ? 28
                            : "OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(format) ? 24 : 16,
                    request.officialTieDecisions());
        } else {
            if (request.qualifyingRoundId() != null)
                throw new IllegalArgumentException("A qualificação só pode ligar ao quadro de 16, 24 ou 28 ciclistas");
            if (request.officialTieDecisions() != null && !request.officialTieDecisions().isEmpty())
                throw new IllegalArgumentException("Um quadro de oito ciclistas não aceita desempates de 200 m");
        }
        List<CommunityTrackSprintBracketDtos.TieDecision> officialDecisions =
                request.officialTieDecisions() == null ? List.of() : request.officialTieDecisions();
        CommunityTrackSprintBracket bracket = trackSprintBracketRepository.save(
                CommunityTrackSprintBracket.builder().community(community).season(season)
                        .name(name).format(format).qualifyingRound(qualification)
                        .qualifiedRiders(seeds)
                        .tieDecisionsJson(encodeSprintTieDecisions(officialDecisions))
                        .tieDecisionsRecordedBy(officialDecisions.isEmpty() ? null : username)
                        .createdAt(Instant.now()).build());
        return trackSprintBracketResponse(bracket, List.of());
    }

    public void deleteTrackSprintBracket(Long communityId, Long seasonId,
            Long bracketId, String username) {
        requireAdmin(communityId, profile(username));
        CommunitySeason season = seasonRepository.lockByIdAndCommunityId(seasonId, communityId)
                .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
        if (!season.isActive())
            throw new IllegalStateException("Não podes apagar um quadro de época encerrada");
        CommunityTrackSprintBracket bracket = trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId, communityId, seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Quadro não encontrado"));
        String prefix = "SP" + bracketId + "-";
        boolean containsHeats = roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId, seasonId)
                .stream().anyMatch(r -> r.getSprintSeriesKey() != null
                        && r.getSprintSeriesKey().startsWith(prefix));
        if (bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não podes eliminar um quadro encerrado oficialmente");
        if (containsHeats)
            throw new IllegalStateException("Apaga as mangas do quadro antes de o eliminar");
        if (trackSprintIncidentRepository.existsByBracketId(bracketId))
            throw new IllegalStateException("Um quadro com incidentes documentados não pode ser apagado");
        trackSprintBracketRepository.delete(bracket);
        trackSprintBracketRepository.flush();
    }

    private void protectSprintQualification(Long communityId, Long roundId) {
        if (trackSprintBracketRepository.existsByCommunityIdAndQualifyingRoundId(communityId, roundId))
            throw new IllegalStateException(
                    "O resultado dos 200 m já suporta um quadro Sprint; elimina primeiro o quadro");
    }

    @Transactional(readOnly = true)
    public List<CommunityTrackSprintBracketDtos.Overview> trackSprintBrackets(
            Long communityId, Long seasonId, String username) {
        requireAccess(community(communityId), profile(username));
        CommunitySeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
        if (!season.getCommunity().getId().equals(communityId))
            throw new IllegalArgumentException("Época de outra comunidade");
        List<CommunityTrackSprintBracket> brackets = trackSprintBracketRepository
                .findByCommunityIdAndSeasonIdOrderByIdAsc(communityId, seasonId);
        if (brackets.isEmpty()) return List.of();
        AcceptedAliases aliases = acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds = roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId, seasonId)
                 .stream().filter(r -> "TRACK_SPRINT_HEAT".equals(r.getEventType())
                        || "TRACK_SPRINT_REPECHAGE".equals(r.getEventType())
                        || "TRACK_SPRINT_5_8".equals(r.getEventType()))
                .map(r -> roundResponse(r, aliases)).toList();
        return brackets.stream().map(b -> trackSprintBracketResponse(b, rounds)).toList();
    }

    private CommunityTrackSprintBracketDtos.Overview trackSprintBracketResponse(
            CommunityTrackSprintBracket bracket, List<CommunityRoundResponse> rounds) {
        return new CommunityTrackSprintBracketDtos.Overview(
                bracket.getId(), bracket.getSeason().getId(), bracket.getName(),
                bracket.getFormat() == null ? "EIGHT_RIDER_FINALS" : bracket.getFormat(),
                CommunityTrackSprintBracketPlanner.project(bracket.getId(), rounds,
                        bracket.getQualifiedRiders() == null ? List.of() : bracket.getQualifiedRiders(),
                        sprintOfficialReruns(bracket.getId()),sprintOfficialAwards(bracket.getId()),sprintOfficialDisqualified(bracket.getId()),sprintBo3OfficialHeats(bracket.getId())),
                bracket.getQualifyingRound() == null ? null : bracket.getQualifyingRound().getId(),
                bracket.getQualifiedRiders() == null ? List.of() : List.copyOf(bracket.getQualifiedRiders()),
                decodeSprintTieDecisions(bracket.getTieDecisionsJson()),
                bracket.getTieDecisionsRecordedBy(),
                trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracket.getId())
                        .stream().map(CommunityCompetitionService::sprintIncidentResponse).toList(),
                sprintInterruptionResponse(bracket,rounds));
    }

    private CommunityTrackSprintBracketDtos.Interruption sprintInterruptionResponse(
            CommunityTrackSprintBracket bracket,List<CommunityRoundResponse> rounds) {
        if(bracket.getInterruptionStage()==null) return null;
        var matches=CommunityTrackSprintBracketPlanner.project(bracket.getId(),rounds,
                bracket.getQualifiedRiders()==null?List.of():bracket.getQualifiedRiders(),
                sprintOfficialReruns(bracket.getId()),sprintOfficialAwards(bracket.getId()),sprintOfficialDisqualified(bracket.getId()),sprintBo3OfficialHeats(bracket.getId()));
        List<CommunityTrackSprintBracketDtos.InterruptionPlacement> placements=
                CommunityTrackSprintInterruption.placements(
                        bracket.getInterruptionStage(),matches);
        return new CommunityTrackSprintBracketDtos.Interruption(
                bracket.getInterruptionStage(),bracket.getInterruptionReference(),
                bracket.getInterruptionReason(),bracket.getInterruptionRecordedBy(),
                bracket.getInterruptionRecordedAt(),
                "BEFORE_QUARTERS".equals(bracket.getInterruptionStage()),
                List.copyOf(placements));
    }

    /**
     * UCI 3.2.048 bis: irrevocable official termination of a 24-rider
     * Olympic Sprint competition. This stores an audited commissaires'
     * interruption decision, NEVER creates missing heats or awards points.
     */
    public CommunityTrackSprintBracketDtos.Overview endTrackSprintCompetition(
            Long communityId,Long seasonId,Long bracketId,String username,
            CommunityTrackSprintBracketDtos.EndEvent request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"A época está encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não encontrado"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new IllegalArgumentException("Este procedimento exige formato olímpico de 24");
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta interrupção oficial já foi registada");
        if(trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId).stream()
                .anyMatch(i -> CommunityTrackSprintReviewPolicy.blocks(
                    latestReviewAction(i.getId()))))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Revisão oficial pendente ou revogada; não é possível terminar o quadro");
        if(request==null||request.reference()==null
                ||request.reference().trim().length()<12
                ||request.reference().trim().length()>255
                ||request.reason()==null||request.reason().trim().length()<12
                ||request.reason().trim().length()>500)
            throw new IllegalArgumentException("Ata e fundamentação oficiais obrigatórias");
        AcceptedAliases aliases=acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds=roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
                .stream().filter(r->Set.of("TRACK_SPRINT_HEAT",
                        "TRACK_SPRINT_REPECHAGE","TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,aliases)).toList();
        var matches=CommunityTrackSprintBracketPlanner.project(bracketId,rounds,
                bracket.getQualifiedRiders(),sprintOfficialReruns(bracketId),sprintOfficialAwards(bracketId),sprintOfficialDisqualified(bracketId),sprintBo3OfficialHeats(bracketId));
        String stage;
        try { stage=CommunityTrackSprintInterruption.stage(matches); }
        catch(IllegalStateException invalid) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,invalid.getMessage(),invalid);
        }
        bracket.setInterruptionStage(stage);
        bracket.setInterruptionReason(request.reason().trim());
        bracket.setInterruptionReference(request.reference().trim());
        bracket.setInterruptionRecordedBy(username);
        bracket.setInterruptionRecordedAt(Instant.now());
        trackSprintBracketRepository.saveAndFlush(bracket);
        return trackSprintBracketResponse(bracket,rounds);
    }

    private static final com.fasterxml.jackson.databind.ObjectMapper SPRINT_TIE_MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();

    private static String encodeSprintTieDecisions(
            List<CommunityTrackSprintBracketDtos.TieDecision> decisions) {
        if (decisions.isEmpty()) return null; // all historical brackets remain unchanged
        try {
            return SPRINT_TIE_MAPPER.writeValueAsString(decisions);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalArgumentException("Não foi possível guardar o desempate oficial", e);
        }
    }

    private static List<CommunityTrackSprintBracketDtos.TieDecision> decodeSprintTieDecisions(
            String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return SPRINT_TIE_MAPPER.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<
                    List<CommunityTrackSprintBracketDtos.TieDecision>>() {});
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("O registo oficial do desempate está ilegível", e);
        }
    }


    private String latestReviewAction(Long incidentId) {
        return CommunityTrackSprintReviewPolicy.latest(
                sprintReviewRepository.findByIncidentIdOrderByIdAsc(incidentId));
    }

    private void rejectReviewedSprintProgression(Long bracketId,String seriesKey) {
        String slot=seriesKey.substring(seriesKey.lastIndexOf('-')+1);
        for(var incident:trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId)) {
            String latest=latestReviewAction(incident.getId());
            if(!CommunityTrackSprintReviewPolicy.blocks(latest))continue;
            String affected=incident.getSeriesKey();
            String ancestor=affected.substring(affected.lastIndexOf('-')+1);
            if(CommunityTrackSprintReviewPolicy.affects(ancestor,slot))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Revisão oficial pendente ou revogação sem retificação: manga suspensa");
        }
    }

    private CommunityTrackSprintReviewDtos.View sprintReviewView(CommunityTrackSprintReview e) {
        return new CommunityTrackSprintReviewDtos.View(e.getId(),e.getIncident().getId(),
                e.getAction(),e.getOfficialName(),e.getOfficialReference(),
                e.getPanelReference(),e.getReason(),e.getRecordedBy(),e.getRecordedAt(),
                e.getRemedyKind(),decodeRerunRiders(e.getOrderedRidersJson()),
                e.getNextHeatNumber());
    }

    private CommunityTrackSprintIncident sprintReviewedIncident(
            Long communityId,Long seasonId,Long bracketId,Long incidentId) {
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro de outra comunidade ou época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Só Sprint olímpico 24");
        return trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .filter(i->"OFFICIAL_SERIES_AWARDED".equals(i.getStatus()))
                .filter(i->i.getSeriesKey().matches("SP"+bracketId+"-(Q[1-4]|S[1-2]|B|G)"))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,
                        "Só séries BO3 com veredito oficial documentado aceitam este procedimento"));
    }

    /** UCI 3.2.011: visible to authorized community members; historical events are immutable. */
    @Transactional(readOnly=true)
    public List<CommunityTrackSprintReviewDtos.View> sprintVerdictReviews(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,String username) {
        requireAccess(community(communityId),profile(username));
        sprintReviewedIncident(communityId,seasonId,bracketId,incidentId);
        return sprintReviewRepository.findByIncidentIdOrderByIdAsc(incidentId)
                .stream().map(this::sprintReviewView).toList();
    }

    private void assertNoReviewedDescendants(Long communityId,Long seasonId,
            Long bracketId,String affectedSlot) {
        String prefix="SP"+bracketId+"-";
        for(var round:roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId)) {
            String key=round.getSprintSeriesKey();
            if(key!=null&&key.startsWith(prefix)) {
                String descendant=key.substring(prefix.length());
                if(!descendant.equals(affectedSlot)
                        &&CommunityTrackSprintReviewPolicy.affects(affectedSlot,descendant))
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Existem resultados seguintes; é necessária retificação externa documentada");
            }
        }
        for(var i:trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId)) {
            String descendant=i.getSeriesKey().substring(prefix.length());
            if(!descendant.equals(affectedSlot)
                    &&CommunityTrackSprintReviewPolicy.affects(affectedSlot,descendant)
                    &&Set.of("OFFICIAL_HEAT_AWARDED","OFFICIAL_SERIES_AWARDED",
                            "RESTART_AUTHORIZED").contains(i.getStatus()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existem decisões oficiais seguintes; não alterar a progressão");
        }
    }

    /** Record an actual request by the president. A user appeal is NOT a UCI 3.2.011 review. */
    public CommunityTrackSprintReviewDtos.View requestSprintVerdictReview(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintReviewDtos.Request request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var incident=sprintReviewedIncident(communityId,seasonId,bracketId,incidentId);
        if(incident.getBracket().getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Competição interrompida");
        if(request==null||!Boolean.TRUE.equals(request.originalJudgeRefereeDecision())
                ||!validOfficialName(request.presidentName())
                ||!validOfficialReference(request.requestReference())
                ||!validOfficialReason(request.reason()))
            throw new IllegalArgumentException("Indica presidente, ata, motivo e origem no juiz-árbitro");
        var last=latestReviewAction(incidentId);
        if("REQUESTED".equals(last)||"OVERTURNED".equals(last)||"RECTIFIED".equals(last))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe revisão pendente ou revogação sem retificação");
        String slot=incident.getSeriesKey().substring(incident.getSeriesKey().lastIndexOf('-')+1);
        assertNoReviewedDescendants(communityId,seasonId,bracketId,slot);
        return sprintReviewView(sprintReviewRepository.save(CommunityTrackSprintReview.builder()
                .incident(incident).action("REQUESTED")
                .officialName(request.presidentName().trim())
                .officialReference(request.requestReference().trim())
                .reason(request.reason().trim()).recordedBy(username)
                .recordedAt(Instant.now()).build()));
    }

    /** Record the panel's review and the president's express verdict: no inferred winner. */
    public CommunityTrackSprintReviewDtos.View resolveSprintVerdictReview(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintReviewDtos.Resolve request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var incident=sprintReviewedIncident(communityId,seasonId,bracketId,incidentId);
        if(incident.getBracket().getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Competição interrompida");
        if(!"REQUESTED".equals(latestReviewAction(incidentId)))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Não existe pedido oficial pendente");
        if(request==null||!Set.of("UPHELD","OVERTURNED").contains(request.outcome())
                ||!validOfficialName(request.presidentName())
                ||!validOfficialReference(request.panelReviewReference())
                ||!validOfficialReference(request.presidentDecisionReference())
                ||!validOfficialReason(request.reason()))
            throw new IllegalArgumentException(
                    "Exige revisão do colégio e decisão documentada pelo presidente");
        String slot=incident.getSeriesKey().substring(incident.getSeriesKey().lastIndexOf('-')+1);
        assertNoReviewedDescendants(communityId,seasonId,bracketId,slot);
        // OVERTURNED suppresses the effective award projection without touching
        // historical heat results or the incident's original signed decision.
        // It then blocks related stages until a separately supported correction.
        return sprintReviewView(sprintReviewRepository.save(CommunityTrackSprintReview.builder()
                .incident(incident).action(request.outcome())
                .officialName(request.presidentName().trim())
                .officialReference(request.presidentDecisionReference().trim())
                .panelReference(request.panelReviewReference().trim())
                .reason(request.reason().trim()).recordedBy(username)
                .recordedAt(Instant.now()).build()));
    }

    /**
     * Records an explicit, post-revocation chair instruction allowing the same
     * two riders to continue the original BO3. The prior disqualification must
     * be expressly annulled. No heat is created, no previous heat is rewritten
     * and no winner is inferred from the correction.
     *
     * Different riders, different start order, or a new official winner need a
     * separate, regulation-specific procedure rather than this endpoint.
     */
    public CommunityTrackSprintReviewDtos.View rectifyOverturnedSprintVerdict(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintReviewDtos.Rectify request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var incident=sprintReviewedIncident(communityId,seasonId,bracketId,incidentId);
        if(incident.getBracket().getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Evento definitivamente interrompido");
        var history=sprintReviewRepository.findByIncidentIdOrderByIdAsc(incidentId);
        if(!"OVERTURNED".equals(CommunityTrackSprintReviewPolicy.latest(history)))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só um veredito revogado e sem retificação admite continuação");
        var overturned=history.get(history.size()-1);
        if(request==null||!Boolean.TRUE.equals(request.sanctionAnnulledByPresident())
                ||!validOfficialName(request.presidentName())
                ||!validOfficialReference(request.instructionReference())
                ||!validOfficialReference(request.overturnedDecisionReference())
                ||!overturned.getOfficialReference().equals(
                        request.overturnedDecisionReference().trim())
                ||!validOfficialReason(request.reason())
                ||request.nextHeatNumber()==null
                ||request.nextHeatNumber()<1||request.nextHeatNumber()>3
                ||request.orderedRiders()==null||request.orderedRiders().size()!=2
                ||!"DISQUALIFICATION".equals(incident.getSanctionType()))
            throw new IllegalArgumentException(
                    "Ata de continuação, referência da revogação, anulação da sanção e dois ciclistas obrigatórios");
        String slot=incident.getSeriesKey().substring(
                incident.getSeriesKey().lastIndexOf('-')+1);
        assertNoReviewedDescendants(communityId,seasonId,bracketId,slot);
        var all=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId);
        var rounds=all.stream().filter(r->Set.of(
                "TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE","TRACK_SPRINT_5_8")
                    .contains(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        var match=trackSprintBracketResponse(incident.getBracket(),rounds)
                .matches().stream().filter(m->incident.getSeriesKey().equals(m.seriesKey()))
                .findFirst().orElseThrow(()->new IllegalArgumentException("Série não encontrada"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.riderA()==null||match.riderB()==null
                ||match.riderC()!=null||match.riderD()!=null||match.winner()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A série não está apta a continuar com dois ciclistas");
        List<String> declared=new ArrayList<>();
        for(String rider:request.orderedRiders()) {
            if(rider==null||rider.isBlank())
                throw new IllegalArgumentException("Identifica ambos os ciclistas");
            declared.add(rider.trim().toLowerCase(Locale.ROOT));
        }
        List<String> required=List.of(match.riderA(),match.riderB());
        if(!declared.equals(required)
                ||!required.contains(incident.getSanctionedUsername()))
            throw new IllegalArgumentException(
                    "A continuação simples exige os mesmos dois ciclistas pela ordem original");
        int existingHeats=(int)rounds.stream()
                .filter(r->incident.getSeriesKey().equals(r.sprintSeriesKey())).count();
        int awards=sprintBo3OfficialHeats(bracketId)
                .getOrDefault(incident.getSeriesKey(),Map.of()).size();
        int next=existingHeats+awards+1;
        if(next!=request.nextHeatNumber()||next!=incident.getHeatNumber()||next>3)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A numeração da próxima manga deve preservar o histórico real");
        String orderJson;
        try { orderJson=SPRINT_TIE_MAPPER.writeValueAsString(required); }
        catch(com.fasterxml.jackson.core.JsonProcessingException invalid) {
            throw new IllegalStateException("Ordem oficial inválida",invalid);
        }
        var saved=sprintReviewRepository.save(CommunityTrackSprintReview.builder()
                .incident(incident).action("RECTIFIED")
                .officialName(request.presidentName().trim())
                .officialReference(request.instructionReference().trim())
                .panelReference(overturned.getOfficialReference())
                .reason(request.reason().trim()).recordedBy(username)
                .recordedAt(Instant.now()).remedyKind("RESUME_SAME_RIDERS")
                .orderedRidersJson(orderJson).nextHeatNumber(next).build());
        return sprintReviewView(saved);
    }

    private static boolean validOfficialName(String value) {
        return value!=null&&value.trim().length()>=3&&value.trim().length()<=120;
    }
    private static boolean validOfficialReference(String value) {
        return value!=null&&value.trim().length()>=12&&value.trim().length()<=255;
    }
    private static boolean validOfficialReason(String value) {
        return value!=null&&value.trim().length()>=12&&value.trim().length()<=500;
    }

    private static final Set<String> SPRINT_INCIDENT_TYPES =
            Set.of("FALL","PUNCTURE","BICYCLE_DEFECT","RULE_INFRINGEMENT","OTHER",
                    "PRE_SPRINT_BALANCE","PRE_SPRINT_OPPONENT_CONTACT","PRE_SPRINT_BARRIER_CONTACT");
    private static final Set<String> SPRINT_PRE_SPRINT_TYPES =
            Set.of("PRE_SPRINT_BALANCE","PRE_SPRINT_OPPONENT_CONTACT","PRE_SPRINT_BARRIER_CONTACT");
    private static final Set<String> SPRINT_INCIDENT_DECISIONS =
            Set.of("RESTART_REQUIRED","OFFICIAL_RESULT_TO_RECORD","DISCIPLINARY_RULING",
                    "UNINTENTIONAL_FALL","PRE_SPRINT_RESTART");
    private static final Set<String> SPRINT_DISCIPLINARY_SANCTIONS =
            Set.of("WARNING","RELEGATION","DISQUALIFICATION");
    private static final Set<String> SPRINT_UNRESOLVED_INCIDENT_STATUSES =
            Set.of("PENDING","ADJUDICATION_REQUIRED","RESTART_PENDING","POSITIONS_PENDING","FAULT_START_PENDING");
    private static final Set<String> SPRINT_OPEN_INCIDENT_STATUSES =
            Set.of("PENDING","ADJUDICATION_REQUIRED","RESTART_AUTHORIZED",
                    "RESTART_PENDING","POSITIONS_PENDING","FAULT_START_PENDING");

    private static List<String> decodeRerunRiders(String json) {
        if(json==null || json.isBlank()) return List.of();
        try {
            return SPRINT_TIE_MAPPER.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<
                List<String>>() {});
        } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("A ordem oficial da repetição está ilegível",e);
        }
    }

    /** Only a witnessed, post-sanction rerun order changes a three-up slot. */
    private Map<String,List<String>> sprintOfficialReruns(Long bracketId) {
        Map<String,List<String>> orders = new HashMap<>();
        for (CommunityTrackSprintIncident incident:
                trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId)) {
            if ("RESTART_AUTHORIZED".equals(incident.getStatus())) {
                List<String> riders=decodeRerunRiders(incident.getRerunRidersJson());
                if(orders.putIfAbsent(incident.getSeriesKey(),riders)!=null)
                    throw new IllegalStateException("Repetições oficiais duplicadas na mesma série");
            }
        }
        return Map.copyOf(orders);
    }

    /** Only a second signed decision supplies a referee-awarded *match*
     * outcome; the ordinary round table remains unchanged. */
    private Map<String,String> sprintOfficialAwards(Long bracketId) {
        Map<String,String> awarded=new HashMap<>();
        for (CommunityTrackSprintIncident incident:
                trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId)) {
            boolean single=Set.of("OFFICIAL_HEAT_AWARDED","OFFICIAL_POSITIONS")
                    .contains(incident.getStatus())
                    &&!incident.getSeriesKey().matches("SP[0-9]+-(Q[1-4]|S[1-2]|B|G)");
            boolean series="OFFICIAL_SERIES_AWARDED".equals(incident.getStatus())
                    &&incident.getSeriesKey().matches("SP[0-9]+-(Q[1-4]|S[1-2]|B|G)")
                    &&"DISQUALIFICATION".equals(incident.getSanctionType())
                    &&!Set.of("OVERTURNED","RECTIFIED").contains(
                            latestReviewAction(incident.getId())==null ? ""
                                    : latestReviewAction(incident.getId()));
            if (single||series) {
                if(incident.getAwardWinner()==null
                        ||awarded.putIfAbsent(incident.getSeriesKey(),
                                             incident.getAwardWinner())!=null)
                    throw new IllegalStateException("Atribuição oficial duplicada ou incompleta");
            }
        }
        return Map.copyOf(awarded);
    }

    /** Per-heat awards are not physical rounds and never infer a BO3 match winner. */
    private Map<String,Map<Integer,String>> sprintBo3OfficialHeats(Long bracketId) {
        Map<String,Map<Integer,String>> awardMap=new HashMap<>();
        for(var i:trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId)) {
            if(!Set.of("OFFICIAL_HEAT_AWARDED","OFFICIAL_POSITIONS")
                    .contains(i.getStatus())
                    ||!i.getSeriesKey().matches("SP[0-9]+-(Q[1-4]|S[1-2]|B|G)"))continue;
            if(("OFFICIAL_HEAT_AWARDED".equals(i.getStatus())
                        &&!"RELEGATION".equals(i.getSanctionType()))
                    ||("OFFICIAL_POSITIONS".equals(i.getStatus())
                        &&(i.getSanctionType()!=null
                            ||decodeRerunRiders(i.getOfficialPositionsJson()).size()!=2))
                    ||i.getAwardWinner()==null
                    ||i.getHeatNumber()<1||i.getHeatNumber()>3)
                throw new IllegalStateException("Decisão BO3 sem evidência regulamentar");
            var values=awardMap.computeIfAbsent(i.getSeriesKey(),ignored->new HashMap<>());
            if(values.putIfAbsent(i.getHeatNumber(),i.getAwardWinner())!=null)
                throw new IllegalStateException("Decisão BO3 duplicada na mesma manga");
        }
        Map<String,Map<Integer,String>> stable=new HashMap<>();
        awardMap.forEach((k,v)->stable.put(k,Map.copyOf(v)));
        return Map.copyOf(stable);
    }

    private Set<String> sprintOfficialDisqualified(Long bracketId) {
        Set<String> excluded=new HashSet<>();
        for(var incident:trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId))
            if("OFFICIAL_HEAT_AWARDED".equals(incident.getStatus())
                    &&"DISQUALIFICATION".equals(incident.getSanctionType())) {
                if(incident.getSanctionedUsername()==null)
                    throw new IllegalStateException("Desclassificação sem ciclista identificado");
                excluded.add(incident.getSanctionedUsername());
            }
        return Set.copyOf(excluded);
    }

    private static CommunityTrackSprintIncidentDtos.View sprintIncidentResponse(
            CommunityTrackSprintIncident i) {
        return new CommunityTrackSprintIncidentDtos.View(
                i.getId(),i.getSeriesKey(),i.getHeatNumber(),i.getIncidentType(),
                i.getDetails(),i.getStatus(),i.getReportedBy(),i.getReportedAt(),
                i.getDecisionType(),i.getDecisionReference(),i.getDecisionReason(),
                i.getDecidedBy(),i.getDecidedAt(),
                i.getSanctionType(),i.getSanctionedUsername(),
                decodeRerunRiders(i.getRerunRidersJson()),i.getRerunReference(),
                i.getRerunReason(),i.getRerunAuthorizedBy(),i.getRerunAuthorizedAt(),
                i.getAwardWinner(),i.getAwardReference(),i.getAwardReason(),
                i.getAwardConfirmedBy(),i.getAwardConfirmedAt(),
                decodeRerunRiders(i.getOfficialPositionsJson()),
                i.getFallFaultRider(),i.getFallNonFaultRider(),i.getFallChosenStartPosition(),
                i.getPreSprintIncidentRider(),i.getPreSprintNonFaultRider(),
                i.getPreSprintStartPosition(),i.getPreSprintBeforeStartConfirmed(),
                i.getPreSprintStarterStoppedConfirmed());
    }

    /**
     * Report an incident against the NEXT unrecorded heat. This is a
     * non-sporting, append-only audit entry and never assigns a winner.
     * The season lock plus the PG partial unique index stop simultaneous
     * contradictory unresolved reports.
     */
    public CommunityTrackSprintIncidentDtos.View reportTrackSprintIncident(
            Long communityId,Long seasonId,Long bracketId,String username,
            CommunityTrackSprintIncidentDtos.Report request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive()) throw new IllegalStateException("Época encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não encontrado nesta época"));
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Competição interrompida oficialmente");
        String series=request.seriesKey()==null?"":request.seriesKey().trim();
        if(!SPRINT_INCIDENT_TYPES.contains(request.incidentType()))
            throw new IllegalArgumentException("Tipo de incidente não reconhecido");
        if(request.details()==null || request.details().trim().length()<12 ||
                request.details().trim().length()>500)
            throw new IllegalArgumentException("Descreve o incidente, sem declarar um vencedor");
        if(!series.startsWith("SP"+bracketId+"-") || series.length()>40 ||
                request.heatNumber()<1 || request.heatNumber()>3)
            throw new IllegalArgumentException("Série ou número de manga inválido");
        rejectReviewedSprintProgression(bracketId,series);
        if(trackSprintIncidentRepository.existsByBracketIdAndSeriesKeyAndStatusIn(
                bracketId,series,SPRINT_OPEN_INCIDENT_STATUSES))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este confronto já tem um incidente por resolver");
        AcceptedAliases aliases=acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> related=roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
                .stream().filter(r->"TRACK_SPRINT_HEAT".equals(r.getEventType())
                        ||"TRACK_SPRINT_REPECHAGE".equals(r.getEventType())
                        ||"TRACK_SPRINT_5_8".equals(r.getEventType()))
                .map(r->roundResponse(r,aliases)).toList();
        CommunityTrackSprintBracketDtos.Match match=trackSprintBracketResponse(bracket,related)
                .matches().stream().filter(m->series.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Série fora do quadro"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status()) ||
                "SPRINT_PLACES_5_8".equals(match.phase()))
            throw new IllegalArgumentException("Não é possível reportar esta manga");
        int previousAwards=sprintBo3OfficialHeats(bracketId).getOrDefault(series,Map.of()).size();
        int nextHeat=1+previousAwards+(int)related.stream()
                .filter(r->series.equals(r.sprintSeriesKey())).count();
        if(nextHeat!=request.heatNumber())
            throw new IllegalArgumentException("O incidente tem de referir a próxima manga ainda não registada");
        CommunityTrackSprintIncident incident=trackSprintIncidentRepository.save(
                CommunityTrackSprintIncident.builder().bracket(bracket)
                        .seriesKey(series).heatNumber(nextHeat)
                        .incidentType(request.incidentType())
                        .details(request.details().trim()).status("PENDING")
                        .reportedBy(username).reportedAt(Instant.now()).build());
        return sprintIncidentResponse(incident);
    }

    /**
     * Only a signed, explicitly chosen commissaires' decision unblocks a
     * series. The organizer must still record the actual official heat result
     * with the existing heat editor. No auto-advancement is possible here.
     */
    public CommunityTrackSprintIncidentDtos.View decideTrackSprintIncident(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.Decide request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())throw new IllegalStateException("Época encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não encontrado"));
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Competição interrompida oficialmente");
        CommunityTrackSprintIncident incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        if(!"PENDING".equals(incident.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Decisão já registada");
        if(request==null || !SPRINT_INCIDENT_DECISIONS.contains(request.decisionType())
                ||request.reference()==null||request.reference().trim().length()<12
                ||request.reference().trim().length()>255
                ||request.reason()==null||request.reason().trim().length()<12
                ||request.reason().trim().length()>500)
            throw new IllegalArgumentException("É necessária uma decisão e referência oficial válidas");
        String sanctioned = request.sanctionedUsername() == null ? null
                : request.sanctionedUsername().trim().toLowerCase(Locale.ROOT);
        if(SPRINT_PRE_SPRINT_TYPES.contains(incident.getIncidentType())
                && !"PRE_SPRINT_RESTART".equals(request.decisionType()))
            throw new IllegalArgumentException(
                    "Incidente anterior ao sprint exige decisão regulamentar própria");
        boolean disciplinary = "DISCIPLINARY_RULING".equals(request.decisionType());
        if (disciplinary) {
            if (!Set.of("RULE_INFRINGEMENT","FALL").contains(incident.getIncidentType()))
                throw new IllegalArgumentException(
                    "Uma sanção disciplinar exige infração ou queda registada");
            if (!SPRINT_DISCIPLINARY_SANCTIONS.contains(request.sanctionType())
                    || sanctioned == null || sanctioned.isBlank())
                throw new IllegalArgumentException(
                    "Identifica o ciclista e a sanção do colégio de comissários");
            // Bracket scope and active season already verified above.
            AcceptedAliases aliases = acceptedAliasProfiles(communityId);
            var heats = roundRepository
                    .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
                    .stream().filter(r -> "TRACK_SPRINT_HEAT".equals(r.getEventType())
                            || "TRACK_SPRINT_REPECHAGE".equals(r.getEventType())
                            || "TRACK_SPRINT_5_8".equals(r.getEventType()))
                    .map(r -> roundResponse(r,aliases)).toList();
            CommunityTrackSprintBracketDtos.Match match =
                    trackSprintBracketResponse(bracket,heats).matches().stream()
                    .filter(m -> incident.getSeriesKey().equals(m.seriesKey()))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException(
                            "O confronto já não pertence ao quadro"));
            Set<String> competitors = new HashSet<>();
            for (String rider : List.of(match.riderA(),match.riderB())) {
                if (rider != null) competitors.add(rider.trim().toLowerCase(Locale.ROOT));
            }
            if (match.riderC() != null)
                competitors.add(match.riderC().trim().toLowerCase(Locale.ROOT));
            if (match.riderD() != null)
                competitors.add(match.riderD().trim().toLowerCase(Locale.ROOT));
            if (!competitors.contains(sanctioned))
                throw new IllegalArgumentException(
                    "O ciclista sancionado não participa neste confronto");
            incident.setSanctionType(request.sanctionType());
            incident.setSanctionedUsername(sanctioned);
            // DQ / relegation in multi-rider heats change the starting field.
            // Remain blocked until an explicit, format-aware adjudication flow.
            incident.setStatus("WARNING".equals(request.sanctionType())
                    ? "DECIDED" : "ADJUDICATION_REQUIRED");
        } else {
            if (request.sanctionType() != null || request.sanctionedUsername() != null)
                throw new IllegalArgumentException(
                    "Uma decisão sem sanção não pode identificar um ciclista sancionado");
            if ("UNINTENTIONAL_FALL".equals(request.decisionType())) {
                if (!"FALL".equals(incident.getIncidentType())
                        ||!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
                    throw new IllegalArgumentException(
                            "Falta involuntária exige queda no Sprint olímpico 24");
                incident.setStatus("FAULT_START_PENDING");
            } else if ("PRE_SPRINT_RESTART".equals(request.decisionType())) {
                if(!SPRINT_PRE_SPRINT_TYPES.contains(incident.getIncidentType())
                        ||!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
                    throw new IllegalArgumentException(
                            "UCI 3.2.048(3) exige incidente anterior ao sprint olímpico 24");
                String key=incident.getSeriesKey();
                var related=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                        communityId,seasonId).stream()
                        .filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                                "TRACK_SPRINT_5_8").contains(r.getEventType()))
                        .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
                var match=trackSprintBracketResponse(bracket,related).matches().stream()
                        .filter(m->key.equals(m.seriesKey())).findFirst()
                        .orElseThrow(()->new IllegalArgumentException("Confronto inexistente"));
                if(!key.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|Q[1-4]|S[1-2]|B|G)")
                        ||!Set.of("READY","IN_PROGRESS").contains(match.status())
                        ||match.winner()!=null||match.riderA()==null||match.riderB()==null
                        ||match.riderC()!=null||match.riderD()!=null)
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Repetição pré-sprint de três/quatro ciclistas exige procedimento próprio");
                incident.setStatus("FAULT_START_PENDING");
            } else {
            // In a neutral mishap, a generic declaration is never enough to
            // unlock a new heat or to ratify the race order automatically.
            // Legacy DECIDED incidents remain readable and unchanged.
            if (Set.of("FALL","PUNCTURE","BICYCLE_DEFECT").contains(
                    incident.getIncidentType())) {
                incident.setStatus("RESTART_REQUIRED".equals(request.decisionType())
                        ? "RESTART_PENDING" : "POSITIONS_PENDING");
            } else incident.setStatus("DECIDED");
            }
        }
        incident.setDecisionType(request.decisionType());
        incident.setDecisionReason(request.reason().trim());
        incident.setDecisionReference(request.reference().trim());
        incident.setDecidedBy(username);
        incident.setDecidedAt(Instant.now());
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * UCI Track 3.2.048(1), unintentional rider-caused fall (two-up only):
     * after an explicit ruling, the rider NOT at fault chooses their own
     * starting position for a real restart. Neither sanctions nor a winner
     * are inferred. The previous heat history remains unchanged.
     */
    public CommunityTrackSprintIncidentDtos.View confirmUnintentionalFallStart(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.UnintentionalFallRestart request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat())
                ||bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este procedimento só abrange Sprint olímpico 24 não interrompido");
        var incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente fora do quadro"));
        if(!"FAULT_START_PENDING".equals(incident.getStatus())
                ||!"UNINTENTIONAL_FALL".equals(incident.getDecisionType())
                ||!"FALL".equals(incident.getIncidentType()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não existe uma queda involuntária pendente de escolha de partida");
        if(request==null
                ||!Boolean.TRUE.equals(request.nonFaultChoiceConfirmed())
                ||!Set.of("INSIDE","OUTSIDE").contains(
                        request.nonFaultStartPosition()==null
                            ? "" : request.nonFaultStartPosition())
                ||!validOfficialReference(request.reference())
                ||!validOfficialReason(request.reason())
                ||request.reference().trim().equals(incident.getDecisionReference())
                ||incident.getSanctionType()!=null||incident.getSanctionedUsername()!=null)
            throw new IllegalArgumentException(
                    "São obrigatórias a escolha da vítima e uma segunda ata independente");
        String series=incident.getSeriesKey();
        if(!series.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|Q[1-4]|S[1-2]|B|G)"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Escolha de partida de mais ciclistas exige procedimento específico");
        rejectReviewedSprintProgression(bracketId,series);
        var related=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId).stream()
                .filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                        "TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        var match=trackSprintBracketResponse(bracket,related).matches().stream()
                .filter(m->series.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Confronto não encontrado"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.winner()!=null||match.riderA()==null||match.riderB()==null
                ||match.riderC()!=null||match.riderD()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Confronto com mais de dois ciclistas ou já terminado");
        String faulty=request.faultRider()==null?""
                :request.faultRider().trim().toLowerCase(Locale.ROOT);
        String nonFault=request.nonFaultRider()==null?""
                :request.nonFaultRider().trim().toLowerCase(Locale.ROOT);
        if(faulty.equals(nonFault)
                ||!Set.of(faulty,nonFault).equals(Set.of(match.riderA(),match.riderB())))
            throw new IllegalArgumentException(
                    "A ata tem de identificar o responsável e o ciclista sem culpa deste confronto");
        int count=(int)related.stream().filter(r->series.equals(r.sprintSeriesKey())).count();
        int awards=sprintBo3OfficialHeats(bracketId).getOrDefault(series,Map.of()).size();
        if(incident.getHeatNumber()!=count+awards+1
                ||related.stream().anyMatch(r->series.equals(r.sprintSeriesKey())
                        &&incident.getHeatNumber()==r.sprintHeatNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Manga já disputada ou não é a próxima manga desta série");
        List<String> positions="INSIDE".equals(request.nonFaultStartPosition())
                ?List.of(nonFault,faulty):List.of(faulty,nonFault);
        try {incident.setRerunRidersJson(SPRINT_TIE_MAPPER.writeValueAsString(positions));}
        catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("Ordem de partida inválida",e);
        }
        incident.setFallFaultRider(faulty);
        incident.setFallNonFaultRider(nonFault);
        incident.setFallChosenStartPosition(request.nonFaultStartPosition());
        incident.setRerunReference(request.reference().trim());
        incident.setRerunReason(request.reason().trim());
        incident.setRerunAuthorizedBy(username);
        incident.setRerunAuthorizedAt(Instant.now());
        incident.setStatus("FAULT_START_OK");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * UCI 3.2.048(3): independently document the starter's pre-sprint
     * stoppage and the choice made by the rider not at fault (two-up).
     * No result, sanction or fictitious race is generated.
     */
    public CommunityTrackSprintIncidentDtos.View confirmPreSprintStart(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.PreSprintRestart request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat())
                ||bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este procedimento só abrange Sprint olímpico 24 não interrompido");
        var incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente fora do quadro"));
        if(!"FAULT_START_PENDING".equals(incident.getStatus())
                ||!"PRE_SPRINT_RESTART".equals(incident.getDecisionType())
                ||!SPRINT_PRE_SPRINT_TYPES.contains(incident.getIncidentType()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não existe decisão pré-sprint pendente de escolha de partida");
        if(request==null
                ||!Boolean.TRUE.equals(request.nonFaultChoiceConfirmed())
                ||!Boolean.TRUE.equals(request.beforeSprintConfirmed())
                ||!Boolean.TRUE.equals(request.starterStoppedConfirmed())
                ||!Set.of("INSIDE","OUTSIDE").contains(
                        request.nonFaultStartPosition()==null
                            ? "" : request.nonFaultStartPosition())
                ||!validOfficialReference(request.reference())
                ||!validOfficialReason(request.reason())
                ||request.reference().trim().equals(incident.getDecisionReference())
                ||incident.getSanctionType()!=null||incident.getSanctionedUsername()!=null)
            throw new IllegalArgumentException(
                    "São obrigatórias a escolha da vítima e uma segunda ata independente");
        String series=incident.getSeriesKey();
        if(!series.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|Q[1-4]|S[1-2]|B|G)"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Escolha de partida de mais ciclistas exige procedimento específico");
        rejectReviewedSprintProgression(bracketId,series);
        var related=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId).stream()
                .filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                        "TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        var match=trackSprintBracketResponse(bracket,related).matches().stream()
                .filter(m->series.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Confronto não encontrado"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.winner()!=null||match.riderA()==null||match.riderB()==null
                ||match.riderC()!=null||match.riderD()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Confronto com mais de dois ciclistas ou já terminado");
        String involved=request.incidentRider()==null?""
                :request.incidentRider().trim().toLowerCase(Locale.ROOT);
        String nonFault=request.nonFaultRider()==null?""
                :request.nonFaultRider().trim().toLowerCase(Locale.ROOT);
        if(involved.equals(nonFault)
                ||!Set.of(involved,nonFault).equals(Set.of(match.riderA(),match.riderB())))
            throw new IllegalArgumentException(
                    "A ata tem de identificar o ciclista envolvido e o ciclista sem culpa deste confronto");
        int count=(int)related.stream().filter(r->series.equals(r.sprintSeriesKey())).count();
        int awards=sprintBo3OfficialHeats(bracketId).getOrDefault(series,Map.of()).size();
        if(incident.getHeatNumber()!=count+awards+1
                ||related.stream().anyMatch(r->series.equals(r.sprintSeriesKey())
                        &&incident.getHeatNumber()==r.sprintHeatNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Manga já disputada ou não é a próxima manga desta série");
        List<String> positions="INSIDE".equals(request.nonFaultStartPosition())
                ?List.of(nonFault,involved):List.of(involved,nonFault);
        try {incident.setRerunRidersJson(SPRINT_TIE_MAPPER.writeValueAsString(positions));}
        catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("Ordem de partida inválida",e);
        }
        incident.setPreSprintIncidentRider(involved);
        incident.setPreSprintNonFaultRider(nonFault);
        incident.setPreSprintStartPosition(request.nonFaultStartPosition());
        incident.setPreSprintBeforeStartConfirmed(true);
        incident.setPreSprintStarterStoppedConfirmed(true);
        incident.setRerunReference(request.reference().trim());
        incident.setRerunReason(request.reason().trim());
        incident.setRerunAuthorizedBy(username);
        incident.setRerunAuthorizedAt(Instant.now());
        incident.setStatus("FAULT_START_OK");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * UCI 3.2.048(1-2). The panel may declare the riders' actual positions
     * at the time of a neutral incident final. The organiser transcribes a
     * second, independently referenced panel record; this is NOT a race and
     * no CommunityRound or fictitious finishing time is inserted.
     * All prior incident, decision, and raced-heat history is left intact.
     */
    public CommunityTrackSprintIncidentDtos.View ratifySprintIncidentPositions(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.RatifyIncidentPositions request) {
        requireAdmin(communityId,profile(username));
        var season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat())
                ||bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Homologação apenas no Sprint olímpico 24 não interrompido");
        var incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        if(!"POSITIONS_PENDING".equals(incident.getStatus())
                ||!"OFFICIAL_RESULT_TO_RECORD".equals(incident.getDecisionType())
                ||!Set.of("FALL","PUNCTURE","BICYCLE_DEFECT")
                    .contains(incident.getIncidentType()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só posições decididas pelo colégio e ainda pendentes podem ser homologadas");
        if(request==null||!Boolean.TRUE.equals(request.panelPositionsConfirmed())
                ||request.orderedRiders()==null||request.orderedRiders().size()!=2
                ||!validOfficialReference(request.reference())
                ||!validOfficialReason(request.reason())
                ||request.reference().trim().equals(incident.getDecisionReference())
                ||incident.getSanctionType()!=null||incident.getSanctionedUsername()!=null)
            throw new IllegalArgumentException(
                    "Exige segunda ata distinta, confirmação e as posições oficiais dos dois ciclistas");
        String series=incident.getSeriesKey();
        if(!series.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|Q[1-4]|S[1-2]|B|G)"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Homologação multi-ciclista exige procedimento separado");
        rejectReviewedSprintProgression(bracketId,series);
        String slot=series.substring(series.lastIndexOf('-')+1);
        assertNoReviewedDescendants(communityId,seasonId,bracketId,slot);
        var related=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId).stream()
                .filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                        "TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        var match=trackSprintBracketResponse(bracket,related).matches().stream()
                .filter(m->series.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Confronto não encontrado"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.winner()!=null||match.riderA()==null||match.riderB()==null
                ||match.riderC()!=null||match.riderD()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A homologação exige exatamente dois ciclistas e confronto incompleto");
        List<String> official=new ArrayList<>();
        for(var rider:request.orderedRiders()) {
            if(rider==null||rider.isBlank())
                throw new IllegalArgumentException("Identifica os dois ciclistas");
            official.add(rider.trim().toLowerCase(Locale.ROOT));
        }
        if(official.get(0).equals(official.get(1))
                ||!Set.copyOf(official).equals(Set.of(match.riderA(),match.riderB())))
            throw new IllegalArgumentException(
                    "A ata tem de ordenar apenas os dois ciclistas deste confronto");
        int existing=(int)related.stream().filter(r->series.equals(r.sprintSeriesKey())).count();
        int awarded=sprintBo3OfficialHeats(bracketId).getOrDefault(series,Map.of()).size();
        if(incident.getHeatNumber()!=existing+awarded+1
                ||incident.getHeatNumber()>3
                ||related.stream().anyMatch(r->series.equals(r.sprintSeriesKey())
                        &&incident.getHeatNumber()==r.sprintHeatNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A posição oficial só pode substituir a próxima manga ainda não disputada");
        for(var i:trackSprintIncidentRepository.findByBracketIdOrderByIdAsc(bracketId))
            if(!i.getId().equals(incidentId)&&series.equals(i.getSeriesKey())
                    &&i.getHeatNumber()==incident.getHeatNumber()
                    &&Set.of("OFFICIAL_POSITIONS","OFFICIAL_HEAT_AWARDED",
                            "OFFICIAL_SERIES_AWARDED").contains(i.getStatus()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existe resultado oficial para esta manga");
        try {
            incident.setOfficialPositionsJson(SPRINT_TIE_MAPPER.writeValueAsString(official));
        } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("Não foi possível registar posições oficiais",e);
        }
        incident.setAwardWinner(official.get(0));
        incident.setAwardReference(request.reference().trim());
        incident.setAwardReason(request.reason().trim());
        incident.setAwardConfirmedBy(username);
        incident.setAwardConfirmedAt(Instant.now());
        incident.setStatus("OFFICIAL_POSITIONS");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * UCI 3.2.048(1-2): a no-fault fall, puncture or essential mechanical
     * breakage may be restarted only after an explicit, documentary decision.
     * This narrow procedure retains the original order of exactly two riders
     * and never creates a race result or replaces an already raced heat.
     */
    public CommunityTrackSprintIncidentDtos.View authorizeNeutralSprintRestart(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.NeutralRestart request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        var bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Reinício documentado limitado ao Sprint olímpico 24");
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Evento definitivamente interrompido");
        var incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        if(!"RESTART_PENDING".equals(incident.getStatus())
                ||!"RESTART_REQUIRED".equals(incident.getDecisionType()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não existe decisão de repetição pendente para este incidente");
        String cause=switch(incident.getIncidentType()) {
            case "FALL" -> "NO_FAULT_FALL";
            case "PUNCTURE" -> "PUNCTURE";
            case "BICYCLE_DEFECT" -> "ESSENTIAL_BICYCLE_BREAKAGE";
            default -> "";
        };
        if(request==null||!cause.equals(request.cause())
                ||!Boolean.TRUE.equals(request.noFaultConfirmed())
                ||request.orderedRiders()==null||request.orderedRiders().size()!=2
                ||!validOfficialReference(request.reference())
                ||!validOfficialReason(request.reason()))
            throw new IllegalArgumentException(
                    "Identifica a causa sem culpa, os ciclistas e a segunda ata do colégio");
        String series=incident.getSeriesKey();
        if(!series.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|Q[1-4]|S[1-2]|B|G)"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Repetição com mais participantes exige procedimento específico");
        rejectReviewedSprintProgression(bracketId,series);
        var related=roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                communityId,seasonId).stream()
                .filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                        "TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        var match=trackSprintBracketResponse(bracket,related).matches().stream()
                .filter(m->series.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Confronto não encontrado"));
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.riderA()==null||match.riderB()==null
                ||match.riderC()!=null||match.riderD()!=null
                ||match.winner()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só uma manga incompleta com exatamente dois ciclistas pode recomeçar");
        List<String> expected=List.of(match.riderA(),match.riderB());
        List<String> actual=new ArrayList<>();
        for(String rider:request.orderedRiders()) {
            if(rider==null||rider.isBlank())
                throw new IllegalArgumentException("Identifica ambos os ciclistas");
            actual.add(rider.trim().toLowerCase(Locale.ROOT));
        }
        if(!actual.equals(expected))
            throw new IllegalArgumentException(
                    "Repetição neutra obriga aos dois ciclistas pela ordem original");
        int previous= (int)related.stream().filter(r->series.equals(r.sprintSeriesKey())).count();
        int awards=sprintBo3OfficialHeats(bracketId).getOrDefault(series,Map.of()).size();
        if(incident.getHeatNumber()!=previous+awards+1
                ||related.stream().anyMatch(r->series.equals(r.sprintSeriesKey())
                        &&incident.getHeatNumber()==r.sprintHeatNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Manga já disputada, adjudicada ou fora de sequência");
        try {incident.setRerunRidersJson(SPRINT_TIE_MAPPER.writeValueAsString(expected));}
        catch(com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Não foi possível conservar a ordem oficial",ex);
        }
        incident.setRerunReference(request.reference().trim());
        incident.setRerunReason(request.reason().trim());
        incident.setRerunAuthorizedBy(username);
        incident.setRerunAuthorizedAt(Instant.now());
        incident.setStatus("NEUTRAL_RESTART_OK");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * Article 3.2.047 / 3.2.048: in a three-up repechage, a specifically
     * sanctioned rider may be removed from a rerun ONLY after a separate,
     * witnessed authorisation. This does not create a sporting result.
     * Unsupported two-up judgements remain blocked.
     */
    public CommunityTrackSprintIncidentDtos.View authorizeTrackSprintRerun(
            Long communityId, Long seasonId, Long bracketId, Long incidentId,
            String username, CommunityTrackSprintIncidentDtos.AuthorizeRerun request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Não podes rever uma época encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new IllegalArgumentException("Repetição reduzida exclusiva de repescagem olímpica");
        CommunityTrackSprintIncident incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(x->x.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        if(!"ADJUDICATION_REQUIRED".equals(incident.getStatus())
                ||!"DISCIPLINARY_RULING".equals(incident.getDecisionType())
                ||!Set.of("RELEGATION","DISQUALIFICATION").contains(incident.getSanctionType()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só decisões disciplinares pendentes admitem esta revisão");
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Competição interrompida oficialmente");
        String series=incident.getSeriesKey();
        if(!series.matches("SP"+bracketId+"-(U[1-4]|W[1-2])")
                || incident.getHeatNumber()!=1)
            throw new IllegalArgumentException("A revisão de composição exige repescagem de três ciclistas");
        if(request==null || request.orderedRiders()==null || request.orderedRiders().size()!=2
                ||request.reference()==null||request.reference().trim().length()<12
                ||request.reference().trim().length()>255
                ||request.reason()==null||request.reason().trim().length()<12
                ||request.reason().trim().length()>500)
            throw new IllegalArgumentException("Confirma os dois ciclistas e a nova ata oficial");
        List<String> order=new ArrayList<>();
        for(String rider:request.orderedRiders()) {
            if(rider==null||rider.isBlank())
                throw new IllegalArgumentException("Identifica os ciclistas autorizados");
            order.add(rider.trim().toLowerCase(Locale.ROOT));
        }
        if(order.size()!=2||order.get(0).equals(order.get(1)))
            throw new IllegalArgumentException("A repetição exige dois ciclistas distintos");
        List<CommunityRoundResponse> heats=roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
                .stream().filter(r->"TRACK_SPRINT_HEAT".equals(r.getEventType())
                        ||"TRACK_SPRINT_REPECHAGE".equals(r.getEventType())
                        ||"TRACK_SPRINT_5_8".equals(r.getEventType()))
                .map(r->roundResponse(r,acceptedAliasProfiles(communityId))).toList();
        if(heats.stream().anyMatch(r->series.equals(r.sprintSeriesKey())))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não podes rever uma manga que já tem resultados");
        CommunityTrackSprintBracketDtos.Match match=CommunityTrackSprintOlympic24
                .project(bracketId,heats,bracket.getQualifiedRiders(),sprintOfficialReruns(bracketId))
                .stream().filter(x->series.equals(x.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Repescagem não encontrada"));
        if(!"READY".equals(match.status()) || match.riderC()==null
                ||match.riderD()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A repescagem não está pronta para repetição de dois ciclistas");
        Set<String> original=Set.of(match.riderA(),match.riderB(),match.riderC());
        Set<String> remaining=new HashSet<>(original);
        if(!remaining.remove(incident.getSanctionedUsername())
                ||remaining.size()!=2||!remaining.equals(new HashSet<>(order)))
            throw new IllegalArgumentException(
                    "A nova partida tem de conter exatamente os dois ciclistas não sancionados");
        // Immutable original decision is preserved; this records separate
        // authorisation and restart order. No WIN/LOSS is assigned here.
        try { incident.setRerunRidersJson(SPRINT_TIE_MAPPER.writeValueAsString(order)); }
        catch(com.fasterxml.jackson.core.JsonProcessingException error) {
            throw new IllegalStateException("Não foi possível guardar a ordem oficial",error);
        }
        incident.setRerunReference(request.reference().trim());
        incident.setRerunReason(request.reason().trim());
        incident.setRerunAuthorizedBy(username);
        incident.setRerunAuthorizedAt(Instant.now());
        incident.setStatus("RESTART_AUTHORIZED");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    /**
     * UCI 3.2.048(1): after an expressly documented intentional fall in a
     * two-rider single-heat race, the commissaires may declare the other rider
     * winner. This is a separately witnessed award, NOT a raced/virtual round.
     * BO3 series (Q/S/B/G) remain blocked pending heat-specific accounting.
     */
    public CommunityTrackSprintIncidentDtos.View confirmTwoUpOfficialAward(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username, CommunityTrackSprintIncidentDtos.ConfirmOfficialAward request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Não é possível decidir resultados numa época encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Competição definitivamente interrompida");
        if(!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new IllegalArgumentException("Atribuição oficial só está implementada no quadro olímpico");
        CommunityTrackSprintIncident incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        if(!"ADJUDICATION_REQUIRED".equals(incident.getStatus())
                ||!"DISCIPLINARY_RULING".equals(incident.getDecisionType())
                ||!"FALL".equals(incident.getIncidentType())
                ||!Set.of("RELEGATION","DISQUALIFICATION").contains(incident.getSanctionType())
                )
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta decisão disciplinar não permite atribuição direta do confronto");
        String key=incident.getSeriesKey();
        boolean single=incident.getHeatNumber()==1
                &&key.matches("SP"+bracketId+"-(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|V[1-4])");
        boolean bo3=key.matches("SP"+bracketId+"-(Q[1-4]|S[1-2]|B|G)")
                &&"RELEGATION".equals(incident.getSanctionType());
        if(!single&&!bo3)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "DQ BO3 e repescagens exigem procedimento especial; não existe vitória automática");
        if(request==null || !"INTENTIONAL_FALL".equals(request.basis())
                ||request.winnerUsername()==null||request.winnerUsername().isBlank()
                ||request.reference()==null||request.reference().trim().length()<12
                ||request.reference().trim().length()>255
                ||request.reason()==null||request.reason().trim().length()<12
                ||request.reason().trim().length()>500)
            throw new IllegalArgumentException("Confirma queda intencional, vencedor, ata e fundamentação");
        AcceptedAliases aliases=acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds=roundRepository
            .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
            .stream().filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                    "TRACK_SPRINT_5_8").contains(r.getEventType()))
            .map(r->roundResponse(r,aliases)).toList();
        long raced=rounds.stream().filter(r->key.equals(r.sprintSeriesKey())).count();
        if((single && raced>0)||rounds.stream().anyMatch(r->key.equals(r.sprintSeriesKey())
                &&Integer.valueOf(incident.getHeatNumber()).equals(r.sprintHeatNumber())))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Número da manga já registado como corrida disputada");
        if(bo3) {
            var earlier=sprintBo3OfficialHeats(bracketId).getOrDefault(key,Map.of());
            if(incident.getHeatNumber()!=raced+earlier.size()+1
                    ||earlier.containsKey(incident.getHeatNumber()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Decisão fora de sequência ou já registada");
        }
        CommunityTrackSprintBracketDtos.Match match=trackSprintBracketResponse(bracket,rounds)
                .matches().stream().filter(m->key.equals(m.seriesKey()))
                .findFirst().orElseThrow(()->new IllegalArgumentException("Confronto inválido"));
        if(!(bo3?Set.of("READY","IN_PROGRESS").contains(match.status())
                :"READY".equals(match.status())) ||match.riderC()!=null||match.riderD()!=null
                ||match.riderA()==null||match.riderB()==null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "É necessária uma eliminatória preparada entre exatamente dois ciclistas");
        String winner=request.winnerUsername().trim().toLowerCase(Locale.ROOT);
        String sanctioned=incident.getSanctionedUsername();
        if(sanctioned==null || !Set.of(match.riderA(),match.riderB()).contains(sanctioned)
                || winner.equals(sanctioned)
                || !Set.of(match.riderA(),match.riderB()).contains(winner))
            throw new IllegalArgumentException(
                    "O vencedor declarado tem de ser o adversário não sancionado");
        incident.setAwardWinner(winner);
        incident.setAwardReference(request.reference().trim());
        incident.setAwardReason(request.reason().trim());
        incident.setAwardConfirmedBy(username);
        incident.setAwardConfirmedAt(Instant.now());
        incident.setStatus("OFFICIAL_HEAT_AWARDED");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }


    /** A second documented UCI 3.2.048(1) decision closes the BO3 series
     * after competition disqualification, without fictitious heat victories. */
    public CommunityTrackSprintIncidentDtos.View confirmBo3DisqualificationVerdict(
            Long communityId,Long seasonId,Long bracketId,Long incidentId,
            String username,CommunityTrackSprintIncidentDtos.ConfirmOfficialAward request) {
        requireAdmin(communityId,profile(username));
        CommunitySeason season=seasonRepository.lockByIdAndCommunityId(seasonId,communityId)
                .orElseThrow(()->new IllegalArgumentException("Época não encontrada"));
        if(!season.isActive())throw new ResponseStatusException(HttpStatus.CONFLICT,"Época encerrada");
        CommunityTrackSprintBracket bracket=trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId,communityId,seasonId)
                .orElseThrow(()->new IllegalArgumentException("Quadro não pertence à época"));
        if(bracket.getInterruptionStage()!=null
                ||!"OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(bracket.getFormat()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Quadro interrompido ou formato incompatível");
        CommunityTrackSprintIncident incident=trackSprintIncidentRepository.findById(incidentId)
                .filter(i->i.getBracket().getId().equals(bracketId))
                .orElseThrow(()->new IllegalArgumentException("Incidente não pertence ao quadro"));
        String key=incident.getSeriesKey();
        rejectReviewedSprintProgression(bracketId,key);
        if(!"ADJUDICATION_REQUIRED".equals(incident.getStatus())
                ||!"DISCIPLINARY_RULING".equals(incident.getDecisionType())
                ||!"FALL".equals(incident.getIncidentType())
                ||!"DISQUALIFICATION".equals(incident.getSanctionType())
                ||!key.matches("SP"+bracketId+"-(Q[1-4]|S[1-2]|B|G)"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Veredito BO3 não suportado para este incidente");
        if(request==null||!"INTENTIONAL_FALL".equals(request.basis())
                ||request.winnerUsername()==null||request.winnerUsername().isBlank()
                ||request.reference()==null||request.reference().trim().length()<12
                ||request.reference().trim().length()>255
                ||request.reason()==null||request.reason().trim().length()<12
                ||request.reason().trim().length()>500)
            throw new IllegalArgumentException("Confirma vencedor, ata e fundamentação da decisão oficial");
        AcceptedAliases aliases=acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds=roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId,seasonId)
                .stream().filter(r->Set.of("TRACK_SPRINT_HEAT","TRACK_SPRINT_REPECHAGE",
                        "TRACK_SPRINT_5_8").contains(r.getEventType()))
                .map(r->roundResponse(r,aliases)).toList();
        var match=trackSprintBracketResponse(bracket,rounds).matches().stream()
                .filter(m->key.equals(m.seriesKey())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Série desconhecida"));
        String winner=request.winnerUsername().trim().toLowerCase(Locale.ROOT);
        String sanctioned=incident.getSanctionedUsername();
        if(!Set.of("READY","IN_PROGRESS").contains(match.status())
                ||match.riderA()==null||match.riderB()==null||match.riderC()!=null
                ||match.riderD()!=null||sanctioned==null
                ||!Set.of(match.riderA(),match.riderB()).contains(sanctioned)
                ||!Set.of(match.riderA(),match.riderB()).contains(winner)
                ||winner.equals(sanctioned))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Veredito incompatível com a série e sanção");
        String slot=key.substring(key.lastIndexOf('-')+1);
        for(String next:CommunityTrackSprintOlympic24.nextSlots(slot))
            if(!roundRepository.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                    communityId,seasonId,"SP"+bracketId+"-"+next).isEmpty())
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existem resultados seguintes");
        incident.setAwardWinner(winner);
        incident.setAwardReference(request.reference().trim());
        incident.setAwardReason(request.reason().trim());
        incident.setAwardConfirmedBy(username);
        incident.setAwardConfirmedAt(Instant.now());
        incident.setStatus("OFFICIAL_SERIES_AWARDED");
        return sprintIncidentResponse(trackSprintIncidentRepository.save(incident));
    }

    private static final java.util.regex.Pattern SPRINT_BRACKET_SERIES =
            java.util.regex.Pattern.compile("^SP([1-9][0-9]*)-(A(?:[1-9]|1[0-2])|U[1-4]|C[1-8]|V[1-4]|E[1-6]|W[1-2]|F58|P(?:[5-9]|1[0-6])|R[1-8]|Q[1-4]|S[1-2]|B|G)$");

    /** Guards organizer-created heats against the source knockout series. */
    private void validateBracketRound(Long communityId, CommunitySeason season,
            String seriesKey, String phase, String eventType,
            List<CommunityRoundEntryRequest> entrants) {
        if (seriesKey == null) return;
        var matcher = SPRINT_BRACKET_SERIES.matcher(seriesKey);
        if (!matcher.matches()) return; // independent/saved pre-bracket series remain untouched
        if (season == null) throw new IllegalArgumentException("Quadro exige época ativa");
        long bracketId;
        try { bracketId = Long.parseLong(matcher.group(1)); }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Identificador do quadro inválido");
        }
        CommunityTrackSprintBracket bracket = trackSprintBracketRepository
                .findByIdAndCommunityIdAndSeasonId(bracketId, communityId, season.getId())
                .orElseThrow(() -> new IllegalArgumentException("Quadro Sprint não pertence à época"));
        if(bracket.getInterruptionStage()!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Competição interrompida oficialmente");
        // A reported incident never becomes an official result by itself.
        rejectReviewedSprintProgression(bracketId,seriesKey.trim());
        if (trackSprintIncidentRepository.existsByBracketIdAndSeriesKeyAndStatusIn(
                bracketId, seriesKey.trim(), SPRINT_UNRESOLVED_INCIDENT_STATUSES))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Manga suspensa até ser registada a decisão oficial dos comissários");
        AcceptedAliases aliases = acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds = roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId, season.getId())
                 .stream().filter(r -> "TRACK_SPRINT_HEAT".equals(r.getEventType())
                        || "TRACK_SPRINT_REPECHAGE".equals(r.getEventType())
                        || "TRACK_SPRINT_5_8".equals(r.getEventType()))
                .map(r -> roundResponse(r, aliases)).toList();
        List<String> seeds = bracket.getQualifiedRiders() == null
                ? List.of() : bracket.getQualifiedRiders();
        if (seeds.size()==24) {
            try {
                CommunityTrackSprintOlympic24.validateNewHeat(
                        bracketId, seriesKey, phase, eventType, rounds, entrants, seeds,
                        sprintOfficialReruns(bracketId),sprintOfficialAwards(bracketId),sprintOfficialDisqualified(bracketId),sprintBo3OfficialHeats(bracketId));
            } catch (IllegalArgumentException invalidHeat) {
                // A finished or suspended bracket is a user validation error,
                // not an unhandled 500. Race entries remain unchanged.
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidHeat.getMessage(), invalidHeat);
            }
        } else {
            if (!"TRACK_SPRINT_HEAT".equals(eventType))
                throw new IllegalArgumentException("Repescagem não pertence a este formato");
            CommunityTrackSprintBracketPlanner.validateNewHeat(
                    bracketId, seriesKey, phase, rounds, entrants, seeds);
        }
    }

    /** Protect all child results when a parent quarter/semifinal is deleted. */
    private void preventBracketAncestorRemoval(Long communityId, CommunityRound round) {
        if (round.getSeason() == null || round.getSprintSeriesKey() == null) return;
        var match = SPRINT_BRACKET_SERIES.matcher(round.getSprintSeriesKey());
        if (!match.matches()) return;
        String slot = match.group(2);
        if (CommunityTrackSprintOlympic24.isOlympicSlot(slot)
                || trackSprintBracketRepository
                    .findByIdAndCommunityIdAndSeasonId(Long.parseLong(match.group(1)),
                            communityId, round.getSeason().getId())
                    .map(b -> "OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(b.getFormat()))
                    .orElse(false)) {
            for (String dest: CommunityTrackSprintOlympic24.nextSlots(slot)) {
                String downstream = "SP" + match.group(1) + "-" + dest;
                if (!roundRepository.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                        communityId, round.getSeason().getId(), downstream).isEmpty())
                    throw new IllegalStateException("Existem eliminatórias seguintes; não podes apagar esta manga");
            }
            return;
        }
        List<String> next = switch (slot) {
            case "P16" -> List.of("R1");
            case "P15" -> List.of("R2");
            case "P14" -> List.of("R3");
            case "P13" -> List.of("R4");
            case "P5", "P12" -> List.of("R5");
            case "P6", "P11" -> List.of("R6");
            case "P7", "P10" -> List.of("R7");
            case "P8", "P9" -> List.of("R8");
            case "R1", "R8" -> List.of("Q1");
            case "R2", "R7" -> List.of("Q2");
            case "R3", "R6" -> List.of("Q3");
            case "R4", "R5" -> List.of("Q4");
            case "Q1", "Q4" -> List.of("S1");
            case "Q2", "Q3" -> List.of("S2");
            case "S1", "S2" -> List.of("B", "G");
            default -> List.of();
        };
        for (String dest : next) {
            String downstream = "SP" + match.group(1) + "-" + dest;
            if (!roundRepository.findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                    communityId, round.getSeason().getId(), downstream).isEmpty()) {
                throw new IllegalStateException(
                        "Existem confrontos seguintes registados; não podes apagar esta manga");
            }
        }
    }

    /**
     * Set one sport+format scoring policy for one ACTIVE season. Changes to a
     * closed season are rejected to preserve published historical standings.
     * Existing W/D/L, round results and other sport formats are unchanged.
     */
    /**
     * Points are explicitly defined per active season and race discipline.
     * No configuration => no championship points. Read-only old seasons.
     */
    public CommunityRaceRuleResponse updateRaceRule(Long communityId, Long seasonId,
            String username, UpdateCommunityRaceRuleRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunitySeason season = seasonRepository.lockByIdAndCommunityId(seasonId, communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Época não encontrada nesta comunidade"));
        if (!season.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Não podes alterar regras de uma época encerrada");
        Community community = season.getCommunity();
        if (!community.isRankingsEnabled() || !CommunityRoundEventType.supportsRaceChampionship(request.sport()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A modalidade não suporta classificação de corrida");
        String sport;
        try {
            sport = CommunityRoundSport.resolve(community, request.sport());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        List<Integer> points;
        try {
            points = CommunityRaceStandings.parsePoints(request.pointsByPlace());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        CommunityRaceRule rule = raceRuleRepository.findBySeasonIdAndSportIgnoreCase(seasonId, sport)
                .orElseGet(() -> CommunityRaceRule.builder().season(season).sport(sport).build());
        rule.setPointsByPlace(points.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",")));
        raceRuleRepository.save(rule);
        return new CommunityRaceRuleResponse(sport, points);
    }

    /**
     * Only race results in this community and its active season can be penalized.
     * Recorded finishing position/timing is never overwritten. Reason required
     * for positive deductions. Penalties apply per race, never to football WDL.
     */
    public CommunityRoundEntryResponse updateRacePenalty(Long communityId, Long roundId,
            Long entryId, String username, UpdateCommunityRacePenaltyRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunityRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prova não encontrada"));
        if (!round.getCommunity().getId().equals(communityId) ||
                !CommunityRoundEventType.isRaceEvent(round.getSport(), round.getEventType()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prova não encontrada");
        if (round.getSeason() == null || !round.getSeason().isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só podes aplicar penalizações a provas de épocas ativas");
        if (request.penaltyPoints() > 0 && (request.reason() == null || request.reason().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indica o motivo da penalização");
        protectSprintQualification(communityId, roundId);
        CommunityRoundEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Participante não encontrado"));
        if (!entry.getRound().getId().equals(roundId) || entry.getFinishPosition() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Só participantes classificados nesta prova podem ser penalizados");
        entry.setRacePenaltyPoints(request.penaltyPoints());
        entry.setRacePenaltyReason(request.penaltyPoints() == 0
                ? null : request.reason().trim());
        entryRepository.save(entry);
        return roundResponse(round, acceptedAliasProfiles(communityId)).entries().stream()
                .filter(row -> entryId.equals(row.entryId()))
                .findFirst().orElseThrow();
    }

    /**
     * Steward decisions never mutate the original finish place or measured time.
     * Existing point penalties are independent; DSQ suppresses sporting points
     * and podiums in the derived championship table.
     */
    public CommunityRoundEntryResponse updateRaceDecision(Long communityId, Long roundId,
            Long entryId, String username, UpdateCommunityRaceDecisionRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunityRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prova não encontrada"));
        if (!round.getCommunity().getId().equals(communityId)
                || !CommunityRoundEventType.isRaceEvent(round.getSport(), round.getEventType())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prova não encontrada");
        }
        if (round.getSeason() == null || !round.getSeason().isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não podes alterar decisões de épocas encerradas");
        }
        protectSprintQualification(communityId, roundId);
        CommunityRoundEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Participante não encontrado"));
        if (!entry.getRound().getId().equals(roundId) || entry.getFinishPosition() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A decisão exige um participante classificado nesta prova");
        }
        java.math.BigDecimal timePenalty;
        try {
            timePenalty = CommunityRaceDecisions.validate(request, entry.getElapsedSeconds());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        entry.setRaceTimePenaltySeconds(timePenalty);
        entry.setRaceDisqualified(request.disqualified());
        entry.setRaceStewardReason(request.disqualified() || timePenalty.signum() > 0
                ? request.reason().trim() : null);
        entryRepository.saveAndFlush(entry);
        return roundResponse(round, acceptedAliasProfiles(communityId)).entries().stream()
                .filter(row -> entryId.equals(row.entryId()))
                .findFirst().orElseThrow();
    }

    public CommunityRacketRuleResponse updateRacketRule(
            Long communityId, Long seasonId, String username,
            UpdateCommunityRacketRuleRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunitySeason season = seasonRepository.lockByIdAndCommunityId(seasonId, communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Época não encontrada nesta comunidade"));
        if (!season.isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "As regras de uma época encerrada não podem ser modificadas");
        }
        Community community = season.getCommunity();
        if (!community.isRankingsEnabled() ||
                !CommunityRacketResults.isSupported(request.sport())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Esta comunidade ou modalidade não suporta classificações de raquete");
        }
        String sport;
        try {
            sport = CommunityRoundSport.resolve(community, request.sport());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        if (request.winPoints() <= request.lossPoints()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Os pontos por vitória devem ser superiores aos pontos por derrota");
        }
        CommunityRacketRule rule = racketRuleRepository
                .findBySeasonIdAndSportIgnoreCaseAndMatchFormat(
                        seasonId, sport, request.matchFormat())
                .orElseGet(() -> CommunityRacketRule.builder()
                        .season(season).sport(sport).matchFormat(request.matchFormat()).build());
        rule.setWinPoints(request.winPoints());
        rule.setLossPoints(request.lossPoints());
        rule.setTieBreak(request.tieBreak());
        return racketRuleResponse(racketRuleRepository.save(rule));
    }

    private CommunityRacketRuleResponse racketRuleResponse(CommunityRacketRule rule) {
        return new CommunityRacketRuleResponse(rule.getSport(), rule.getMatchFormat(),
                rule.getWinPoints(), rule.getLossPoints(), rule.getTieBreak());
    }

    public CommunitySeasonResponse createSeason(
            Long communityId,
            String username,
            CreateCommunitySeasonRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        seasonRepository.findByCommunityIdAndActiveTrue(communityId)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Encerra a época ativa antes de criares outra"
                    );
                });

        CommunitySeason season = seasonRepository.save(CommunitySeason.builder()
                .community(community)
                .name(request.name().trim())
                .startsAt(request.startsAt() == null ? Instant.now() : request.startsAt())
                .active(true)
                .createdAt(Instant.now())
                .build());

        return seasonResponse(community, season);
    }

    public CommunitySeasonResponse renameSeason(
            Long communityId, Long seasonId, String username, UpdateCommunitySeasonRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);
        CommunitySeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
        if (!season.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("A época não pertence a esta comunidade");
        }
        season.setName(request.name().trim());
        return seasonResponse(community, seasonRepository.save(season));
    }

    /**
     * Permanently removes one season and ONLY its own dependent competition data.
     * Keeps all other seasons, unassigned rounds, community members, shared player
     * identities and match archives with no season association untouched.
     * Entire operation is atomic via the service's @Transactional boundary.
     */
    public void deleteSeason(Long communityId, Long seasonId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        // Season imports use the same row lock: do not race an import/season deletion.
        CommunitySeason season = seasonRepository.lockByIdAndCommunityId(seasonId, communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Época não encontrada nesta comunidade"));

        // Capture imported identity IDs before removing their only source entries.
        // These identities are community-scoped, so the regular season cascade
        // cannot decide which of them are now safe to remove.
        List<Long> historicalPlayerIds =
                entryRepository.findHistoricalPlayerIdsBySeason(communityId, seasonId);

        // Bracket references its flying-200m source; remove the dependent
        // record and its ordered seeds before season rounds or their FK.
        // An explicitly deleted season is the ONLY deletion path for incident
        // history. Normal bracket deletion always protects its audit record.
        for (CommunityTrackSprintBracket b: trackSprintBracketRepository
                .findByCommunityIdAndSeasonIdOrderByIdAsc(communityId, seasonId))
            trackSprintIncidentRepository.deleteByBracketId(b.getId());
        trackSprintIncidentRepository.flush();
        trackSprintBracketRepository.deleteBySeasonId(seasonId);
        trackSprintBracketRepository.flush();
        List<CommunityRound> rounds =
                roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId, seasonId);
        for (CommunityRound round : rounds) {
            entryRepository.deleteByRoundId(round.getId());
        }
        entryRepository.flush(); // Delete children before their rounds (FK).
        roundRepository.deleteAll(rounds);
        roundRepository.flush();

        List<CommunityStandingsArchive> archives =
                standingsArchiveRepository.findByCommunityIdAndSeasonId(communityId, seasonId);
        for (CommunityStandingsArchive archive : archives) {
            standingsArchiveRowRepository.deleteByArchiveId(archive.getId());
        }
        standingsArchiveRowRepository.flush();
        standingsArchiveRepository.deleteAll(archives);
        standingsArchiveRepository.flush();

        seasonRepository.delete(season);
        seasonRepository.flush();

        // Purge historical names and self-claims only when every linked round is gone.
        // Do not delete registered community players or names shared with other seasons.
        if (!historicalPlayerIds.isEmpty()) {
            List<CommunityPlayer> orphans = playerRepository.findUnusedHistoricalPlayers(
                    communityId, historicalPlayerIds);
            for (CommunityPlayer orphan : orphans) {
                historicalPlayerLinkRepository.findByCommunityIdAndPlayerId(communityId, orphan.getId())
                        .ifPresent(historicalPlayerLinkRepository::delete);
            }
            historicalPlayerLinkRepository.flush();
            playerRepository.deleteAll(orphans);
            playerRepository.flush();
        }
    }

    public CommunitySeasonResponse closeSeason(
            Long communityId,
            Long seasonId,
            String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        CommunitySeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
        if (!season.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("A época não pertence a esta comunidade");
        }
        if (!season.isActive()) {
            throw new IllegalStateException("Esta época já está encerrada");
        }

        // Freeze the trophy at closure: future scoring-rule changes must not change its winner.
        List<CommunityStandingResponse> finalStandings = standings(community, seasonId);
        CommunityStandingResponse champion = !finalStandings.isEmpty()
                && finalStandings.get(0).games() > 0
                ? finalStandings.get(0) : null;
        season.setChampionUsername(champion == null ? null : champion.username());
        season.setChampionDisplayName(champion == null ? null : champion.fullName());
        season.setChampionPoints(champion == null ? null : champion.points());
        season.setChampionFinalized(true);
        season.setActive(false);
        season.setEndedAt(Instant.now());
        CommunitySeason saved = seasonRepository.save(season);
        return seasonResponse(community, saved);
    }

    public CommunityCompetitionSettingsResponse updateSettings(
            Long communityId,
            String username,
            UpdateCommunityCompetitionSettingsRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        if (request.rankingsEnabled() != null) {
            community.setRankingsEnabled(request.rankingsEnabled());
        }
        if (request.winPoints() != null) {
            community.setWinPoints(request.winPoints());
        }
        if (request.drawPoints() != null) {
            community.setDrawPoints(request.drawPoints());
        }
        if (request.lossPoints() != null) {
            community.setLossPoints(request.lossPoints());
        }

        return settings(communityRepository.save(community));
    }

    /**
     * World Skate sprint heats can be recorded for an individual competition without
     * enabling the unrelated community W/D/L table. Only the catalogue's 2026
     * track 500m +D and road One Lap bracket stages qualify; legacy Final B and
     * standalone 200m time trial do not acquire this exception.
     */
    static boolean isStandingsIndependentSpeedHeat(String sport, String eventType) {
        return "Patinagem de Velocidade".equals(sport)
                && CommunityInlineSpeed2026.stage(eventType) != null;
    }

    public CommunityRoundResponse createRound(
            Long communityId,
            String username,
            CreateCommunityRoundRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        // A tournament may use a different catalog sport from the community profile.
        // Allow that difference ONLY for existing, non-historical individual skating
        // tournaments when generic standings are disabled.
        boolean registeredSpeedTournament = !community.isRankingsEnabled()
                && request.sport() != null
                && "Patinagem de Velocidade".equalsIgnoreCase(request.sport().trim())
                && tournamentRepository
                        .existsByCommunityIdAndSportIgnoreCaseAndEntryTypeAndHistoricalArchiveIdIsNullAndStatusNot(
                                communityId, "Patinagem de Velocidade",
                                TournamentEntryType.INDIVIDUAL, TournamentStatus.COMPLETED);
        String resolvedSport = registeredSpeedTournament
                ? "Patinagem de Velocidade"
                : CommunityRoundSport.resolve(community, request.sport());
        String resolvedEventType = CommunityRoundEventType.validate(resolvedSport, request.eventType());
        // Competition heat results are independent of the optional W/D/L standings.
        // Keep all other rounds disabled when the community rankings module is off.
        if (!community.isRankingsEnabled()
                && !isStandingsIndependentSpeedHeat(resolvedSport, resolvedEventType)) {
            throw new IllegalStateException("Ativa primeiro o módulo de classificação desta comunidade");
        }
        String resolvedDiscipline = CommunityFieldDisciplines.validate(
                resolvedSport, resolvedEventType, request.eventDiscipline());
        CommunityRaceMetrics.validate(resolvedSport, resolvedEventType, request.entries());
        CommunityBasketballScore basketballScore = CommunityBasketballResults.validate(
                resolvedSport, resolvedEventType, request.entries(), request.basketballScore());
        CommunityTeamFinalScore teamScore = CommunityTeamFinalResults.validate(
                resolvedSport, resolvedEventType, request.entries(), request.teamScore());
        CommunityCricketDetails cricketDetails = CommunityCricketResults.validate(
                resolvedSport, resolvedEventType, teamScore, request.cricketDetails());
        String groupScoresJson = CommunityGroupRoutineResults.validateAndEncode(
                resolvedSport, resolvedEventType, request.entries(), request.groupScores());
        CommunityRoundEventType.validateEntries(resolvedSport, resolvedEventType, request.entries());
        // Volleyball, beach volleyball and footvolley reuse compact per-set storage,
        // but each has its own scoring format; never use racket singles/doubles rules.
        boolean volleyballEvent = CommunityVolleyballResults.isVolleyballEvent(resolvedEventType);
        String encodedSetScores = volleyballEvent
                ? CommunityVolleyballResults.validateAndEncode(
                        resolvedSport, resolvedEventType, request.matchFormat(),
                        request.setScores(), request.entries())
                : CommunityRacketResults.validateAndEncode(
                        resolvedSport, request.matchFormat(), request.setScores(), request.entries());

        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest entry : request.entries()) {
            String normalized = entry.username().trim().toLowerCase(Locale.ROOT);
            if (!usernames.add(normalized)) {
                throw new IllegalArgumentException("Um jogador só pode aparecer uma vez por jornada");
            }
        }

        CommunitySeason activeSeason = seasonRepository
                .findByCommunityIdAndActiveTrue(communityId)
                .orElse(null);
        if (!community.isRankingsEnabled() && activeSeason == null) {
            throw new IllegalStateException(
                    "Cria uma época ativa antes de registares mangas de Patinagem de Velocidade");
        }

        // Grouping is opt-in. Explicit keys are isolated by community and season.
        // Lock the season to serialize concurrent writes to the same series.
        List<CommunityTrackSprintSeries.Heat> existingSprintHeats = List.of();
        if (request.sprintSeriesKey() != null && !request.sprintSeriesKey().isBlank()
                && ("TRACK_SPRINT_HEAT".equals(resolvedEventType)
                    || "TRACK_SPRINT_REPECHAGE".equals(resolvedEventType)
                    || "TRACK_SPRINT_5_8".equals(resolvedEventType))) {
            if (activeSeason == null) {
                throw new IllegalArgumentException("Cria uma época ativa antes de agrupar mangas de Sprint");
            }
            activeSeason = seasonRepository.lockByIdAndCommunityId(activeSeason.getId(), communityId)
                    .orElseThrow(() -> new IllegalArgumentException("Época indisponível"));
            AcceptedAliases aliases = acceptedAliasProfiles(communityId);
            var existing = roundRepository
                    .findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                            communityId, activeSeason.getId(), request.sprintSeriesKey().trim());
            if ("TRACK_SPRINT_5_8".equals(resolvedEventType)) {
                if (!existing.isEmpty())
                    throw new IllegalStateException("A prova do 5.º ao 8.º já foi registada");
            } else {
                existingSprintHeats = existing.stream()
                    .map(item -> CommunityTrackSprintSeries.fromRound(
                            roundResponse(item, aliases))).toList();
            }
        }
        boolean officiallyReduced = false;
        if ("TRACK_SPRINT_REPECHAGE".equals(resolvedEventType)
                && request.sprintSeriesKey()!=null) {
            var linked=SPRINT_BRACKET_SERIES.matcher(request.sprintSeriesKey().trim());
            if (linked.matches() && linked.group(2).matches("[UW][1-4]")) {
                officiallyReduced=trackSprintIncidentRepository
                    .existsByBracketIdAndSeriesKeyAndStatus(
                        Long.parseLong(linked.group(1)),
                        request.sprintSeriesKey().trim(),"RESTART_AUTHORIZED");
            }
        }
        // Include previously signed BO3 heats ONLY for sequence validation.
        // Do not persist manufactured CommunityRounds.
        if("TRACK_SPRINT_HEAT".equals(resolvedEventType)
                &&request.sprintSeriesKey()!=null
                &&request.sprintSeriesKey().matches("SP[1-9][0-9]*-(Q[1-4]|S[1-2]|B|G)")
                &&request.entries()!=null&&request.entries().size()==2) {
            var linked=SPRINT_BRACKET_SERIES.matcher(request.sprintSeriesKey().trim());
            if(linked.matches()) {
                long linkedId=Long.parseLong(linked.group(1));
                if(trackSprintBracketRepository.findByIdAndCommunityIdAndSeasonId(
                        linkedId,communityId,activeSeason.getId()).isPresent()) {
                    var all=new ArrayList<>(existingSprintHeats);
                    var awards=sprintBo3OfficialHeats(linkedId)
                            .getOrDefault(request.sprintSeriesKey().trim(),Map.of());
                    for(var award:awards.entrySet())
                        all.add(new CommunityTrackSprintSeries.Heat(resolvedDiscipline,
                                award.getKey(),request.entries().get(0).username(),
                                request.entries().get(1).username(),award.getValue()));
                    all.sort(Comparator.comparingInt(CommunityTrackSprintSeries.Heat::number));
                    existingSprintHeats=List.copyOf(all);
                }
            }
        }
        CommunityTrackSprintSeries.validate(resolvedEventType, resolvedDiscipline,
                request.sprintSeriesKey(), request.sprintHeatNumber(),
                existingSprintHeats, request.entries(), officiallyReduced);
        validateBracketRound(communityId, activeSeason, request.sprintSeriesKey(),
                resolvedDiscipline, resolvedEventType, request.entries());

        CommunityRound round = roundRepository.save(CommunityRound.builder()
                .community(community)
                .season(activeSeason)
                .name(request.name().trim())
                .sport(resolvedSport)
                .eventType(resolvedEventType)
                .eventDiscipline(resolvedDiscipline)
                .sprintSeriesKey(request.sprintSeriesKey() == null || request.sprintSeriesKey().isBlank()
                        ? null : request.sprintSeriesKey().trim())
                .sprintHeatNumber(request.sprintHeatNumber())
                .groupScoresJson(groupScoresJson)
                .basketballPointsA(basketballScore == null ? null : basketballScore.sideA())
                .basketballPointsB(basketballScore == null ? null : basketballScore.sideB())
                .teamScoreA(teamScore == null ? null : teamScore.sideA())
                .teamScoreB(teamScore == null ? null : teamScore.sideB())
                .cricketWicketsA(cricketDetails == null ? null : cricketDetails.wicketsA())
                .cricketBallsA(cricketDetails == null ? null : cricketDetails.legalBallsA())
                .cricketWicketsB(cricketDetails == null ? null : cricketDetails.wicketsB())
                .cricketBallsB(cricketDetails == null ? null : cricketDetails.legalBallsB())
                .matchFormat(volleyballEvent || encodedSetScores == null ? null : request.matchFormat())
                .setScores(encodedSetScores)
                .playedAt(request.playedAt())
                .createdAt(Instant.now())
                .build());

        List<CommunityRoundEntry> entries = new ArrayList<>();
        for (CommunityRoundEntryRequest requestEntry : request.entries()) {
            Profile player = profile(requestEntry.username().trim());

            if (!memberRepository.existsByCommunityIdAndProfileId(communityId, player.getId())) {
                throw new IllegalArgumentException(
                        "O jogador @" + player.getUsername() + " não pertence à comunidade"
                );
            }

            CommunityPlayer communityPlayer = playerRepository
                    .findByCommunityIdAndProfileId(communityId, player.getId())
                    .orElseGet(() -> playerRepository.save(CommunityPlayer.builder()
                            .community(community)
                            .displayName(playerDisplayName(player))
                            .normalizedName(normalizePlayerName(playerDisplayName(player)))
                            .profile(player)
                            .createdAt(Instant.now())
                            .build()));

            entries.add(CommunityRoundEntry.builder()
                    .round(round)
                    .profile(player)
                    .communityPlayer(communityPlayer)
                    .team(trimToNull(requestEntry.team()))
                    .result(requestEntry.result())
                    .goals(valueOrZero(requestEntry.goals()))
                    .assists(valueOrZero(requestEntry.assists()))
                    .mvp(requestEntry.mvp())
                    .cleanSheet(requestEntry.cleanSheet())
                    .finishPosition(requestEntry.finishPosition())
                    .elapsedSeconds(requestEntry.elapsedSeconds())
                    .bestLapSeconds(requestEntry.bestLapSeconds())
                    .lapsCompleted(requestEntry.lapsCompleted())
                    .performanceValue(requestEntry.performanceValue())
                    .waveScore1(requestEntry.waveScore1())
                    .waveScore2(requestEntry.waveScore2())
                    .build());
        }

        entryRepository.saveAll(entries);
        return roundResponse(round, acceptedAliasProfiles(communityId));
    }

    public void deleteRound(Long communityId, Long roundId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        CommunityRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new IllegalArgumentException("Jornada não encontrada"));

        if (!round.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("A jornada não pertence a esta comunidade");
        }
        if (round.getSeason() != null && !round.getSeason().isActive()) {
            throw new IllegalStateException("Não podes apagar jornadas de uma época encerrada");
        }
        if(round.getSeason()!=null && round.getSprintSeriesKey()!=null) {
            var m=SPRINT_BRACKET_SERIES.matcher(round.getSprintSeriesKey());
            if(m.matches() && trackSprintBracketRepository
                    .findByIdAndCommunityIdAndSeasonId(Long.valueOf(m.group(1)),
                            communityId,round.getSeason().getId())
                    .map(b->b.getInterruptionStage()!=null).orElse(false))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Não podes eliminar resultados de competição encerrada oficialmente");
        }

        // Deleting a non-final heat would leave a false numbered series.
        if (round.getSprintSeriesKey() != null && round.getSeason() != null
                && round.getSprintHeatNumber() != null) {
            boolean laterHeatExists = roundRepository
                    .findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                            communityId, round.getSeason().getId(), round.getSprintSeriesKey())
                    .stream().anyMatch(other -> other.getSprintHeatNumber() != null
                            && other.getSprintHeatNumber() > round.getSprintHeatNumber());
            if (laterHeatExists)
                throw new IllegalStateException("Apaga primeiro as mangas seguintes desta série Sprint");
        }
        protectSprintQualification(communityId, roundId);
        preventBracketAncestorRemoval(communityId, round);
        entryRepository.deleteByRoundId(roundId);
        roundRepository.delete(round);
    }

    /**
     * Every accepted Excel identity stays associated with its Jynlo account, but
     * its imported name is the preferred display name within this community.
     * Select deterministically by historical player ID if an account has more
     * than one accepted historical identity.
     */
    private AcceptedAliases acceptedAliasProfiles(Long communityId) {
        Map<Long, Profile> profilesByHistoricalId = new HashMap<>();
        Map<Long, String> namesByProfileId = new HashMap<>();
        List<CommunityHistoricalPlayerLink> links = new ArrayList<>(
                historicalPlayerLinkRepository.findByCommunityIdAndStatus(communityId, "ACCEPTED"));
        links.sort(Comparator.comparing(link -> link.getPlayer().getId()));
        for (CommunityHistoricalPlayerLink link : links) {
            profilesByHistoricalId.put(link.getPlayer().getId(), link.getProfile());
            namesByProfileId.putIfAbsent(link.getProfile().getId(), link.getPlayer().getDisplayName());
        }
        return new AcceptedAliases(profilesByHistoricalId, namesByProfileId);
    }

    private record AcceptedAliases(Map<Long, Profile> profilesByHistoricalId,
                                   Map<Long, String> namesByProfileId) {
    }

    private List<CommunityStandingResponse> standings(Community community, Long seasonId) {
        return standings(community, seasonId, acceptedAliasProfiles(community.getId()));
    }

    private List<CommunityStandingResponse> standings(Community community, Long seasonId,
                                                       AcceptedAliases aliases) {
        Map<Long, Profile> linkedProfiles = aliases.profilesByHistoricalId();
        Map<String, StandingAccumulator> table = new LinkedHashMap<>();
        Set<Long> linkedProfileIds = new HashSet<>();
        for (Profile linked : linkedProfiles.values()) linkedProfileIds.add(linked.getId());

        for (CommunityMember member : memberRepository.findByCommunityIdOrderByJoinedAtAsc(community.getId())) {
            Profile profile = member.getProfile();
            CommunityPlayer communityPlayer = playerRepository
                    .findByCommunityIdAndProfileId(community.getId(), profile.getId())
                    .orElse(null);
            // Confirmed historical aliases are consolidated under the actual profile.
            if (linkedProfileIds.contains(profile.getId())) communityPlayer = null;
            table.putIfAbsent(standingKey(communityPlayer, profile),
                    new StandingAccumulator(communityPlayer, profile, aliases.namesByProfileId().get(profile.getId())));
        }

        List<CommunityRoundEntry> sourceEntries = seasonId == null
                ? entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(community.getId())
                : entryRepository.findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(seasonId);

        for (CommunityRoundEntry entry : sourceEntries) {
            // Race results have positions/times, not football's 3-1-0 points.
            // Preserve historical rows but never award W/D/L leaderboard points
            // from race events. A separate race championship is a later feature.
            if (entry.getRound() != null &&
                    (CommunityRoundEventType.isRaceEvent(entry.getRound().getSport(),
                            entry.getRound().getEventType())
                    || CommunityRoundEventType.isNeutralEvent(entry.getRound().getSport(),
                            entry.getRound().getEventType())
                    || entry.getRound().getMatchFormat() != null)) continue;
            CommunityPlayer communityPlayer = entry.getCommunityPlayer();
            Profile profile = entry.getProfile() != null
                    ? entry.getProfile()
                    : communityPlayer == null ? null : communityPlayer.getProfile();
            if (communityPlayer != null && linkedProfiles.containsKey(communityPlayer.getId())) {
                profile = linkedProfiles.get(communityPlayer.getId());
            }
            final Profile resolvedProfile = profile;
            final CommunityPlayer originalPlayer = communityPlayer;
            final CommunityPlayer displayPlayer = profile != null && linkedProfileIds.contains(profile.getId())
                    ? null : originalPlayer;
            StandingAccumulator row = table.computeIfAbsent(
                    standingKey(displayPlayer, resolvedProfile),
                    ignored -> new StandingAccumulator(displayPlayer, resolvedProfile,
                            resolvedProfile == null ? null
                                    : aliases.namesByProfileId().get(resolvedProfile.getId()))
            );
            row.add(entry);
        }

        int winPoints = pointsOrDefault(community.getWinPoints(), 3);
        int drawPoints = pointsOrDefault(community.getDrawPoints(), 1);
        int lossPoints = pointsOrDefault(community.getLossPoints(), 0);

        List<StandingAccumulator> ordered = table.values().stream()
                .sorted(
                        Comparator.comparingInt((StandingAccumulator row) -> row.points(winPoints, drawPoints, lossPoints))
                                .reversed()
                                .thenComparing(Comparator.comparingInt(StandingAccumulator::wins).reversed())
                                .thenComparing(Comparator.comparingInt(StandingAccumulator::goals).reversed())
                                .thenComparing(StandingAccumulator::displayName, String.CASE_INSENSITIVE_ORDER)
                )
                .toList();

        List<CommunityStandingResponse> response = new ArrayList<>();
        for (int index = 0; index < ordered.size(); index++) {
            StandingAccumulator row = ordered.get(index);
            int games = row.games();
            response.add(new CommunityStandingResponse(
                    index + 1,
                    row.profileId(),
                    row.username(),
                    row.displayName(),
                    row.profilePicUrl(),
                    games,
                    row.wins(),
                    row.draws(),
                    row.losses(),
                    row.points(winPoints, drawPoints, lossPoints),
                    row.goals(),
                    row.assists(),
                    row.mvps(),
                    row.cleanSheets(),
                    games == 0 ? 0.0 : Math.round((row.wins() * 10000.0 / games)) / 100.0,
                    row.currentStreak(),
                    row.bestWinStreak()
            ));
        }
        return response;
    }

    private String standingKey(CommunityPlayer communityPlayer, Profile profile) {
        if (communityPlayer != null) return "player:" + communityPlayer.getId();
        if (profile != null) return "profile:" + profile.getId();
        throw new IllegalStateException("A estatística não tem jogador associado");
    }

    private CommunityRoundResponse roundResponse(CommunityRound round,
                                                 AcceptedAliases aliases) {
        List<CommunityRoundEntryResponse> entries = entryRepository
                .findByRoundIdOrderByIdAsc(round.getId())
                .stream()
                .map(entry -> {
                    CommunityPlayer historicalPlayer = entry.getCommunityPlayer();
                    Profile profile = entry.getProfile() != null
                            ? entry.getProfile()
                            : historicalPlayer.getProfile() != null
                            ? historicalPlayer.getProfile()
                            : aliases.profilesByHistoricalId().get(historicalPlayer.getId());
                    return new CommunityRoundEntryResponse(
                        profile == null ? null : profile.getId(),
                        profile == null ? null : profile.getUsername(),
                        profile != null && aliases.namesByProfileId().containsKey(profile.getId())
                                ? aliases.namesByProfileId().get(profile.getId())
                                : historicalPlayer.getDisplayName(),
                        profile == null ? null : profile.getProfilePicUrl(),
                        entry.getTeam(),
                        entry.getResult(),
                        entry.getGoals(),
                        entry.getAssists(),
                        entry.isMvp(),
                        entry.isCleanSheet(),
                        entry.getFinishPosition(),
                        entry.getElapsedSeconds(),
                        entry.getBestLapSeconds(),
                        entry.getLapsCompleted(),
                        entry.getId(),
                        historicalPlayer.getId(),
                        entry.getRacePenaltyPoints(),
                        entry.getRacePenaltyReason(),
                        entry.getRaceTimePenaltySeconds(),
                        CommunityRaceDecisions.adjustedTime(
                                entry.getElapsedSeconds(), entry.getRaceTimePenaltySeconds()),
                        entry.isRaceDisqualified(),
                        entry.getRaceStewardReason(),
                        entry.getPerformanceValue(),
                        entry.getWaveScore1(),
                        entry.getWaveScore2()
                    );
                })
                .toList();

        return new CommunityRoundResponse(
                round.getId(),
                round.getName(),
                round.getPlayedAt(),
                entries,
                round.getSport(),
                round.getMatchFormat(),
                CommunityRacketResults.decode(round.getSetScores()),
                round.getEventType(),
                round.getEventDiscipline(),
                CommunityGroupRoutineResults.decode(round.getGroupScoresJson()),
                CommunityBasketballResults.decode(
                        round.getBasketballPointsA(), round.getBasketballPointsB()),
                CommunityTeamFinalResults.decode(
                        round.getTeamScoreA(), round.getTeamScoreB()),
                CommunityCricketResults.decode(round.getCricketWicketsA(), round.getCricketBallsA(),
                        round.getCricketWicketsB(), round.getCricketBallsB()),
                // Preserve the persisted Sprint relation on every API read:
                // the bracket planner must see exactly the recorded series and heat.
                round.getSprintSeriesKey(),
                round.getSprintHeatNumber()
        );
    }

    private CommunitySeasonResponse seasonResponse(
            Community community,
            CommunitySeason season) {
        String championUsername = null;
        String championDisplayName = null;
        Integer championPoints = null;

        if (!season.isActive()) {
            if (season.isChampionFinalized()) {
                championUsername = season.getChampionUsername();
                championDisplayName = season.getChampionDisplayName();
                championPoints = season.getChampionPoints();
            } else {
                // Compatibility with seasons closed before the champion snapshot migration.
                List<CommunityStandingResponse> seasonStandings = standings(
                        community, season.getId());
                if (!seasonStandings.isEmpty() && seasonStandings.get(0).games() > 0) {
                    CommunityStandingResponse champion = seasonStandings.get(0);
                    championUsername = champion.username();
                    championDisplayName = champion.fullName();
                    championPoints = champion.points();
                }
            }
        }

        return new CommunitySeasonResponse(
                season.getId(),
                season.getName(),
                season.getStartsAt(),
                season.getEndedAt(),
                season.isActive(),
                championUsername,
                championDisplayName,
                championPoints
        );
    }

    private CommunityCompetitionSettingsResponse settings(Community community) {
        return new CommunityCompetitionSettingsResponse(
                community.isRankingsEnabled(),
                pointsOrDefault(community.getWinPoints(), 3),
                pointsOrDefault(community.getDrawPoints(), 1),
                pointsOrDefault(community.getLossPoints(), 0)
        );
    }

    private void requireAccess(Community community, Profile actor) {
        boolean member = memberRepository.existsByCommunityIdAndProfileId(
                community.getId(), actor.getId());

        if (!member && community.getVisibility() == CommunityVisibility.PRIVATE) {
            throw new AccessDeniedException("Não tens acesso a esta comunidade");
        }
    }

    private void requireAdmin(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));

        if (membership.getRole() != CommunityRole.OWNER
                && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem gerir a competição");
        }
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private Community community(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
    }

    private int pointsOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private String playerDisplayName(Profile profile) {
        String nickname = trimToNull(profile.getNickname());
        if (nickname != null) {
            return nickname;
        }
        String fullName = trimToNull(profile.getFullName());
        return fullName != null ? fullName : profile.getUsername();
    }

    private String normalizePlayerName(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Fallback for members with no imported name; Excel/community names take
     * precedence wherever the participant has one.
     */
    private static String publicPlayerName(Profile profile) {
        if (profile.getFullName() != null && !profile.getFullName().isBlank()) {
            return profile.getFullName().trim();
        }
        if (profile.getNickname() != null && !profile.getNickname().isBlank()) {
            return profile.getNickname().trim();
        }
        return profile.getUsername();
    }

    private static final class StandingAccumulator {
        private final CommunityPlayer communityPlayer;
        private final Profile profile;
        private final String preferredHistoricalName;
        private int games;
        private int wins;
        private int draws;
        private int losses;
        private int goals;
        private int assists;
        private int mvps;
        private int cleanSheets;
        private CommunityMatchResult lastResult;
        private int currentStreakCount;
        private int currentWinStreak;
        private int bestWinStreak;

        private StandingAccumulator(CommunityPlayer communityPlayer, Profile profile,
                                    String preferredHistoricalName) {
            this.communityPlayer = communityPlayer;
            this.profile = profile;
            this.preferredHistoricalName = preferredHistoricalName;
        }

        private void add(CommunityRoundEntry entry) {
            games++;
            goals += entry.getGoals();
            assists += entry.getAssists();
            if (entry.isMvp()) mvps++;
            if (entry.isCleanSheet()) cleanSheets++;

            switch (entry.getResult()) {
                case WIN -> {
                    wins++;
                    currentWinStreak++;
                    bestWinStreak = Math.max(bestWinStreak, currentWinStreak);
                }
                case DRAW -> {
                    draws++;
                    currentWinStreak = 0;
                }
                case LOSS -> {
                    losses++;
                    currentWinStreak = 0;
                }
            }

            if (entry.getResult() == lastResult) {
                currentStreakCount++;
            } else {
                lastResult = entry.getResult();
                currentStreakCount = 1;
            }
        }

        private int points(int winPoints, int drawPoints, int lossPoints) {
            return wins * winPoints + draws * drawPoints + losses * lossPoints;
        }

        private String currentStreak() {
            if (lastResult == null || currentStreakCount == 0) {
                return "-";
            }
            String code = switch (lastResult) {
                case WIN -> "W";
                case DRAW -> "D";
                case LOSS -> "L";
            };
            return code + currentStreakCount;
        }

        private Long profileId() { return profile == null ? null : profile.getId(); }
        private String username() { return profile == null ? null : profile.getUsername(); }
        private String displayName() {
            if (preferredHistoricalName != null && !preferredHistoricalName.isBlank()) {
                return preferredHistoricalName;
            }
            if (communityPlayer != null) return communityPlayer.getDisplayName();
            return profile != null ? publicPlayerName(profile) : "Jogador";
        }
        private String profilePicUrl() { return profile == null ? null : profile.getProfilePicUrl(); }
        private int games() { return games; }
        private int wins() { return wins; }
        private int draws() { return draws; }
        private int losses() { return losses; }
        private int goals() { return goals; }
        private int assists() { return assists; }
        private int mvps() { return mvps; }
        private int cleanSheets() { return cleanSheets; }
        private int bestWinStreak() { return bestWinStreak; }
    }
}
