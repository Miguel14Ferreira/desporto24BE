package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.SessionMessageResponse;
import com.example.desporto24.service.SessionChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/** Session files share the dedicated private storage, not the public feed. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/{sessionId}/messages/attachments")
public class SessionChatAttachmentController {
    private final SessionChatService sessions;

    @GetMapping("/availability")
    public Map<String, Boolean> available(@PathVariable Long sessionId,
            Authentication authentication) {
        return Map.of("available", sessions.attachmentsAvailable(sessionId, authentication.getName()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SessionMessageResponse send(@PathVariable Long sessionId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "replyToMessageId", required = false) Long replyToMessageId,
            Authentication authentication) {
        return sessions.postAttachment(sessionId, file, caption,
                authentication.getName(), replyToMessageId);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<byte[]> download(@PathVariable Long sessionId,
            @PathVariable Long messageId, Authentication authentication) {
        var file = sessions.downloadAttachment(sessionId, messageId, authentication.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.mimeType()))
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.filename(), StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(file.content());
    }
}
