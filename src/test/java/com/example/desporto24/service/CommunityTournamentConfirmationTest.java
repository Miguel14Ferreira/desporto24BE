package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityTournamentConfirmationTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentParticipantRepository participants;
    @Mock ProfileRepository profiles;
    @InjectMocks CommunityTournamentService service;

    private final Profile organizer = profile(5L, "ana");
    private final Profile player = profile(6L, "rui");
    private final Community community = Community.builder().id(7L).name("Futebol")
            .visibility(CommunityVisibility.PRIVATE).owner(organizer).build();
    private final CommunityTournament tournament = CommunityTournament.builder()
            .id(9L).community(community).name("Liga").status(TournamentStatus.DRAFT).build();

    private static Profile profile(long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private void manager(CommunityRole role) {
        when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(organizer));
        when(members.findByCommunityIdAndProfileId(7L, 5L)).thenReturn(
                Optional.of(CommunityMember.builder().community(community)
                        .profile(organizer).role(role).build()));
    }

    private void tournamentExists() {
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
    }

    private CommunityTournamentParticipant pending() {
        return CommunityTournamentParticipant.builder().id(77L).tournament(tournament)
                .profile(player).joinedAt(Instant.now())
                .status(TournamentParticipantStatus.PENDING).build();
    }

    @Test
    void ownerConfirmsOnlyAnExistingOptInOfACurrentMember() {
        manager(CommunityRole.OWNER);
        tournamentExists();
        var entry = pending();
        when(participants.findByTournamentIdAndProfileId(9L, 6L)).thenReturn(Optional.of(entry));
        when(members.existsByCommunityIdAndProfileId(7L, 6L)).thenReturn(true);
        when(participants.save(entry)).thenReturn(entry);
        var response = service.confirmParticipant(7L, 9L, 6L, "ana");
        assertEquals(TournamentParticipantStatus.CONFIRMED, response.status());
        verify(participants, times(1)).save(entry);
        verify(participants, never()).save(argThat(x -> x.getProfile() == organizer));
    }

    @Test
    void adminMayConfirmAndRepeatedApprovalIsIdempotent() {
        manager(CommunityRole.ADMIN);
        tournamentExists();
        var entry = pending();
        entry.setStatus(TournamentParticipantStatus.CONFIRMED);
        when(participants.findByTournamentIdAndProfileId(9L, 6L)).thenReturn(Optional.of(entry));
        when(members.existsByCommunityIdAndProfileId(7L, 6L)).thenReturn(true);
        var confirmed = service.confirmParticipant(7L, 9L, 6L, "ana");
        assertEquals(TournamentParticipantStatus.CONFIRMED, confirmed.status());
        verify(participants, never()).save(any());
    }

    @Test
    void ordinaryMemberCannotConfirm() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () -> service.confirmParticipant(7L, 9L, 6L, "ana"));
        verifyNoInteractions(tournaments, participants);
    }

    @Test
    void nonexistentEnrollmentCannotBeManufacturedByManager() {
        manager(CommunityRole.OWNER);
        tournamentExists();
        when(participants.findByTournamentIdAndProfileId(9L, 6L)).thenReturn(Optional.empty());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.confirmParticipant(7L, 9L, 6L, "ana")).getStatusCode().value());
        verify(participants, never()).save(any());
    }

    @Test
    void formerCommunityMemberIsNotConfirmable() {
        manager(CommunityRole.OWNER);
        tournamentExists();
        var entry = pending();
        when(participants.findByTournamentIdAndProfileId(9L, 6L)).thenReturn(Optional.of(entry));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.confirmParticipant(7L, 9L, 6L, "ana")).getStatusCode().value());
        verify(participants, never()).save(any());
    }

    @Test
    void confirmationIsBlockedAfterTournamentStarts() {
        manager(CommunityRole.OWNER);
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournamentExists();
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.confirmParticipant(7L, 9L, 6L, "ana")).getStatusCode().value());
        verifyNoInteractions(participants);
    }

    @Test
    void cannotConfirmEnrollmentForAnotherCommunity() {
        manager(CommunityRole.OWNER);
        tournament.setCommunity(Community.builder().id(19L).build());
        tournamentExists();
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.confirmParticipant(7L, 9L, 6L, "ana")).getStatusCode().value());
        verifyNoInteractions(participants);
    }

    @Test
    void invalidPlayerIdRejectedWithoutDatabaseQueries() {
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.confirmParticipant(7L, 9L, 0L, "ana")).getStatusCode().value());
        verifyNoInteractions(profiles, members, tournaments, participants);
    }
}
