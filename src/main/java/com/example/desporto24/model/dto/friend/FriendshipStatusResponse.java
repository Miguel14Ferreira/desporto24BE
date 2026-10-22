package com.example.desporto24.model.dto.friend;

/** Friendship and outstanding request between the current member and a public profile. */
public record FriendshipStatusResponse(boolean friends, boolean pendingRequest) {
}
