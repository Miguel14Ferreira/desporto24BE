package com.example.desporto24.model;

import com.example.desporto24.model.dto.chat.ChatMessageRequest;
import jakarta.persistence.Column;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatTextLengthContractTest {

    @Test
    void persistedDirectChatTextMatchesTwoThousandCharacterRequestLimit() throws Exception {
        Column column = Chat.class.getDeclaredField("text").getAnnotation(Column.class);
        assertNotNull(column, "The text column needs an explicit length to match the API");
        assertEquals("text", column.name());
        assertEquals(2000, column.length());
    }

    @Test
    void directChatAllowsTwoThousandCharactersButRejectsTwoThousandAndOne() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertTrue(validator.validate(new ChatMessageRequest("x".repeat(2000))).isEmpty(),
                    "A message at the accepted upper bound must persist without truncation");
            assertFalse(validator.validate(new ChatMessageRequest("x".repeat(2001))).isEmpty(),
                    "The request must reject messages longer than the persisted column");
        }
    }
}
