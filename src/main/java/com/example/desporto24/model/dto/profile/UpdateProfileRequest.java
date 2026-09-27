package com.example.desporto24.model.dto.profile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.DateTimeException;
import java.time.LocalDate;

public record UpdateProfileRequest(
        @Size(max = 120) String fullName,
        @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$", message = "A data de nascimento deve estar no formato yyyy-MM-dd")
        String dateOfBirth,
        @Size(max = 255) String address,
        @Size(max = 100) String country,
        @Size(max = 120) String location,
        @Size(max = 32) String postalCode,
        @Pattern(regexp = "^$|\\d{1,4}$", message = "O indicativo deve conter apenas dígitos")
        String indicativePhone,
        @Pattern(regexp = "^$|\\d{4,15}$", message = "O telefone deve conter apenas dígitos")
        String phone,
        @Pattern(regexp = "^$|(?i:masculino|feminino|outro)", message = "O género deve ser masculino, feminino ou outro")
        String gender,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 255) String favoriteSports,
        Boolean mfa
) {
}
