package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityOwnerDeletionTest {
    private final CommunityRepository groups = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
    private final CommunityJoinRequestRepository joinRequests = mock(CommunityJoinRequestRepository.class);
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityManagementService service = new CommunityManagementService(groups, members,
            invites, joinRequests, profiles, mock(NotificationsRepository.class),
            mock(ApplicationEventPublisher.class));
    private final Profile owner = new Profile();
    private final Community group = Community.builder().id(42L).name("Grupo de Teste").build();
    private CommunityMember ownership;

    @BeforeEach
    void setUp() {
        owner.setId(7L);
        owner.setUsername("owner");
        group.setOwner(owner);
        ownership = CommunityMember.builder().id(11L).community(group).profile(owner)
                .role(CommunityRole.OWNER).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(42L, 7L)).thenReturn(Optional.of(ownership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(42L)).thenReturn(List.of(ownership));
    }

    @Test
    void ownerDeletesEmptyCommunityOnlyAfterExactNameConfirmation() {
        service.deleteCommunity(42L, "owner", "Grupo de Teste");
        verify(members).delete(ownership);
        verify(members).flush();
        verify(groups).delete(group);
        verify(groups).flush();
    }

    @Test
    void confirmationMismatchCannotDeleteAnything() {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de teste"));
        verify(groups, never()).delete(any());
        verify(members, never()).delete(any());
    }

    @Test
    void administratorsCannotDeleteCommunityEvenWithCorrectName() {
        ownership.setRole(CommunityRole.ADMIN);
        assertThrows(SecurityException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de Teste"));
        verify(groups, never()).delete(any());
        verify(members, never()).delete(any());
    }

    @Test
    void communityWithOtherMembersIsBlockedBeforeAnyWrite() {
        Profile second = new Profile();
        second.setId(8L);
        when(members.findByCommunityIdOrderByJoinedAtAsc(42L)).thenReturn(List.of(
                ownership, CommunityMember.builder().profile(second).role(CommunityRole.MEMBER).build()));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de Teste")).getStatusCode().value());
        verify(groups, never()).delete(any());
    }

    @Test
    void pendingInvitesOrJoinRequestsBlockDeletion() {
        when(invites.countByCommunityId(42L)).thenReturn(1L);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de Teste")).getStatusCode().value());
        verify(members, never()).delete(any());
        clearInvites();
        when(joinRequests.countByCommunityId(42L)).thenReturn(1L);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de Teste")).getStatusCode().value());
        verify(groups, never()).delete(any());
    }

    private void clearInvites() {
        reset(invites);
    }

    @Test
    void postsAndHistoricActivityCannotBeCascadeDeleted() {
        when(groups.hasActivityPreventingDeletion(42L)).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.deleteCommunity(42L, "owner", "Grupo de Teste")).getStatusCode().value());
        verify(groups, never()).delete(any());
        verify(members, never()).delete(any());
    }
}
