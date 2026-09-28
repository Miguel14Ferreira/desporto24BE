package com.example.desporto24.model.dto.profile;

public record PublicProfileResponse(
        String username,
        String fullName,
        String nickname,
        String country,
        String location,
        String favoriteSports,
        String bio,
        String skillLevel,
        String availability,
        String profilePicUrl,
        String joinDateDisplay
) {
}
