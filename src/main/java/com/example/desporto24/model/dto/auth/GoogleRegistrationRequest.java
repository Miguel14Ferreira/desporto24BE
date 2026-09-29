package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.DateTimeException;
import java.time.LocalDate;

public record GoogleRegistrationRequest(
        @NotBlank String credential,

        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @Size(max = 80)
        String nickname,

        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "A data de nascimento deve estar no formato yyyy-MM-dd")
        String dateOfBirth,

        @NotBlank
        @Size(max = 100)
        String country,

        @Pattern(regexp = "^$|\\d{1,4}", message = "O indicativo deve conter apenas dígitos")
        String indicativePhone,

        @Pattern(regexp = "^$|\\d{4,15}", message = "O telemóvel deve conter apenas dígitos")
        String phone,

        @Pattern(
                regexp = "^$|(?i:masculino|feminino|outro)",
                message = "O género deve ser masculino, feminino ou outro"
        )
        String gender
) {
    @AssertTrue(message = "Tens de ter pelo menos 18 anos e uma data de nascimento válida")
    public boolean isDateOfBirthValid() {
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth);
            LocalDate today = LocalDate.now();
            return !birthDate.isAfter(today.minusYears(18))
                    && !birthDate.isBefore(today.minusYears(120));
        } catch (DateTimeException | NullPointerException exception) {
            return false;
        }
    }

    @AssertTrue(message = "Preenche o indicativo apenas quando adicionares um número de telefone")
    public boolean isPhonePairValid() {
        boolean hasPhone = phone != null && !phone.isBlank();
        boolean hasIndicative = indicativePhone != null && !indicativePhone.isBlank();
        return hasPhone == hasIndicative;
    }

    @AssertTrue(message = "O número de telefone completo não pode exceder 15 dígitos")
    public boolean isPhoneLengthValid() {
        if (phone == null || phone.isBlank()) {
            return true;
        }
        if (indicativePhone == null
                || !indicativePhone.matches("\\d+")
                || !phone.matches("\\d+")) {
            return true;
        }
        return indicativePhone.length() + phone.length() <= 15;
    }
}
