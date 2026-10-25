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
                           List<TieBreakView> tieBreaks) {}
    public record ConfirmRequest(List<Long> playerIds, String reason) {}
    public record TieBreakView(String sourceStage, List<Long> selectedPlayerIds,
                               String reason, String confirmedBy, Instant confirmedAt,
                               boolean stale) {}

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
        CommunityInlineSpeedTieBreak ruling = tieBreaks
                .findByTournamentIdAndSourceStage(tournamentId, "FIRST")
                .orElseGet(CommunityInlineSpeedTieBreak::new);
        ruling.setTournament(tournament);
        ruling.setSourceStage("FIRST");
        ruling.setSelectedPlayerIds(selected.stream().map(String::valueOf)
                .collect(Collectors.joining(",")));
        ruling.setSourceFingerprint(CommunityInlineSpeed2026.fingerprint(source));
        ruling.setJudgeReason(reason);
        ruling.setConfirmedBy(username);
        ruling.setConfirmedAt(Instant.now());
        tieBreaks.save(ruling);
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
                .map(d -> new TieBreakView(d.getSourceStage(),
                        readIds(d.getSelectedPlayerIds()), d.getJudgeReason(),
                        d.getConfirmedBy(), d.getConfirmedAt(),
                        !d.getSourceFingerprint().equals(CommunityInlineSpeed2026.fingerprint(
                                raw(linked, d.getSourceStage()))))).toList();
        return new Overview(tournament.getId(), discipline, visible, available,
                List.copyOf(proposals), approved, resolutions);
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
