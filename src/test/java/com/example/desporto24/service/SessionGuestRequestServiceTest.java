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
    private final SessionCommunityShareRepository sharedCommunities=mock(SessionCommunityShareRepository.class);
    private final CommunityPlayerRepository players=mock(CommunityPlayerRepository.class);
    private final CommunityHistoricalPlayerLinkRepository links=mock(CommunityHistoricalPlayerLinkRepository.class);
    private final SessionGuestRequestRepository requests=mock(SessionGuestRequestRepository.class);
    private final SessionParticipantRepository participants=mock(SessionParticipantRepository.class);
    private final ProfileRepository profiles=mock(ProfileRepository.class);
    private final CommunityHistoricalPlayerLinkService historicalLinks=mock(CommunityHistoricalPlayerLinkService.class);
    private final WaitlistPromotionService waitlistPromotionService=mock(WaitlistPromotionService.class);
    private final SessionGuestRequestService service=new SessionGuestRequestService(
            sessions,sharedCommunities,players,links,requests,participants,profiles,historicalLinks,waitlistPromotionService);
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
        when(links.findByCommunityIdAndStatus(11L,"ACCEPTED")).thenReturn(List.of());
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
        assertEquals("APPROVED",receipt.status());
        assertThrows(ResponseStatusException.class,()->service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")));
    }

    @Test void crosspostedMemberOnlyGameCanIssueSignedGuestLinkWithoutDirectCommunity() {
        session.setCommunity(null);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        String secret=token();
        assertEquals(7L,service.preview(7L,secret).id());
        assertTrue(service.roster(7L,secret).players().isEmpty());
        assertEquals("APPROVED",service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,null,"Segundo Jogador","")).status());
        assertThrows(ResponseStatusException.class,()->service.preview(7L,"b".repeat(43)));
    }

    @Test void oneCommunityCrosspostAllowsHistoricalPlayerChoiceWithoutAccount(){
        session.setCommunity(null);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        when(sharedCommunities.findBySession_IdOrderByIdAsc(7L))
                .thenReturn(List.of(new SessionCommunityShare(session,community)));
        when(players.findByCommunityIdOrderByDisplayNameAsc(11L))
                .thenReturn(List.of(historic()));
        var person=historic();
        when(players.findById(15L)).thenReturn(Optional.of(person));
        String secret=token();
        var roster=service.roster(7L,secret);
        assertEquals(11L,roster.communityId());
        assertEquals("Afonso Salvador",roster.players().getFirst().name());
        assertEquals("APPROVED",service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")).status());
    }

    @Test void multiCommunityCrosspostDoesNotExposeCombinedHistoricalPlayers(){
        session.setCommunity(null);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        Community other=new Community();other.setId(22L);
        when(sharedCommunities.findBySession_IdOrderByIdAsc(7L)).thenReturn(List.of(
                new SessionCommunityShare(session,community),
                new SessionCommunityShare(session,other)));
        var roster=service.roster(7L,token());
        assertNull(roster.communityId());
        assertTrue(roster.players().isEmpty());
        verify(players,never()).findByCommunityIdOrderByDisplayNameAsc(anyLong());
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
        assertEquals("APPROVED",service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")).status());
        session.setPrivateGame(true);
        assertThrows(ResponseStatusException.class,()->service.preview(7L,secret));
    }
    @Test void confirmedMemberSeesApprovedGuestsButNotPrivateNotesOrCancelledNames() {
        Profile monica=new Profile();monica.setId(12L);monica.setUsername("monicagomes");
        when(profiles.findUserByUsernameIgnoreCase("monicagomes")).thenReturn(Optional.of(monica));
        when(participants.existsBySessionIdAndProfileIdAndState(
                7L,12L,ParticipantState.ACTIVE)).thenReturn(true);
        var guest=new SessionGuestRequest();guest.setId(41L);
        guest.setDisplayName("oal");guest.setNote("Não divulgar");
        guest.setTeamCode("A");guest.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findBySessionIdAndStatusOrderByCreatedAtAsc(
                7L,SessionGuestRequest.State.APPROVED)).thenReturn(List.of(guest));
        var visible=service.confirmedGuestsForMember(7L,"monicagomes");
        assertEquals(1,visible.size());
        assertEquals("oal",visible.getFirst().name());
        assertEquals("A",visible.getFirst().teamCode());
        assertEquals(41L,visible.getFirst().id());
        assertEquals(3,SessionGuestRequestService.ConfirmedGuest.class.getRecordComponents().length);
        verify(requests,never()).findBySessionIdOrderByCreatedAtAsc(7L);
    }

    @Test void creatorCanSeeApprovedGuestsWithoutJoiningAndStrangerCannot() {
        var guest=new SessionGuestRequest();guest.setId(42L);
        guest.setDisplayName("Fresco");guest.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findBySessionIdAndStatusOrderByCreatedAtAsc(
                7L,SessionGuestRequest.State.APPROVED)).thenReturn(List.of(guest));
        assertEquals("Fresco",service.confirmedGuestsForMember(7L,"RUI").getFirst().name());
        Profile stranger=new Profile();stranger.setId(29L);
        when(profiles.findUserByUsernameIgnoreCase("outsider")).thenReturn(Optional.of(stranger));
        assertThrows(AccessDeniedException.class,
                ()->service.confirmedGuestsForMember(7L,"outsider"));
        assertThrows(AccessDeniedException.class,
                ()->service.confirmedGuestsForMember(7L,null));
        verify(participants).existsBySessionIdAndProfileIdAndState(
                7L,29L,ParticipantState.ACTIVE);
        verify(requests,times(1)).findBySessionIdAndStatusOrderByCreatedAtAsc(
                7L,SessionGuestRequest.State.APPROVED);
    }

    @Test void signedLinkShowsAllConfirmedPlayersWithoutExposingUnapprovedOrLinkedGuests(){
        String secret=token();
        var member=new SessionParticipant();member.setId(30L);member.setProfile(organizer);
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(7L,ParticipantState.ACTIVE))
                .thenReturn(List.of(member));
        var guest=new SessionGuestRequest();guest.setId(40L);
        guest.setDisplayName("Fresco");guest.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findBySessionIdAndStatusOrderByCreatedAtAsc(
                7L,SessionGuestRequest.State.APPROVED)).thenReturn(List.of(guest));
        assertThrows(ResponseStatusException.class,
                ()->service.confirmedPlayers(7L,"invalid"));
        var confirmed=service.confirmedPlayers(7L,secret);
        assertEquals(2,confirmed.size());
        assertEquals("account:30",confirmed.getFirst().key());
        assertEquals("rui",confirmed.getFirst().name());
        assertTrue(confirmed.getFirst().hasAccount());
        assertEquals("guest:40",confirmed.get(1).key());
        assertEquals("Fresco",confirmed.get(1).name());
        assertFalse(confirmed.get(1).hasAccount());
        verify(requests,never()).findBySessionIdOrderByCreatedAtAsc(7L);
    }

    @Test void signedGuestRosterDoesNotOfferAlreadyReservedHistoricalName(){
        var fresco=historic();fresco.setDisplayName("Fresco");fresco.setNormalizedName("fresco");
        var free=historic();free.setId(16L);free.setDisplayName("Outro Jogador");
        free.setNormalizedName("outro jogador");
        when(players.findByCommunityIdOrderByDisplayNameAsc(11L))
                .thenReturn(List.of(fresco,free));
        var submitted=new SessionGuestRequest();
        submitted.setNormalizedName("fresco");
        submitted.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findBySessionIdOrderByCreatedAtAsc(7L)).thenReturn(List.of(submitted));
        var roster=service.roster(7L,token());
        assertEquals(1,roster.players().size());
        assertEquals("Outro Jogador",roster.players().getFirst().name());
    }

    @Test void signedInviteReservesSeatImmediatelyAndReturnsApprovedReceipt(){
        String token=token();var person=historic();
        when(players.findById(15L)).thenReturn(Optional.of(person));
        var receipt=service.request(7L,new SessionGuestRequestService.GuestRequestInput(
                token,15L,null,"Chego a horas"));
        assertEquals("APPROVED",receipt.status());
        assertEquals("Afonso Salvador",receipt.playerName());
        assertEquals(43,receipt.receipt().length());
        var captor=org.mockito.ArgumentCaptor.forClass(SessionGuestRequest.class);
        verify(requests).saveAndFlush(captor.capture());
        assertEquals("Afonso Salvador",captor.getValue().getDisplayName());
        assertNotEquals(receipt.receipt(),captor.getValue().getReceiptHash());
        assertEquals(SessionGuestRequest.State.APPROVED,captor.getValue().getStatus());
        assertEquals(1,session.getExternalConfirmedParticipants());
        assertNotNull(captor.getValue().getReviewedAt());
        verify(sessions,atLeastOnce()).save(session);
        assertNull(captor.getValue().getTeamCode());
    }
    @Test void privateMeetingPointRequiresConfirmedGuestReceiptAndReflectsLatestEdit(){
        session.setMeetingPointInstructions("Portão 2");
        String secret=token();
        var receipt=service.request(7L,new SessionGuestRequestService.GuestRequestInput(
                secret,null,"Convidado Confirmado",""));
        assertEquals("APPROVED",receipt.status());
        assertEquals("Portão 2",receipt.meetingPointInstructions());
        assertThrows(ResponseStatusException.class,()->service.status(7L,
                new SessionGuestRequestService.ReceiptInput("invalid")));

        var saved=org.mockito.ArgumentCaptor.forClass(SessionGuestRequest.class);
        verify(requests).saveAndFlush(saved.capture());
        when(requests.findBySessionIdAndReceiptHash(eq(7L),anyString()))
                .thenReturn(Optional.of(saved.getValue()));

        session.setMeetingPointInstructions("Entrada norte");
        var refreshed=service.status(7L,new SessionGuestRequestService.ReceiptInput(receipt.receipt()));
        assertEquals("Entrada norte",refreshed.meetingPointInstructions());
        saved.getValue().setStatus(SessionGuestRequest.State.CANCELLED);
        assertNull(service.status(7L,new SessionGuestRequestService.ReceiptInput(
                receipt.receipt())).meetingPointInstructions());
        saved.getValue().setStatus(SessionGuestRequest.State.APPROVED);
        session.setStatus(SessionStatus.CANCELLED);
        assertNull(service.status(7L,new SessionGuestRequestService.ReceiptInput(
                receipt.receipt())).meetingPointInstructions());
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
    @Test void lastAvailableSeatIsConfirmedAndNoFurtherGuestCanOverbook() {
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(9L);
        String secret=token();
        var first=service.request(7L,new SessionGuestRequestService.GuestRequestInput(
                secret,null,"Novo Convidado",""));
        assertEquals("APPROVED",first.status());
        assertEquals(1,session.getExternalConfirmedParticipants());
        assertEquals(SessionStatus.FULL,session.getStatus());
        assertThrows(ResponseStatusException.class,()->service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,null,"Outro Convidado","")));
        verify(requests,times(1)).saveAndFlush(any(SessionGuestRequest.class));
    }

    @Test void linkedHistoricalNamesAreAbsentFromRosterAndCannotClaimGuestSeats() {
        var reserved=historic();
        var available=historic();available.setId(16L);available.setDisplayName("Outro Jogador");
        var claim=CommunityHistoricalPlayerLink.builder().player(reserved).community(community)
                .status("ACCEPTED").build();
        when(links.findByCommunityIdAndStatus(11L,"ACCEPTED")).thenReturn(List.of(claim));
        when(links.existsByCommunityIdAndPlayerIdAndStatus(11L,15L,"ACCEPTED")).thenReturn(true);
        when(players.findByCommunityIdOrderByDisplayNameAsc(11L)).thenReturn(List.of(reserved,available));
        when(players.findById(15L)).thenReturn(Optional.of(reserved));
        String secret=token();
        var roster=service.roster(7L,secret);
        assertEquals(List.of(16L),roster.players().stream().map(
                SessionGuestRequestService.GuestPlayer::id).toList());
        assertThrows(ResponseStatusException.class,()->service.request(7L,
                new SessionGuestRequestService.GuestRequestInput(secret,15L,null,"")));
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
    @Test void changingMyConfirmedNameKeepsSameReceiptSameSeatEvenWhenGameIsFull(){
        token();
        clearInvocations(sessions);
        var original=new SessionGuestRequest();
        original.setId(90L);original.setSession(session);
        original.setDisplayName("Barata");original.setNormalizedName("barata");
        original.setStatus(SessionGuestRequest.State.APPROVED);
        var player=historic();player.setDisplayName("Fresco");player.setNormalizedName("fresco");
        when(players.findById(15L)).thenReturn(Optional.of(player));
        String receipt="M".repeat(43);
        String digest;
        try{digest=HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest)).thenReturn(Optional.of(original));
        when(requests.findBySessionIdAndNormalizedName(7L,"fresco")).thenReturn(Optional.empty());
        session.setExternalConfirmedParticipants(9);
        when(participants.countBySessionIdAndState(7L,ParticipantState.ACTIVE)).thenReturn(1L);
        session.setStatus(SessionStatus.FULL);
        var result=service.changeGuestIdentity(7L,
                new SessionGuestRequestService.ChangeGuestIdentityInput(receipt,15L,null));
        assertEquals("APPROVED",result.status());
        assertEquals("Fresco",result.playerName());
        assertEquals(receipt,result.receipt());
        assertEquals("Fresco",original.getDisplayName());
        assertEquals("fresco",original.getNormalizedName());
        assertEquals(SessionGuestRequest.State.APPROVED,original.getStatus());
        assertEquals(9,session.getExternalConfirmedParticipants());
        assertEquals(SessionStatus.FULL,session.getStatus());
        verify(requests).saveAndFlush(same(original));
        verify(sessions,never()).save(any(Session.class));
        verifyNoInteractions(waitlistPromotionService);
        assertThrows(ResponseStatusException.class,()->service.changeGuestIdentity(7L,
            new SessionGuestRequestService.ChangeGuestIdentityInput("invalid",null,"Intruso")));
    }

    @Test void cannotChangeMyBookingToAnotherConfirmedNameOrLinkedProfile(){
        token();
        var owner=new SessionGuestRequest();
        owner.setId(91L);owner.setSession(session);
        owner.setNormalizedName("barata");owner.setStatus(SessionGuestRequest.State.APPROVED);
        var other=new SessionGuestRequest();other.setId(92L);
        other.setNormalizedName("fresco");other.setStatus(SessionGuestRequest.State.APPROVED);
        when(requests.findBySessionIdAndNormalizedName(7L,"fresco")).thenReturn(Optional.of(other));
        String receipt="N".repeat(43);
        String digest;
        try{digest=HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest)).thenReturn(Optional.of(owner));
        assertThrows(ResponseStatusException.class,()->service.changeGuestIdentity(7L,
                new SessionGuestRequestService.ChangeGuestIdentityInput(receipt,null,"Fresco")));
        var linked=historic();linked.setProfile(organizer);
        when(players.findById(15L)).thenReturn(Optional.of(linked));
        assertThrows(ResponseStatusException.class,()->service.changeGuestIdentity(7L,
                new SessionGuestRequestService.ChangeGuestIdentityInput(receipt,15L,null)));
        assertEquals("barata",owner.getNormalizedName());
        verify(requests,never()).saveAndFlush(any());
    }

    @Test void guestMayCancelOwnReceiptOnceAndFreeSeatWithoutAccount(){
        String secret=token();
        var original=new SessionGuestRequest();
        original.setId(82L);original.setSession(session);
        original.setDisplayName("Barata");original.setNormalizedName("barata");
        original.setStatus(SessionGuestRequest.State.APPROVED);
        original.setTeamCode("A");
        session.setExternalConfirmedParticipants(1);
        session.setStatus(SessionStatus.FULL);
        String receipt="B".repeat(43);
        String digest;
        try {
            digest=HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest))
                .thenReturn(Optional.of(original));
        when(requests.findBySessionIdOrderByCreatedAtAsc(7L))
                .thenReturn(List.of(original));
        when(waitlistPromotionService.promoteNext(eq(session),any(Instant.class)))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->service.cancelByReceipt(7L,
                new SessionGuestRequestService.ReceiptInput("wrong")));
        assertEquals(1,session.getExternalConfirmedParticipants());
        var result=service.cancelByReceipt(7L,new SessionGuestRequestService.ReceiptInput(receipt));
        assertEquals("CANCELLED",result.status());
        assertEquals("Barata",result.playerName());
        assertEquals(0,session.getExternalConfirmedParticipants());
        assertEquals(SessionStatus.OPEN,session.getStatus());
        assertNull(original.getTeamCode());
        assertTrue(service.confirmedPlayers(7L,secret).isEmpty());
        assertEquals("CANCELLED",service.cancelByReceipt(7L,
                new SessionGuestRequestService.ReceiptInput(receipt)).status());
        assertEquals(0,session.getExternalConfirmedParticipants());
        verify(requests,times(1)).save(original);
        verify(waitlistPromotionService,times(1)).promoteNext(eq(session),any(Instant.class));
    }

    @Test void aCancelledHistoricalGuestMayBeSelectedAgainWithANewReceipt(){
        String secret=token();
        var player=historic();
        when(players.findById(15L)).thenReturn(Optional.of(player));
        when(players.findByCommunityIdOrderByDisplayNameAsc(11L)).thenReturn(List.of(player));
        var previous=new SessionGuestRequest();
        previous.setId(82L);previous.setSession(session);
        previous.setNormalizedName("afonso salvador");
        previous.setStatus(SessionGuestRequest.State.CANCELLED);
        previous.setReceiptHash("old-digest");
        when(requests.findBySessionIdOrderByCreatedAtAsc(7L)).thenReturn(List.of(previous));
        when(requests.findBySessionIdAndNormalizedName(7L,"afonso salvador"))
                .thenReturn(Optional.of(previous));
        assertEquals(1,service.roster(7L,secret).players().size());
        var rejoined=service.request(7L,new SessionGuestRequestService.GuestRequestInput(
                secret,15L,null,""));
        assertEquals("APPROVED",rejoined.status());
        assertEquals(1,session.getExternalConfirmedParticipants());
        assertNotEquals("old-digest",previous.getReceiptHash());
        assertEquals(SessionGuestRequest.State.APPROVED,previous.getStatus());
        verify(requests).saveAndFlush(same(previous));
    }

    @Test void linkedGuestReceiptCannotRemoveRegisteredParticipants(){
        var linked=new SessionGuestRequest();
        linked.setSession(session);linked.setStatus(SessionGuestRequest.State.LINKED);
        String receipt="L".repeat(43);
        String digest;
        try {
            digest=HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }catch(Exception ex){throw new AssertionError(ex);}
        when(requests.findBySessionIdAndReceiptHash(7L,digest)).thenReturn(Optional.of(linked));
        assertThrows(ResponseStatusException.class,()->service.cancelByReceipt(7L,
                new SessionGuestRequestService.ReceiptInput(receipt)));
        verify(requests,never()).save(linked);
        verifyNoInteractions(waitlistPromotionService);
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

    @Test void privateReceiptAuthorizesOnlyApprovedUnlinkedGuestOfSameSession() {
        String receipt="g".repeat(43);
        SessionGuestRequest guest=new SessionGuestRequest();
        guest.setStatus(SessionGuestRequest.State.APPROVED);
        guest.setDisplayName("André (Borralho)");
        when(requests.findBySessionIdAndReceiptHash(eq(7L),anyString()))
                .thenReturn(Optional.of(guest));
        assertSame(guest,service.requireApprovedChatGuest(7L,receipt));
        verify(requests,never()).findBySessionIdAndReceiptHash(eq(8L),anyString());
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,"signed-link"));
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,null));
        when(requests.findBySessionIdAndReceiptHash(eq(8L),anyString()))
                .thenReturn(Optional.empty());
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(8L,receipt));

        guest.setStatus(SessionGuestRequest.State.CANCELLED);
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,receipt));
        guest.setStatus(SessionGuestRequest.State.REJECTED);
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,receipt));
        guest.setStatus(SessionGuestRequest.State.LINKED);
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,receipt));
        guest.setStatus(SessionGuestRequest.State.APPROVED);
        guest.setLinkedProfile(organizer);
        assertThrows(AccessDeniedException.class,()->service.requireApprovedChatGuest(7L,receipt));
    }

}
