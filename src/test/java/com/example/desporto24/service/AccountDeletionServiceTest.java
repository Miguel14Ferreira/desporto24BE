package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.exception.AccountDeletionBlockedException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ConfirmationTokenRepository;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.FriendRequestRepository;
import com.example.desporto24.repository.MFAVerificationRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.moderation.UserBlockRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import com.example.desporto24.security.PasswordHashService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDeletionServiceTest {

    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityInviteRepository communityInviteRepository;
    @Mock private CommunityMemberRepository communityMemberRepository;
    @Mock private ProfileFollowRepository profileFollowRepository;
    @Mock private SocialPostRepository socialPostRepository;
    @Mock private SocialPostCommentRepository socialPostCommentRepository;
    @Mock private SocialPostLikeRepository socialPostLikeRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private SessionParticipantRepository sessionParticipantRepository;
    @Mock private SessionWaitlistRepository sessionWaitlistRepository;
    @Mock private SessionMessageRepository sessionMessageRepository;
    @Mock private WaitlistPromotionService waitlistPromotionService;
    @Mock private SessionNotificationService sessionNotificationService;
    @Mock private UserBlockRepository userBlockRepository;
    @Mock private ChatRepository chatRepository;
    @Mock private FriendRepository friendRepository;
    @Mock private FriendRequestRepository friendRequestRepository;
    @Mock private NotificationsRepository notificationsRepository;
    @Mock private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock private MFAVerificationRepository mfaVerificationRepository;
    @Mock private PasswordHashService passwordHashService;

    @InjectMocks
    private AccountDeletionService service;

    @Test
    void deletionIsBlockedWhileUserOwnsACommunity() {
        Profile profile = profile();
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(profile));
        when(communityRepository.existsByOwnerId(7L)).thenReturn(true);

        assertThrows(
                AccountDeletionBlockedException.class,
                () -> service.deleteMyAccount("miguel")
        );

        verify(profileRepository, never()).saveAndFlush(any(Profile.class));
    }

    @Test
    void deletionAnonymizesProfileAndInvalidatesAccess() {
        Profile profile = profile();
        profile.setEmail("miguel@example.com");
        profile.setFullName("Miguel Ferreira");
        profile.setGoogleSubject("google-subject");
        profile.setMFA(true);
        profile.setMfaSecret("encrypted-secret");
        profile.setTokenVersion(4L);

        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(profile));
        when(communityRepository.existsByOwnerId(7L)).thenReturn(false);
        when(socialPostRepository.findByAuthorId(7L)).thenReturn(List.of());
        when(sessionRepository.findByProfileIdOrderByGameDateAsc(7L))
                .thenReturn(List.of());
        when(sessionParticipantRepository
                .findByProfileIdAndStateOrderByJoinedAtAsc(
                        7L, ParticipantState.ACTIVE))
                .thenReturn(List.of());
        when(passwordHashService.encode(anyString()))
                .thenReturn("disabled-hash");

        service.deleteMyAccount("miguel");

        assertTrue(profile.getUsername().startsWith("deleted-7-"));
        assertTrue(profile.getEmail().endsWith("@deleted.invalid"));
        assertNull(profile.getFullName());
        assertNull(profile.getGoogleSubject());
        assertFalse(profile.getMFA());
        assertNull(profile.getMfaSecret());
        assertFalse(profile.getEnabled());
        assertFalse(profile.getNotLocked());
        assertEquals(5L, profile.getTokenVersion());
        assertEquals("disabled-hash", profile.getPassword());

        verify(profileFollowRepository)
                .deleteByFollowerIdOrFollowingId(7L, 7L);
        verify(friendRepository)
                .deleteByProfile1IdOrProfile2Id(7L, 7L);
        verify(chatRepository)
                .deleteByUsername1OrUsername2("miguel", "miguel");
        verify(confirmationTokenRepository)
                .deleteByProfile("miguel");
        verify(mfaVerificationRepository)
                .deleteByPerfil("miguel");
        verify(profileRepository).saveAndFlush(profile);
        verify(profileRepository, never()).delete(any(Profile.class));
    }

    private Profile profile() {
        Profile profile = new Profile();
        profile.setId(7L);
        profile.setUsername("miguel");
        profile.setUserId("user-7");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        return profile;
    }
}
