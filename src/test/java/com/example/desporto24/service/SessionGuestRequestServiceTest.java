package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.session.*;
import com.example.desporto24.repository.*;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.repository.session.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionGuestRequestServiceTest {
    private final SessionRepository sessions=mock(SessionRepository.class);
    private final CommunityPlayerRepository players=mock(CommunityPlayerRepository.class);
    private final SessionGuestRequestRepository requests=mock(SessionGuestRequestRepository.class);
    private final SessionParticipantRepository participants=mock(SessionParticipantRepository.class);
    private final ProfileRepository profiles=mock(ProfileRepository.class);
    private final CommunityHistoricalPlayerLinkService historicalLinks=mock(CommunityHistoricalPlayerLinkService.class);
    private final WaitlistPromotionService waitlistPromotionService=mock(WaitlistPromotionService.class);
    private final SessionGuestRequestService service=new SessionGuestRequestService(
            sessions,players,requests,participants,profiles,historicalLinks,waitlistPromotionService);
    private final Session session=new Session();
    private final Community community=new Community();
    private final Profile organizer=new Profile();

    @BeforeEach void setup(){
        community.setId(11L);organizer.setUsername("rui");organizer.setId(5L);
        session.setId(7L);session.setCommunity(community);session.setProfile(organizer);
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(false);session.setEnabled(true);
        session.setGameDate(Date.from(Instant.parse("2099-11-10T14:00:00Z")));
        session.setMaxParticipants(10);session.setStatus(SessionStatus.OPEN);
        session.setExternalConfirmedParticipants(0);
        when(sessions.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(sessions.findById(7L)).thenReturn(Optional.of(session));
    }
    private String token(){return service.issueLink(7L,"rui");}
    private CommunityPlayer historic(){
        var p=new CommunityPlayer();p.setId(15L);p.setCommunity(community);
        p.setDisplayName("Afonso Salvador");p.setNormalizedName("afonso salvador");return p;
    }
    @Test void standaloneGameCanInviteWithoutAccountAndWithoutExposingCommunityRoster() {
        session.setCommunity(null);
        String secret=token();
        var roster=service.roster(7L,secret);
        assertNull(roster.communityId());
        assertTrue(roster.players().isEmpty());
        when(players.findByCommunityIdOrderByDisplayNameAsc(anyLong()))
                .thenThrow(new AssertionError("No historical roster for standalone games"));
        var receipt=service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,null,"Primeiro Convidado",""));
        assertEquals("PENDING",receipt.status());
        assertThrows(ResponseStatusException.class,()->service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")));
    }

    @Test void crosspostedMemberOnlyGameCanIssueSignedGuestLinkWithoutDirectCommunity() {
        session.setCommunity(null);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        String secret=token();
        assertEquals(7L,service.preview(7L,secret).id());
        assertTrue(service.roster(7L,secret).players().isEmpty());
        assertEquals("PENDING",service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,null,"Segundo Jogador","")).status());
        assertThrows(ResponseStatusException.class,()->service.preview(7L,"b".repeat(43)));
    }

    @Test void publicLinkIsOpaqueAndCannotExposeRosterWithoutToken(){
        String token=token();
        assertEquals(43,token.length());
        assertNotEquals(token,session.getGuestLinkHash());
        when(players.findByCommunityIdOrderByDisplayNameAsc(11L)).thenReturn(List.of(historic()));
        assertThrows(ResponseStatusException.class,()->service.roster(7L,"invalid-token"));
        var result=service.roster(7L,token);
        assertEquals(1,result.players().size());
        assertEquals("Afonso Salvador",result.players().getFirst().name());
    }
    @Test void signedLinkOpensCommunityOnlyGameWithoutPublicRosterPermission(){
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        String secret=token();
        assertThrows(ResponseStatusException.class,()->service.preview(7L,"invalid"));
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(1L);
        var preview=service.preview(7L,secret);
        assertEquals(7L,preview.id());
        assertEquals(1,preview.participantCount());
        var person=historic();
        when(players.findById(15L)).thenReturn(Optional.of(person));
        assertEquals("PENDING",service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")).status());
        session.setPrivateGame(true);
        assertThrows(ResponseStatusException.class,()->service.preview(7L,secret));
    }
    @Test void onlyUnlinkedCommunityPlayerCanRequestAndGetsPendingReceipt(){
        String token=token();var person=historic();
        when(players.findById(15L)).thenReturn(Optional.of(person));
        var receipt=service.request(7L,new SessionGuestRequestService.GuestRequestInput(
                token,15L,null,"Chego a horas"));
        assertEquals("PENDING",receipt.status());
        assertEquals(43,receipt.receipt().length());
        var captor=org.mockito.ArgumentCaptor.forClass(SessionGuestRequest.class);
        verify(requests).saveAndFlush(captor.capture());
        assertEquals("Afonso Salvador",captor.getValue().getDisplayName());
        assertNotEquals(receipt.receipt(),captor.getValue().getReceiptHash());
        assertEquals(SessionGuestRequest.State.PENDING,captor.getValue().getStatus());
        assertNull(captor.getValue().getTeamCode());
    }
    @Test void linkedProfilesCannotBeImpersonatedByGuestRequest(){
        String token=token();var person=historic();person.setProfile(organizer);
        when(players.findById(15L)).thenReturn(Optional.of(person));
        assertThrows(ResponseStatusException.class,
                ()->service.request(7L,new SessionGuestRequestService.GuestRequestInput(token,15L,null,"")));
        verify(requests,never()).saveAndFlush(any());
    }
    @Test void requestCannotSelectTheSameNameTwice(){
        String token=token();var person=historic();
        when(players.findById(15L)).thenReturn(Optional.of(person));
        when(requests.findBySessionIdAndNormalizedName(7L,"afonso salvador"))
                .thenReturn(Optional.of(new SessionGuestRequest()));
        assertThrows(ResponseStatusException.class,()->service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(token,15L,null,"")));
        verify(requests,never()).saveAndFlush(any());
    }
    @Test void organizerApprovalReservesExactlyOneSeatWithoutCreatingUser(){
        var guest=new SessionGuestRequest();guest.setId(20L);guest.setSession(session);
        guest.setDisplayName("Afonso Salvador");guest.setStatus(SessionGuestRequest.State.PENDING);
        when(requests.findById(20L)).thenReturn(Optional.of(guest));
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(1L);
        var approved=service.review(7L,20L,true,"rui");
        assertEquals("APPROVED",approved.status());
        assertEquals(1,session.getExternalConfirmedParticipants());
        assertNotNull(guest.getReviewedAt());
        verify(sessions).save(session);
        verify(requests).save(guest);
        assertThrows(ResponseStatusException.class,()->service.review(7L,20L,true,"rui"));
    }
    @Test void onlyOrganizerCanApproveAndOnlyWhileCapacityExists(){
        var guest=new SessionGuestRequest();guest.setId(20L);guest.setSession(session);
        guest.setStatus(SessionGuestRequest.State.PENDING);
        when(requests.findById(20L)).thenReturn(Optional.of(guest));
        assertThrows(AccessDeniedException.class,()->service.review(7L,20L,true,"intruso"));
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(10L);
        assertThrows(ResponseStatusException.class,()->service.review(7L,20L,true,"rui"));
        assertEquals(0,session.getExternalConfirmedParticipants());
        verify(requests,never()).save(guest);
    }
    @Test void approvedGuestExplicitlyAssociatesVerifiedProfileWithoutDoubleBooking(){
        var person=historic();
        var guest=new SessionGuestRequest();
        guest.setId(25L);guest.setSession(session);guest.setCommunityPlayer(person);
        guest.setDisplayName("Afonso Salvador");guest.setStatus(SessionGuestRequest.State.APPROVED);
        var owner=new Profile();owner.setId(80L);owner.setUsername("afonso");
        String receipt="B".repeat(43);
        // The hash is only ever held server-side; an invalid secret never reaches linking.
        String digest;
        try{
            byte[] raw=java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            digest=java.util.HexFormat.of().formatHex(raw);
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest)).thenReturn(Optional.of(guest));
        when(profiles.findUserByUsernameIgnoreCase("afonso")).thenReturn(Optional.of(owner));
        session.setExternalConfirmedParticipants(1);
        var result=service.linkAccount(7L,new SessionGuestRequestService.ReceiptInput(receipt),"afonso");
        assertEquals("LINKED",result.status());
        assertEquals(0,session.getExternalConfirmedParticipants());
        assertEquals(SessionGuestRequest.State.LINKED,guest.getStatus());
        assertEquals(owner,guest.getLinkedProfile());
        verify(participants).saveAndFlush(argThat(p->p.getProfile()==owner
                && p.getState()==ParticipantState.ACTIVE
                && p.getAttendanceStatus()==AttendanceStatus.PENDING));
        verify(historicalLinks).claimOrganizerApprovedGuest(11L,"afonso",15L);
        verify(sessions).save(session);
    }
    @Test void unapprovedGuestCannotLinkToSomebodyElsesAuthenticatedAccount(){
        var guest=new SessionGuestRequest();guest.setId(25L);guest.setSession(session);
        guest.setStatus(SessionGuestRequest.State.PENDING);
        String receipt="C".repeat(43);
        String digest;
        try{digest=java.util.HexFormat.of().formatHex(
                java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest)).thenReturn(Optional.of(guest));
        var account=new Profile();account.setId(88L);account.setUsername("intruso");
        when(profiles.findUserByUsernameIgnoreCase("intruso")).thenReturn(Optional.of(account));
        assertThrows(ResponseStatusException.class,()->service.linkAccount(7L,
                new SessionGuestRequestService.ReceiptInput(receipt),"intruso"));
        verifyNoInteractions(historicalLinks);
        verify(participants,never()).saveAndFlush(any());
    }
    @Test void organizerCancellationReleasesOneSeatAndClearsBothKindsOfTeam(){
        var guest=new SessionGuestRequest();
        guest.setId(20L);guest.setSession(session);
        guest.setDisplayName("Afonso Salvador");
        guest.setStatus(SessionGuestRequest.State.APPROVED);
        guest.setTeamCode("A");
        session.setExternalConfirmedParticipants(1);
        session.setStatus(SessionStatus.FULL);
        when(requests.findById(20L)).thenReturn(Optional.of(guest));
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(1L);

        var registered=new SessionParticipant();registered.setTeamCode("B");
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(7L,ParticipantState.ACTIVE))
                .thenReturn(List.of(registered));
        var other=new SessionGuestRequest();other.setTeamCode("A");
        when(requests.findBySessionIdAndStatusOrderByCreatedAtAsc(7L,SessionGuestRequest.State.APPROVED))
                .thenReturn(List.of(other));
        when(waitlistPromotionService.promoteNext(eq(session),any(Instant.class)))
                .thenReturn(Optional.empty());

        var result=service.cancel(7L,20L,"rui");
        assertEquals("CANCELLED",result.status());
        assertEquals(SessionGuestRequest.State.CANCELLED,guest.getStatus());
        assertEquals(0,session.getExternalConfirmedParticipants());
        assertEquals(SessionStatus.OPEN,session.getStatus());
        assertNull(guest.getTeamCode());
        assertNull(registered.getTeamCode());
        assertNull(other.getTeamCode());
        verify(participants).saveAll(List.of(registered));
        verify(requests).saveAll(List.of(other));
        verify(waitlistPromotionService).promoteNext(eq(session),any(Instant.class));
        assertThrows(ResponseStatusException.class,()->service.cancel(7L,20L,"rui"));
        assertEquals(0,session.getExternalConfirmedParticipants());
    }
    @Test void cannotCancelOthersReservationOrDoubleReleaseLinkedMember(){
        var guest=new SessionGuestRequest();guest.setId(20L);guest.setSession(session);
        guest.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findById(20L)).thenReturn(Optional.of(guest));
        session.setExternalConfirmedParticipants(1);
        assertThrows(AccessDeniedException.class,()->service.cancel(7L,20L,"outsider"));
        assertEquals(1,session.getExternalConfirmedParticipants());
        guest.setStatus(SessionGuestRequest.State.LINKED);
        assertThrows(ResponseStatusException.class,()->service.cancel(7L,20L,"rui"));
        assertEquals(1,session.getExternalConfirmedParticipants());
        verifyNoInteractions(waitlistPromotionService);
    }
    @Test void rejectsStatusLookupWithoutPossessionOfReceipt(){
        assertThrows(ResponseStatusException.class,()->service.status(7L,
                new SessionGuestRequestService.ReceiptInput("name-only")));
        verify(requests,never()).findBySessionIdAndReceiptHash(anyLong(),anyString());
    }
}
