package com.example.desporto24.model.dto.profile;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateProfileRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validAdultDateOfBirthPassesValidation() {
        assertTrue(validator.validate(validRequest()).isEmpty());
    }

    @Test
    void underageDateOfBirthIsRejected() {
        UpdateProfileRequest request = withDateOfBirth(
                LocalDate.now().minusYears(18).plusDays(1).toString()
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void exactlyEighteenYearsOldIsAccepted() {
        UpdateProfileRequest request = withDateOfBirth(
                LocalDate.now().minusYears(18).toString()
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void futureDateOfBirthIsRejected() {
        UpdateProfileRequest request = withDateOfBirth(
                LocalDate.now().plusDays(1).toString()
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void blankDateOfBirthIsRejected() {
        assertFalse(validator.validate(withDateOfBirth("")).isEmpty());
    }

    @Test
    void implausiblyOldDateOfBirthIsRejected() {
        UpdateProfileRequest request = withDateOfBirth(
                LocalDate.now().minusYears(121).toString()
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    private UpdateProfileRequest validRequest() {
        return withDateOfBirth("1995-05-30");
    }

    private UpdateProfileRequest withDateOfBirth(String dateOfBirth) {
        return new UpdateProfileRequest(
                "Fixture User",
                "Fixture",
                dateOfBirth,
                "Rua de Teste 1",
                "Portugal",
                "Lisboa",
                "1000-001",
                "351",
                "912345678",
                "outro",
                "fixture@example.test",
                "Futebol",
                "Perfil de teste",
                "INTERMEDIATE",
                "Fins de semana",
                false
        );
    }
}
