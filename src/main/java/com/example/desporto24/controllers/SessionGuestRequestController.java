package com.example.desporto24.controllers;

import com.example.desporto24.service.SessionGuestRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/** Guest requests are not bookings until the real organizer explicitly approves. */
@RestController
@RequestMapping("/api/sessions/{sessionId}")
@RequiredArgsConstructor
public class SessionGuestRequestController {
    private final SessionGuestRequestService guests;
    private final com.example.desporto24.service.interfaces.SessionService sessionService;

    @PostMapping("/guest-join-account")
    public com.example.desporto24.model.dto.session.JoinSessionResponse joinWithAccount(
            @PathVariable Long sessionId, @RequestBody Map<String,String> body, Authentication auth) {
        return sessionService.joinWithGuestToken(
                sessionId, body == null ? null : body.get("token"), auth.getName());
    }

    @PostMapping("/guest-link")
    public Map<String,String> createLink(@PathVariable Long sessionId,Authentication auth){
        return Map.of("token",guests.issueLink(sessionId,auth.getName()));
    }
    @PostMapping("/guest-preview")
    public com.example.desporto24.model.dto.session.SessionPublicPreviewResponse preview(
            @PathVariable Long sessionId,@RequestBody Map<String,String> body){
        return guests.preview(sessionId,body.get("token"));
    }
    @PostMapping("/guest-roster")
    public SessionGuestRequestService.GuestRoster roster(
            @PathVariable Long sessionId,@RequestBody Map<String,String> body){
        return guests.roster(sessionId,body.get("token"));
    }
    /** Participant display roster only, never the organizer's guest administration DTO. */
    @GetMapping("/guest-confirmed-guests")
    public List<SessionGuestRequestService.ConfirmedGuest> confirmedGuestsForParticipant(
            @PathVariable Long sessionId, Authentication auth) {
        return guests.confirmedGuestsForMember(sessionId,auth==null?null:auth.getName());
    }

    /** Invitation bearer only; never exposed by the anonymous public preview. */
    @PostMapping("/guest-confirmed-players")
    public List<SessionGuestRequestService.ConfirmedPlayer> confirmedPlayers(
            @PathVariable Long sessionId,@RequestBody Map<String,String> body){
        return guests.confirmedPlayers(sessionId,body==null?null:body.get("token"));
    }

    @PostMapping("/guest-requests")
    public SessionGuestRequestService.GuestReceipt request(
            @PathVariable Long sessionId,
            @RequestBody SessionGuestRequestService.GuestRequestInput body){
        return guests.request(sessionId,body);
    }
    @PostMapping("/guest-requests/status")
    public SessionGuestRequestService.GuestReceipt status(
            @PathVariable Long sessionId,
            @RequestBody SessionGuestRequestService.ReceiptInput body){
        return guests.status(sessionId,body);
    }
    /** Change only the confirmed player authenticated by the private receipt. */
    @PostMapping("/guest-requests/change-player")
    public SessionGuestRequestService.GuestReceipt changePlayer(
            @PathVariable Long sessionId,
            @RequestBody SessionGuestRequestService.ChangeGuestIdentityInput body){
        return guests.changeGuestIdentity(sessionId,body);
    }

    /** Receipt proves ownership; the shared invitation does not. */
    @PostMapping("/guest-requests/cancel-self")
    public SessionGuestRequestService.GuestReceipt cancelSelf(
            @PathVariable Long sessionId,@RequestBody SessionGuestRequestService.ReceiptInput body){
        return guests.cancelByReceipt(sessionId,body);
    }

    @PostMapping("/guest-requests/link-account")
    public SessionGuestRequestService.LinkedReceipt linkAccount(
            @PathVariable Long sessionId,@RequestBody SessionGuestRequestService.ReceiptInput body,
            Authentication auth){
        return guests.linkAccount(sessionId,body,auth.getName());
    }

    @PostMapping("/guest-requests/{requestId}/cancel")
    public SessionGuestRequestService.GuestView cancel(
            @PathVariable Long sessionId,@PathVariable Long requestId,Authentication auth){
        return guests.cancel(sessionId,requestId,auth.getName());
    }

    @GetMapping("/guest-requests")
    public List<SessionGuestRequestService.GuestView> list(
            @PathVariable Long sessionId,Authentication auth){
        return guests.list(sessionId,auth.getName());
    }
    @PostMapping("/guest-requests/{requestId}/decision")
    public SessionGuestRequestService.GuestView review(
            @PathVariable Long sessionId,@PathVariable Long requestId,
            @RequestBody Map<String,Boolean> body,Authentication auth){
        return guests.review(sessionId,requestId,Boolean.TRUE.equals(body.get("approve")),auth.getName());
    }
}
