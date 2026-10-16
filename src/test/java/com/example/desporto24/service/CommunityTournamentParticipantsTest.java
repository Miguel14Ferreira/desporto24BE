package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
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
class CommunityTournamentParticipantsTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityTournamentParticipantRepository participants;
    @Mock ProfileRepository profiles;
    @InjectMocks CommunityTournamentService service;

    private final Profile actor = profile(5L, "ana");
    private final Community community = Community.builder().id(7L).name("Futebol")
            .visibility(CommunityVisibility.PRIVATE).owner(actor).createdAt(Instant.now()).build();
    private final CommunityTournament tournament = CommunityTournament.builder().id(9L).community(community)
            .name("Liga").status(TournamentStatus.DRAFT).createdAt(Instant.now()).build();

    private static Profile profile(Long id, String username) {
        var profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private void authenticated() {
        when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(actor));
    }
    private void member() {
        authenticated();
        when(members.existsByCommunityIdAndProfileId(7L, 5L)).thenReturn(true);
    }
    private void validTournament() {
        when(tournaments.findById(9L)).thenReturn(Optional.of(tournament));
    }
    private void manager(CommunityRole role) {
        authenticated();
        when(members.findByCommunityIdAndProfileId(7L, 5L))
                .thenReturn(Optional.of(CommunityMember.builder()
                        .community(community).profile(actor).role(role).build()));
    }

    @Test
    void selfEnrollmentCreatesOnlyTheAuthenticatedMember() {
        member();
        validTournament();
        actor.setFullName("Ana Silva");
        when(participants.findByTournamentIdAndProfileId(9L, 5L)).thenReturn(Optional.empty());
        when(participants.save(any(CommunityTournamentParticipant.class))).thenAnswer(i -> i.getArgument(0));
        var joined = service.join(7L, 9L, "ana");
        assertEquals(5L, joined.profileId());
        assertEquals("Ana Silva", joined.displayName());
        assertNotNull(joined.joinedAt());
        assertEquals(TournamentParticipantStatus.PENDING, joined.status());
        verify(participants).save(argThat(p -> p.getProfile() == actor && p.getTournament() == tournament));
    }

    @Test
    void joiningTwiceIsIdempotent() {
        member();
        validTournament();
        var already = CommunityTournamentParticipant.builder()
                .id(1L).tournament(tournament).profile(actor).joinedAt(Instant.now()).build();
        when(participants.findByTournamentIdAndProfileId(9L, 5L)).thenReturn(Optional.of(already));
        assertEquals(5L, service.join(7L, 9L, "ana").profileId());
        assertEquals(TournamentParticipantStatus.CONFIRMED, already.getStatus());
        verify(participants, never()).save(any());
    }

    @Test
    void outsiderCannotSelfEnroll() {
        authenticated();
        assertThrows(AccessDeniedException.class, () -> service.join(7L, 9L, "ana"));
        verifyNoInteractions(participants, tournaments);
    }

    @Test
    void cannotJoinOrLeaveOnceStarted() {
        member();
        validTournament();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.join(7L, 9L, "ana")).getStatusCode().value());
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.leave(7L, 9L, "ana")).getStatusCode().value());
        verifyNoInteractions(participants);
    }

    @Test
    void guessedTournamentFromOtherCommunityIsHidden() {
        member();
        Community differentCommunity = Community.builder().id(99L).build();
        tournament.setCommunity(differentCommunity);
        validTournament();
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.join(7L, 9L, "ana")).getStatusCode().value());
        verifyNoInteractions(participants);
    }

    @Test
    void memberCanWithdrawOnlyTheirOwnEnrollment() {
        member();
        validTournament();
        var mine = CommunityTournamentParticipant.builder().id(10L).tournament(tournament)
                .profile(actor).joinedAt(Instant.now()).build();
        when(participants.findByTournamentIdAndProfileId(9L, 5L)).thenReturn(Optional.of(mine));
        service.leave(7L, 9L, "ana");
        verify(participants).findByTournamentIdAndProfileId(9L, 5L);
        verify(participants, times(1)).delete(mine);
    }

    @Test
    void organizerCanRemoveMemberWhileDraft() {
        manager(CommunityRole.ADMIN);
        validTournament();
        var other = profile(6L, "rui");
        var entry = CommunityTournamentParticipant.builder().id(11L).tournament(tournament).profile(other)
                .joinedAt(Instant.now()).build();
        when(participants.findByTournamentIdAndProfileId(9L, 6L)).thenReturn(Optional.of(entry));
        service.removeParticipant(7L, 9L, 6L, "ana");
        verify(participants).delete(entry);
    }

    @Test
    void regularMemberCannotRemoveAnotherParticipant() {
        manager(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class,
                () -> service.removeParticipant(7L, 9L, 6L, "ana"));
        verifyNoInteractions(participants, tournaments);
    }

    @Test
    void paginationDoesNotExposeRosterOutsideCommunity() {
        authenticated();
        assertThrows(AccessDeniedException.class, () -> service.participants(7L, 9L, "ana", 0));
        verifyNoInteractions(participants, tournaments);
    }

    @Test
    void rosterIsPaginatedAndSortsById() {
        member();
        validTournament();
        var page = PageRequest.of(1, 20, Sort.by(Sort.Direction.ASC, "id"));
        var registered = CommunityTournamentParticipant.builder().id(11L).tournament(tournament).profile(actor)
                .joinedAt(Instant.parse("2026-10-16T10:00:00Z")).build();
        when(participants.findByTournamentId(9L, page))
                .thenReturn(new SliceImpl<>(List.of(registered), page, true));
        var result = service.participants(7L, 9L, "ana", 1);
        assertEquals(1, result.page());
        assertTrue(result.hasMore());
        assertEquals("ana", result.participants().get(0).username());
        verify(participants).findByTournamentId(9L, page);
    }

    @Test
    void negativePageFailsBeforeRepositoryAccess() {
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.participants(7L, 9L, "ana", -1)).getStatusCode().value());
        verifyNoInteractions(participants, tournaments, members, profiles);
    }
}
