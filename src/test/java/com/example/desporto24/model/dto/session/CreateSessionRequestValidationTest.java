package com.example.desporto24.model.dto.session;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateSessionRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validPortugueseLocalityIsAccepted() {
        assertTrue(validator.validate(requestWithCity("São Domingos de Rana")).isEmpty());
    }

    @Test
    void hyphenatedLocalityIsAccepted() {
        assertTrue(validator.validate(requestWithCity("A-dos-Cunhados")).isEmpty());
    }

    @Test
    void numericOnlyLocalityIsRejected() {
        assertFalse(validator.validate(requestWithCity("1500")).isEmpty());
    }

    @Test
    void localityStartingWithNumberIsRejected() {
        assertFalse(validator.validate(requestWithCity("1500 Lisboa")).isEmpty());
    }

    @Test
    void blankLocalityIsRejected() {
        assertFalse(validator.validate(requestWithCity("")).isEmpty());
    }

    private CreateSessionRequest requestWithCity(String city) {
        return new CreateSessionRequest(
                "Futsal",
                Instant.now().plusSeconds(3600),
                city,
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.7223,
                -9.1393,
                null,
                null
        );
    }
}
