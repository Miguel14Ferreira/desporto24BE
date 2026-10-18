package com.example.desporto24.service.impl;

import com.example.desporto24.exception.RequestFriendException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;
import com.example.desporto24.service.interfaces.NotificationService;
import com.example.desporto24.service.interfaces.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_ALREADY_PENDING;
import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_SELF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FriendServiceImplTest {

    private ProfileService profileService;
    private FriendRequestService friendRequestService;
    private EmailService emailService;
    private FriendRepository friendRepository;
    private NotificationService notificationService;
    private ProfileRepository profileRepository;
    private UserBlockService userBlockService;
    private FriendServiceImpl service;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        friendRequestService = mock(FriendRequestService.class);
        emailService = mock(EmailService.class);
        friendRepository = mock(FriendRepository.class);
        notificationService = mock(NotificationService.class);
        profileRepository = mock(ProfileRepository.class);
        userBlockService = mock(UserBlockService.class);

        service = new FriendServiceImpl(
                profileService,
                friendRequestService,
                emailService,
                friendRepository,
                notificationService,
                profileRepository,
                userBlockService
        );
    }

    @Test
    void newFriendRequestSendsBilingualEmailWithSafeSenderAndNotificationsLink() {
        ProfileDTO senderDto = profileDto("owner<script>", "owner@example.test");
        ProfileDTO recipientDto = profileDto("recipient", "recipient@example.test");
        Profile sender = profile(1L, "owner<script>");
        Profile recipient = profile(2L, "recipient");
        when(profileService.findUserByUsername("owner<script>")).thenReturn(senderDto);
        when(profileService.findUserByUsername("recipient")).thenReturn(recipientDto);
        when(profileRepository.findUserByUsername("owner<script>")).thenReturn(Optional.of(sender));
        when(profileRepository.findUserByUsername("recipient")).thenReturn(Optional.of(recipient));
        when(friendRepository.findConnection(1L, 2L)).thenReturn(Optional.empty());
        org.springframework.test.util.ReflectionTestUtils.setField(service, "uiAppUrl", "https://example.test/");

        service.sendFriendRequest(request("owner<script>", "recipient"));

        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(org.mockito.ArgumentMatchers.eq("recipient@example.test"),
                org.mockito.ArgumentMatchers.eq("Pedido de amizade | Friend request — Desporto24"), html.capture());
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("owner&lt;script&gt;"));
        org.junit.jupiter.api.Assertions.assertFalse(html.getValue().contains("<script>"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Ver pedido / View request"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("href=\"https://example.test/menu/notifications\""));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("accept or reject the request"));
        verify(friendRequestService).saveFriendRequest(any());
        verify(notificationService).save(any());
    }

    @Test
    void cannotSendFriendRequestToSelf() {
        SendFriendRequest request = request("miguel", "miguel");

        RequestFriendException exception = assertThrows(
                RequestFriendException.class,
                () -> service.sendFriendRequest(request)
        );

        assertEquals(FRIEND_REQUEST_SELF, exception.getMessage());
        verify(profileService, never()).findUserByUsername(any());
        verify(friendRequestService, never()).saveFriendRequest(any());
    }

    @Test
    void duplicatePendingRequestIsRejected() {
        ProfileDTO miguelDto = profileDto("miguel", "miguel@example.com");
        ProfileDTO anaDto = profileDto("ana", "ana@example.com");
        Profile miguel = profile(1L, "miguel");
        Profile ana = profile(2L, "ana");

        when(profileService.findUserByUsername("miguel")).thenReturn(miguelDto);
        when(profileService.findUserByUsername("ana")).thenReturn(anaDto);
        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(miguel));
        when(profileRepository.findUserByUsername("ana")).thenReturn(Optional.of(ana));
        when(friendRepository.findConnection(1L, 2L))
                .thenReturn(Optional.empty());
        when(friendRequestService.hasPendingRequestBetween("miguel", "ana"))
                .thenReturn(true);

        RequestFriendException exception = assertThrows(
                RequestFriendException.class,
                () -> service.sendFriendRequest(request("miguel", "ana"))
        );

        assertEquals(FRIEND_REQUEST_ALREADY_PENDING, exception.getMessage());
        verify(friendRequestService, never()).saveFriendRequest(any());
        org.mockito.Mockito.verifyNoInteractions(emailService);
    }

    @Test
    void blockedUserCannotUnblockChatOwnedByOtherUser() {
        Profile miguel = profile(1L, "miguel");
        Profile ana = profile(2L, "ana");
        Friend friendship = friendship(miguel, ana, true, "ana");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(miguel));
        when(profileRepository.findUserByUsername("ana"))
                .thenReturn(Optional.of(ana));
        when(friendRepository.findConnection(1L, 2L))
                .thenReturn(Optional.of(friendship));

        assertThrows(
                AccessDeniedException.class,
                () -> service.unblockFriend("miguel", "ana")
        );

        verify(friendRepository, never()).save(any());
        assertEquals("ana", friendship.getWhoBlocked());
        org.junit.jupiter.api.Assertions.assertTrue(friendship.isChatBlocked());
    }

    @Test
    void blockedUserCannotOverwriteBlockOwner() {
        Profile miguel = profile(1L, "miguel");
        Profile ana = profile(2L, "ana");
        Friend friendship = friendship(miguel, ana, true, "ana");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(miguel));
        when(profileRepository.findUserByUsername("ana"))
                .thenReturn(Optional.of(ana));
        when(friendRepository.findConnection(1L, 2L))
                .thenReturn(Optional.of(friendship));

        assertThrows(
                AccessDeniedException.class,
                () -> service.blockFriend("miguel", "ana")
        );

        verify(friendRepository, never()).save(any());
        assertEquals("ana", friendship.getWhoBlocked());
    }

    @Test
    void blockerCanUnblockOwnChat() {
        Profile miguel = profile(1L, "miguel");
        Profile ana = profile(2L, "ana");
        Friend friendship = friendship(miguel, ana, true, "miguel");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(miguel));
        when(profileRepository.findUserByUsername("ana"))
                .thenReturn(Optional.of(ana));
        when(friendRepository.findConnection(1L, 2L))
                .thenReturn(Optional.of(friendship));

        service.unblockFriend("miguel", "ana");

        verify(friendRepository).save(friendship);
        org.junit.jupiter.api.Assertions.assertFalse(friendship.isChatBlocked());
        org.junit.jupiter.api.Assertions.assertNull(friendship.getWhoBlocked());
    }

    @Test
    void pendingRequestCheckIsDirectionIndependent() {
        when(friendRequestService.hasPendingRequestBetween("ana", "miguel"))
                .thenReturn(true);

        boolean pending = friendRequestService.hasPendingRequestBetween(
                "ana",
                "miguel"
        );

        org.junit.jupiter.api.Assertions.assertTrue(pending);
    }

    private SendFriendRequest request(String first, String second) {
        SendFriendRequest request = new SendFriendRequest();
        request.setUsername1(first);
        request.setUsername2(second);
        return request;
    }

    private ProfileDTO profileDto(String username, String email) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(username);
        dto.setEmail(email);
        return dto;
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Friend friendship(
            Profile first,
            Profile second,
            boolean blocked,
            String whoBlocked) {
        Friend friend = new Friend();
        friend.setProfile1(first);
        friend.setProfile2(second);
        friend.setChatBlocked(blocked);
        friend.setWhoBlocked(whoBlocked);
        return friend;
    }
}
