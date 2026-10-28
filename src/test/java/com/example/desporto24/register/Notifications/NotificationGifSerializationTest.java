package com.example.desporto24.register.Notifications;

import com.example.desporto24.enumeration.NotificationType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationGifSerializationTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void historicalGifAlertIsSafeOnWireWithoutChangingStoredData() throws Exception {
        String raw = "[JYNLO:GIPHY:1]{\"id\":\"XdUMQuuiaAEkOUtezd\","
                + "\"p\":\"https://giphy.com/gifs/example-XdUMQuuiaAEkOUtezd\","
                + "\"a\":\"creator\",\"c\":\"\"}";
        Notifications alert = new Notifications();
        alert.setType(NotificationType.CHAT_MESSAGE);
        alert.setMessage(raw);

        JsonNode json = mapper.readTree(mapper.writeValueAsString(alert));
        assertEquals("🎞️ GIF", json.get("message").asText());
        assertFalse(mapper.writeValueAsString(alert).contains("XdUMQuuiaAEkOUtezd"));
        assertEquals(raw, alert.getMessage(), "Serialization must not mutate the JPA field");
    }

    @Test
    void malformedReservedEnvelopeIsAlsoHidden() throws Exception {
        Notifications alert = new Notifications();
        alert.setMessage("[JYNLO:GIPHY:1]{broken-json}");
        assertEquals("🎞️ GIF",
                mapper.readTree(mapper.writeValueAsString(alert)).get("message").asText());
    }

    @Test
    void NormalMessagesAndEmojiStayUnchanged() throws Exception {
        Notifications alert = new Notifications();
        alert.setMessage("Olá! 😀");
        assertEquals("Olá! 😀",
                mapper.readTree(mapper.writeValueAsString(alert)).get("message").asText());
    }
}
