package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentTeamTieBreakPriority;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentServiceTest {
    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunityTournamentRepository tournamentRepository;
    @Mock private CommunityTournamentParticipantRepository participantRepository;
    @Mock private CommunityTournamentMatchRepository matchRepository;
    @Mock private ProfileRepository profileRepository;
    @InjectMocks private CommunityTournamentService service;

    private final Profile owner = ownerProfile();

    private static Profile ownerProfile() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("owner");
        return profile;
    }
    private final Community community = Community.builder().id(7L).name("CNG")
            .visibility(CommunityVisibility.PRIVATE).owner(owner).createdAt(Instant.now()).build();

    private void actingAsOwner(CommunityRole role) {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(owner).role(role).build()));
    }

    @Test
    void ownerCreatesDraftLeagueUsingDefaultsAndKeepsExplicitZero() {
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class))).thenAnswer(invocation -> {
            CommunityTournament tournament = invocation.getArgument(0);
            tournament.setId(19L);
            return tournament;
        });

        var created = service.create(7L, "owner",
                new CreateCommunityTournamentRequest("  Torneio da Praia  ", 0, null, null));

        assertEquals(19L, created.id());
        assertEquals("Torneio da Praia", created.name());
        assertEquals(TournamentFormat.LEAGUE, created.format());
        assertEquals(TournamentStatus.DRAFT, created.status());
        assertEquals(0, created.winPoints());
        assertEquals(1, created.drawPoints());
        assertEquals(0, created.lossPoints());
        assertNotNull(created.createdAt());
    }


    @Test
    void newTournamentUsesItsOwnSportIndependentlyOfCommunitySports() {
        community.setSport("Futebol");
        community.setSports(new java.util.ArrayList<>(List.of("Futebol", "Padel")));
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        var created = service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga Natação", null, null, null, "  Natação  "));

        assertEquals("Natação", created.sport());
        var captor = org.mockito.ArgumentCaptor.forClass(CommunityTournament.class);
        verify(tournamentRepository).save(captor.capture());
        assertEquals("Natação", captor.getValue().getSport());
    }

    @Test
    void legacyCreationSnapshotsCommunityPrimarySportOrLeavesSportless() {
        community.setSport("Padel");
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        assertEquals("Padel", service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga antiga", null, null, null)).sport());
        community.setSport(null);
        assertNull(service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga aberta", null, null, null)).sport());
    }

    @Test
    void rejectsBlankExplicitSportBeforeSaving() {
        actingAsOwner(CommunityRole.OWNER);
        var exception = assertThrows(ResponseStatusException.class, () -> service.create(
                7L, "owner", new CreateCommunityTournamentRequest("Liga", null, null, null, "  ")));
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verifyNoInteractions(tournamentRepository);
    }

    @Test
    void readingTournamentUsesItsOwnStoredSportNotCurrentCommunitySport() {
        community.setVisibility(CommunityVisibility.PUBLIC);
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        var tournament = CommunityTournament.builder().id(21L).community(community)
                .name("Liga de Ténis").sport("Ténis").createdAt(Instant.now()).build();
        var pageable = PageRequest.of(0, 20,
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        when(tournamentRepository.findByCommunityId(7L, pageable))
                .thenReturn(new SliceImpl<>(List.of(tournament), pageable, false));
        community.setSport("Futebol");

        assertEquals("Ténis", service.list(7L, "owner", 0).tournaments().get(0).sport());
    }


    @Test
    void organizerSetsTwoLegRuleAtTeamCreationAndLegacyDefaultsRemainOne() {
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var two = service.create(7L, "owner", new CreateCommunityTournamentRequest(
                "Liga casa e fora", null, null, null, "Futebol", TournamentEntryType.TEAM, 5, 2));
        assertEquals(2, two.roundRobinLegs());
        var one = service.create(7L, "owner", new CreateCommunityTournamentRequest(
                "Liga simples", null, null, null, "Futebol", TournamentEntryType.TEAM, 5));
        assertEquals(1, one.roundRobinLegs());
    }

    @Test
    void organizerSetsTeamTieBreakPriorityAndExistingTeamsKeepDefault() {
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var chosen = service.create(7L, "owner", new CreateCommunityTournamentRequest(
                "Marcador primeiro", 3, 1, 0, "Futebol", TournamentEntryType.TEAM,
                5, 2, TournamentTeamTieBreakPriority.GOALS_SCORED_FIRST));
        assertEquals(TournamentTeamTieBreakPriority.GOALS_SCORED_FIRST,
                chosen.teamTieBreakPriority());
        var legacy = service.create(7L, "owner", new CreateCommunityTournamentRequest(
                "Liga tradicional", null, null, null, "Futebol", TournamentEntryType.TEAM, 5, 1));
        assertEquals(TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST,
                legacy.teamTieBreakPriority());
    }

    @Test
    void individualCompetitionsRejectTeamOnlyTieBreakRule() {
        actingAsOwner(CommunityRole.OWNER);
        var request = new CreateCommunityTournamentRequest(
                "Liga individual", 3, 1, 0, "Ténis", TournamentEntryType.INDIVIDUAL,
                null, null, TournamentTeamTieBreakPriority.WINS_FIRST);
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> service.create(7L, "owner", request)).getStatusCode());
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void rejectsUnsupportedNumberOfRoundsOrIndividualRule() {
        actingAsOwner(CommunityRole.OWNER);
        for (var value : List.of(0, 3)) {
            var invalid = new CreateCommunityTournamentRequest(
                    "Liga", null, null, null, "Futebol", TournamentEntryType.TEAM, 5, value);
            assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,
                    assertThrows(ResponseStatusException.class,
                            () -> service.create(7L, "owner", invalid)).getStatusCode());
        }
        var individual = new CreateCommunityTournamentRequest(
                "Liga", null, null, null, "Ténis", TournamentEntryType.INDIVIDUAL, null, 2);
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> service.create(7L, "owner", individual)).getStatusCode());
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void teamsAreCreatedAsASeparateParticipationType() {
        actingAsOwner(CommunityRole.OWNER);
        when(tournamentRepository.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var created = service.create(7L, "owner", new CreateCommunityTournamentRequest(
                "Liga 5x5", null, null, null, "Futebol", TournamentEntryType.TEAM, 5));
        assertEquals(TournamentEntryType.TEAM, created.entryType());
        assertEquals(5, created.playersPerTeam());
        assertEquals(TournamentStatus.DRAFT, created.status());
    }

    @Test
    void teamCompetitionsRequireTheirOwnSportAndRosterSize() {
        actingAsOwner(CommunityRole.OWNER);
        var noSize = new CreateCommunityTournamentRequest(
                "Liga", null, null, null, "Futebol", TournamentEntryType.TEAM, null);
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> service.create(7L, "owner", noSize)).getStatusCode());
        var noSport = new CreateCommunityTournamentRequest(
                "Liga", null, null, null, null, TournamentEntryType.TEAM, 5);
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> service.create(7L, "owner", noSport)).getStatusCode());
        verifyNoInteractions(tournamentRepository);
    }

    @Test
    void legacyRequestsRemainIndividual() {
        actingAsOwner(CommunityRole.ADMIN);
        when(tournamentRepository.save(any(CommunityTournament.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var old = service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga antiga", null, null, null));
        assertEquals(TournamentEntryType.INDIVIDUAL, old.entryType());
        assertNull(old.playersPerTeam());
    }

    @Test
    void adminCanCreateDraftLeague() {
        actingAsOwner(CommunityRole.ADMIN);
        when(tournamentRepository.save(any(CommunityTournament.class))).thenAnswer(invocation ->
                invocation.getArgument(0));
        var created = service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga", 5, 2, 1));
        assertEquals(5, created.winPoints());
        assertEquals(2, created.drawPoints());
        assertEquals(1, created.lossPoints());
    }

    @Test
    void memberCannotCreateATournament() {
        actingAsOwner(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () -> service.create(7L, "owner",
                new CreateCommunityTournamentRequest("Liga", null, null, null)));
        verifyNoInteractions(tournamentRepository);
    }

    @Test
    void outsiderCannotReadPrivateCommunityTournaments() {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        assertThrows(AccessDeniedException.class, () -> service.list(7L, "owner", 0));
        verifyNoInteractions(tournamentRepository);
    }

    @Test
    void publicCommunityAllowsBoundedSortedReadWithHasMore() {
        community.setVisibility(CommunityVisibility.PUBLIC);
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        var pageable = PageRequest.of(1, 20,
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        CommunityTournament tournament = CommunityTournament.builder()
                .id(19L).community(community).name("Liga")
                .createdAt(Instant.parse("2026-10-16T12:00:00Z")).build();
        when(tournamentRepository.findByCommunityId(7L, pageable))
                .thenReturn(new SliceImpl<>(List.of(tournament), pageable, true));

        var result = service.list(7L, "owner", 1);
        assertEquals(1, result.page());
        assertTrue(result.hasMore());
        assertEquals(19L, result.tournaments().get(0).id());
        assertEquals(TournamentStatus.DRAFT, result.tournaments().get(0).status());
        verify(memberRepository, never()).existsByCommunityIdAndProfileId(any(), any());
    }


    @Test
    void ownerCanRemoveUnscoredInProgressTournamentAndItsDependencies() {
        actingAsOwner(CommunityRole.OWNER);
        var tournament = CommunityTournament.builder()
                .id(19L).name("Ensaio").community(community)
                .status(TournamentStatus.IN_PROGRESS).createdAt(Instant.now()).build();
        when(tournamentRepository.findByIdForUpdate(19L)).thenReturn(Optional.of(tournament));
        var scheduled = CommunityTournamentMatch.builder().id(45L).tournament(tournament)
                .status(TournamentMatchStatus.SCHEDULED).build();
        when(matchRepository.findByTournamentIdOrderByRoundNumberAscIdAsc(19L))
                .thenReturn(List.of(scheduled));

        service.delete(7L, 19L, "owner");

        var order = inOrder(matchRepository, participantRepository, tournamentRepository);
        order.verify(matchRepository).deleteAll(List.of(scheduled));
        order.verify(matchRepository).flush();
        order.verify(participantRepository).findByTournamentIdOrderByIdAsc(19L);
        order.verify(participantRepository).deleteAll(List.of());
        order.verify(participantRepository).flush();
        order.verify(tournamentRepository).delete(tournament);
    }

    @Test
    void refusesDeletingTournamentOfAnotherCommunityBeforeReadingItsMatches() {
        actingAsOwner(CommunityRole.ADMIN);
        var other = Community.builder().id(8L).build();
        when(tournamentRepository.findByIdForUpdate(19L)).thenReturn(Optional.of(
                CommunityTournament.builder().id(19L).community(other).build()));

        var ex = assertThrows(ResponseStatusException.class, () -> service.delete(7L, 19L, "owner"));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
        verifyNoInteractions(matchRepository, participantRepository);
        verify(tournamentRepository, never()).delete(any());
    }

    @Test
    void memberCannotDeleteTournament() {
        actingAsOwner(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () -> service.delete(7L, 19L, "owner"));
        verifyNoInteractions(matchRepository, participantRepository, tournamentRepository);
    }

    @Test
    void refusesDeletingCompletedTournamentOrOneWithRecordedResults() {
        actingAsOwner(CommunityRole.OWNER);
        var tournament = CommunityTournament.builder().id(19L).community(community)
                .status(TournamentStatus.COMPLETED).build();
        when(tournamentRepository.findByIdForUpdate(19L)).thenReturn(Optional.of(tournament));
        var error = assertThrows(ResponseStatusException.class,
                () -> service.delete(7L, 19L, "owner"));
        assertEquals(org.springframework.http.HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(matchRepository, participantRepository);

        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        when(matchRepository.findByTournamentIdOrderByRoundNumberAscIdAsc(19L))
                .thenReturn(List.of(CommunityTournamentMatch.builder()
                        .status(TournamentMatchStatus.COMPLETED).homeScore(2).awayScore(1).build()));
        error = assertThrows(ResponseStatusException.class,
                () -> service.delete(7L, 19L, "owner"));
        assertEquals(org.springframework.http.HttpStatus.CONFLICT, error.getStatusCode());
        verify(matchRepository, never()).deleteAll(anyList());
        verify(tournamentRepository, never()).delete(any());
    }

    @Test
    void negativePageIsRejectedBeforeAnyDatabaseCalls() {
        assertThrows(ResponseStatusException.class, () -> service.list(7L, "owner", -1));
        verifyNoInteractions(tournamentRepository, memberRepository, communityRepository, profileRepository);
    }
}
