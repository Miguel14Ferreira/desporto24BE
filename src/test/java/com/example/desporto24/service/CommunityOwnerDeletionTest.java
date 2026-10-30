package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityOwnerDeletionTest {
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
    private final CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final CommunityManagementService service = new CommunityManagementService(communities, members,
            invites, requests, profiles, mock(NotificationsRepository.class),
            mock(ApplicationEventPublisher.class));
    private final Profile owner = new Profile();
    private final Community community = Community.builder().id(42L).name("Cacém").build();
    private CommunityMember ownership;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbc);
        owner.setId(7L);
        owner.setUsername("owner");
        community.setOwner(owner);
        ownership = CommunityMember.builder().id(11L).community(community).profile(owner)
                .role(CommunityRole.OWNER).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(42L, 7L)).thenReturn(Optional.of(ownership));
        when(communities.lockForDeletion(42L)).thenReturn(Optional.of(community));
        when(jdbc.queryForObject(eq("SELECT desporto24.delete_community_subtree(?)"),
                eq(Integer.class), eq(42L))).thenReturn(1);
    }

    @Test
    void ownerMayDeletePopulatedCommunityAfterExactNameConfirmation() {
        when(members.findByCommunityIdOrderByJoinedAtAsc(42L)).thenReturn(
                List.of(ownership, CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        when(invites.countByCommunityId(42L)).thenReturn(9L);
        when(requests.countByCommunityId(42L)).thenReturn(7L);
        service.deleteCommunity(42L, "owner", "Cacém");
        verify(jdbc).queryForObject("SELECT desporto24.delete_community_subtree(?)", Integer.class, 42L);
        verify(communities, never()).delete(any());
        verify(members, never()).delete(any());
        verify(invites, never()).countByCommunityId(anyLong());
        verify(requests, never()).countByCommunityId(anyLong());
    }

    @Test
    void confirmationMismatchCannotDeleteAnything() {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteCommunity(42L, "owner", "Cacem"));
        verify(jdbc, never()).queryForObject(anyString(), eq(Integer.class), anyLong());
    }

    @Test
    void administratorsCannotDeletePopulatedCommunity() {
        ownership.setRole(CommunityRole.ADMIN);
        assertThrows(SecurityException.class,
                () -> service.deleteCommunity(42L, "owner", "Cacém"));
        verify(jdbc, never()).queryForObject(anyString(), eq(Integer.class), anyLong());
    }

    @Test
    void ownershipChangedInAnotherTransactionCannotDelete() {
        Profile differentOwner = new Profile();
        differentOwner.setId(10L);
        community.setOwner(differentOwner);
        assertThrows(SecurityException.class,
                () -> service.deleteCommunity(42L, "owner", "Cacém"));
        verify(jdbc, never()).queryForObject(anyString(), eq(Integer.class), anyLong());
    }

    @Test
    void missingDatabaseDeleteResultIsNotReportedAsSuccess() {
        when(jdbc.queryForObject(eq("SELECT desporto24.delete_community_subtree(?)"),
                eq(Integer.class), eq(42L))).thenReturn(0);
        assertThrows(IllegalStateException.class,
                () -> service.deleteCommunity(42L, "owner", "Cacém"));
    }
}
