package com.example.desporto24.model.dto.social;

import java.time.Instant;

public record FeedItemResponse(
        String type,
        Long postId,
        String authorUsername,
        String authorDisplayName,
        String authorProfilePicUrl,
        String content,
        Instant createdAt,
        Long sessionId,
        String sport,
        String city,
        Instant gameDate,
        long likeCount,
        boolean likedByMe,
        long commentCount
) {
}
