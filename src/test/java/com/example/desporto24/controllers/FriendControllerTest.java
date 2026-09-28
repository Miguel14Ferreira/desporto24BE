package com.example.desporto24.controllers;

import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.friend.FriendSummaryResponse;

import java.util.List;
import com.example.desporto24.service.interfaces.FriendService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FriendController controller;

    @Test
    void myFriendListUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        FriendSummaryResponse friend = new FriendSummaryResponse(
                "ana",
                "Ana Silva",
                "Aninhas",
                null,
                false,
                false
        );
        when(friendService.getFriendSummaries("miguel")).thenReturn(List.of(friend));

        var response = controller.getMyFriendList(authentication);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(friend), response.getBody());
        verify(friendService).getFriendSummaries("miguel");
    }

    @Test
    void authenticatedBlockUsesLoggedInIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.blockFriendAuthenticated("ana", authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).blockFriend("miguel", "ana");
    }

    @Test
    void authenticatedUnblockUsesLoggedInIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.unblockFriendAuthenticated("ana", authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).unblockFriend("miguel", "ana");
    }

    @Test
    void authenticatedRemoveUsesLoggedInIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.removeFriendAuthenticated("ana", authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).removeFriend("miguel", "ana");
    }

    @Test
    void legacyFriendRequestCannotSpoofSender() {
        when(authentication.getName()).thenReturn("miguel");
        SendFriendRequest request = new SendFriendRequest();
        request.setUsername1("attacker-selected-user");
        request.setUsername2("ana");

        controller.addFriend(request, authentication);

        org.mockito.ArgumentCaptor<SendFriendRequest> captor =
                org.mockito.ArgumentCaptor.forClass(SendFriendRequest.class);
        verify(friendService).sendFriendRequest(captor.capture());
        assertEquals("miguel", captor.getValue().getUsername1());
        assertEquals("ana", captor.getValue().getUsername2());
    }

    @Test
    void legacyFriendListCannotReadAnotherUsersFriends() {
        when(authentication.getName()).thenReturn("miguel");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> controller.getFriendList("ana", authentication)
        );
    }

    @Test
    void authenticatedFriendRequestUsesLoggedInIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.addFriendAuthenticated("ana", authentication);

        assertEquals(200, response.getStatusCode().value());

        ArgumentCaptor<SendFriendRequest> captor = ArgumentCaptor.forClass(SendFriendRequest.class);
        verify(friendService).sendFriendRequest(captor.capture());
        assertEquals("miguel", captor.getValue().getUsername1());
        assertEquals("ana", captor.getValue().getUsername2());
    }
}
