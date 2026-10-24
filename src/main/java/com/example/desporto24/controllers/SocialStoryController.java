package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.social.StoryResponse;
import com.example.desporto24.service.SocialStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stories")
public class SocialStoryController {
    private final SocialStoryService stories;

    @GetMapping
    public List<StoryResponse> feed(Authentication auth) {
        return stories.feed(auth.getName());
    }

    @GetMapping("/{id}")
    public StoryResponse view(@PathVariable Long id, Authentication auth) {
        return stories.view(id, auth.getName());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StoryResponse publish(@RequestPart(value = "caption", required = false) String caption,
                                 @RequestPart(value = "communityId", required = false) Long communityId,
                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                 @RequestPart(value = "video", required = false) MultipartFile video,
                                 Authentication auth) {
        return stories.create(auth.getName(), communityId, caption, image, video);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        stories.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
