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
        lenient().when(caller.getId()).thenReturn(12L);
        lenient().when(recipient.getId()).thenReturn(13L);
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
    void missedIncomingInvitationCanBeRecoveredWithoutExposingCredentials() {
        var outgoing = calls.start("ana", "bea", "video");
        assertNull(calls.pendingFor("ana"), "The caller cannot fetch an incoming invitation");
        assertNull(calls.pendingFor("mallory"), "Other accounts cannot see the invitation");

        var recovered = calls.pendingFor("bea");
        assertNotNull(recovered);
        assertEquals(outgoing.callId(), recovered.callId());
        assertEquals("ana", recovered.caller());
        assertEquals("bea", recovered.peer());
        assertEquals("video", recovered.media());
        assertEquals(outgoing.expiresAt(), recovered.expiresAt());
        verify(inbox).requireConversationAccess("bea", "direct", "ana");

        calls.accept(outgoing.callId(), "bea");
        assertNull(calls.pendingFor("bea"), "Accepted calls must not ring again");

        calls.end(outgoing.callId(), "ana");
        assertNull(calls.pendingFor("bea"));
    }

    @Test
    void pendingRecoveryRevalidatesChatAccessAndCannotBypassBlocks() {
        calls.start("ana", "bea", "voice");
        doThrow(new org.springframework.security.access.AccessDeniedException("Blocked"))
                .when(inbox).requireConversationAccess("bea", "direct", "ana");
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> calls.pendingFor("bea"));
        assertNull(calls.pendingFor("mallory"));
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
    void invitesOnlyAuthorizedRecipientsAndIssuesRoomTokensAfterAccept() {
        Profile carlos = mock(Profile.class);
        lenient().when(carlos.getId()).thenReturn(14L);
        lenient().when(profiles.findUserByUsernameIgnoreCase("carlos")).thenReturn(Optional.of(carlos));
        var call = calls.start("ana","bea","voice");
        calls.accept(call.callId(),"bea");
        assertEquals(HttpStatus.NOT_FOUND,assertThrows(ResponseStatusException.class,
                () -> calls.accept(call.callId(),"carlos")).getStatusCode());
        calls.invite(call.callId(),"bea","carlos");
        verify(inbox).requireConversationAccess("bea","direct","carlos");
        var invitation = calls.pendingFor("carlos");
        assertNotNull(invitation);
        assertEquals("bea",invitation.caller());
        assertEquals("carlos",invitation.peer());
        assertNull(calls.pendingFor("mallory"));
        assertEquals(HttpStatus.FORBIDDEN,assertThrows(ResponseStatusException.class,
                () -> calls.members(call.callId(),"carlos")).getStatusCode());
        var extra = calls.accept(call.callId(),"carlos");
        assertEquals("jynlo-user-14",JWT.decode(extra.token()).getSubject());
        assertEquals(JWT.decode(call.token()).getClaim("video").asMap().get("room"),
                JWT.decode(extra.token()).getClaim("video").asMap().get("room"));
        assertEquals(3,calls.members(call.callId(),"ana").participants().size());
        assertTrue(calls.members(call.callId(),"ana").pending().isEmpty());
        calls.end(call.callId(),"carlos");
        assertEquals(2,calls.members(call.callId(),"ana").participants().size());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
                () -> calls.members(call.callId(),"carlos")).getStatusCode());
    }

    @Test
    void declinedOrExpiredGuestNeverTerminatesTheExistingRoom() {
        var call=calls.start("ana","bea","video");
        calls.accept(call.callId(),"bea");
        calls.invite(call.callId(),"ana","carlos");
        calls.decline(call.callId(),"carlos");
        assertNull(calls.pendingFor("carlos"));
        assertEquals(2,calls.members(call.callId(),"ana").participants().size());
        calls.invite(call.callId(),"ana","carlos");
        calls.end(call.callId(),"carlos"); // timer expired while invitation pending
        assertEquals(2,calls.members(call.callId(),"bea").participants().size());
        assertNull(calls.pendingFor("carlos"));
        assertEquals(HttpStatus.BAD_REQUEST,assertThrows(ResponseStatusException.class,
                () -> calls.invite(call.callId(),"ana","ana")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND,assertThrows(ResponseStatusException.class,
                () -> calls.invite(call.callId(),"outsider","other")).getStatusCode());
    }

    @Test
    void rejectsBlockedInviteesAndRevokesTokensWhenOriginalCallEnds() {
        Profile carlos = mock(Profile.class);
        lenient().when(carlos.getId()).thenReturn(14L);
        lenient().when(profiles.findUserByUsernameIgnoreCase("carlos")).thenReturn(Optional.of(carlos));
        var call=calls.start("ana","bea","voice");
        calls.accept(call.callId(),"bea");
        doThrow(new org.springframework.security.access.AccessDeniedException("blocked"))
                .when(inbox).requireConversationAccess("ana","direct","mallory");
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> calls.invite(call.callId(),"ana","mallory"));
        calls.invite(call.callId(),"ana","carlos");
        assertEquals(HttpStatus.BAD_REQUEST,assertThrows(ResponseStatusException.class,
                () -> calls.invite(call.callId(),"ana","carlos")).getStatusCode());
        calls.accept(call.callId(),"carlos");
        calls.end(call.callId(),"ana");
        assertThrows(ResponseStatusException.class,()->calls.members(call.callId(),"carlos"));
        assertNull(calls.pendingFor("carlos"));
        verify(gateway).publishDirectCallEvent(eq("ana"),eq("carlos"),
                eq(call.callId().toString()),eq("voice"),eq("ENDED"),eq("ana"),any());
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

    @Test
    void availabilityRequiresActualLiveKitServerAndMatchingCredentials() throws Exception {
        DirectCallService healthy = spy(calls);
        doReturn(200).when(healthy).probeProvider();
        assertEquals("ready", healthy.providerStatus());
        assertEquals("ready", healthy.providerStatus(), "Successful probe is cached");
        verify(healthy, times(1)).probeProvider();

        DirectCallService invalidKey = spy(calls);
        doReturn(401).when(invalidKey).probeProvider();
        assertEquals("invalidCredentials", invalidKey.providerStatus());

        DirectCallService insufficientPermissions = spy(calls);
        doReturn(403).when(insufficientPermissions).probeProvider();
        assertEquals("insufficientPermissions", insufficientPermissions.providerStatus(),
                "HTTP 403 must not be described as an invalid API key");

        DirectCallService unreachable = spy(calls);
        doThrow(new java.io.IOException("No network")).when(unreachable).probeProvider();
        assertEquals("providerUnavailable", unreachable.providerStatus());
    }

    @Test
    void missingSettingsNeverProbeOrExposeLiveKitCredentials() throws Exception {
        ReflectionTestUtils.setField(calls, "apiKey", "");
        DirectCallService unavailable = spy(calls);
        assertEquals("notConfigured", unavailable.providerStatus());
        verify(unavailable, never()).probeProvider();
    }
}
