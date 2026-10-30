package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.chat.DirectReadReceiptResponse;
import com.example.desporto24.model.dto.chat.InboxReadRequest;
import com.example.desporto24.model.dto.chat.InboxRequest;
import com.example.desporto24.model.dto.chat.InboxSummaryResponse;
import com.example.desporto24.service.InboxSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages/inbox")
public class InboxController {
    private final InboxSummaryService service;

    @PostMapping("/summaries")
    public List<InboxSummaryResponse> summaries(
            @RequestBody @Valid InboxRequest request, Authentication authentication) {
        return service.summaries(authentication.getName(), request);
    }

    @GetMapping("/direct/{peer}/receipt")
    public DirectReadReceiptResponse directReadReceipt(
            @PathVariable String peer, Authentication authentication) {
        return service.peerDirectReadReceipt(authentication.getName(), peer);
    }

    @PutMapping("/read")
    public InboxSummaryResponse markRead(
            @RequestBody @Valid InboxReadRequest request, Authentication authentication) {
        return service.markRead(authentication.getName(), request);
    }
}
