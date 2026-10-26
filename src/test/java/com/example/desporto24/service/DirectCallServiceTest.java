package com.example.desporto24.service;

import com.auth0.jwt.JWT;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.websocket.ChatRealtimeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectCallServiceTest {
    @Mock InboxSummaryService inbox;
    @Mock ProfileRepository profiles;
    @Mock ChatRealtimeGateway gateway;
    @InjectMocks DirectCallService calls;

    @BeforeEach
    void configure() {
        ReflectionTestUtils.setField(calls, "serverUrl", "wss://example.livekit.cloud");
        ReflectionTestUtils.setField(calls, "apiKey", "jynlo-api-key");
        ReflectionTestUtils.setField(calls, "apiSecret", "only-for-tests-long-enough-livekit-secret");
        Profile caller = mock(Profile.class);
        Profile recipient = mock(Profile.class);
        when(caller.getId()).thenReturn(12L);
        when(recipient.getId()).thenReturn(13L);
        lenient().when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(caller));
        lenient().when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(recipient));
    }

    @Test
    void createsScopedTokensForExactlyTwoAuthorizedMembers() {
        var outgoing = calls.start("ana", "bea", "video");
        verify(inbox).requireConversationAccess("ana", "direct", "bea");
        assertEquals("bea", outgoing.peer());
        assertEquals("video", outgoing.media());
        assertEquals("wss://example.livekit.cloud", outgoing.serverUrl());
        var first = JWT.decode(outgoing.token());
        assertEquals("jynlo-api-key", first.getIssuer());
        assertEquals("jynlo-user-12", first.getSubject());
        assertEquals(true, first.getClaim("video").asMap().get("roomJoin"));
        assertEquals(false, first.getClaim("video").asMap().get("canPublishData"));
        assertTrue(first.getClaim("video").asMap().get("room").toString().startsWith("jynlo-"));
        verify(gateway).publishDirectCallEvent(eq("ana"), eq("bea"), eq(outgoing.callId().toString()),
                eq("video"), eq("RINGING"), eq("ana"), any());

        var accepted = calls.accept(outgoing.callId(), "bea");
        verify(inbox).requireConversationAccess("bea", "direct", "ana");
        assertEquals(outgoing.callId(), accepted.callId());
        assertEquals("ana", accepted.peer());
        assertEquals("jynlo-user-13", JWT.decode(accepted.token()).getSubject());
        assertEquals(first.getClaim("video").asMap().get("room"),
                JWT.decode(accepted.token()).getClaim("video").asMap().get("room"));

        calls.end(outgoing.callId(), "ana");
        verify(gateway).publishDirectCallEvent(eq("ana"), eq("bea"), eq(outgoing.callId().toString()),
                eq("video"), eq("ENDED"), eq("ana"), any());
    }

    @Test
    void deniesUnknownParticipantAndCallerAcceptance() {
        var outgoing = calls.start("ana", "bea", "voice");
        ResponseStatusException outsider = assertThrows(ResponseStatusException.class,
                () -> calls.accept(outgoing.callId(), "mallory"));
        assertEquals(HttpStatus.NOT_FOUND, outsider.getStatusCode());
        ResponseStatusException caller = assertThrows(ResponseStatusException.class,
                () -> calls.accept(outgoing.callId(), "ana"));
        assertEquals(HttpStatus.FORBIDDEN, caller.getStatusCode());
        calls.decline(outgoing.callId(), "bea");
        assertThrows(ResponseStatusException.class, () -> calls.accept(outgoing.callId(), "bea"));
    }

    @Test
    void neverStartsWithoutProviderSettings() {
        ReflectionTestUtils.setField(calls, "apiSecret", "");
        var failure = assertThrows(ResponseStatusException.class,
                () -> calls.start("ana", "bea", "voice"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, failure.getStatusCode());
        verifyNoInteractions(inbox, gateway);
    }

    @Test
    void respectsChatAclAndPreventsOverlappingCalls() {
        var start = calls.start("ana", "bea", "video");
        var conflict = assertThrows(ResponseStatusException.class,
                () -> calls.start("bea", "ana", "voice"));
        assertEquals(HttpStatus.CONFLICT, conflict.getStatusCode());
        calls.end(start.callId(), "ana");
        doThrow(new org.springframework.security.access.AccessDeniedException("No chat"))
                .when(inbox).requireConversationAccess("bea", "direct", "ana");
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> calls.start("bea", "ana", "voice"));
    }

    @Test
    void refusesSelfCallsAndInvalidMediaWithoutSendingNotifications() {
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> calls.start("ana", "ana", "voice")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> calls.start("ana", "bea", "screen")).getStatusCode());
        verifyNoInteractions(gateway);
    }
}
