package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityJoinRequest;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityJoinNotificationsTest {
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
    private final CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final NotificationsRepository notifications = mock(NotificationsRepository.class);
    private final CommunityManagementService service = new CommunityManagementService(
            communities, members, invites, requests, profiles, notifications,
            mock(ApplicationEventPublisher.class));

    private final Profile owner = profile(1L, "owner");
    private final Profile admin = profile(2L, "admin");
    private final Profile applicant = profile(3L, "player");
    private final Community community = Community.builder().id(12L).name("Futsal das Quintas")
            .owner(owner).visibility(CommunityVisibility.PRIVATE).build();

    @Test
    void firstJoinRequestNotifiesOwnerAndAdminsButNotOrdinaryMembers() {
        when(profiles.findUserByUsernameIgnoreCase("player")).thenReturn(Optional.of(applicant));
        when(communities.findById(12L)).thenReturn(Optional.of(community));
        when(requests.save(any(CommunityJoinRequest.class))).thenAnswer(call -> call.getArgument(0));
        when(members.findByCommunityIdOrderByJoinedAtAsc(12L)).thenReturn(List.of(
                member(owner, CommunityRole.OWNER), member(admin, CommunityRole.ADMIN),
                member(profile(4L, "other"), CommunityRole.MEMBER)));

        var response = service.requestJoin(12L, "player");

        assertEquals(CommunityInviteStatus.PENDING, response.status());
        ArgumentCaptor<Notifications> captured = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications, times(2)).save(captured.capture());
        assertEquals(List.of("owner", "admin"),
                captured.getAllValues().stream().map(Notifications::getUsername).toList());
        captured.getAllValues().forEach(n -> {
            assertEquals(NotificationType.COMMUNITY_JOIN_REQUEST, n.getType());
            assertEquals(12L, n.getCommunityId());
            assertEquals("player", n.getRelatedUsername());
            assertFalse(n.isRead());
        });
    }

    @Test
    void repeatOfExistingPendingRequestNeverSendsAnotherNotification() {
        when(profiles.findUserByUsernameIgnoreCase("player")).thenReturn(Optional.of(applicant));
        when(communities.findById(12L)).thenReturn(Optional.of(community));
        var pending = request();
        when(requests.findByCommunityIdAndRequesterIdAndStatus(
                12L, 3L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(pending));

        service.requestJoin(12L, "player");

        verify(requests, never()).save(any());
        verifyNoInteractions(notifications);
    }

    @Test
    void acceptingRequestNotifiesApplicantAndAddsMembership() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(12L, 1L))
                .thenReturn(Optional.of(member(owner, CommunityRole.OWNER)));
        when(requests.findById(56L)).thenReturn(Optional.of(request()));
        when(members.findByCommunityIdOrderByJoinedAtAsc(12L))
                .thenReturn(List.of(member(owner, CommunityRole.OWNER),
                        member(applicant, CommunityRole.MEMBER)));

        service.acceptJoinRequest(12L, 56L, "owner");

        verify(members).save(any(CommunityMember.class));
        verify(requests).save(argThat(r -> r.getStatus() == CommunityInviteStatus.ACCEPTED));
        ArgumentCaptor<Notifications> captured = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications).save(captured.capture());
        assertEquals("player", captured.getValue().getUsername());
        assertEquals(12L, captured.getValue().getCommunityId());
        assertEquals(NotificationType.COMMUNITY_JOIN_ACCEPTED, captured.getValue().getType());
    }

    @Test
    void rejectingRequestNotifiesApplicantWithoutAddingMembership() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(12L, 1L))
                .thenReturn(Optional.of(member(owner, CommunityRole.OWNER)));
        when(requests.findById(56L)).thenReturn(Optional.of(request()));

        service.rejectJoinRequest(12L, 56L, "owner");

        verify(members, never()).save(any());
        verify(requests).save(argThat(r -> r.getStatus() == CommunityInviteStatus.REJECTED));
        ArgumentCaptor<Notifications> captured = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications).save(captured.capture());
        assertEquals("player", captured.getValue().getUsername());
        assertEquals(NotificationType.COMMUNITY_JOIN_REJECTED, captured.getValue().getType());
        assertEquals(12L, captured.getValue().getCommunityId());
    }

    private CommunityJoinRequest request() {
        return CommunityJoinRequest.builder().id(56L).community(community).requester(applicant)
                .status(CommunityInviteStatus.PENDING).build();
    }

    private CommunityMember member(Profile person, CommunityRole role) {
        return CommunityMember.builder().community(community).profile(person).role(role).build();
    }

    private static Profile profile(Long id, String username) {
        Profile person = new Profile();
        person.setId(id);
        person.setUsername(username);
        return person;
    }
}
