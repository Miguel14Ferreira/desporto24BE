package com.example.desporto24.service;

import com.example.desporto24.model.Chat;
import java.util.Locale;

/**
 * Only produces human-readable, non-sensitive previews. Never changes the
 * stored chat text, attachment metadata or the authorization of a download.
 */
public final class ChatMessagePreview {
    private ChatMessagePreview() {}

    public static String forText(String text) {
        return text != null && text.startsWith("[JYNLO:GIPHY:1]") ? "🎞️ GIF" : text;
    }

    public static String forMessage(Chat message) {
        if (message == null) return null;
        String mime = message.getAttachmentMimeType();
        // Use server-validated MIME, not the user-supplied attachment name.
        if (message.getAttachmentKey() != null && mime != null) {
            String normalized = mime.toLowerCase(Locale.ROOT);
            if ("image/gif".equals(normalized)) return "🎞️ GIF";
            if (normalized.startsWith("image/")) return "🖼️ Fotografia";
            if ("audio/webm".equals(normalized) || "audio/ogg".equals(normalized)) {
                return "🎤 Mensagem de voz";
            }
            if ("application/pdf".equals(normalized)) return "📄 Documento";
            return "📎 Anexo";
        }
        return forText(message.getText());
    }
}
