package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.social.StoryResponse;
import com.example.desporto24.model.social.SocialStory;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialStoryRepository;
import com.example.desporto24.storage.MediaStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocialStoryServiceTest {
    @Mock private SocialStoryRepository stories;
    @Mock private ProfileRepository profiles;
    @Mock private CommunityRepository communities;
    @Mock private CommunityMemberRepository memberships;
    @Mock private ProfileFollowRepository follows;
    @Mock private MediaStorage storage;
    @InjectMocks private SocialStoryService service;

    private Profile actor;

    @BeforeEach
    void init() {
        actor = new Profile();
        actor.setId(19L);
        actor.setUsername("joao");
        when(profiles.findUserByUsernameIgnoreCase("joao")).thenReturn(Optional.of(actor));
    }

    @Test
    void acceptsOnePictureAndExpiresExactly24HoursAfterCreation() {
        MockMultipartFile image = new MockMultipartFile("image", "photo.jpg", "image/jpeg", new byte[] {1,2,3});
        when(storage.store(startsWith("stories/19/"), same(image)))
                .thenReturn(URI.create("/media/stories/19/photo.jpg"));
        when(stories.save(any(SocialStory.class))).thenAnswer(call -> {
            SocialStory story = call.getArgument(0);
            story.setId(7L);
            return story;
        });

        StoryResponse response = service.create("joao", null, "Primeiro jogo!", image, null);

        assertEquals(7L, response.id());
        assertEquals("Primeiro jogo!", response.caption());
        assertNull(response.communityId());
        assertTrue(response.canDelete());
        assertFalse(response.video());
        assertEquals(24, ChronoUnit.HOURS.between(response.createdAt(), response.expiresAt()));
        assertEquals(0, ChronoUnit.MINUTES.between(response.createdAt(), response.expiresAt()) % 60);
    }

    @Test
    void rejectsMissingAndConflictingMediaBeforeSaving() {
        MockMultipartFile image = new MockMultipartFile("image", "a.jpg", "image/jpeg", new byte[]{1});
        MockMultipartFile video = new MockMultipartFile("video", "a.mp4", "video/mp4", new byte[]{1});
        assertThrows(IllegalArgumentException.class, () -> service.create("joao", null, "", null, null));
        assertThrows(IllegalArgumentException.class, () -> service.create("joao", null, "", image, video));
        verifyNoInteractions(storage, stories);
    }

    @Test
    void rejectsRegularMemberPublishingOnBehalfOfCommunity() {
        Community community = publicCommunity();
        CommunityMember member = new CommunityMember();
        member.setRole(CommunityRole.MEMBER);
        when(communities.findById(5L)).thenReturn(Optional.of(community));
        when(memberships.findByCommunityIdAndProfileId(5L, 19L)).thenReturn(Optional.of(member));
        MockMultipartFile image = new MockMultipartFile("image", "a.jpg", "image/jpeg", new byte[]{1});

        assertThrows(AccessDeniedException.class, () -> service.create("joao", 5L, "Olá", image, null));
        verifyNoInteractions(storage, stories);
    }

    @Test
    void neverUploadsPrivateCommunityMediaToPublicStorage() {
        Community community = publicCommunity();
        community.setVisibility(CommunityVisibility.PRIVATE);
        community.setOwner(actor);
        when(communities.findById(5L)).thenReturn(Optional.of(community));
        MockMultipartFile image = new MockMultipartFile("image", "a.jpg", "image/jpeg", new byte[]{1});

        assertThrows(AccessDeniedException.class, () -> service.create("joao", 5L, "Olá", image, null));
        verifyNoInteractions(storage, stories);
    }

    @Test
    void refusesAccessToExpiredStory() {
        SocialStory story = SocialStory.builder().id(4L).author(actor)
                .createdAt(Instant.now().minus(25, ChronoUnit.HOURS))
                .expiresAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .mediaUrl("/media/stories/a.jpg").mediaStorageKey("stories/a.jpg")
                .build();
        when(stories.findById(4L)).thenReturn(Optional.of(story));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.view(4L, "joao"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    private Community publicCommunity() {
        Profile owner = new Profile();
        owner.setId(200L);
        Community community = new Community();
        community.setId(5L);
        community.setName("CNG");
        community.setOwner(owner);
        community.setVisibility(CommunityVisibility.PUBLIC);
        return community;
    }
}
