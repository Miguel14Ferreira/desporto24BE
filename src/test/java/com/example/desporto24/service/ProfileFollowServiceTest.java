package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.social.ProfileFollow;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileFollowServiceTest {

    @Mock private ProfileFollowRepository followRepository;
    @Mock private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileFollowService service;

    @Test
    void followsAnotherProfileAndReturnsUpdatedCounts() {
        Profile actor = profile(1L, "miguel");
        Profile target = profile(2L, "ana");

        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(actor));
        when(profileRepository.findUserByUsernameIgnoreCase("ana"))
                .thenReturn(Optional.of(target));
        when(followRepository.existsByFollowerIdAndFollowingId(1L, 2L))
                .thenReturn(false, true);
        when(followRepository.existsByFollowerIdAndFollowingId(2L, 1L))
                .thenReturn(false);
        when(followRepository.countByFollowingId(2L)).thenReturn(4L);
        when(followRepository.countByFollowerId(2L)).thenReturn(7L);

        var result = service.follow("miguel", "ana");

        ArgumentCaptor<ProfileFollow> captor = ArgumentCaptor.forClass(ProfileFollow.class);
        verify(followRepository).save(captor.capture());

        assertEquals(actor, captor.getValue().getFollower());
        assertEquals(target, captor.getValue().getFollowing());
        assertTrue(result.followedByMe());
        assertFalse(result.followsMe());
        assertEquals(4L, result.followers());
        assertEquals(7L, result.following());
    }

    @Test
    void rejectsFollowingOwnProfile() {
        Profile actor = profile(1L, "miguel");

        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(actor));

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.follow("miguel", "miguel")
        );

        assertTrue(exception.getMessage().contains("próprio perfil"));
        verify(followRepository, never()).save(any());
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }
}
