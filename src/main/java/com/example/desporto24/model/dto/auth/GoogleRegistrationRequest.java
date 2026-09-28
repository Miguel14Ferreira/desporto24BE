package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record GoogleRegistrationRequest(
        @NotBlank String credential,

        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "A data de nascimento deve estar no formato yyyy-MM-dd")
        String dateOfBirth,

        @NotBlank
        @Size(max = 255)
        String address,

        @NotBlank
        @Size(max = 100)
        String country,

        @NotBlank
        @Size(max = 120)
        String location,

        @NotBlank
        @Size(max = 32)
        String postalCode,

        @NotBlank
        @Pattern(regexp = "\\d{1,4}", message = "O indicativo deve conter apenas dígitos")
        String indicativePhone,

        @NotBlank
        @Pattern(regexp = "\\d{4,15}", message = "O telemóvel deve conter apenas dígitos")
        String phone,

        @NotBlank
        @Pattern(
                regexp = "(?i)masculino|feminino|outro",
                message = "O género deve ser masculino, feminino ou outro"
        )
        String gender,

        @NotBlank
        @Size(max = 255)
        String favoriteSports
) {
    @AssertTrue(message = "Tens de ter pelo menos 18 anos e uma data de nascimento válida")
    public boolean isDateOfBirthValid() {
        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            return true;
        }

        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth);
            LocalDate today = LocalDate.now();
            return !birthDate.isAfter(today.minusYears(18))
                    && !birthDate.isBefore(today.minusYears(120));
        } catch (DateTimeException exception) {
            return false;
        }
    }

    @AssertTrue(message = "Escolhe apenas desportos disponíveis na lista")
    public boolean isFavoriteSportsValid() {
        if (favoriteSports == null || favoriteSports.isBlank()) {
            return true;
        }

        Set<String> allowedSports = Set.of(
                "Futebol", "Futsal", "Basquetebol", "Ténis", "Padel",
                "Voleibol", "Corrida", "Ciclismo", "Natação", "Andebol",
                "Rugby", "Badminton", "Ténis de mesa", "Squash", "Hóquei",
                "Surf", "Escalada", "Ginásio", "Yoga", "Pilates"
        );

        Set<String> selected = Arrays.stream(favoriteSports.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toSet());

        return !selected.isEmpty() && selected.stream().allMatch(allowedSports::contains);
    }

    @AssertTrue(message = "O número de telefone completo não pode exceder 15 dígitos")
    public boolean isPhoneLengthValid() {
        if (indicativePhone == null || phone == null
                || !indicativePhone.matches("\\d+")
                || !phone.matches("\\d+")) {
            return true;
        }

        return indicativePhone.length() + phone.length() <= 15;
    }
}
