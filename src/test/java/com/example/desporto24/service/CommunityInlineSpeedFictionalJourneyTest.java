package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * An in-memory journey with fictional skaters, not real users and not production
 * data. Exercises the actual service, judge decisions and progression guards.
 * Repositories are stateful Mockito fakes; no PostgreSQL or HTTP is involved.
 */
@ExtendWith(MockitoExtension.class)
class CommunityInlineSpeedFictionalJourneyTest {
    private static final String FIRST = "INLINE_TRACK_500D_FIRST_ROUND";
    private static final String SEMI = "INLINE_TRACK_500D_SEMI_HEAT";
    private static final String FINAL = "INLINE_TRACK_500D_FINAL_A";
    private static final String[] NAMES = {
        "Ana Exemplo", "Bruno Exemplo", "Carla Exemplo", "Diogo Exemplo",
        "Eva Exemplo", "Filipe Exemplo", "Gabriela Exemplo", "Hugo Exemplo",
        "Inês Exemplo", "João Exemplo", "Lara Exemplo", "Marco Exemplo",
        "Nuno Exemplo", "Olga Exemplo", "Paulo Exemplo", "Rita Exemplo",
        "Sara Exemplo", "Tiago Exemplo", "Vera Exemplo", "Zeca Exemplo"
    };

    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityRoundEntryRepository entries;
    @Mock CommunityInlineSpeedHeatRepository links;
    @Mock CommunityInlineSpeedDecisionRepository decisions;
    @Mock CommunityInlineSpeedTieBreakRepository tieBreaks;
    @Mock CommunityInlineSpeedTieBreakRevisionRepository tieBreakRevisions;
    @InjectMocks CommunityInlineSpeedProgressionService service;

    private Community community;
    private CommunitySeason season;
    private CommunityTournament tournament;
    private final Map<Long, CommunityRound> roundsById = new LinkedHashMap<>();
    private final Map<Long, List<CommunityRoundEntry>> resultsByRound = new HashMap<>();
    private final List<CommunityInlineSpeedHeatLink> linked = new ArrayList<>();
    private final Map<String, CommunityInlineSpeedDecision> approved = new LinkedHashMap<>();
    private final Map<String, CommunityInlineSpeedTieBreak> rulings = new LinkedHashMap<>();
    private final Map<Long, CommunityPlayer> skaters = new HashMap<>();

