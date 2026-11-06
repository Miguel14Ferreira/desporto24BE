package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityTournamentEligibilityServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityTournamentRepository tournaments = mock(CommunityTournamentRepository.class);
    private final CommunityTournamentParticipantRepository participants = mock(CommunityTournamentParticipantRepository.class);
    private final CommunityTournamentTeamPlayerRepository teamPlayers = mock(CommunityTournamentTeamPlayerRepository.class);
    private final CommunityTournamentSuspensionRepository suspensions = mock(CommunityTournamentSuspensionRepository.class);
    private final CommunityTournamentEligibilityService service = new CommunityTournamentEligibilityService(
            profiles, members, tournaments, participants, teamPlayers, suspensions);

    private final Profile owner = profile(10L);
    private final Profile player = profile(11L);
    private final Profile other = profile(12L);
    private final Community community = Community.builder().id(7L).build();

    private static Profile profile(long id) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername("user" + id);
        return profile;
    }

    private CommunityTournament tournament(TournamentEntryType entryType) {
        return CommunityTournament.builder()
                .id(19L).community(community).entryType(entryType).build();
    }

    @BeforeEach
    void setup() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(7L, 10L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.OWNER).build()));
    }

    @Test
    void detectsAlreadyConfirmedSuspendedIndividualWithoutRemovingEnrollment() {
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament(TournamentEntryType.INDIVIDUAL)));
        when(participants.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(
                CommunityTournamentParticipant.builder().profile(player)
                        .status(TournamentParticipantStatus.CONFIRMED).build(),
                CommunityTournamentParticipant.builder().profile(other)
                        .status(TournamentParticipantStatus.PENDING).build()));
        Instant end = Instant.now().plusSeconds(3600);
        when(suspensions.findActiveForProfiles(eq(7L), eq(List.of(11L, 12L)), any(Instant.class)))
                .thenReturn(List.of(CommunityTournamentSuspension.builder()
                        .communityId(7L).targetProfileId(11L).endsAt(end).build()));

        var result = service.overview(7L, 19L, "owner");
        assertEquals(TournamentEntryType.INDIVIDUAL, result.entryType());
        assertEquals(1, result.ineligibleEntries().size());
        var blocked = result.ineligibleEntries().getFirst();
        assertEquals(11L, blocked.profileId());
        assertNull(blocked.teamId());
        assertEquals(TournamentParticipantStatus.CONFIRMED, blocked.participantStatus());
        assertEquals(end, blocked.suspendedUntil());
        verify(participants, never()).delete(any());
    }

    @Test
    void detectsSuspendedTeamMemberWithoutRemovingPlayerOrDisqualifyingTeam() {
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament(TournamentEntryType.TEAM)));
        var team = CommunityTournamentTeam.builder().id(55L).build();
        when(teamPlayers.findByTournamentIdOrderByTeamIdAscIdAsc(19L)).thenReturn(List.of(
                CommunityTournamentTeamPlayer.builder().profile(player).team(team).build()));
        when(suspensions.findActiveForProfiles(eq(7L), eq(List.of(11L)), any(Instant.class)))
                .thenReturn(List.of(CommunityTournamentSuspension.builder()
                        .targetProfileId(11L).endsAt(Instant.now().plusSeconds(3600)).build()));

        var result = service.overview(7L, 19L, "owner");
        assertEquals(1, result.ineligibleEntries().size());
        assertEquals(55L, result.ineligibleEntries().getFirst().teamId());
        assertNull(result.ineligibleEntries().getFirst().participantStatus());
        verify(participants, never()).findByTournamentIdOrderByIdAsc(any());
        verify(teamPlayers, never()).delete(any());
    }

    @Test
    void noRosterAvoidsEmptyInClauseAndReturnsNoAffectedPlayers() {
        when(tournaments.findById(19L)).thenReturn(Optional.of(tournament(TournamentEntryType.TEAM)));
        assertEquals(List.of(), service.overview(7L, 19L, "owner").ineligibleEntries());
        verifyNoInteractions(suspensions);
    }

    @Test
    void memberCannotInspectDisciplinaryStatusOfOtherPlayers() {
        when(members.findByCommunityIdAndProfileId(7L, 10L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        status(HttpStatus.FORBIDDEN, () -> service.overview(7L, 19L, "owner"));
        verifyNoInteractions(tournaments, suspensions);
    }

    @Test
    void guessedTournamentFromAnotherCommunityIsNotExposed() {
        when(tournaments.findById(19L)).thenReturn(Optional.of(CommunityTournament.builder()
                .id(19L).entryType(TournamentEntryType.TEAM)
                .community(Community.builder().id(8L).build()).build()));
        status(HttpStatus.NOT_FOUND, () -> service.overview(7L, 19L, "owner"));
        verifyNoInteractions(suspensions, participants, teamPlayers);
    }

    @Test
    void outsiderCannotInspectExistingRoster() {
        when(members.findByCommunityIdAndProfileId(7L, 10L)).thenReturn(Optional.empty());
        status(HttpStatus.FORBIDDEN, () -> service.overview(7L, 19L, "owner"));
        verifyNoInteractions(tournaments, suspensions);
    }

    private static void status(HttpStatus expected, org.junit.jupiter.api.function.Executable operation) {
        assertEquals(expected, assertThrows(ResponseStatusException.class, operation).getStatusCode());
    }
}
