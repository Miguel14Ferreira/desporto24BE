package com.example.desporto24.model.dto.profile;

public record PublicProfileResponse(
        String username,
        String fullName,
        String country,
        String location,
        String favoriteSports,
        String profilePicUrl,
        String joinDateDisplay
) {
}
