package com.example.desporto24.model.dto.session;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateSessionExternalConfirmationsRequestValidationTest {

    @Test
    void validatesExternalConfirmationRange() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertTrue(validator.validate(new UpdateSessionExternalConfirmationsRequest(7)).isEmpty());
            assertTrue(validator.validate(new UpdateSessionExternalConfirmationsRequest(0)).isEmpty());
            assertFalse(validator.validate(new UpdateSessionExternalConfirmationsRequest(-1)).isEmpty());
            assertFalse(validator.validate(new UpdateSessionExternalConfirmationsRequest(100)).isEmpty());
            assertFalse(validator.validate(new UpdateSessionExternalConfirmationsRequest(null)).isEmpty());
        }
    }
}
