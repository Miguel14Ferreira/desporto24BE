package com.example.desporto24.websocket;

import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import java.util.Set;

/** Publish only after the database transaction commits. Recipients are current members. */
public record CommunityChatMessageCreatedEvent(
        CommunityTimelineEntryResponse message,
        Set<String> recipients) {
}
