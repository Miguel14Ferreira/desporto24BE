package com.example.desporto24.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatServiceImplNotificationPreviewTest {
    @Test void gifAlertUsesReadablePreviewAndDoesNotChangeChatBody() {
        String original="[JYNLO:GIPHY:1]{\"id\":\"XdUMQuuiaAEkOUtezd\","
                + "\"p\":\"https://giphy.com/gifs/animated-thumbs-up-joeyahlbum-XdUMQuuiaAEkOUtezd\","
                + "\"a\":\"joeyahlbum\",\"c\":\"\"}";
        assertEquals("🎞️ GIF",ChatServiceImpl.notificationPreview(original));
        assertTrue(original.contains("XdUMQuuiaAEkOUtezd"));
    }

    @Test void normalMessagesStayUnmodified() {
        assertEquals("Olá! 😀",ChatServiceImpl.notificationPreview("Olá! 😀"));
        assertEquals("GIF",ChatServiceImpl.notificationPreview("GIF"));
        assertNull(ChatServiceImpl.notificationPreview(null));
    }

    @Test void malformedReservedGifPrefixIsNotLeakedToNotifications() {
        assertEquals("🎞️ GIF",ChatServiceImpl.notificationPreview("[JYNLO:GIPHY:1]{not-json}"));
    }
}