    @BeforeEach
    void setUp() {
        community = Community.builder().id(7L).name("Clube QA (fictício)")
                .rankingsEnabled(false).build();
        season = CommunitySeason.builder().id(42L).community(community)
                .name("Época de ensaio 2026").active(true).build();
        tournament = CommunityTournament.builder().id(9L).community(community)
                .sport("Patinagem de Velocidade")
                .entryType(TournamentEntryType.INDIVIDUAL)
                .status(TournamentStatus.DRAFT).build();

        Profile organizer = mock(Profile.class);
        when(organizer.getId()).thenReturn(99L);
        when(profiles.findUserByUsernameIgnoreCase("qa_owner"))
                .thenReturn(Optional.of(organizer));
        CommunityMember membership = CommunityMember.builder()
                .community(community).profile(organizer).role(CommunityRole.OWNER).build();
        when(members.findByCommunityIdAndProfileId(7L, 99L))
                .thenReturn(Optional.of(membership));
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(anyLong()))
                .thenAnswer(call -> Optional.ofNullable(roundsById.get(call.getArgument(0))));
        when(rounds.findByCommunityIdOrderByPlayedAtDescIdDesc(7L))
                .thenAnswer(call -> new ArrayList<>(roundsById.values()));
        when(entries.findByRoundIdOrderByIdAsc(anyLong()))
                .thenAnswer(call -> resultsByRound.getOrDefault(call.getArgument(0), List.of()));
        when(links.findByTournamentIdOrderByIdAsc(9L))
                .thenAnswer(call -> List.copyOf(linked));
        when(links.existsByRoundId(anyLong()))
                .thenAnswer(call -> linked.stream().anyMatch(link ->
                        link.getRound().getId().equals(call.<Long>getArgument(0))));
        when(decisions.findByTournamentIdAndSourceStage(eq(9L), anyString()))
                .thenAnswer(call -> Optional.ofNullable(approved.get(call.getArgument(1))));
        when(decisions.findByTournamentIdOrderByIdAsc(9L))
                .thenAnswer(call -> new ArrayList<>(approved.values()));
        lenient().when(tieBreaks.findByTournamentIdAndSourceStage(eq(9L), anyString()))
                .thenAnswer(call -> Optional.ofNullable(rulings.get(call.getArgument(1))));
        when(tieBreaks.findByTournamentIdOrderByIdAsc(9L))
                .thenAnswer(call -> new ArrayList<>(rulings.values()));
        lenient().when(tieBreaks.save(any(CommunityInlineSpeedTieBreak.class)))
                .thenAnswer(call -> {
                    CommunityInlineSpeedTieBreak item = call.getArgument(0);
                    rulings.put(item.getSourceStage(), item);
                    return item;
                });
        when(links.save(any(CommunityInlineSpeedHeatLink.class)))
                .thenAnswer(call -> {
                    CommunityInlineSpeedHeatLink item = call.getArgument(0);
                    linked.add(item);
                    return item;
                });
        when(decisions.save(any(CommunityInlineSpeedDecision.class)))
                .thenAnswer(call -> {
                    CommunityInlineSpeedDecision item = call.getArgument(0);
                    approved.put(item.getSourceStage(), item);
                    return item;
                });
    }

    private CommunityRound record(long roundId, String type, String title,
                                  long[] playerIds, boolean withPositions) {
        CommunityRound round = CommunityRound.builder().id(roundId)
                .community(community).season(season).sport("Patinagem de Velocidade")
                .name(title).eventType(type).playedAt(Instant.parse("2026-10-25T09:00:00Z"))
                .createdAt(Instant.parse("2026-10-25T09:01:00Z")).build();
        List<CommunityRoundEntry> observations = new ArrayList<>();
        for (int i = 0; i < playerIds.length; i++) {
            long id = playerIds[i];
            CommunityPlayer player = skaters.computeIfAbsent(id, k ->
                    CommunityPlayer.builder().id(k).community(community)
                            .displayName(NAMES[(int) (k - 1)]).build());
            observations.add(CommunityRoundEntry.builder()
                    .id(roundId * 100 + i + 1).round(round).communityPlayer(player)
                    .finishPosition(withPositions ? i + 1 : null)
                    .elapsedSeconds(withPositions ? null :
                            BigDecimal.valueOf(30).add(BigDecimal.valueOf(id, 2)))
                    .build());
        }
        roundsById.put(roundId, round);
        resultsByRound.put(roundId, observations);
        return round;
    }

    private static long[] ids(long... values) { return values; }
    private static List<Long> all(long from, long to) {
        return LongStream.rangeClosed(from, to).boxed().toList();
    }
    private static void hasStatus(HttpStatus expected, Executable action) {
        assertEquals(expected, assertThrows(ResponseStatusException.class, action)
                .getStatusCode());
    }

    @Test
    void fictionalSkatersTravelFromTimedRoundViaTwoSemisToFinalWithExplicitJudges() {
        record(100, FIRST, "500 m +D — primeira fase",
                LongStream.rangeClosed(1, 12).toArray(), false);
        var linkedFirst = service.link(7L, 9L, 100, 1, "qa_owner");

        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        assertEquals("Ana Exemplo", linkedFirst.linkedHeats().get(0).skaters().get(0).name());
        var firstProposal = linkedFirst.proposals().get(0);
        assertTrue(firstProposal.ready());
        assertEquals("SEMI", firstProposal.targetStage());
        assertEquals(all(1, 8), firstProposal.playerIds());
        assertTrue(linkedFirst.decisions().isEmpty(), "the service must never auto-ratify");
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 8), ""), "qa_owner"));

        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 7), "Justificação para lista curta"), "qa_owner"));
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 9), "Justificação para lista longa"), "qa_owner"));

        var firstDecision = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(all(1, 8),
                        "O júri atesta que todas as mangas da primeira fase foram registadas"),
                "qa_owner");
        assertEquals(all(1, 8), firstDecision.decisions().get(0).playerIds());
        assertFalse(firstDecision.decisions().get(0).stale());

        record(190, SEMI, "Manga inválida (atleta não apurado)",
                ids(1, 2, 3, 9), true);
        hasStatus(HttpStatus.CONFLICT,
                () -> service.link(7L, 9L, 190, 1, "qa_owner"));
        assertEquals(1, linked.size(), "invalid semifinal must not be saved");

        record(201, SEMI, "Meia-final 1",
                ids(1, 2, 3, 4), true);
        record(202, SEMI, "Meia-final 2",
                ids(5, 6, 7, 8), true);
        var onlyOne = service.link(7L, 9L, 201, 1, "qa_owner");
        assertFalse(onlyOne.proposals().stream()
                .filter(p -> p.sourceStage().equals("SEMI")).findFirst().orElseThrow().ready());
        var two = service.link(7L, 9L, 202, 2, "qa_owner");
        var semifinalProposal = two.proposals().stream()
                .filter(p -> p.sourceStage().equals("SEMI")).findFirst().orElseThrow();
        assertTrue(semifinalProposal.ready());
        assertEquals(List.of(1L, 2L, 5L, 6L), semifinalProposal.playerIds());
        assertEquals(1, two.decisions().size(), "semifinal is not automatically approved");

        service.confirm(7L, 9L, "SEMI",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        semifinalProposal.playerIds(), ""), "qa_owner");
        record(301, FINAL, "Final A — 500 m +D",
                ids(1, 5, 2, 6), true);
        var finalSnapshot = service.link(7L, 9L, 301, 1, "qa_owner");
        assertEquals(4, finalSnapshot.linkedHeats().size());
        assertEquals(2, finalSnapshot.decisions().size());
        assertEquals("Ana Exemplo", finalSnapshot.linkedHeats().get(3).skaters().get(0).name());
        assertEquals(Integer.valueOf(1), resultsByRound.get(301L).get(0).getFinishPosition());
        assertEquals(new BigDecimal("30.01"),
                resultsByRound.get(100L).get(0).getElapsedSeconds());
        assertTrue(community.isRankingsEnabled() == false, "community W/D/L remains off");
        verify(rounds, never()).save(any());
        verify(entries, never()).save(any());
    }

    @Test
    void disqualificationNeverBecomesAnApprovedManualQualifier() {
        record(100, FIRST, "Manga fictícia com desclassificação",
                LongStream.rangeClosed(1, 12).toArray(), false);
        resultsByRound.get(100L).get(7).setRaceDisqualified(true);
        var first = service.link(7L, 9L, 100, 1, "qa_owner");
        assertEquals(8, first.proposals().get(0).playerIds().size());
        assertFalse(first.proposals().get(0).playerIds().contains(8L));
        hasStatus(HttpStatus.CONFLICT, () -> service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L),
                        "O juiz tenta incluir um patinador desclassificado"),
                "qa_owner"));
        assertTrue(approved.isEmpty());
        // Officials must update the original result before reconsidering the entry.
        resultsByRound.get(100L).get(7).setRaceDisqualified(false);
        var corrected = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(all(1, 8),
                        "O juiz reviu a desclassificação na manga de origem"), "qa_owner");
        assertEquals(all(1, 8), corrected.decisions().get(0).playerIds());
    }

    @Test
    void additionalSeventeenthRequiresDetailedJudgeDecisionAndFiveInQuarterOne() {
        record(100, FIRST, "Vinte patinadores e circunstância injusta",
                LongStream.rangeClosed(1, 20).toArray(), false);
        var overview = service.link(7L, 9L, 100, 1, "qa_owner");
        var proposal = overview.proposals().get(0);
        assertEquals("QUARTER", proposal.targetStage());
        assertEquals(16, proposal.playerIds().size());
        List<Long> approvedSeventeen = new ArrayList<>(all(1, 17));
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        approvedSeventeen, "Júri decide"), "qa_owner"));
        var ratified = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        approvedSeventeen,
                        "Júri inclui adicionalmente o atleta 17 por uma circunstância injusta"),
                "qa_owner");
        assertEquals(17, ratified.decisions().get(0).playerIds().size());
        record(201, "INLINE_TRACK_500D_QUARTER_HEAT", "Quarto 1 incompleto",
                ids(1, 2, 3, 4), true);
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY,
                () -> service.link(7L, 9L, 201, 1, "qa_owner"));
        record(202, "INLINE_TRACK_500D_QUARTER_HEAT", "Quarto 1 com cinco",
                ids(1, 2, 3, 4, 17), true);
        var valid = service.link(7L, 9L, 202, 1, "qa_owner");
        assertEquals(2, valid.linkedHeats().size());
        record(203, "INLINE_TRACK_500D_QUARTER_HEAT", "Quarto 2 grande",
                ids(5, 6, 7, 8, 9), true);
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY,
                () -> service.link(7L, 9L, 203, 2, "qa_owner"));
    }

    @Test
    void tieBetweenSixteenthAndSeventeenthAllowsSeventeenButNotEighteen() {
        record(100, FIRST, "20 tempos, empate no corte",
                LongStream.rangeClosed(1, 20).toArray(), false);
        resultsByRound.get(100L).get(16).setElapsedSeconds(new BigDecimal("30.16"));
        var proposal = service.link(7L, 9L, 100, 1, "qa_owner").proposals().get(0);
        assertTrue(proposal.ready());
        assertEquals(17, proposal.playerIds().size());
        assertEquals(16, proposal.expectedCount());
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 18), "O juiz confirma o empate existente"), "qa_owner"));
        var decision = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 17), "O juiz confirma o empate entre 16.º e 17.º"), "qa_owner");
        assertEquals(17, decision.decisions().get(0).playerIds().size());
    }

    @Test
    void aJuryResolvesThreeWayCutoffWithoutEditingOriginalTimes() {
        for (int heat = 0; heat < 4; heat++) {
            long[] ids = LongStream.rangeClosed(1 + heat * 5, 5 + heat * 5).toArray();
            record(100 + heat, FIRST, "Primeira fase " + (heat + 1), ids, false);
        }
        // The 16th, 17th and 18th share the same recorded time.
        for (long id : new long[]{17, 18}) {
            long roundId = 100 + (id - 1) / 5;
            resultsByRound.get(roundId).stream()
                    .filter(e -> e.getCommunityPlayer().getId().equals(id))
                    .findFirst().orElseThrow().setElapsedSeconds(new BigDecimal("30.16"));
        }
        CommunityInlineSpeedProgressionService.Overview state = null;
        for (int heat = 0; heat < 4; heat++)
            state = service.link(7L, 9L, 100 + heat, heat + 1, "qa_owner");
        assertNotNull(state);
        assertFalse(state.proposals().get(0).ready());
        assertTrue(state.proposals().get(0).warning().contains("Empate múltiplo"));
        hasStatus(HttpStatus.CONFLICT, () -> service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 16), "A primeira fase está completa"), "qa_owner"));
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.resolveTieBreak(
                7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(2, 17), "O júri observou um desempate oficial documentado"),
                "qa_owner"));
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.resolveTieBreak(
                7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 16), "Curto"), "qa_owner"));
        var tied = service.resolveTieBreak(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 16),
                        "O júri decidiu o desempate do 16.º lugar após rever a chegada oficial"),
                "qa_owner");
        assertEquals(1, tied.tieBreaks().size());
        assertEquals("qa_owner", tied.tieBreaks().get(0).confirmedBy());
        assertTrue(tied.proposals().get(0).ready());
        assertEquals(all(1, 16), tied.proposals().get(0).playerIds());
        assertEquals(new BigDecimal("30.16"),
                resultsByRound.get(103L).get(1).getElapsedSeconds());
        assertTrue(approved.isEmpty(), "Tie resolution cannot auto-confirm the qualification");
        var confirmed = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 16), "O júri confirma o apuramento após desempate registado"),
                "qa_owner");
        assertEquals(1, confirmed.decisions().size());
        hasStatus(HttpStatus.CONFLICT, () -> service.resolveTieBreak(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        all(1, 15).stream().toList(), "Não é permitido reabrir após confirmação"),
                "qa_owner"));
        // Editing a source time invalidates both the ruling and its proposal.
        resultsByRound.get(100L).get(0).setElapsedSeconds(new BigDecimal("30.02"));
        when(members.existsByCommunityIdAndProfileId(7L, 99L)).thenReturn(true);
        when(tournaments.findById(9L)).thenReturn(Optional.of(tournament));
        var stale = service.overview(7L, 9L, "qa_owner");
        assertTrue(stale.tieBreaks().get(0).stale());
        assertFalse(stale.proposals().get(0).ready());
        assertTrue(stale.decisions().get(0).stale());
    }

    @Test
    void correctedJudgeSelectionIsAuditedAndEditedSourceBlocksLaterRound() {
        record(100, FIRST, "Primeira fase", LongStream.rangeClosed(1, 12).toArray(), false);
        service.link(7L, 9L, 100, 1, "qa_owner");
        List<Long> corrected = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 9L);
        hasStatus(HttpStatus.UNPROCESSABLE_ENTITY, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        corrected, ""), "qa_owner"));
        hasStatus(HttpStatus.CONFLICT, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 99L),
                        "Júri tenta apurar alguém sem resultado de origem"), "qa_owner"));
        // JSON arrays and Java collections can be mutable OR immutable.
        // A null participant is a validation error, never an unexpected NPE.
        hasStatus(HttpStatus.BAD_REQUEST, () -> service.confirm(7L, 9L,
                "FIRST", new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        Arrays.asList(1L, null, 3L),
                        "Erro no formulário de confirmação do júri"), "qa_owner"));
        var overview = service.confirm(7L, 9L, "FIRST",
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        corrected, "Decisão fundamentada do júri: patinador 9 substitui 8"),
                "qa_owner");
        assertEquals(corrected, overview.decisions().get(0).playerIds());
        assertEquals("qa_owner", overview.decisions().get(0).confirmedBy());
        assertFalse(overview.decisions().get(0).stale());
        record(201, SEMI, "Meia-final com atleta excluído", ids(1, 2, 3, 8), true);
        hasStatus(HttpStatus.CONFLICT, () -> service.link(7L, 9L, 201, 1, "qa_owner"));

        // A timing correction must mark a decision as stale, not silently update it.
        resultsByRound.get(100L).get(0).setElapsedSeconds(new BigDecimal("31.99"));
        record(202, SEMI, "Meia-final após alterar cronómetro", ids(1, 2, 3, 9), true);
        hasStatus(HttpStatus.CONFLICT, () -> service.link(7L, 9L, 202, 1, "qa_owner"));
        assertEquals(1, linked.size());
        assertEquals(1, approved.size());
    }
}
