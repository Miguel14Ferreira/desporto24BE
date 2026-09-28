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
    public PublicProfileResponse(
            String username,
            String fullName,
            String nickname,
            String country,
            String location,
            String favoriteSports,
            String profilePicUrl,
            String joinDateDisplay) {
        this(
                username,
                fullName,
                nickname,
                country,
                location,
                favoriteSports,
                null,
                null,
                null,
                profilePicUrl,
                joinDateDisplay
        );
    }
}
