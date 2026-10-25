package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.*;
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
 * Opt-in 2026 championship bracket for the existing Patinagem de Velocidade catalog entry.
 * Community rounds remain the source of timings and finishing positions; no fabricated
 * fixture, score or automatic judge verdict is written.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityInlineSpeedProgressionService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityRoundRepository rounds;
    private final CommunityRoundEntryRepository entries;
    private final CommunityInlineSpeedHeatRepository links;
    private final CommunityInlineSpeedDecisionRepository decisions;
    private final CommunityInlineSpeedTieBreakRepository tieBreaks;
    private final CommunityInlineSpeedTieBreakRevisionRepository tieBreakRevisions;
    private final CommunityInlineSpeedTieTrialRepository tieTrials;
    private final CommunityInlineSpeedTieTrialResultRepository trialResults;
    private final CommunityInlineSpeedTrialUsageRepository trialUsages;

    public record SkaterView(long playerId, String name, Integer position,
                             BigDecimal seconds, boolean disqualified) {}
    public record HeatView(long roundId, String name, String eventType, int heatNumber,
                           List<SkaterView> skaters) {}
    public record ProposalView(String sourceStage, String targetStage, List<Long> playerIds,
                               int expectedCount, boolean ready, String warning) {}
    public record DecisionView(String sourceStage, String targetStage, List<Long> playerIds,
                               String reason, String confirmedBy, Instant confirmedAt,
                               boolean stale) {}
    public record Overview(Long tournamentId, String discipline, List<HeatView> linkedHeats,
                           List<HeatView> availableHeats, List<ProposalView> proposals,
                           List<DecisionView> decisions,
                           List<TieBreakView> tieBreaks, List<TieTrialView> trials) {}
    public record ConfirmRequest(List<Long> playerIds, String reason) {}
    public record TieBreakRequest(List<Long> playerIds, String reason,
                                  String evidenceType, String evidenceReference,
                                  List<Long> trialIds) {
        /** Previous internal integrations did not attach the structured trials. */
        public TieBreakRequest(List<Long> playerIds, String reason, String evidenceType,
                               String evidenceReference) {
            this(playerIds, reason, evidenceType, evidenceReference, null);
        }
    }
    public record TrialSkaterRequest(Long playerId, Integer place, BigDecimal seconds) {}
    public record TrialCreateRequest(boolean actuallyHeld, Instant heldAt,
                                     String officialReference, String reason,
                                     List<TrialSkaterRequest> results) {}
    public record TrialSkaterView(long playerId, int place, BigDecimal seconds) {}
    public record TieTrialView(long id, String sourceStage, Instant heldAt,
                               String officialReference, String reason,
                               String recordedBy, Instant recordedAt, boolean stale,
                               List<TrialSkaterView> results) {}
    public record TieBreakRevisionView(int revision, List<Long> selectedPlayerIds,
                                       String reason, String confirmedBy, Instant confirmedAt,
                                       boolean stale, String evidenceType, String evidenceReference,
                                       List<Long> trialIds) {}
    public record TieBreakView(String sourceStage, List<Long> selectedPlayerIds,
                               String reason, String confirmedBy, Instant confirmedAt,
                               boolean stale, List<TieBreakRevisionView> revisions,
                               String evidenceType, String evidenceReference,
                               List<Long> trialIds) {}

    @Transactional(readOnly = true)
    public Overview overview(Long communityId, Long tournamentId, String username) {
        requireMember(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, false);
        return snapshot(communityId, tournament, true);
    }

    public Overview link(Long communityId, Long tournamentId, long roundId,
                         int heatNumber, String username) {
        requireAdmin(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, true);
        requireEditable(tournament);
        CommunityRound round = rounds.findById(roundId)
                .orElseThrow(() -> problem(HttpStatus.NOT_FOUND, "Manga não encontrada"));
        String phase = CommunityInlineSpeed2026.stage(round.getEventType());
        String discipline = CommunityInlineSpeed2026.discipline(round.getEventType());
        if (phase == null || discipline == null
                || !round.getCommunity().getId().equals(communityId)
                || !"Patinagem de Velocidade".equals(round.getSport())
                || round.getSeason() == null || !round.getSeason().isActive()
                || heatNumber < 1 || heatNumber > CommunityInlineSpeed2026.maximumHeats(phase))
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY, "Manga inválida ou de época encerrada");
        List<CommunityInlineSpeedHeatLink> existing = links.findByTournamentIdOrderByIdAsc(tournamentId);
        for (var prior : existing) {
            if (prior.getRound().getId().equals(roundId)) {
                if (prior.getHeatNumber() == heatNumber) return snapshot(communityId, tournament, true);
                throw problem(HttpStatus.CONFLICT, "Manga já associada com outro número");
            }
            if (prior.getStage().equals(phase) && prior.getHeatNumber() == heatNumber)
                throw problem(HttpStatus.CONFLICT, "Já existe uma manga neste número da fase");
            if (!prior.getRound().getSeason().getId().equals(round.getSeason().getId()))
                throw problem(HttpStatus.CONFLICT, "Não mistures épocas na mesma competição");
            if (!discipline.equals(CommunityInlineSpeed2026.discipline(prior.getRound().getEventType())))
                throw problem(HttpStatus.CONFLICT, "Não mistures 500 m em pista com uma volta em estrada");
        }
        if (links.existsByRoundId(roundId))
            throw problem(HttpStatus.CONFLICT, "Manga já associada a outra competição");
        if (decisions.findByTournamentIdAndSourceStage(tournamentId, phase).isPresent())
            throw problem(HttpStatus.CONFLICT, "Fase já confirmada: não é possível alterar as mangas");
        if (!"FIRST".equals(phase)) {
            String source = "QUARTER".equals(phase) ? "FIRST"
                    : "SEMI".equals(phase) ? hasValidDecision(tournamentId, "QUARTER",
                      "SEMI", existing) ? "QUARTER" : "FIRST" : "SEMI";
            CommunityInlineSpeedDecision approved = validDecision(tournamentId, source, phase, existing);
            Set<Long> allowed = Set.copyOf(readIds(approved.getQualifiedPlayerIds()));
            for (var entrant : entries.findByRoundIdOrderByIdAsc(roundId)) {
                if (entrant.getCommunityPlayer() == null
                        || !allowed.contains(entrant.getCommunityPlayer().getId()))
                    throw problem(HttpStatus.CONFLICT,
                            "Esta manga inclui um atleta que não está oficialmente apurado");
            }
        }
        List<CommunityRoundEntry> entrants = entries.findByRoundIdOrderByIdAsc(roundId);
        if (entrants.isEmpty())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY, "Manga sem resultados");
        if ("QUARTER".equals(phase)) {
            CommunityInlineSpeedDecision firstDecision =
                    validDecision(tournamentId, "FIRST", "QUARTER", existing);
            int qualified = readIds(firstDecision.getQualifiedPlayerIds()).size();
            int required = qualified == 17 && heatNumber == 1 ? 5 : 4;
            if (entrants.size() != required)
                throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Quartos de final: a manga " + heatNumber
                        + " exige " + required + " patinadores");
        }
        Set<Long> currentPlayers = entrants.stream()
                .map(e -> e.getCommunityPlayer().getId()).collect(Collectors.toSet());
        for (CommunityInlineSpeedHeatLink prior : existing) {
            if (!prior.getStage().equals(phase)) continue;
            for (CommunityRoundEntry entry : entries.findByRoundIdOrderByIdAsc(prior.getRound().getId())) {
                if (currentPlayers.contains(entry.getCommunityPlayer().getId()))
                    throw problem(HttpStatus.CONFLICT,
                            "Um patinador não pode disputar duas mangas na mesma fase");
            }
        }
        links.save(CommunityInlineSpeedHeatLink.builder().tournament(tournament)
                .round(round).stage(phase).heatNumber(heatNumber).build());
        if (tournament.getStatus() == TournamentStatus.DRAFT) {
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
        }
        return snapshot(communityId, tournament, true);
    }

    public Overview unlink(Long communityId, Long tournamentId, long roundId, String username) {
        requireAdmin(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, true);
        requireEditable(tournament);
        CommunityInlineSpeedHeatLink link = links.findByTournamentIdAndRoundId(tournamentId, roundId)
                .orElseThrow(() -> problem(HttpStatus.NOT_FOUND, "Manga não associada"));
        if (decisions.findByTournamentIdAndSourceStage(tournamentId, link.getStage()).isPresent())
            throw problem(HttpStatus.CONFLICT, "Apuramento confirmado: não podes remover a manga");
        links.delete(link);
        links.flush();
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).isEmpty()) {
            tournament.setStatus(TournamentStatus.DRAFT);
            tournament.setStartedAt(null);
            tournaments.save(tournament);
        }
        return snapshot(communityId, tournament, true);
    }

    /** Explicit human acceptance or correction. All exceptions need a recorded reason. */
    public Overview confirm(Long communityId, Long tournamentId, String sourceStage,
                            ConfirmRequest request, String username) {
        requireAdmin(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, true);
        requireEditable(tournament);
        if (!List.of("FIRST", "QUARTER", "SEMI").contains(sourceStage)
                || request == null || request.playerIds() == null
                || request.playerIds().isEmpty() || request.playerIds().size() > 64)
            throw problem(HttpStatus.BAD_REQUEST, "Fase ou lista de apurados inválida");
        List<CommunityInlineSpeedHeatLink> existing = links.findByTournamentIdOrderByIdAsc(tournamentId);
        String discipline = discipline(existing);
        List<CommunityInlineSpeed2026.Heat> sourceHeats = raw(existing, sourceStage);
        CommunityInlineSpeed2026.Proposal proposal =
                resolvedProposal(tournamentId, sourceHeats, sourceStage, discipline);
        if (!proposal.ready())
            throw problem(HttpStatus.CONFLICT, proposal.warning());
        List<Long> selected = request.playerIds();
        if (selected.stream().anyMatch(v -> v == null || v <= 0)
                || Set.copyOf(selected).size() != selected.size())
            throw problem(HttpStatus.BAD_REQUEST, "Apurados duplicados ou inválidos");
        Set<Long> observed = sourceHeats.stream().flatMap(h -> h.skaters().stream())
                .map(CommunityInlineSpeed2026.Skater::playerId).collect(Collectors.toSet());
        if (!observed.containsAll(selected))
            throw problem(HttpStatus.CONFLICT, "Não podes apurar atletas sem resultado de origem");
        // A disqualified skater must have their SOURCE race result reviewed by
        // officials, rather than being certified by editing only the qualifiers.
        Set<Long> disqualified = sourceHeats.stream()
                .flatMap(h -> h.skaters().stream())
                .filter(CommunityInlineSpeed2026.Skater::disqualified)
                .map(CommunityInlineSpeed2026.Skater::playerId)
                .collect(Collectors.toSet());
        if (selected.stream().anyMatch(disqualified::contains))
            throw problem(HttpStatus.CONFLICT,
                    "Patinador desclassificado: revê primeiro o resultado da manga de origem");
        int minimum = proposal.expectedCount();
        // Article 115 also permits one exceptional additional skater following
        // a sports fault or unfair circumstance, even without a tied time.
        // This MUST be a human selection with an explicit detailed reason.
        boolean exceptionalSeventeenth = "FIRST".equals(sourceStage)
                && "QUARTER".equals(proposal.targetStage())
                && minimum == 16 && selected.size() == 17
                && proposal.candidatePlayerIds().size() == 16;
        int maximum = Math.max(minimum, proposal.candidatePlayerIds().size());
        if (exceptionalSeventeenth) maximum = 17;
        if (selected.size() < minimum || selected.size() > maximum)
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Número de apurados inválido para a fase: esperado " + minimum
                    + (maximum == minimum ? "" : " a " + maximum));
        boolean correction = !selected.equals(proposal.candidatePlayerIds());
        String reason = request.reason() == null ? "" : request.reason().trim();
        if (exceptionalSeventeenth && reason.length() < 20)
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A inclusão excecional do 17.º patinador exige uma fundamentação detalhada");
        // Organizer explicitly attests that the first round is complete; without that
        // declaration, an incomplete collection of timed heats could falsely advance.
        if ((correction || "FIRST".equals(sourceStage)) && reason.length() < 8)
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Regista a decisão fundamentada do júri para este apuramento");
        if (reason.length() > 500)
            throw problem(HttpStatus.BAD_REQUEST, "Justificação demasiado longa");
        Optional<CommunityInlineSpeedDecision> existingDecision =
                decisions.findByTournamentIdAndSourceStage(tournamentId, sourceStage);
        if (existingDecision.isPresent()
                && existing.stream().anyMatch(l -> proposal.targetStage().equals(l.getStage())))
            throw problem(HttpStatus.CONFLICT, "Existem mangas seguintes; não podes corrigir esta decisão");
        // The next stage cannot already contain another approved selection from a different source.
        if ("FIRST".equals(sourceStage) && existingDecision.isEmpty()
                && decisions.findByTournamentIdAndSourceStage(tournamentId, "QUARTER").isPresent())
            throw problem(HttpStatus.CONFLICT, "Já existe um percurso de apuramento posterior");
        CommunityInlineSpeedDecision record = existingDecision.orElseGet(CommunityInlineSpeedDecision::new);
        record.setTournament(tournament);
        record.setSourceStage(sourceStage);
        record.setTargetStage(proposal.targetStage());
        record.setQualifiedPlayerIds(selected.stream().map(String::valueOf).collect(Collectors.joining(",")));
        record.setSourceFingerprint(CommunityInlineSpeed2026.fingerprint(sourceHeats));
        record.setJudgeReason(reason.isBlank() ? null : reason);
        record.setConfirmedBy(username);
        record.setConfirmedAt(Instant.now());
        decisions.save(record);
        return snapshot(communityId, tournament, true);
    }

    /**
     * Explicit jury ruling on the 16th-place multiway tie. This is not a new
     * race time; the original heats and times remain unchanged.
     */
    public Overview resolveTieBreak(Long communityId, Long tournamentId,
                                    String sourceStage, ConfirmRequest request, String username) {
        // Preserve compatibility with existing internal callers and historical decisions.
        return resolveTieBreak(communityId, tournamentId, sourceStage,
                request == null ? null : new TieBreakRequest(request.playerIds(), request.reason(),
                        null, null), username);
    }

    public Overview resolveTieBreak(Long communityId, Long tournamentId,
                                    String sourceStage, TieBreakRequest request, String username) {
        requireAdmin(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, true);
        requireEditable(tournament);
        if (!"FIRST".equals(sourceStage) || request == null || request.playerIds() == null)
            throw problem(HttpStatus.BAD_REQUEST, "Desempate de fase inválida");
        List<CommunityInlineSpeedHeatLink> linked = links.findByTournamentIdOrderByIdAsc(tournamentId);
        List<CommunityInlineSpeed2026.Heat> source = raw(linked, "FIRST");
        if (source.isEmpty()) throw problem(HttpStatus.CONFLICT, "Sem mangas da primeira fase");
        CommunityInlineSpeed2026.Proposal base =
                CommunityInlineSpeed2026.propose(source, "FIRST", discipline(linked));
        if (base.ready() || !"QUARTER".equals(base.targetStage())
                || !base.warning().contains("Empate múltiplo"))
            throw problem(HttpStatus.CONFLICT, "Não há empate múltiplo pendente");
        if (decisions.findByTournamentIdAndSourceStage(tournamentId, "FIRST").isPresent()
                || linked.stream().anyMatch(l -> !"FIRST".equals(l.getStage())))
            throw problem(HttpStatus.CONFLICT, "Já existe apuramento ou fase posterior");

        List<Long> selected = request.playerIds();
        if (selected.size() != 16 || selected.stream().anyMatch(v -> v == null || v <= 0)
                || new HashSet<>(selected).size() != 16)
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O desempate exige exatamente 16 patinadores distintos");
        List<CommunityInlineSpeed2026.Skater> ordered = source.stream()
                .flatMap(h -> h.skaters().stream()).filter(s -> !s.disqualified())
                .sorted(Comparator.comparing(CommunityInlineSpeed2026.Skater::seconds)
                        .thenComparingLong(CommunityInlineSpeed2026.Skater::playerId)).toList();
        if (ordered.size() < 18)
            throw problem(HttpStatus.CONFLICT, "Faltam resultados para desempate");
        BigDecimal cutoff = ordered.get(15).seconds();
        Set<Long> guaranteed = ordered.stream().filter(s -> s.seconds().compareTo(cutoff) < 0)
                .map(CommunityInlineSpeed2026.Skater::playerId).collect(Collectors.toSet());
        Set<Long> tied = ordered.stream().filter(s -> s.seconds().compareTo(cutoff) == 0)
                .map(CommunityInlineSpeed2026.Skater::playerId).collect(Collectors.toSet());
        Set<Long> chosen = Set.copyOf(selected);
        if (!chosen.containsAll(guaranteed)
                || chosen.stream().anyMatch(id -> !guaranteed.contains(id) && !tied.contains(id))
                || chosen.stream().filter(tied::contains).count() != 16 - guaranteed.size())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O júri só pode desempatar participantes efetivamente empatados no limite");
        String reason = request.reason() == null ? "" : request.reason().trim();
        if (reason.length() < 20 || reason.length() > 500)
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O desempate exige uma fundamentação oficial de 20 a 500 caracteres");
        // Evidence is declared by the jury; never inferred from recorded times.
        // Null/null is retained for older API clients and historical rulings.
        String evidenceType = request.evidenceType() == null ? null : request.evidenceType().trim();
        String evidenceReference = request.evidenceReference() == null
                ? null : request.evidenceReference().trim();
        if (evidenceType != null && evidenceType.isBlank()) evidenceType = null;
        if (evidenceReference != null && evidenceReference.isBlank()) evidenceReference = null;
        if ((evidenceType == null) != (evidenceReference == null)
                || (evidenceType != null && (!Set.of("PHOTO_FINISH", "VIDEO_REVIEW",
                        "OFFICIAL_MINUTES", "TIEBREAK_TRIAL", "OTHER").contains(evidenceType)
                        || evidenceReference.length() < 8 || evidenceReference.length() > 500)))
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A evidência exige tipo válido e referência oficial de 8 a 500 caracteres");

        // A decision may cite multiple independently recorded real trials, but no
        // fabricated, cross-tournament, repeated or stale trial identifiers.
        List<Long> requestedTrialIds = request.trialIds() == null ? List.of() : request.trialIds();
        if (requestedTrialIds.size() > 10
                || requestedTrialIds.stream().anyMatch(id -> id == null || id <= 0)
                || requestedTrialIds.stream().distinct().count() != requestedTrialIds.size())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Seleciona até 10 provas reais distintas e válidas");
        // The API must enforce the same audit trail as the UI. Existing legacy
        // rulings without links remain readable, but new trial-based rulings
        // cannot claim a real trial unless they cite its actual record.
        if ("TIEBREAK_TRIAL".equals(evidenceType) && requestedTrialIds.isEmpty())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Para fundamentar uma decisão numa prova adicional, associa pelo menos uma prova registada");
        Map<Long, CommunityInlineSpeedTieTrial> byId = tieTrials
                .findByTournamentIdOrderByIdAsc(tournamentId).stream()
                .collect(Collectors.toMap(CommunityInlineSpeedTieTrial::getId, t -> t));
        String sourceFingerprint = CommunityInlineSpeed2026.fingerprint(source);
        List<CommunityInlineSpeedTieTrial> usedTrials = new ArrayList<>();
        for (Long trialId : requestedTrialIds) {
            CommunityInlineSpeedTieTrial used = byId.get(trialId);
            if (used == null || !"FIRST".equals(used.getSourceStage()))
                throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                        "A prova associada não pertence a este torneio e fase");
            if (!sourceFingerprint.equals(used.getSourceFingerprint()))
                throw problem(HttpStatus.CONFLICT,
                        "A prova associada está desatualizada face às mangas originais");
            usedTrials.add(used);
        }

        CommunityInlineSpeedTieBreak ruling = tieBreaks
                .findByTournamentIdAndSourceStage(tournamentId, "FIRST")
                .orElseGet(CommunityInlineSpeedTieBreak::new);
        // Legacy rulings have no revision rows yet. Keep the previous state
        // before overwriting the latest ruling. Tournament locking serializes edits.
        List<CommunityInlineSpeedTieBreakRevision> history = ruling.getId() == null
                ? List.of()
                : tieBreakRevisions.findByTieBreakIdOrderByRevisionNumberAsc(ruling.getId());
        int nextRevision = history.isEmpty() ? 1
                : history.get(history.size() - 1).getRevisionNumber() + 1;
        if (ruling.getId() != null && history.isEmpty()) {
            tieBreakRevisions.save(revisionSnapshot(ruling, 1));
            nextRevision = 2;
        }
        ruling.setTournament(tournament);
        ruling.setSourceStage("FIRST");
        ruling.setSelectedPlayerIds(selected.stream().map(String::valueOf)
                .collect(Collectors.joining(",")));
        ruling.setSourceFingerprint(CommunityInlineSpeed2026.fingerprint(source));
        ruling.setJudgeReason(reason);
        ruling.setEvidenceType(evidenceType);
        ruling.setEvidenceReference(evidenceReference);
        ruling.setConfirmedBy(username);
        ruling.setConfirmedAt(Instant.now());
        ruling = tieBreaks.save(ruling);
        CommunityInlineSpeedTieBreakRevision revision =
                tieBreakRevisions.save(revisionSnapshot(ruling, nextRevision));
        if (!usedTrials.isEmpty()) {
            trialUsages.saveAll(usedTrials.stream()
                    .map(trial -> CommunityInlineSpeedTrialUsage.builder()
                            .revision(revision).trial(trial).build())
                    .toList());
        }
        return snapshot(communityId, tournament, true);
    }

    private static CommunityInlineSpeedTieBreakRevision revisionSnapshot(
            CommunityInlineSpeedTieBreak ruling, int version) {
        return CommunityInlineSpeedTieBreakRevision.builder()
                .tieBreak(ruling).revisionNumber(version)
                .selectedPlayerIds(ruling.getSelectedPlayerIds())
                .sourceFingerprint(ruling.getSourceFingerprint())
                .judgeReason(ruling.getJudgeReason())
                .evidenceType(ruling.getEvidenceType())
                .evidenceReference(ruling.getEvidenceReference())
                .confirmedBy(ruling.getConfirmedBy())
                .confirmedAt(ruling.getConfirmedAt()).build();
    }

    /** Append-only official record of a separately held trial: NEVER alters source heat timings.
     *  The recorded placing does not promote anyone automatically.
     */
    public Overview recordTieTrial(Long communityId, Long tournamentId, String sourceStage,
                                   TrialCreateRequest request, String username) {
        requireAdmin(communityId, username);
        CommunityTournament tournament = tournament(communityId, tournamentId, true);
        requireEditable(tournament);
        if (!"FIRST".equals(sourceStage) || request == null || !request.actuallyHeld())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Confirma explicitamente que a prova adicional ocorreu");
        List<CommunityInlineSpeedHeatLink> linked = links.findByTournamentIdOrderByIdAsc(tournamentId);
        List<CommunityInlineSpeed2026.Heat> source = raw(linked, "FIRST");
        if (source.isEmpty()) throw problem(HttpStatus.CONFLICT, "Sem mangas de origem");
        CommunityInlineSpeed2026.Proposal base =
                CommunityInlineSpeed2026.propose(source, "FIRST", discipline(linked));
        if (base.ready() || !"QUARTER".equals(base.targetStage())
                || !base.warning().contains("Empate múltiplo")
                || decisions.findByTournamentIdAndSourceStage(tournamentId, "FIRST").isPresent()
                || linked.stream().anyMatch(l -> !"FIRST".equals(l.getStage())))
            throw problem(HttpStatus.CONFLICT,
                    "A prova adicional só pode ser registada enquanto o desempate estiver pendente");
        List<CommunityInlineSpeed2026.Skater> ordered = source.stream()
                .flatMap(h -> h.skaters().stream()).filter(s -> !s.disqualified())
                .sorted(Comparator.comparing(CommunityInlineSpeed2026.Skater::seconds)
                        .thenComparingLong(CommunityInlineSpeed2026.Skater::playerId)).toList();
        if (ordered.size() < 18) throw problem(HttpStatus.CONFLICT, "Sem empate elegível");
        BigDecimal cutoff = ordered.get(15).seconds();
        Set<Long> tied = ordered.stream().filter(s -> s.seconds().compareTo(cutoff) == 0)
                .map(CommunityInlineSpeed2026.Skater::playerId).collect(Collectors.toSet());
        List<TrialSkaterRequest> results = request.results();
        if (results == null || results.size() < 2 || results.size() > 6
                || results.stream().anyMatch(r -> r == null || r.playerId() == null
                        || !tied.contains(r.playerId())
                        || r.place() == null || r.place() < 1 || r.place() > results.size()
                        || r.seconds() != null && (r.seconds().signum() <= 0
                            || r.seconds().scale() > 3 || r.seconds().precision() > 9))
                || results.stream().map(TrialSkaterRequest::playerId).distinct().count() != results.size()
                || results.stream().map(TrialSkaterRequest::place).distinct().count() != results.size())
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Regista 2 a 6 patinadores realmente empatados, com posições distintas de 1 a N");
        // A jury may use photo finish for equal rounded times. Never infer places from time.
        List<TrialSkaterRequest> byPlace = results.stream()
                .sorted(Comparator.comparingInt(TrialSkaterRequest::place)).toList();
        BigDecimal lastSeconds = null;
        for (TrialSkaterRequest result : byPlace) {
            if (result.seconds() != null && lastSeconds != null
                    && result.seconds().compareTo(lastSeconds) < 0)
                throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Tempos adicionais contradizem a ordem de chegada registada");
            if (result.seconds() != null) lastSeconds = result.seconds();
        }
        String reference = request.officialReference() == null ? "" : request.officialReference().trim();
        String reason = request.reason() == null ? "" : request.reason().trim();
        if (reference.length() < 8 || reference.length() > 500
                || reason.length() < 20 || reason.length() > 500
                || request.heldAt() == null || request.heldAt().isAfter(Instant.now()))
            throw problem(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Indica a data real da prova, referência (8–500) e fundamentação (20–500)");
        CommunityInlineSpeedTieTrial trial = tieTrials.saveAndFlush(
                CommunityInlineSpeedTieTrial.builder().tournament(tournament)
                        .sourceStage("FIRST")
                        .sourceFingerprint(CommunityInlineSpeed2026.fingerprint(source))
                        .officialReference(reference).judgeReason(reason)
                        .heldAt(request.heldAt()).recordedBy(username)
                        .recordedAt(Instant.now()).build());
        trialResults.saveAll(byPlace.stream().map(r ->
                CommunityInlineSpeedTieTrialResult.builder().trial(trial)
                        .playerId(r.playerId()).place(r.place()).seconds(r.seconds()).build()).toList());
        return snapshot(communityId, tournament, true);
    }

    /** A saved ruling is effective only while its source heats have the same fingerprint. */
    private CommunityInlineSpeed2026.Proposal resolvedProposal(
            Long tournamentId, List<CommunityInlineSpeed2026.Heat> source,
            String stage, String discipline) {
        CommunityInlineSpeed2026.Proposal base =
                CommunityInlineSpeed2026.propose(source, stage, discipline);
        if (!"FIRST".equals(stage) || base.ready() || !base.warning().contains("Empate múltiplo"))
            return base;
        return tieBreaks.findByTournamentIdAndSourceStage(tournamentId, "FIRST")
                .filter(t -> t.getSourceFingerprint().equals(CommunityInlineSpeed2026.fingerprint(source)))
                .map(t -> new CommunityInlineSpeed2026.Proposal("QUARTER",
                        readIds(t.getSelectedPlayerIds()), 16, true,
                        "Desempate múltiplo resolvido por decisão registada do júri"))
                .orElse(base);
    }

    private Overview snapshot(Long communityId, CommunityTournament tournament, boolean includeAvailable) {
        List<CommunityInlineSpeedHeatLink> linked = links.findByTournamentIdOrderByIdAsc(tournament.getId());
        String discipline = discipline(linked);
        List<HeatView> visible = linked.stream().map(this::view).toList();
        List<ProposalView> proposals = new ArrayList<>();
        for (String phase : List.of("FIRST", "QUARTER", "SEMI")) {
            List<CommunityInlineSpeed2026.Heat> source = raw(linked, phase);
            if (source.isEmpty() || discipline == null) continue;
            CommunityInlineSpeed2026.Proposal p =
                    resolvedProposal(tournament.getId(), source, phase, discipline);
            proposals.add(new ProposalView(phase, p.targetStage(), p.candidatePlayerIds(),
                    p.expectedCount(), p.ready(), p.warning()));
        }
        List<DecisionView> approved = decisions.findByTournamentIdOrderByIdAsc(tournament.getId())
                .stream().map(d -> new DecisionView(d.getSourceStage(), d.getTargetStage(),
                        readIds(d.getQualifiedPlayerIds()), d.getJudgeReason(),
                        d.getConfirmedBy(), d.getConfirmedAt(),
                        !d.getSourceFingerprint().equals(CommunityInlineSpeed2026.fingerprint(
                                raw(linked, d.getSourceStage()))))).toList();
        List<HeatView> available = new ArrayList<>();
        if (includeAvailable && tournament.getStatus() != TournamentStatus.COMPLETED
                && tournament.getHistoricalArchiveId() == null) {
            for (CommunityRound round : rounds.findByCommunityIdOrderByPlayedAtDescIdDesc(communityId)) {
                if (available.size() >= 50) break;
                if (CommunityInlineSpeed2026.stage(round.getEventType()) == null
                        || !"Patinagem de Velocidade".equals(round.getSport())
                        || links.existsByRoundId(round.getId())
                        || round.getSeason() == null || !round.getSeason().isActive()
                        || discipline != null
                           && !discipline.equals(CommunityInlineSpeed2026.discipline(round.getEventType())))
                    continue;
                available.add(view(round, 0));
            }
        }
        List<TieBreakView> resolutions = tieBreaks
                .findByTournamentIdOrderByIdAsc(tournament.getId()).stream()
                .map(d -> {
                    String fingerprint = CommunityInlineSpeed2026.fingerprint(
                            raw(linked, d.getSourceStage()));
                    List<TieBreakRevisionView> revisions = tieBreakRevisions
                            .findByTieBreakIdOrderByRevisionNumberAsc(d.getId()).stream()
                            .map(v -> new TieBreakRevisionView(v.getRevisionNumber(),
                                    readIds(v.getSelectedPlayerIds()), v.getJudgeReason(),
                                    v.getConfirmedBy(), v.getConfirmedAt(),
                                    !v.getSourceFingerprint().equals(fingerprint),
                                    v.getEvidenceType(), v.getEvidenceReference(),
                                    trialUsages.findByRevisionIdOrderByIdAsc(v.getId()).stream()
                                            .map(use -> use.getTrial().getId()).toList())).toList();
                    if (revisions.isEmpty()) {
                        // An existing ruling is still visible before its first edit.
                        revisions = List.of(new TieBreakRevisionView(1,
                                readIds(d.getSelectedPlayerIds()), d.getJudgeReason(),
                                d.getConfirmedBy(), d.getConfirmedAt(),
                                !d.getSourceFingerprint().equals(fingerprint),
                                d.getEvidenceType(), d.getEvidenceReference(), List.of()));
                    }
                    return new TieBreakView(d.getSourceStage(),
                            readIds(d.getSelectedPlayerIds()), d.getJudgeReason(),
                            d.getConfirmedBy(), d.getConfirmedAt(),
                            !d.getSourceFingerprint().equals(fingerprint), revisions,
                            d.getEvidenceType(), d.getEvidenceReference(),
                            revisions.get(revisions.size() - 1).trialIds());
                }).toList();
        List<TieTrialView> recordedTrials = tieTrials
                .findByTournamentIdOrderByIdAsc(tournament.getId()).stream()
                .map(trial -> new TieTrialView(trial.getId(), trial.getSourceStage(),
                        trial.getHeldAt(), trial.getOfficialReference(), trial.getJudgeReason(),
                        trial.getRecordedBy(), trial.getRecordedAt(),
                        !trial.getSourceFingerprint().equals(CommunityInlineSpeed2026.fingerprint(
                                raw(linked, trial.getSourceStage()))),
                        trialResults.findByTrialIdOrderByPlaceAsc(trial.getId()).stream()
                                .map(r -> new TrialSkaterView(r.getPlayerId(), r.getPlace(), r.getSeconds()))
                                .toList())).toList();
        return new Overview(tournament.getId(), discipline, visible, available,
                List.copyOf(proposals), approved, resolutions, recordedTrials);
    }

    private boolean hasValidDecision(Long tournamentId, String source, String target,
                                     List<CommunityInlineSpeedHeatLink> existing) {
        try { validDecision(tournamentId, source, target, existing); return true; }
        catch (ResponseStatusException e) { return false; }
    }

    private CommunityInlineSpeedDecision validDecision(Long tournamentId, String source,
                                                        String target, List<CommunityInlineSpeedHeatLink> existing) {
        CommunityInlineSpeedDecision approved = decisions.findByTournamentIdAndSourceStage(tournamentId, source)
                .orElseThrow(() -> problem(HttpStatus.CONFLICT, "Falta confirmar os apurados da fase anterior"));
        if (!target.equals(approved.getTargetStage())
                || !approved.getSourceFingerprint().equals(CommunityInlineSpeed2026.fingerprint(raw(existing, source))))
            throw problem(HttpStatus.CONFLICT,
                    "A decisão anterior está desatualizada ou pertence a outra fase");
        return approved;
    }

    private String discipline(List<CommunityInlineSpeedHeatLink> links) {
        return links.isEmpty() ? null : CommunityInlineSpeed2026.discipline(
                links.get(0).getRound().getEventType());
    }

    private List<CommunityInlineSpeed2026.Heat> raw(List<CommunityInlineSpeedHeatLink> links, String phase) {
        return links.stream().filter(l -> phase.equals(l.getStage()))
                .map(l -> new CommunityInlineSpeed2026.Heat(l.getRound().getId(),
                        l.getRound().getEventType(), l.getHeatNumber(),
                        entries.findByRoundIdOrderByIdAsc(l.getRound().getId()).stream()
                                .map(e -> new CommunityInlineSpeed2026.Skater(
                                        e.getCommunityPlayer().getId(), e.getElapsedSeconds(),
                                        e.getFinishPosition(), e.isRaceDisqualified())).toList()))
                .toList();
    }

    private HeatView view(CommunityInlineSpeedHeatLink link) {
        return view(link.getRound(), link.getHeatNumber());
    }

    private HeatView view(CommunityRound round, int heatNumber) {
        return new HeatView(round.getId(), round.getName(), round.getEventType(), heatNumber,
                entries.findByRoundIdOrderByIdAsc(round.getId()).stream()
                        .map(e -> new SkaterView(e.getCommunityPlayer().getId(),
                                e.getCommunityPlayer().getDisplayName(), e.getFinishPosition(),
                                e.getElapsedSeconds(), e.isRaceDisqualified())).toList());
    }

    private static List<Long> readIds(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(",")).map(Long::valueOf).toList();
    }

    private CommunityTournament tournament(Long communityId, Long tournamentId, boolean lock) {
        CommunityTournament t = (lock ? tournaments.findByIdForUpdate(tournamentId) : tournaments.findById(tournamentId))
                .orElseThrow(() -> problem(HttpStatus.NOT_FOUND, "Competição não encontrada"));
        if (!t.getCommunity().getId().equals(communityId)
                || !"Patinagem de Velocidade".equals(t.getSport())
                || t.getEntryType() != TournamentEntryType.INDIVIDUAL
                || t.getScoringMode() != null)
            throw problem(HttpStatus.NOT_FOUND, "Competição de patinagem não encontrada");
        return t;
    }

    private void requireEditable(CommunityTournament tournament) {
        if (tournament.getStatus() == TournamentStatus.COMPLETED
                || tournament.getHistoricalArchiveId() != null)
            throw problem(HttpStatus.CONFLICT, "Uma competição histórica ou concluída é imutável");
    }

    private void requireMember(Long communityId, String username) {
        var user = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        if (!members.existsByCommunityIdAndProfileId(communityId, user.getId()))
            throw new AccessDeniedException("Não pertences à comunidade");
    }

    private void requireAdmin(Long communityId, String username) {
        var user = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        var m = members.findByCommunityIdAndProfileId(communityId, user.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences à comunidade"));
        if (m.getRole() != CommunityRole.OWNER && m.getRole() != CommunityRole.ADMIN)
            throw new AccessDeniedException("Só administradores podem ratificar apuramentos");
    }

    private static ResponseStatusException problem(HttpStatus status, String message) {
        return new ResponseStatusException(status, message);
    }
}
