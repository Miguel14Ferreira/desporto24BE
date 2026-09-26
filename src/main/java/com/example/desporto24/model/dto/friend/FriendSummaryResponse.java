package com.example.desporto24.model.dto.friend;

public record FriendSummaryResponse(
        String username,
        String fullName,
        String location,
        String favoriteSports,
        String profilePicUrl,
        boolean chatBlocked,
        boolean blockedByMe
) {
}
