package com.example.desporto24.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceBrandTest {

    @Test
    void normalizesRetiredBrandInEmailSubjectAndBody() {
        assertEquals("Convite — Jynlo", EmailService.currentBrand("Convite — DESPORTO24"));
        assertEquals("Jynlo", EmailService.currentBrand("desporto24"));
        assertEquals("Jynlo", EmailService.currentBrand("Desporto 24"));
        assertEquals("<h1>Jynlo</h1>", EmailService.currentBrand("<h1>Desporto24</h1>"));
    }

    @Test
    void normalizesSenderDisplayNameWithoutRewritingEmailAddresses() {
        assertEquals("Jynlo <noreply@d24sports.com>",
                EmailService.senderDisplayName("Desporto24 <noreply@d24sports.com>"));
        assertEquals("Jynlo <noreply@desporto24.com>",
                EmailService.senderDisplayName("DESPORTO24 <noreply@desporto24.com>"));
        assertEquals("noreply@desporto24.com",
                EmailService.senderDisplayName("noreply@desporto24.com"));
    }

    @Test
    void preservesExistingJynloBrandAndLegacyWebsiteLinks() {
        String original = "Jynlo · https://d24sports.com/login · https://api.d24sports.com";
        assertEquals(original, EmailService.currentBrand(original));
        assertEquals("Assunto Jynlo", EmailService.currentBrand("Assunto Jynlo"));
    }
}
