package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.UpdateCommunityFeaturesRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Explicit service authorization matrix, with disposable in-memory fixtures.
 * No Spring context, database, network, Render, or QA login required.
 *
 * These tests prove the service layer contract, not HTTP 403 mapping nor
 * real-browser OWNER/ADMIN/MEMBER UI permissions.
 */
class CommunityRoleAuthorizationMatrixTest {

    private static final long COMMUNITY_ID = 8101L;

    private static final class Fixture {
        final CommunityRepository communities = mock(CommunityRepository.class);
        final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        final CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        final CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
        final ProfileRepository profiles = mock(ProfileRepository.class);
        final NotificationsRepository notifications = mock(NotificationsRepository.class);
        final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
        final CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, requests, profiles, notifications, events);

        final Profile owner = profile(1L, "fixture-owner");
        final Profile admin = profile(2L, "fixture-admin");
        final Profile member = profile(3L, "fixture-member");
        final Profile outsider = profile(4L, "fixture-outsider");
        final Community community = Community.builder()
                .id(COMMUNITY_ID)
                .name("Isolated community QA")
                .owner(owner)
                .visibility(CommunityVisibility.PRIVATE)
                .rankingsEnabled(false)
                .build();

        Fixture() {
            Map<String, Profile> byName = Map.of(
                    "fixture-owner", owner, "fixture-admin", admin,
                    "fixture-member", member, "fixture-outsider", outsider);
            when(profiles.findUserByUsernameIgnoreCase(any()))
                    .thenAnswer(invocation -> Optional.ofNullable(
                            byName.get(invocation.getArgument(0, String.class))));
            when(communities.findById(COMMUNITY_ID)).thenReturn(Optional.of(community));

            var ownerMembership = membership(owner, CommunityRole.OWNER);
            var adminMembership = membership(admin, CommunityRole.ADMIN);
            var memberMembership = membership(member, CommunityRole.MEMBER);
            var memberList = List.of(ownerMembership, adminMembership, memberMembership);

            when(members.findByCommunityIdAndProfileId(eq(COMMUNITY_ID), any()))
                    .thenAnswer(invocation -> {
                        Long requestedProfileId = invocation.getArgument(1);
                        return memberList.stream()
                                .filter(item -> item.getProfile().getId().equals(requestedProfileId))
                                .findFirst();
                    });
            when(members.findByCommunityIdOrderByJoinedAtAsc(COMMUNITY_ID))
                    .thenReturn(memberList);
        }

        private CommunityMember membership(Profile user, CommunityRole role) {
            return CommunityMember.builder()
                    .community(community).profile(user).role(role).build();
        }
    }

    private static Profile profile(long id, String username) {
        Profile result = new Profile();
        result.setId(id);
        result.setUsername(username);
        return result;
    }

    @Test
    void fullPrivateCommunityDetailsAreVisibleToMembersButNotOutsiders() {
        Fixture f = new Fixture();
        assertEquals(CommunityRole.OWNER,
                f.service.get(COMMUNITY_ID, "fixture-owner").myRole());
        assertEquals(CommunityRole.ADMIN,
                f.service.get(COMMUNITY_ID, "fixture-admin").myRole());
        assertEquals(CommunityRole.MEMBER,
                f.service.get(COMMUNITY_ID, "fixture-member").myRole());

        assertThrows(SecurityException.class,
                () -> f.service.get(COMMUNITY_ID, "fixture-outsider"));
        verifyNoInteractions(f.requests, f.invites, f.notifications);
    }

    @Test
    void onlyOwnerAndAdminCanReadThePendingJoinRequestQueue() {
        Fixture f = new Fixture();
        for (String actor : List.of("fixture-outsider", "fixture-member")) {
            assertThrows(SecurityException.class,
                    () -> f.service.pendingJoinRequests(COMMUNITY_ID, actor));
        }
        verifyNoInteractions(f.requests);

        for (String actor : List.of("fixture-owner", "fixture-admin")) {
            assertEquals(List.of(),
                    f.service.pendingJoinRequests(COMMUNITY_ID, actor));
        }
        verify(f.requests, times(2)).findByCommunityIdAndStatusOrderByCreatedAtAsc(
                COMMUNITY_ID, CommunityInviteStatus.PENDING);
    }

    @Test
    void memberAndOutsiderCannotApproveAJoinRequestOrTriggerWrites() {
        Fixture f = new Fixture();
        for (String actor : List.of("fixture-outsider", "fixture-member")) {
            assertThrows(SecurityException.class,
                    () -> f.service.acceptJoinRequest(COMMUNITY_ID, 99L, actor));
        }
        verifyNoInteractions(f.requests, f.invites, f.notifications);
        verify(f.members, never()).save(any());
    }

    @Test
    void onlyOwnerAndAdminCanUpdateOptionalCommunityFeatures() {
        for (CommunityRole role : CommunityRole.values()) {
            Fixture f = new Fixture();
            String actor = switch (role) {
                case OWNER -> "fixture-owner";
                case ADMIN -> "fixture-admin";
                case MEMBER -> "fixture-member";
            };
            var request = new UpdateCommunityFeaturesRequest(true, false, false);
            if (role == CommunityRole.MEMBER) {
                assertThrows(SecurityException.class,
                        () -> f.service.updateFeatures(COMMUNITY_ID, actor, request));
                verify(f.communities, never()).save(any());
            } else {
                when(f.communities.save(f.community)).thenReturn(f.community);
                assertEquals(role,
                        f.service.updateFeatures(COMMUNITY_ID, actor, request).myRole());
                verify(f.communities).save(f.community);
            }
        }
        Fixture outsider = new Fixture();
        assertThrows(SecurityException.class,
                () -> outsider.service.updateFeatures(COMMUNITY_ID,
                        "fixture-outsider",
                        new UpdateCommunityFeaturesRequest(true, true, true)));
        verify(outsider.communities, never()).save(any());
    }
}
