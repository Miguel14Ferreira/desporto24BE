package com.example.desporto24.model.dto.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validRegistrationRequestPassesValidation() {
        assertTrue(validator.validate(validRequest()).isEmpty());
    }

    @Test
    void blankRequiredFieldsAreRejected() {
        RegistrationRequest request = new RegistrationRequest(
                "", "", null, "", "", "", "", null, ""
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void malformedDateIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "dateOfBirth", "1995-99-99"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void underageDateOfBirthIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "dateOfBirth", LocalDate.now().minusDays(9).toString()
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void exactlyEighteenYearsOldIsAccepted() {
        RegistrationRequest request = copyWith(
                validRequest(), "dateOfBirth", LocalDate.now().minusYears(18).toString()
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void futureDateOfBirthIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "dateOfBirth", "2999-01-01"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void nonNumericPhoneIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "phone", "91ABC5678"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void nonNumericIndicativeIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "indicativePhone", "+351"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void e164LengthLimitIsEnforced() {
        RegistrationRequest request = new RegistrationRequest(
                "fixture-user",
                "not-a-real-password",
                "Fixture",
                "1995-05-30",
                "Portugal",
                "1234",
                "123456789012",
                "outro",
                "fixture@example.test"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void unsupportedGenderIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "gender", "valor-inventado"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void blankGenderIsAccepted() {
        RegistrationRequest request = copyWith(
                validRequest(), "gender", ""
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void malformedEmailIsRejected() {
        RegistrationRequest request = copyWith(
                validRequest(), "email", "not-an-email"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    private RegistrationRequest validRequest() {
        return new RegistrationRequest(
                "fixture-user",
                "not-a-real-password",
                "Fixture",
                "1995-05-30",
                "Portugal",
                "351",
                "912345678",
                "outro",
                "fixture@example.test"
        );
    }

    private RegistrationRequest copyWith(
            RegistrationRequest source,
            String field,
            String value) {
        return new RegistrationRequest(
                field.equals("username") ? value : source.username(),
                field.equals("password") ? value : source.password(),
                field.equals("nickname") ? value : source.nickname(),
                field.equals("dateOfBirth") ? value : source.dateOfBirth(),
                field.equals("country") ? value : source.country(),
                field.equals("indicativePhone") ? value : source.indicativePhone(),
                field.equals("phone") ? value : source.phone(),
                field.equals("gender") ? value : source.gender(),
                field.equals("email") ? value : source.email()
        );
    }
}
