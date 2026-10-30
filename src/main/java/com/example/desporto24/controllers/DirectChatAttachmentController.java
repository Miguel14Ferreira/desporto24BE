package com.example.desporto24.controllers;

import com.example.desporto24.model.Chat;
import com.example.desporto24.service.DirectChatAttachmentService;
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

/** Authenticated delivery: objects are never exposed via /media/** or public S3 URLs. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/me/{peer}/attachments")
public class DirectChatAttachmentController {
    private final DirectChatAttachmentService attachments;

    @GetMapping("/availability")
    public java.util.Map<String, Boolean> available(@PathVariable String peer,
                                                      Authentication authentication) {
        return java.util.Map.of("available",
                attachments.available(authentication.getName(), peer));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Chat send(@PathVariable String peer,
                     @RequestPart("file") MultipartFile file,
                     @RequestParam(value = "caption", required = false) String caption,
                     @RequestParam(value = "replyToMessageId", required = false) Long replyToMessageId,
                     Authentication authentication) {
        return attachments.send(authentication.getName(), peer, file, caption, replyToMessageId);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<byte[]> download(@PathVariable String peer,
                                           @PathVariable Long messageId,
                                           Authentication authentication) {
        var file = attachments.download(authentication.getName(), peer, messageId);
        // Content-Disposition attachment prevents active documents rendering
        // inside the app's origin. Frontend fetches an authenticated Blob.
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.mimeType()))
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.filename(), StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(file.content());
    }
}
