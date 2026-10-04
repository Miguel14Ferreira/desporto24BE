package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.exception.CommunityInvitePendingException;
import com.example.desporto24.model.community.CommunityInvite;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.community.UpdateCommunityVisibilityRequest;
import java.time.Instant;
import java.util.List;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.InviteCommunityMemberRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommunityManagementServiceTest {

    @Test
    void newInviteCreatesUnreadNotificationOnlyForInvitedProfile() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        NotificationsRepository notifications = mock(NotificationsRepository.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Profile friend = new Profile();
        friend.setId(2L);
        friend.setUsername("friend");
        friend.setEmail("friend@example.test");
        Community community = Community.builder().id(10L).name("CNG").build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(profiles.findUserByUsernameIgnoreCase("friend")).thenReturn(Optional.of(friend));
        when(members.findByCommunityIdAndProfileId(10L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().profile(owner).role(CommunityRole.OWNER).build()));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(invites.save(any())).thenAnswer(call -> call.getArgument(0));
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
        CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, mock(CommunityJoinRequestRepository.class), profiles, notifications, events);

        service.invite(10L, "owner", new InviteCommunityMemberRequest("friend"));

        ArgumentCaptor<Notifications> notification = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications).save(notification.capture());
        ArgumentCaptor<CommunityInviteCreatedEvent> event = ArgumentCaptor.forClass(CommunityInviteCreatedEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals("friend@example.test", event.getValue().recipientEmail());
        assertEquals("owner", event.getValue().inviterUsername());
        assertEquals("CNG", event.getValue().communityName());
        Notifications saved = notification.getValue();
        assertEquals("friend", saved.getUsername());
        assertEquals(NotificationType.COMMUNITY_INVITE, saved.getType());
        assertEquals(false, saved.isRead());
        assertEquals("Convite para a comunidade CNG", saved.getSubject());
        assertEquals("@owner convidou-te para a comunidade CNG. Abre Comunidades para aceitar ou rejeitar o convite.",
                saved.getMessage());
        assertEquals(false, saved.isFriendRequest());
    }


    @Test
    void pendingInviteReturnsSpecificConflictWithoutCreatingAnotherInvite() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile owner = new Profile();
        owner.setId(1L);
        Profile friend = new Profile();
        friend.setId(2L);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(profiles.findUserByUsernameIgnoreCase("friend")).thenReturn(Optional.of(friend));
        when(members.findByCommunityIdAndProfileId(10L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().profile(owner).role(CommunityRole.OWNER).build()));
        when(invites.findByCommunityIdAndInvitedProfileIdAndStatus(
                10L, 2L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(
                CommunityInvite.builder().status(CommunityInviteStatus.PENDING).build()));
        NotificationsRepository notifications = mock(NotificationsRepository.class);
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
        CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, mock(CommunityJoinRequestRepository.class), profiles, notifications, events);

        CommunityInvitePendingException exception = assertThrows(
                CommunityInvitePendingException.class,
                () -> service.invite(10L, "owner", new InviteCommunityMemberRequest("friend")));

        assertEquals("Já existe um convite pendente para este utilizador. Aguarda a aceitação.",
                exception.getMessage());
        verify(invites, never()).save(any());
        verifyNoInteractions(notifications, events);
    }


    @Test
    void inviteUnknownUsernameUsesUserNotFoundExceptionInsteadOfInternalError() {
        CommunityRepository communityRepository = mock(CommunityRepository.class);
        CommunityMemberRepository memberRepository = mock(CommunityMemberRepository.class);
        CommunityInviteRepository inviteRepository = mock(CommunityInviteRepository.class);
        ProfileRepository profileRepository = mock(ProfileRepository.class);

        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");

        CommunityMember ownerMembership = CommunityMember.builder()
                .profile(owner)
                .role(CommunityRole.OWNER)
                .build();

        when(profileRepository.findUserByUsernameIgnoreCase("owner"))
                .thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(ownerMembership));
        when(profileRepository.findUserByUsernameIgnoreCase("missing-user"))
                .thenReturn(Optional.empty());

        CommunityManagementService service = new CommunityManagementService(
                communityRepository,
                memberRepository,
                inviteRepository,
                mock(CommunityJoinRequestRepository.class),
                profileRepository, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> service.invite(
                        10L,
                        "owner",
                        new InviteCommunityMemberRequest("missing-user")
                )
        );

        assertEquals(NO_USER_FOUND_BY_USERNAME, exception.getMessage());
        verify(inviteRepository, never()).save(any());
    }

    @Test
    void ownerCanChangeVisibilityInBothDirectionsWithoutChangingCommunityDetails() {
        for (CommunityVisibility target : CommunityVisibility.values()) {
            CommunityRepository communities = mock(CommunityRepository.class);
            CommunityMemberRepository members = mock(CommunityMemberRepository.class);
            ProfileRepository profiles = mock(ProfileRepository.class);
            Profile owner = new Profile();
            owner.setId(1L);
            owner.setUsername("owner");
            Community community = Community.builder()
                    .id(10L).name("Existing community").description("Existing description")
                    .sport("Futsal").visibility(target == CommunityVisibility.PUBLIC
                            ? CommunityVisibility.PRIVATE : CommunityVisibility.PUBLIC)
                    .owner(owner).createdAt(Instant.parse("2026-10-01T12:00:00Z")).build();
            CommunityMember membership = CommunityMember.builder()
                    .community(community).profile(owner).role(CommunityRole.OWNER).build();
            when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
            when(members.findByCommunityIdAndProfileId(10L, 1L)).thenReturn(Optional.of(membership));
            when(members.findByCommunityIdOrderByJoinedAtAsc(10L)).thenReturn(List.of(membership));
            when(communities.save(any(Community.class))).thenAnswer(call -> call.getArgument(0));
            CommunityManagementService service = new CommunityManagementService(
                    communities, members, mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class), profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

            var response = service.updateVisibility(10L, "owner",
                    new UpdateCommunityVisibilityRequest(target));

            assertEquals(target, response.visibility());
            assertEquals(target, community.getVisibility());
            assertEquals(10L, response.id());
            assertEquals("Existing community", response.name());
            assertEquals("Existing description", response.description());
            assertEquals("Futsal", response.sport());
            assertEquals(CommunityRole.OWNER, response.myRole());
            verify(communities).save(community);
        }
    }

    @Test
    void visibilityChangeRejectsAdminsMembersAndNonMembersWithoutMutation() {
        for (CommunityRole role : new CommunityRole[]{CommunityRole.ADMIN, CommunityRole.MEMBER, null}) {
            CommunityRepository communities = mock(CommunityRepository.class);
            CommunityMemberRepository members = mock(CommunityMemberRepository.class);
            ProfileRepository profiles = mock(ProfileRepository.class);
            Profile actor = new Profile();
            actor.setId(2L);
            actor.setUsername("actor");
            Community community = Community.builder()
                    .id(10L).visibility(CommunityVisibility.PRIVATE).build();
            Optional<CommunityMember> membership = role == null ? Optional.empty()
                    : Optional.of(CommunityMember.builder()
                        .community(community).profile(actor).role(role).build());
            when(profiles.findUserByUsernameIgnoreCase("actor")).thenReturn(Optional.of(actor));
            when(members.findByCommunityIdAndProfileId(10L, 2L)).thenReturn(membership);
            CommunityManagementService service = new CommunityManagementService(
                    communities, members, mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class), profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

            assertThrows(SecurityException.class, () -> service.updateVisibility(
                    10L, "actor", new UpdateCommunityVisibilityRequest(CommunityVisibility.PUBLIC)));

            assertEquals(CommunityVisibility.PRIVATE, community.getVisibility());
            verify(communities, never()).save(any());
        }
    }
}
