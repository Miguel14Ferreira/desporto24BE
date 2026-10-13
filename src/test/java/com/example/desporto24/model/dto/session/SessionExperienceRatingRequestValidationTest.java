package com.example.desporto24.model.dto.session;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionExperienceRatingRequestValidationTest {

    @Test
    void overallScoreMustBeBetweenOneAndFive() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertTrue(validator.validate(new SessionExperienceRatingRequest(5)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(0)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(6)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(null)).isEmpty());
        }
    }
}
