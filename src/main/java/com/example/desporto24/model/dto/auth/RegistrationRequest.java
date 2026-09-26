package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 8, max = 128) String password,
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Size(max = 32) String dateOfBirth,
        @NotBlank @Size(max = 255) String address,
        @NotBlank @Size(max = 100) String country,
        @NotBlank @Size(max = 120) String location,
        @NotBlank @Size(max = 32) String postalCode,
        @NotBlank @Size(max = 16) String indicativePhone,
        @NotBlank @Size(max = 32) String phone,
        @NotBlank @Size(max = 32) String gender,
        @NotBlank @Email String email,
        @NotBlank @Size(max = 255) String favoriteSports
) {
}
