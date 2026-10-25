package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import java.util.List;

/** Presentation only: never contains members, sessions or competition data. */
public record CommunityDiscoveryResponse(
        Long id, String name, String description, String sport,
        String photoUrl, CommunityVisibility visibility, List<String> sports,
        Double photoZoom, Double photoFocusX, Double photoFocusY) {

    public CommunityDiscoveryResponse(Long id, String name, String description,
                                      String sport, String photoUrl, CommunityVisibility visibility,
                                      List<String> sports) {
        this(id, name, description, sport, photoUrl, visibility, sports, null, null, null);
    }

    public CommunityDiscoveryResponse(Long id, String name, String description,
                                      String sport, String photoUrl, CommunityVisibility visibility,
                                      Double photoZoom, Double photoFocusX, Double photoFocusY) {
        this(id, name, description, sport, photoUrl, visibility, null,
                photoZoom, photoFocusX, photoFocusY);
    }

    // Keep existing JPQL constructor projections and older callers intact.
    public CommunityDiscoveryResponse(Long id, String name, String description, String sport,
                                      String photoUrl, CommunityVisibility visibility) {
        this(id, name, description, sport, photoUrl, visibility, null);
    }

    public CommunityDiscoveryResponse {
        // Older communities only had the legacy primary modality.
        sports = sports == null || sports.isEmpty()
                ? (sport == null || sport.isBlank() ? List.of() : List.of(sport))
                : List.copyOf(sports);
    }

    public CommunityDiscoveryResponse withSports(List<String> selectedSports) {
        return new CommunityDiscoveryResponse(
                id, name, description, sport, photoUrl, visibility, selectedSports,
                photoZoom, photoFocusX, photoFocusY);
    }
}
