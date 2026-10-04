package com.example.desporto24.service;

public record CommunityInviteCreatedEvent(
        Long inviteId,
        String recipientEmail,
        String inviterUsername,
        String communityName) {
}
