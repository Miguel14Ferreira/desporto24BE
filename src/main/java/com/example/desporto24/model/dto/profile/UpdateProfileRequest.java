package com.example.desporto24.model.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 120) String fullName,
        @Size(max = 32) String dateOfBirth,
        @Size(max = 255) String address,
        @Size(max = 100) String country,
        @Size(max = 120) String location,
        @Size(max = 32) String postalCode,
        @Size(max = 16) String indicativePhone,
        @Size(max = 32) String phone,
        @Size(max = 32) String gender,
        @NotBlank @Email String email,
        @Size(max = 255) String favoriteSports,
        Boolean mfa
) {
}
