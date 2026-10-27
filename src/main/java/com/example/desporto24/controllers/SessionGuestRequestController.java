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

    @PostMapping("/guest-link")
    public Map<String,String> createLink(@PathVariable Long sessionId,Authentication auth){
        return Map.of("token",guests.issueLink(sessionId,auth.getName()));
    }
    @PostMapping("/guest-roster")
    public SessionGuestRequestService.GuestRoster roster(
            @PathVariable Long sessionId,@RequestBody Map<String,String> body){
        return guests.roster(sessionId,body.get("token"));
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
