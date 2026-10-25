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
        "Inês Exemplo", "João Exemplo", "Lara Exemplo", "Marco Exemplo"
    };

    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityRoundEntryRepository entries;
    @Mock CommunityInlineSpeedHeatRepository links;
    @Mock CommunityInlineSpeedDecisionRepository decisions;
    @InjectMocks CommunityInlineSpeedProgressionService service;

    private Community community;
    private CommunitySeason season;
    private CommunityTournament tournament;
    private final Map<Long, CommunityRound> roundsById = new LinkedHashMap<>();
    private final Map<Long, List<CommunityRoundEntry>> resultsByRound = new HashMap<>();
    private final List<CommunityInlineSpeedHeatLink> linked = new ArrayList<>();
    private final Map<String, CommunityInlineSpeedDecision> approved = new LinkedHashMap<>();
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
    private static void hasStatus(HttpStatus expected, Runnable action) {
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
