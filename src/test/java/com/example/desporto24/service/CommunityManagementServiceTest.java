package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.storage.MediaStorage;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import java.net.URI;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.exception.CommunityInvitePendingException;
import com.example.desporto24.model.community.CommunityInvite;
import com.example.desporto24.model.community.CommunityJoinRequest;
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
    void ownerConfiguresOptionalModulesWithoutDeletingAnyCommunityHistory() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        var actor = new Profile();
        actor.setId(10L);
        actor.setUsername("owner");
        var group = Community.builder().id(9L).name("CNG").owner(actor)
                .visibility(CommunityVisibility.PRIVATE).rankingsEnabled(true).build();
        var member = CommunityMember.builder().community(group).profile(actor)
                .role(CommunityRole.OWNER).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(9L, 10L)).thenReturn(Optional.of(member));
        when(members.findByCommunityIdOrderByJoinedAtAsc(9L)).thenReturn(List.of(member));
        when(communities.findById(9L)).thenReturn(Optional.of(group));
        when(communities.save(group)).thenReturn(group);
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        var changed = service.updateFeatures(9L, "owner",
                new com.example.desporto24.model.dto.community.UpdateCommunityFeaturesRequest(
                        false, true, false));
        assertEquals(false, changed.rankingsEnabled());
        assertEquals(true, changed.individualTournamentsEnabled());
        assertEquals(false, changed.teamTournamentsEnabled());
        verify(communities).save(group);
    }

    @Test
    void adminsCanSetAndClearHabitualVenueWithoutChangingPastSessions() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        com.example.desporto24.repository.venue.VenueRepository venues =
                mock(com.example.desporto24.repository.venue.VenueRepository.class);
        Profile owner = new Profile();
        owner.setId(8L);
        owner.setUsername("owner");
        Community group = Community.builder().id(9L).owner(owner)
                .name("Grupo").build();
        var membership = CommunityMember.builder().community(group).profile(owner)
                .role(CommunityRole.OWNER).build();
        var venue = new com.example.desporto24.model.venue.Venue();
        venue.setId(55L);
        venue.setName("Pavilhão");
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(9L, 8L)).thenReturn(Optional.of(membership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(9L)).thenReturn(List.of(membership));
        when(venues.findById(55L)).thenReturn(Optional.of(venue));
        when(communities.save(group)).thenReturn(group);
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        ReflectionTestUtils.setField(service, "venueRepository", venues);

        var updated = service.updateDefaultVenue(9L, "owner", 55L);
        assertEquals(55L, updated.defaultVenueId());
        assertEquals("Pavilhão", updated.defaultVenueName());
        assertEquals(venue, group.getDefaultVenue());

        var cleared = service.updateDefaultVenue(9L, "owner", null);
        assertEquals(null, cleared.defaultVenueId());
        assertEquals(null, group.getDefaultVenue());
        verify(communities, times(2)).save(group);
    }

    @Test
    void adminCanSaveMapsAndGpsPointsAndSwitchBackToExistingVenue() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        var venues = mock(com.example.desporto24.repository.venue.VenueRepository.class);
        Profile owner = new Profile();
        owner.setId(8L);
        owner.setUsername("owner");
        Community community = Community.builder().id(9L).owner(owner)
                .name("Grupo").build();
        CommunityMember membership = CommunityMember.builder().community(community)
                .profile(owner).role(CommunityRole.OWNER).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(9L, 8L)).thenReturn(Optional.of(membership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(9L)).thenReturn(List.of(membership));
        when(communities.save(community)).thenReturn(community);
        var venue = new com.example.desporto24.model.venue.Venue();
        venue.setId(55L);
        venue.setName("Pavilhão");
        when(venues.findById(55L)).thenReturn(Optional.of(venue));
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        ReflectionTestUtils.setField(service, "venueRepository", venues);

        var maps = service.updateHabitualLocation(9L, "owner",
                "MAPS", 38.7, -9.2, "Lisboa", "Rua Teste");
        assertEquals("MAPS", maps.habitualLocationSource());
        assertEquals(38.7, maps.habitualLatitude());
        assertEquals(-9.2, maps.habitualLongitude());
        assertEquals(null, maps.defaultVenueId());

        var gps = service.updateHabitualLocation(9L, "owner",
                "GPS", 38.71, -9.21, null, null);
        assertEquals("GPS", gps.habitualLocationSource());
        assertEquals(null, gps.habitualAddress());
        assertEquals(38.71, community.getHabitualLatitude());

        var updated = service.updateDefaultVenue(9L, "owner", 55L);
        assertEquals(55L, updated.defaultVenueId());
        assertEquals(null, updated.habitualLocationSource());
        assertEquals(null, community.getHabitualLatitude());
        verify(communities, times(3)).save(community);
    }

    @Test
    void rejectsInvalidHabitualCoordinatesAndUnauthorizedMembers() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        var owner = new Profile(); owner.setId(8L); owner.setUsername("owner");
        var member = new Profile(); member.setId(10L); member.setUsername("member");
        Community group = Community.builder().id(9L).owner(owner).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(profiles.findUserByUsernameIgnoreCase("member")).thenReturn(Optional.of(member));
        when(members.findByCommunityIdAndProfileId(9L, 8L)).thenReturn(Optional.of(
                CommunityMember.builder().community(group).profile(owner)
                        .role(CommunityRole.OWNER).build()));
        when(members.findByCommunityIdAndProfileId(9L, 10L)).thenReturn(Optional.of(
                CommunityMember.builder().community(group).profile(member)
                        .role(CommunityRole.MEMBER).build()));
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        assertThrows(IllegalArgumentException.class, () ->
                service.updateHabitualLocation(9L, "owner", "MAPS",
                        Double.NaN, -9.2, "Lisboa", "Rua"));
        assertThrows(IllegalArgumentException.class, () ->
                service.updateHabitualLocation(9L, "owner", "MAPS",
                        91.0, -9.2, "Lisboa", "Rua"));
        assertThrows(IllegalArgumentException.class, () ->
                service.updateHabitualLocation(9L, "owner", "OTHER",
                        38.7, -9.2, "Lisboa", "Rua"));
        assertThrows(SecurityException.class, () ->
                service.updateHabitualLocation(9L, "member", "GPS",
                        38.7, -9.2, "Lisboa", "Rua"));
        verify(communities, never()).save(any());
    }

    @Test
    void aRegularMemberCannotChangeHabitualVenue() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile member = new Profile();
        member.setId(10L);
        member.setUsername("member");
        Community group = Community.builder().id(9L).owner(member).build();
        when(profiles.findUserByUsernameIgnoreCase("member")).thenReturn(Optional.of(member));
        when(members.findByCommunityIdAndProfileId(9L, 10L)).thenReturn(Optional.of(
                CommunityMember.builder().community(group).profile(member)
                        .role(CommunityRole.MEMBER).build()));
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        assertThrows(SecurityException.class, () ->
                service.updateDefaultVenue(9L, "member", 55L));
        verify(communities, never()).save(any());
    }

    @Test
    void ordinaryCommunityMemberCannotConfigureModules() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        var actor = new Profile();
        actor.setId(10L);
        actor.setUsername("member");
        var group = Community.builder().id(9L).name("CNG").owner(actor).build();
        when(profiles.findUserByUsernameIgnoreCase("member")).thenReturn(Optional.of(actor));
        when(members.findByCommunityIdAndProfileId(9L, 10L)).thenReturn(Optional.of(
                CommunityMember.builder().community(group).profile(actor)
                        .role(CommunityRole.MEMBER).build()));
        var service = new CommunityManagementService(communities, members,
                mock(CommunityInviteRepository.class), mock(CommunityJoinRequestRepository.class),
                profiles, mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        assertThrows(SecurityException.class, () -> service.updateFeatures(9L, "member",
                new com.example.desporto24.model.dto.community.UpdateCommunityFeaturesRequest(
                        true, true, true)));
        verifyNoInteractions(communities);
    }


    @Test
    void replacingCommunityPhotoDeletesPreviousStoredObject() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        MediaStorage storage = mock(MediaStorage.class);
        MultipartFile upload = mock(MultipartFile.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Community community = Community.builder().id(10L).name("Community")
                .owner(owner).visibility(CommunityVisibility.PUBLIC)
                .photoUrl("https://cdn.example.test/communities/10/previous").build();
        CommunityMember membership = CommunityMember.builder().community(community)
                .profile(owner).role(CommunityRole.OWNER).build();

        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(membership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L))
                .thenReturn(List.of(membership));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(communities.save(community)).thenReturn(community);
        when(storage.storeCommunityPhoto(anyString(), same(upload))).thenReturn(
                URI.create("https://cdn.example.test/communities/10/new-photo"));
        CommunityManagementService service = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class),
                mock(CommunityJoinRequestRepository.class), profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        ReflectionTestUtils.setField(service, "mediaStorage", storage);

        service.updatePhoto(10L, "owner", upload);

        assertEquals("https://cdn.example.test/communities/10/new-photo",
                community.getPhotoUrl());
        verify(communities).save(community);
        verify(storage).delete("communities/10/previous");
    }

    @Test
    void firstCommunityPhotoDoesNotDeleteAnObject() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        MediaStorage storage = mock(MediaStorage.class);
        MultipartFile upload = mock(MultipartFile.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Community community = Community.builder().id(10L).name("Community")
                .owner(owner).visibility(CommunityVisibility.PUBLIC).build();
        CommunityMember membership = CommunityMember.builder().community(community)
                .profile(owner).role(CommunityRole.OWNER).build();

        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(membership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L))
                .thenReturn(List.of(membership));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(communities.save(community)).thenReturn(community);
        when(storage.storeCommunityPhoto(anyString(), same(upload))).thenReturn(
                URI.create("https://cdn.example.test/communities/10/first-photo"));
        CommunityManagementService service = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class),
                mock(CommunityJoinRequestRepository.class), profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
        ReflectionTestUtils.setField(service, "mediaStorage", storage);

        service.updatePhoto(10L, "owner", upload);

        assertEquals("https://cdn.example.test/communities/10/first-photo",
                community.getPhotoUrl());
        verify(storage, never()).delete(anyString());
    }

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
    void joiningPublicCommunityResolvesExistingPendingInvite() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Profile player = new Profile();
        player.setId(2L);
        player.setUsername("player");
        Community community = Community.builder().id(10L).name("Community")
                .owner(owner).visibility(CommunityVisibility.PUBLIC).build();
        CommunityInvite pending = CommunityInvite.builder().community(community)
                .invitedProfile(player).invitedBy(owner)
                .status(CommunityInviteStatus.PENDING).build();

        when(profiles.findUserByUsernameIgnoreCase("player")).thenReturn(Optional.of(player));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(invites.findByCommunityIdAndInvitedProfileIdAndStatus(
                10L, 2L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(pending));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L)).thenReturn(List.of(
                CommunityMember.builder().community(community).profile(player)
                        .role(CommunityRole.MEMBER).build()));

        CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, requests, profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        service.joinPublic(10L, "player");

        assertEquals(CommunityInviteStatus.ACCEPTED, pending.getStatus());
        verify(invites).save(pending);
        verify(members).save(any(CommunityMember.class));
    }

    @Test
    void joiningPublicCommunityResolvesOwnPreviouslyPendingRequest() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile requester = new Profile();
        requester.setId(2L);
        requester.setUsername("requester");
        Community community = Community.builder().id(10L).name("Community")
                .owner(requester).visibility(CommunityVisibility.PUBLIC).build();
        CommunityJoinRequest pending = CommunityJoinRequest.builder()
                .community(community).requester(requester)
                .status(CommunityInviteStatus.PENDING).build();

        when(profiles.findUserByUsernameIgnoreCase("requester")).thenReturn(Optional.of(requester));
        when(communities.findById(10L)).thenReturn(Optional.of(community));
        when(requests.findByCommunityIdAndRequesterIdAndStatus(
                10L, 2L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(pending));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L)).thenReturn(List.of(
                CommunityMember.builder().community(community).profile(requester)
                        .role(CommunityRole.MEMBER).build()));

        CommunityManagementService service = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class), requests, profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        service.joinPublic(10L, "requester");

        assertEquals(CommunityInviteStatus.ACCEPTED, pending.getStatus());
        verify(requests).save(pending);
        verify(members).save(any(CommunityMember.class));
    }

    @Test
    void approvingJoinRequestResolvesExistingPendingCommunityInvite() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
        CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Profile requester = new Profile();
        requester.setId(2L);
        requester.setUsername("requester");
        Community community = Community.builder().id(10L).name("Community")
                .owner(owner).visibility(CommunityVisibility.PRIVATE).build();
        CommunityMember ownerMembership = CommunityMember.builder()
                .community(community).profile(owner).role(CommunityRole.OWNER).build();
        CommunityJoinRequest request = CommunityJoinRequest.builder()
                .community(community).requester(requester)
                .status(CommunityInviteStatus.PENDING).build();
        CommunityInvite invite = CommunityInvite.builder()
                .community(community).invitedProfile(requester).invitedBy(owner)
                .status(CommunityInviteStatus.PENDING).build();

        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(ownerMembership));
        when(requests.findById(100L)).thenReturn(Optional.of(request));
        when(invites.findByCommunityIdAndInvitedProfileIdAndStatus(
                10L, 2L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(invite));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L)).thenReturn(List.of(
                ownerMembership, CommunityMember.builder()
                        .community(community).profile(requester)
                        .role(CommunityRole.MEMBER).build()));

        CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, requests, profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        service.acceptJoinRequest(10L, 100L, "owner");

        assertEquals(CommunityInviteStatus.ACCEPTED, request.getStatus());
        assertEquals(CommunityInviteStatus.ACCEPTED, invite.getStatus());
        verify(members).save(any(CommunityMember.class));
        verify(requests).save(request);
        verify(invites).save(invite);
    }

    @Test
    void acceptingInviteResolvesExistingPendingJoinRequest() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        CommunityInviteRepository invites = mock(CommunityInviteRepository.class);
        CommunityJoinRequestRepository requests = mock(CommunityJoinRequestRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Profile invited = new Profile();
        invited.setId(2L);
        invited.setUsername("invited");
        Community community = Community.builder().id(10L).name("Community")
                .owner(owner).visibility(CommunityVisibility.PRIVATE).build();
        CommunityInvite invite = CommunityInvite.builder().community(community)
                .invitedProfile(invited).invitedBy(owner)
                .status(CommunityInviteStatus.PENDING).build();
        CommunityJoinRequest pending = CommunityJoinRequest.builder().community(community)
                .requester(invited).status(CommunityInviteStatus.PENDING).build();

        when(profiles.findUserByUsernameIgnoreCase("invited")).thenReturn(Optional.of(invited));
        when(invites.findById(100L)).thenReturn(Optional.of(invite));
        when(requests.findByCommunityIdAndRequesterIdAndStatus(
                10L, 2L, CommunityInviteStatus.PENDING)).thenReturn(Optional.of(pending));
        when(members.findByCommunityIdOrderByJoinedAtAsc(10L)).thenReturn(List.of(
                CommunityMember.builder().community(community).profile(invited)
                        .role(CommunityRole.MEMBER).build()));

        CommunityManagementService service = new CommunityManagementService(
                communities, members, invites, requests, profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        service.acceptInvite(100L, "invited");

        assertEquals(CommunityInviteStatus.ACCEPTED, invite.getStatus());
        assertEquals(CommunityInviteStatus.ACCEPTED, pending.getStatus());
        verify(members).save(any(CommunityMember.class));
        verify(requests).save(pending);
        verify(invites).save(invite);
    }

    @Test
    void adminCannotRemoveAnotherAdmin() {
        CommunityRepository communities = mock(CommunityRepository.class);
        CommunityMemberRepository members = mock(CommunityMemberRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        Profile actor = new Profile();
        actor.setId(1L);
        actor.setUsername("admin");
        Profile target = new Profile();
        target.setId(2L);
        target.setUsername("other-admin");
        Community community = Community.builder().id(10L).build();

        when(profiles.findUserByUsernameIgnoreCase("admin")).thenReturn(Optional.of(actor));
        when(profiles.findUserByUsernameIgnoreCase("other-admin")).thenReturn(Optional.of(target));
        when(members.findByCommunityIdAndProfileId(10L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(actor)
                        .role(CommunityRole.ADMIN).build()));
        when(members.findByCommunityIdAndProfileId(10L, 2L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(target)
                        .role(CommunityRole.ADMIN).build()));

        CommunityManagementService service = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class),
                mock(CommunityJoinRequestRepository.class), profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

        assertThrows(SecurityException.class,
                () -> service.removeMember(10L, "admin", "other-admin"));
        verify(members, never()).delete(any());
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
