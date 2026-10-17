package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamPlayer;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamPlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentTeamServiceTest {
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunityTournamentRepository tournamentRepository;
    @Mock private CommunityTournamentTeamRepository teamRepository;
    @Mock private CommunityTournamentTeamPlayerRepository playerRepository;
    @InjectMocks private CommunityTournamentTeamService service;

    private final Profile owner = profile(1L, "owner");
    private final Profile player = profile(2L, "ana");
    private final Community community = Community.builder().id(7L).name("CNG").build();
    private final CommunityTournament teamTournament = CommunityTournament.builder()
            .id(19L).name("Liga 5x5").community(community)
            .entryType(TournamentEntryType.TEAM).playersPerTeam(2)
            .status(TournamentStatus.DRAFT).build();

    private static Profile profile(long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        p.setFullName(username);
        return p;
    }

    private void manager() {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(owner)
                        .role(CommunityRole.OWNER).build()));
    }

    private void draft() {
        when(tournamentRepository.findByIdForUpdate(19L)).thenReturn(Optional.of(teamTournament));
    }

    private CommunityTournamentTeam team(long id, String name) {
        return CommunityTournamentTeam.builder().id(id).tournament(teamTournament)
                .name(name).normalizedName(name.toLowerCase()).createdAt(Instant.now()).build();
    }

    @Test
    void organizerCreatesRealTeamAndNormalizesItsName() {
        manager();
        draft();
        when(teamRepository.save(any())).thenAnswer(invocation -> {
            CommunityTournamentTeam t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });
        var result = service.createTeam(7L, 19L, "owner", "  Equipa   Azul  ");
        assertEquals("Equipa Azul", result.name());
        assertEquals(2, result.playersPerTeam());
        assertTrue(result.players().isEmpty());
        verify(teamRepository).existsByTournamentIdAndNormalizedName(19L, "equipa azul");
    }

    @Test
    void avoidsDuplicateTeamNamesCaseInsensitively() {
        manager();
        draft();
        when(teamRepository.existsByTournamentIdAndNormalizedName(19L, "equipa azul"))
                .thenReturn(true);
        var error = assertThrows(ResponseStatusException.class,
                () -> service.createTeam(7L, 19L, "owner", "EQUIPA AZUL"));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void managerCanAssignOnlyCommunityMemberOncePerCompetition() {
        manager();
        draft();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team(10L, "Azul")));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(profileRepository.findById(2L)).thenReturn(Optional.of(player));
        var response = service.assignPlayer(7L, 19L, 10L, "owner", 2L);
        assertEquals("Azul", response.name());
        var captor = org.mockito.ArgumentCaptor.forClass(CommunityTournamentTeamPlayer.class);
        verify(playerRepository).save(captor.capture());
        assertEquals(19L, captor.getValue().getTournament().getId());
        assertEquals(10L, captor.getValue().getTeam().getId());
        assertEquals(2L, captor.getValue().getProfile().getId());
        verifyNoInteractionsWithIndividualEnrollmentRepository();
    }

    private void verifyNoInteractionsWithIndividualEnrollmentRepository() {
        // This service has no dependency on the existing individual enrollment repository.
        // The captured team-player entity is never an individual tournament participant.
    }

    @Test
    void rejectsMemberOfAnotherCommunity() {
        manager();
        draft();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team(10L, "Azul")));
        var error = assertThrows(ResponseStatusException.class,
                () -> service.assignPlayer(7L, 19L, 10L, "owner", 2L));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(profileRepository);
    }

    @Test
    void preventsPlayerFromJoiningTwoTeamsOfSameCompetition() {
        manager();
        draft();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team(10L, "Azul")));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(playerRepository.existsByTournamentIdAndProfileId(19L, 2L)).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class,
                () -> service.assignPlayer(7L, 19L, 10L, "owner", 2L));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void cannotOverfillTeam() {
        manager();
        draft();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team(10L, "Azul")));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(playerRepository.countByTeamId(10L)).thenReturn(2L);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.assignPlayer(7L, 19L, 10L, "owner", 2L)).getStatusCode());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void cannotManageTeamOwnedByAnotherTournament() {
        manager();
        draft();
        var unrelatedTournament = CommunityTournament.builder().id(20L).build();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(
                CommunityTournamentTeam.builder().id(10L).tournament(unrelatedTournament).build()));
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> service.assignPlayer(7L, 19L, 10L, "owner", 2L)).getStatusCode());
        verifyNoInteractions(playerRepository);
    }

    @Test
    void rejectsNonOrganizers() {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        assertThrows(AccessDeniedException.class, () -> service.createTeam(7L, 19L, "owner", "Azul"));
        verifyNoInteractions(tournamentRepository, teamRepository);
    }

    @Test
    void rejectsTeamChangesAfterCompetitionStarts() {
        manager();
        teamTournament.setStatus(TournamentStatus.IN_PROGRESS);
        draft();
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.createTeam(7L, 19L, "owner", "Azul")).getStatusCode());
        verifyNoInteractions(teamRepository);
    }

    @Test
    void rejectsTeamsForIndividualCompetition() {
        manager();
        teamTournament.setEntryType(TournamentEntryType.INDIVIDUAL);
        draft();
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.createTeam(7L, 19L, "owner", "Azul")).getStatusCode());
        verifyNoInteractions(teamRepository);
    }

    @Test
    void onlyCurrentCommunityMembersCanViewRosters() {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        assertThrows(AccessDeniedException.class, () -> service.list(7L, 19L, "owner"));
        verifyNoInteractions(tournamentRepository, teamRepository, playerRepository);
    }

    @Test
    void teamListLoadsAllRostersOnce() {
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(tournamentRepository.findById(19L)).thenReturn(Optional.of(teamTournament));
        var azul = team(10L, "Azul");
        var verde = team(11L, "Verde");
        when(teamRepository.findByTournamentIdOrderByIdAsc(19L)).thenReturn(List.of(azul, verde));
        when(playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(19L)).thenReturn(
                List.of(CommunityTournamentTeamPlayer.builder()
                        .tournament(teamTournament).team(azul).profile(player).build()));

        var result = service.list(7L, 19L, "owner");
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).players().size());
        assertTrue(result.get(1).players().isEmpty());
        verify(playerRepository, times(1)).findByTournamentIdOrderByTeamIdAscIdAsc(19L);
    }

    @Test
    void cannotDeleteNonemptyTeam() {
        manager();
        draft();
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team(10L, "Azul")));
        when(playerRepository.countByTeamId(10L)).thenReturn(1L);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.deleteTeam(7L, 19L, 10L, "owner")).getStatusCode());
        verify(teamRepository, never()).delete(any());
    }
}
