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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityPhotoFramingTest {
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityManagementService service = new CommunityManagementService(
        communities, members, mock(CommunityInviteRepository.class),
        mock(CommunityJoinRequestRepository.class), profiles,
        mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));

    private final Profile owner = new Profile();
    private final Community community = Community.builder().id(42L).name("ACM").build();
    private final String gifUrl = "https://cdn.example/communities/42/demo.gif";

    @BeforeEach
    void setup() {
        owner.setId(15L);
        owner.setUsername("owner");
        community.setOwner(owner);
        community.setPhotoUrl(gifUrl);
        var membership = CommunityMember.builder()
            .community(community).profile(owner).role(CommunityRole.OWNER).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.findByCommunityIdAndProfileId(42L, 15L))
            .thenReturn(Optional.of(membership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(42L))
            .thenReturn(List.of(membership));
        when(communities.findById(42L)).thenReturn(Optional.of(community));
        when(communities.save(any(Community.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void updatesFramingWithoutChangingGifBytesOrUrl() {
        service.updatePhotoFraming(42L, "owner", 1.7, 24, 76, gifUrl);
        assertEquals(gifUrl, community.getPhotoUrl());
        assertEquals(1.7, community.getPhotoZoom());
        assertEquals(24.0, community.getPhotoFocusX());
        assertEquals(76.0, community.getPhotoFocusY());
        verify(communities).save(community);
    }

    @Test
    void showAllKeepsFullGifAndAllowsZoomOutWithoutChangingStoredUrl() {
        service.updatePhotoFraming(42L, "owner", 0.6, 50, 50, gifUrl, "SHOW_ALL");
        assertEquals("SHOW_ALL", community.getPhotoFitMode());
        assertEquals(0.6, community.getPhotoZoom());
        assertEquals(gifUrl, community.getPhotoUrl());
        verify(communities).save(community);
    }

    @Test
    void coverDoesNotPermitZoomOutAndInvalidModesAreRejected() {
        assertEquals(400, assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 0.6, 50, 50, gifUrl, "COVER")
        ).getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 1.2, 50, 50, gifUrl, "UNKNOWN")
        ).getStatusCode().value());
        verify(communities, never()).save(any());
    }

    @Test
    void staleGifCannotBeReframedEvenInShowAllMode() {
        assertEquals(409, assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 0.85, 50, 50,
                "https://cdn.example/communities/42/stale.gif", "SHOW_ALL")
        ).getStatusCode().value());
        verify(communities, never()).save(any());
    }

    @Test
    void refusesInvalidAndNonFiniteGeometry() {
        for (double bad : new double[]{Double.NaN, Double.POSITIVE_INFINITY, 0, 3.01}) {
            assertEquals(400, assertThrows(ResponseStatusException.class, () ->
                service.updatePhotoFraming(42L, "owner", bad, 50, 50, gifUrl)
            ).getStatusCode().value());
        }
        assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 2, -0.1, 50, gifUrl));
        assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 2, 50, 100.1, gifUrl));
        verify(communities, never()).save(any());
    }

    @Test
    void rejectsPhotoReplacedDuringEditing() {
        community.setPhotoUrl("https://cdn.example/communities/42/new.png");
        assertEquals(409, assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 1.5, 30, 70, gifUrl)
        ).getStatusCode().value());
        verify(communities, never()).save(any());
    }

    @Test
    void onlyOwnerOrAdminCanChangeFraming() {
        var member = CommunityMember.builder().community(community)
            .profile(owner).role(CommunityRole.MEMBER).build();
        when(members.findByCommunityIdAndProfileId(42L, 15L))
            .thenReturn(Optional.of(member));
        assertThrows(SecurityException.class, () ->
            service.updatePhotoFraming(42L, "owner", 1.25, 50, 50, gifUrl));
        verify(communities, never()).save(any());
    }

    @Test
    void cannotEditCommunityWithoutPhoto() {
        community.setPhotoUrl(null);
        assertEquals(409, assertThrows(ResponseStatusException.class, () ->
            service.updatePhotoFraming(42L, "owner", 1.5, 50, 50, gifUrl)
        ).getStatusCode().value());
        verify(communities, never()).save(any());
    }
}
