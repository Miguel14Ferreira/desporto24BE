package com.example.desporto24.model.dto.community;
import java.time.Instant;
public record CommunityRelayDecisionResponse(Long id,Long heatId,String previousStatus,
    String status,String reason,Long actorProfileId,Instant decidedAt) {}
