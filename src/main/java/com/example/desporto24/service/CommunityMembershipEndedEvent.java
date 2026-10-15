package com.example.desporto24.service;

/** Sent synchronously inside the membership-change transaction. */
public record CommunityMembershipEndedEvent(Long communityId, Long profileId) {
}
