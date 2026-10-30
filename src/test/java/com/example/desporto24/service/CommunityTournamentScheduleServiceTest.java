package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentScheduleServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentParticipantRepository participants;
    @Mock CommunityTournamentMatchRepository matches;
    @InjectMocks CommunityTournamentScheduleService service;

    private final Profile owner = profile(1L, "owner");
    private final Community community = Community.builder().id(7L).name("Liga")
            .visibility(CommunityVisibility.PRIVATE).owner(owner).build();
    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(9L).name("Liga semanal").community(community).build();

    private static Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private void manager(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(
                Optional.of(CommunityMember.builder().community(community)
                        .profile(owner).role(role).build()));
    }

    private void lockedTournament() {
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
    }

    private List<CommunityTournamentParticipant> roster(int count) {
        var entries = new ArrayList<CommunityTournamentParticipant>();
        for (long id = 1; id <= count; id++) {
            entries.add(CommunityTournamentParticipant.builder()
                    .id(id).profile(profile(id, "player-" + id))
                    .tournament(tournament).status(TournamentParticipantStatus.CONFIRMED)
                    .joinedAt(Instant.now()).build());
        }
        return entries;
    }

    private void confirmed(List<CommunityTournamentParticipant> entries) {
        var request = PageRequest.of(0, 33, Sort.by("id"));
        when(participants.findByTournamentIdAndStatus(9L, TournamentParticipantStatus.CONFIRMED, request))
                .thenReturn(new SliceImpl<>(entries, request, false));
    }

    private List<CommunityTournamentMatch> savedMatches() {
        var saved = new ArrayList<CommunityTournamentMatch>();
        when(matches.saveAll(any())).thenAnswer(invocation -> {
            Iterable<CommunityTournamentMatch> items = invocation.getArgument(0);
            items.forEach(saved::add);
            return saved;
        });
        return saved;
    }

    @Test
    void fourPlayersProduceEveryPairOnceInThreeRoundsAndStartTournament() {
        manager(CommunityRole.OWNER);
        lockedTournament();
        confirmed(roster(4));
        when(members.existsByCommunityIdAndProfileId(eq(7L), anyLong())).thenReturn(true);
        var scheduled = savedMatches();

        var summary = service.generate(7L, 9L, "owner");

        assertEquals(4, summary.participantCount());
        assertEquals(3, summary.roundCount());
        assertEquals(6, summary.matchCount());
        assertEquals(TournamentStatus.IN_PROGRESS, summary.status());
        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        assertNotNull(tournament.getStartedAt());
        assertEquals(6, scheduled.size());

        Set<String> pairs = new HashSet<>();
        for (int round = 1; round <= 3; round++) {
            final int currentRound = round;
            assertEquals(2, scheduled.stream().filter(m -> m.getRoundNumber() == currentRound).count());
        }
        for (var match : scheduled) {
            long first = match.getHomeParticipant().getProfile().getId();
            long second = match.getAwayParticipant().getProfile().getId();
            assertNotEquals(first, second);
            pairs.add(Math.min(first, second) + "-" + Math.max(first, second));
        }
        assertEquals(6, pairs.size());
        verify(tournaments).findByIdForUpdate(9L);
        verify(tournaments).save(tournament);
    }

    @Test
    void thirtyTwoIndividualPlayersGenerateCompleteLeagueWithoutDuplicateGamesOrDoubleBookings() {
        manager(CommunityRole.OWNER);
        lockedTournament();
        confirmed(roster(32));
        when(members.existsByCommunityIdAndProfileId(eq(7L), anyLong())).thenReturn(true);
        var saved = savedMatches();
        var summary = service.generate(7L, 9L, "owner");

        assertEquals(32, summary.participantCount());
        assertEquals(31, summary.roundCount());
        assertEquals(496, summary.matchCount());
        assertEquals(496, saved.size());
        Set<String> distinctPairs = new HashSet<>();
        for (int round = 1; round <= 31; round++) {
            Set<Long> active = new HashSet<>();
            int fixtures = 0;
            for (var match : saved) {
                if (match.getRoundNumber() != round) continue;
                fixtures++;
                long a = match.getHomeParticipant().getProfile().getId();
                long b = match.getAwayParticipant().getProfile().getId();
                assertTrue(active.add(a), "Player booked twice in round " + round);
                assertTrue(active.add(b), "Player booked twice in round " + round);
                assertTrue(distinctPairs.add(Math.min(a,b) + ":" + Math.max(a,b)),
                        "Duplicate head-to-head pairing");
            }
            assertEquals(16, fixtures, "Each round needs sixteen independent matches");
            assertEquals(32, active.size(), "Every confirmed player should compete in each round");
        }
        assertEquals(496, distinctPairs.size());
    }

    @Test
    void thirtyOneIndividualPlayersReceiveExactlyOneByePerRound() {
        manager(CommunityRole.ADMIN);
        lockedTournament();
        confirmed(roster(31));
        when(members.existsByCommunityIdAndProfileId(eq(7L), anyLong())).thenReturn(true);
        var saved = savedMatches();
        var summary = service.generate(7L, 9L, "owner");

        assertEquals(31, summary.roundCount());
        assertEquals(465, summary.matchCount());
        Set<String> pairs = new HashSet<>();
        for (int round = 1; round <= 31; round++) {
            Set<Long> playersThisRound = new HashSet<>();
            for (var match : saved) {
                if (match.getRoundNumber() != round) continue;
                long a = match.getHomeParticipant().getProfile().getId();
                long b = match.getAwayParticipant().getProfile().getId();
                assertTrue(playersThisRound.add(a));
                assertTrue(playersThisRound.add(b));
                assertTrue(pairs.add(Math.min(a,b) + ":" + Math.max(a,b)));
            }
            assertEquals(30, playersThisRound.size(),
                    "Exactly one player rests each round");
        }
        assertEquals(465, pairs.size());
        for (long id = 1; id <= 31; id++) {
            final long playerId = id;
            assertEquals(30, saved.stream().filter(m ->
                    m.getHomeParticipant().getProfile().getId() == playerId
                    || m.getAwayParticipant().getProfile().getId() == playerId).count());
        }
    }

    @Test
    void fivePlayersGetOneByePerRoundAndEveryPairOnce() {
        manager(CommunityRole.ADMIN);
        lockedTournament();
        confirmed(roster(5));
        when(members.existsByCommunityIdAndProfileId(eq(7L), anyLong())).thenReturn(true);
        var scheduled = savedMatches();

        var result = service.generate(7L, 9L, "owner");

        assertEquals(5, result.roundCount());
        assertEquals(10, result.matchCount());
        assertEquals(10, scheduled.size());
        Set<String> pairs = new HashSet<>();
        for (var match : scheduled) {
            long a = match.getHomeParticipant().getProfile().getId();
            long b = match.getAwayParticipant().getProfile().getId();
            pairs.add(Math.min(a, b) + "-" + Math.max(a, b));
        }
        assertEquals(10, pairs.size());
        for (int round = 1; round <= 5; round++) {
            final int currentRound = round;
            assertEquals(2, scheduled.stream().filter(m -> m.getRoundNumber() == currentRound).count());
        }
        for (long player = 1; player <= 5; player++) {
            final long current = player;
            assertEquals(4, scheduled.stream().filter(m ->
                    m.getHomeParticipant().getProfile().getId() == current ||
                    m.getAwayParticipant().getProfile().getId() == current).count());
        }
    }

    @Test
    void memberCannotGenerateOrSeeDraftScheduleChanges() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () -> service.generate(7L, 9L, "owner"));
        verifyNoInteractions(tournaments, matches, participants);
    }

    @Test
    void repeatedScheduleGenerationConflictsBeforeLoadingRoster() {
        manager(CommunityRole.ADMIN);
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        lockedTournament();
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 9L, "owner")).getStatusCode().value());
        verifyNoInteractions(matches, participants);
    }

    @Test
    void existingMatchesBlockRegenerationEvenWhenTournamentIsDraft() {
        manager(CommunityRole.OWNER);
        lockedTournament();
        when(matches.existsByTournamentId(9L)).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 9L, "owner")).getStatusCode().value());
        verify(participants, never()).findByTournamentIdAndStatus(any(), any(), any());
    }

    @Test
    void rejectSingleConfirmedPlayerWithoutStartingTournament() {
        manager(CommunityRole.OWNER);
        lockedTournament();
        confirmed(roster(1));
        assertEquals(422, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 9L, "owner")).getStatusCode().value());
        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
        verify(matches, never()).saveAll(any());
    }

    @Test
    void removedMemberBlocksCalendarInsteadOfSchedulingThem() {
        manager(CommunityRole.OWNER);
        lockedTournament();
        confirmed(roster(2));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 9L, "owner")).getStatusCode().value());
        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
        verify(matches, never()).saveAll(any());
    }

    @Test
    void foreignCommunityTournamentIsInvisibleEvenToManager() {
        manager(CommunityRole.OWNER);
        tournament.setCommunity(Community.builder().id(44L).build());
        lockedTournament();
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.generate(7L, 9L, "owner")).getStatusCode().value());
        verifyNoInteractions(matches, participants);
    }

    @Test
    void scheduleReadIsMemberOnlyAndPagesByRoundAndId() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournaments.findById(9L)).thenReturn(Optional.of(tournament));
        var pageRequest = PageRequest.of(1, 30,
                Sort.by(Sort.Direction.ASC, "roundNumber").and(Sort.by(Sort.Direction.ASC, "id")));
        var match = CommunityTournamentMatch.builder()
                .id(20L).tournament(tournament).roundNumber(2)
                .homeParticipant(roster(2).get(0)).awayParticipant(roster(2).get(1)).build();
        when(matches.findByTournamentId(9L, pageRequest))
                .thenReturn(new SliceImpl<>(List.of(match), pageRequest, false));

        var result = service.matches(7L, 9L, "owner", 1);

        assertEquals(1, result.page());
        assertEquals(1, result.matches().size());
        assertEquals(2, result.matches().get(0).roundNumber());
        assertEquals("player-1", result.matches().get(0).homeUsername());
        assertEquals("player-2", result.matches().get(0).awayUsername());
    }

    @Test
    void outsiderCannotReadSchedule() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        assertThrows(AccessDeniedException.class, () -> service.matches(7L, 9L, "owner", 0));
        verifyNoInteractions(matches, tournaments);
    }

    @Test
    void negativePageRejectedWithoutRepositoryQueries() {
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.matches(7L, 9L, "owner", -1)).getStatusCode().value());
        verifyNoInteractions(matches, tournaments, members, profiles);
    }
}
