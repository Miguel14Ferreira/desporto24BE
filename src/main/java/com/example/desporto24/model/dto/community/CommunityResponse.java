package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;

import java.time.Instant;
import java.util.List;

public record CommunityResponse(
        Long id,
        String name,
        String description,
        String sport,
        String photoUrl,
        CommunityVisibility visibility,
        boolean rankingsEnabled,
        String ownerUsername,
        CommunityRole myRole,
        int memberCount,
        Instant createdAt,
        List<CommunityMemberResponse> members,
        List<String> sports,
        boolean individualTournamentsEnabled,
        boolean teamTournamentsEnabled,
        Double photoZoom,
        Double photoFocusX,
        Double photoFocusY,
        String photoFitMode,
        Long defaultVenueId,
        String defaultVenueName,
        String habitualLocationSource,
        Double habitualLatitude,
        Double habitualLongitude,
        String habitualCity,
        String habitualAddress,
        boolean membersCanCreateSessions
) {
    // Preserve the former canonical response constructor for existing tests and consumers.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount, Instant createdAt,
                             List<CommunityMemberResponse> members, List<String> sports,
                             boolean individualTournamentsEnabled, boolean teamTournamentsEnabled,
                             Double photoZoom, Double photoFocusX, Double photoFocusY,
                             String photoFitMode, Long defaultVenueId, String defaultVenueName,
                             String habitualLocationSource, Double habitualLatitude,
                             Double habitualLongitude, String habitualCity, String habitualAddress) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
                ownerUsername, myRole, memberCount, createdAt, members, sports,
                individualTournamentsEnabled, teamTournamentsEnabled, photoZoom,
                photoFocusX, photoFocusY, photoFitMode, defaultVenueId,
                defaultVenueName, habitualLocationSource, habitualLatitude,
                habitualLongitude, habitualCity, habitualAddress, true);
    }
    // Existing full 21-field constructions remain source-compatible.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount, Instant createdAt,
                             List<CommunityMemberResponse> members, List<String> sports,
                             boolean individualTournamentsEnabled, boolean teamTournamentsEnabled,
                             Double photoZoom, Double photoFocusX, Double photoFocusY,
                             String photoFitMode, Long defaultVenueId, String defaultVenueName) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
                ownerUsername, myRole, memberCount, createdAt, members, sports,
                individualTournamentsEnabled, teamTournamentsEnabled,
                photoZoom, photoFocusX, photoFocusY, photoFitMode,
                defaultVenueId, defaultVenueName, null, null, null, null, null);
    }
    // Keep old response factories and tests source-compatible.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members,
                             List<String> sports, boolean individualTournamentsEnabled,
                             boolean teamTournamentsEnabled, Double photoZoom,
                             Double photoFocusX, Double photoFocusY, String photoFitMode) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
                ownerUsername, myRole, memberCount, createdAt, members, sports,
                individualTournamentsEnabled, teamTournamentsEnabled, photoZoom,
                photoFocusX, photoFocusY, photoFitMode, null, null);
    }
    // Compatibility with photo-framing responses and existing call sites.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members,
                             List<String> sports, boolean individualTournamentsEnabled,
                             boolean teamTournamentsEnabled,
                             Double photoZoom, Double photoFocusX, Double photoFocusY) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
             ownerUsername, myRole, memberCount, createdAt, members, sports,
             individualTournamentsEnabled, teamTournamentsEnabled,
             photoZoom, photoFocusX, photoFocusY, null);
    }

    // Compatibility with existing tests and older response factories.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members,
                             List<String> sports, boolean individualTournamentsEnabled,
                             boolean teamTournamentsEnabled) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
                ownerUsername, myRole, memberCount, createdAt, members, sports,
                individualTournamentsEnabled, teamTournamentsEnabled, null, null, null);
    }
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members,
                             List<String> sports) {
        this(id, name, description, sport, photoUrl, visibility, rankingsEnabled,
                ownerUsername, myRole, memberCount, createdAt, members, sports, true, true);
    }

    // Preserve existing direct construction and older consumers.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members) {
        this(id, name, description, sport, photoUrl, visibility,
                rankingsEnabled, ownerUsername, myRole, memberCount, createdAt,
                members, sport == null ? List.of() : List.of(sport));
    }
}
