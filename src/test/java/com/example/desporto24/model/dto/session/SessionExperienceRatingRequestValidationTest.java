package com.example.desporto24.model.dto.session;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionExperienceRatingRequestValidationTest {

    @Test
    void eachScoreMustBeBetweenOneAndFive() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertTrue(validator.validate(new SessionExperienceRatingRequest(5, 4, 1)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(0, 3, 3)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(3, 6, 3)).isEmpty());
            assertFalse(validator.validate(new SessionExperienceRatingRequest(3, 3, null)).isEmpty());
        }
    }
}
