package com.example.desporto24.service;

import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.moderation.UserBlock;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.moderation.UserBlockRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBlockServiceTest {

    @Mock private UserBlockRepository userBlockRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private FriendRepository friendRepository;
    @Mock private ProfileFollowRepository followRepository;

    private UserBlockService service;
    private Profile miguel;
    private Profile ana;

    @BeforeEach
    void setUp() {
        service = new UserBlockService(
                userBlockRepository,
                profileRepository,
                friendRepository,
                followRepository
        );
        miguel = profile(1L, "miguel");
        ana = profile(2L, "ana");
    }

    @Test
    void blocksUserAndRemovesExistingSocialLinks() {
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(miguel));
        when(profileRepository.findUserByUsernameIgnoreCase("ana"))
                .thenReturn(Optional.of(ana));

        Friend friendship = new Friend();
        friendship.setProfile1(miguel);
        friendship.setProfile2(ana);

        when(userBlockRepository.existsByBlockerIdAndBlockedId(1L, 2L))
                .thenReturn(false);
        when(friendRepository.findByProfile1OrProfile2(miguel, miguel))
                .thenReturn(List.of(friendship));

        var result = service.blockUser("miguel", "ana");

        assertTrue(result.blocked());

        ArgumentCaptor<UserBlock> captor = ArgumentCaptor.forClass(UserBlock.class);
        verify(userBlockRepository).save(captor.capture());
        assertEquals(miguel, captor.getValue().getBlocker());
        assertEquals(ana, captor.getValue().getBlocked());
        assertNotNull(captor.getValue().getCreatedAt());

        verify(followRepository).deleteByFollowerIdAndFollowingId(1L, 2L);
        verify(followRepository).deleteByFollowerIdAndFollowingId(2L, 1L);
        verify(friendRepository).delete(friendship);
    }

    @Test
    void cannotBlockOwnProfile() {
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(miguel));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.blockUser("miguel", "miguel")
        );

        verify(userBlockRepository, never()).save(any());
    }

    @Test
    void blockInEitherDirectionPreventsInteraction() {
        when(userBlockRepository.existsBetween(1L, 2L)).thenReturn(true);

        assertThrows(
                AccessDeniedException.class,
                () -> service.ensureInteractionAllowed(miguel, ana)
        );
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }
}
