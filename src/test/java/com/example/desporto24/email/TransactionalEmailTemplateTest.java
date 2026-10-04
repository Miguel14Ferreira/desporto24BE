package com.example.desporto24.email;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionalEmailTemplateTest {
    @Test
    void rendersBilingualContentAndEscapesNamesAndActionUrl() {
        String html = TransactionalEmailTemplate.render(
                "Confirma o teu email", "Olá <miguel> & amigos",
                "Confirm your email", "Hi <miguel> & friends",
                "Confirmar email / Confirm email",
                "https://d24sports.com/?confirmRegistration=abc&source=email",
                "O link expira em 15 minutos.", "This link expires in 15 minutes.");
        assertTrue(html.contains("Olá &lt;miguel&gt; &amp; amigos"));
        assertTrue(html.contains("Hi &lt;miguel&gt; &amp; friends"));
        assertFalse(html.contains("<miguel>"));
        assertTrue(html.contains("href=\"https://d24sports.com/?confirmRegistration=abc&amp;source=email\""));
        assertTrue(html.contains("Confirmar email / Confirm email"));
        assertTrue(html.contains("15 minutos"));
        assertTrue(html.contains("15 minutes"));
    }
}
