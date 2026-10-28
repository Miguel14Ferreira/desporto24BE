package com.example.desporto24.service;

import com.example.desporto24.model.Chat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessagePreviewTest {
    private static Chat message(String text, String mime, boolean attached) {
        Chat chat = new Chat();
        chat.setText(text);
        chat.setAttachmentMimeType(mime);
        if (attached) chat.setAttachmentKey("private-chat/stored-file");
        return chat;
    }

    @Test
    void verifiedMediaUsesSafeTypeWithoutLeakingUserProvidedNamesOrCaptions() {
        assertEquals("🖼️ Fotografia", ChatMessagePreview.forMessage(
                message("minha foto privada.jpg", "image/jpeg", true)));
        assertEquals("🎞️ GIF", ChatMessagePreview.forMessage(
                message("img-secret.gif", "image/gif", true)));
        assertEquals("🎤 Mensagem de voz", ChatMessagePreview.forMessage(
                message("mensagem.webm", "audio/webm", true)));
        assertEquals("🎤 Mensagem de voz", ChatMessagePreview.forMessage(
                message("mensagem.ogg", "audio/ogg", true)));
        assertEquals("📄 Documento", ChatMessagePreview.forMessage(
                message("contrato-secreto.pdf", "application/pdf", true)));
        assertEquals("📎 Anexo", ChatMessagePreview.forMessage(
                message("private.bin", "application/octet-stream", true)));
    }

    @Test
    void noAttachmentBehavesLikeExistingTextAndGifMessages() {
        assertEquals("Vamos jogar? 👋", ChatMessagePreview.forMessage(
                message("Vamos jogar? 👋", null, false)));
        assertEquals("🎞️ GIF", ChatMessagePreview.forMessage(
                message("[JYNLO:GIPHY:1]{bad-json}", null, false)));
        assertEquals("📎 ficheiro.jpg", ChatMessagePreview.forMessage(
                message("📎 ficheiro.jpg", "image/jpeg", false)));
        assertNull(ChatMessagePreview.forText(null));
        assertNull(ChatMessagePreview.forMessage(null));
    }
}
