package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import com.example.desporto24.service.CommunityTimelineService;
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

/** Community files are never public media: each operation checks live membership. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/messages/attachments")
public class CommunityChatAttachmentController {
    private final CommunityTimelineService timeline;

    @GetMapping("/availability")
    public Map<String, Boolean> available(@PathVariable Long communityId,
                                          Authentication authentication) {
        return Map.of("available", timeline.chatAttachmentsAvailable(
                communityId, authentication.getName()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommunityTimelineEntryResponse upload(@PathVariable Long communityId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "replyToMessageId", required = false) Long replyToMessageId,
            Authentication authentication) {
        return timeline.postChatAttachment(communityId, file, caption,
                authentication.getName(), replyToMessageId);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<byte[]> download(@PathVariable Long communityId,
            @PathVariable Long messageId, Authentication authentication) {
        var file = timeline.downloadChatAttachment(communityId, messageId,
                authentication.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.mimeType()))
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.filename(), StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(file.content());
    }
}
