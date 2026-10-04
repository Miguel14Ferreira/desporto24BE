package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityVisibility;

/** Presentation only: never contains members, sessions or competition data. */
public record CommunityDiscoveryResponse(
        Long id, String name, String description, String sport,
        String photoUrl, CommunityVisibility visibility) {}
