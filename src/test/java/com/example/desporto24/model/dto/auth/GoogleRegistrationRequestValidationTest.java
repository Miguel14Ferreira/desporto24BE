package com.example.desporto24.model.dto.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoogleRegistrationRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validGoogleRegistrationPassesValidation() {
        assertTrue(validator.validate(validRequest(true)).isEmpty());
    }

    @Test
    void legalAcceptanceIsRequiredForGoogleRegistration() {
        assertFalse(validator.validate(validRequest(false)).isEmpty());
    }

    private GoogleRegistrationRequest validRequest(boolean acceptedLegalTerms) {
        return new GoogleRegistrationRequest(
                "google-credential",
                "fixture-user",
                "Fixture",
                "1995-05-30",
                "Portugal",
                null,
                null,
                "outro",
                acceptedLegalTerms
        );
    }
}
