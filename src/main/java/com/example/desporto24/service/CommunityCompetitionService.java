package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityHistoricalPlayerLink;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.community.CommunityTrackSprintBracket;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityHistoricalPlayerLinkRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunityTrackSprintBracketRepository;
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
    private final CommunityTrackSprintBracketRepository trackSprintBracketRepository;
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
                            : "OLYMPIC_TWENTY_FOUR_REPECHAGE".equals(format) ? 24 : 16);
        } else if (request.qualifyingRoundId() != null) {
            throw new IllegalArgumentException("A qualificação só pode ligar ao quadro de 16, 24 ou 28 ciclistas");
        }
        CommunityTrackSprintBracket bracket = trackSprintBracketRepository.save(
                CommunityTrackSprintBracket.builder().community(community).season(season)
                        .name(name).format(format).qualifyingRound(qualification)
                        .qualifiedRiders(seeds).createdAt(Instant.now()).build());
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
        if (containsHeats)
            throw new IllegalStateException("Apaga as mangas do quadro antes de o eliminar");
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
                        || "TRACK_SPRINT_REPECHAGE".equals(r.getEventType()))
                .map(r -> roundResponse(r, aliases)).toList();
        return brackets.stream().map(b -> trackSprintBracketResponse(b, rounds)).toList();
    }

    private CommunityTrackSprintBracketDtos.Overview trackSprintBracketResponse(
            CommunityTrackSprintBracket bracket, List<CommunityRoundResponse> rounds) {
        return new CommunityTrackSprintBracketDtos.Overview(
                bracket.getId(), bracket.getSeason().getId(), bracket.getName(),
                bracket.getFormat() == null ? "EIGHT_RIDER_FINALS" : bracket.getFormat(),
                CommunityTrackSprintBracketPlanner.project(bracket.getId(), rounds,
                        bracket.getQualifiedRiders() == null ? List.of() : bracket.getQualifiedRiders()),
                bracket.getQualifyingRound() == null ? null : bracket.getQualifyingRound().getId(),
                bracket.getQualifiedRiders() == null ? List.of() : List.copyOf(bracket.getQualifiedRiders()));
    }

    private static final java.util.regex.Pattern SPRINT_BRACKET_SERIES =
            java.util.regex.Pattern.compile("^SP([1-9][0-9]*)-(A(?:[1-9]|1[0-2])|U[1-4]|C[1-8]|V[1-4]|E[1-6]|W[1-2]|P(?:[5-9]|1[0-6])|R[1-8]|Q[1-4]|S[1-2]|B|G)$");

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
        AcceptedAliases aliases = acceptedAliasProfiles(communityId);
        List<CommunityRoundResponse> rounds = roundRepository
                .findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(communityId, season.getId())
                 .stream().filter(r -> "TRACK_SPRINT_HEAT".equals(r.getEventType())
                        || "TRACK_SPRINT_REPECHAGE".equals(r.getEventType()))
                .map(r -> roundResponse(r, aliases)).toList();
        List<String> seeds = bracket.getQualifiedRiders() == null
                ? List.of() : bracket.getQualifiedRiders();
        if (seeds.size()==24) {
            CommunityTrackSprintOlympic24.validateNewHeat(
                    bracketId, seriesKey, phase, eventType, rounds, entrants, seeds);
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

    public CommunityRoundResponse createRound(
            Long communityId,
            String username,
            CreateCommunityRoundRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        if (!community.isRankingsEnabled()) {
            throw new IllegalStateException("Ativa primeiro o módulo de classificação desta comunidade");
        }

        String resolvedSport = CommunityRoundSport.resolve(community, request.sport());
        String resolvedEventType = CommunityRoundEventType.validate(resolvedSport, request.eventType());
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

        // Grouping is opt-in. Explicit keys are isolated by community and season.
        // Lock the season to serialize concurrent writes to the same series.
        List<CommunityTrackSprintSeries.Heat> existingSprintHeats = List.of();
        if (request.sprintSeriesKey() != null && !request.sprintSeriesKey().isBlank()
                && ("TRACK_SPRINT_HEAT".equals(resolvedEventType)
                    || "TRACK_SPRINT_REPECHAGE".equals(resolvedEventType))) {
            if (activeSeason == null) {
                throw new IllegalArgumentException("Cria uma época ativa antes de agrupar mangas de Sprint");
            }
            activeSeason = seasonRepository.lockByIdAndCommunityId(activeSeason.getId(), communityId)
                    .orElseThrow(() -> new IllegalArgumentException("Época indisponível"));
            AcceptedAliases aliases = acceptedAliasProfiles(communityId);
            existingSprintHeats = roundRepository
                    .findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
                            communityId, activeSeason.getId(), request.sprintSeriesKey().trim())
                    .stream().map(item -> CommunityTrackSprintSeries.fromRound(roundResponse(item, aliases))).toList();
        }
        CommunityTrackSprintSeries.validate(resolvedEventType, resolvedDiscipline,
                request.sprintSeriesKey(), request.sprintHeatNumber(),
                existingSprintHeats, request.entries());
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
                        round.getCricketWicketsB(), round.getCricketBallsB())
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
