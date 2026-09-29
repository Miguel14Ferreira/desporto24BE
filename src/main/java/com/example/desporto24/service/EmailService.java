package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import com.example.desporto24.security.RuntimeSecrets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@Slf4j
public class EmailService implements EmailSender {

    private static final String RESEND_EMAILS_URL = "https://api.resend.com/emails";
    private static final String DEFAULT_SUBJECT = "Desporto24";

    private final RestClient restClient = RestClient.create();

    @Override
    public void send(String to, String html) {
        String apiKey = RuntimeSecrets.require("RESEND_API_KEY");
        String from = RuntimeSecrets.require("RESEND_FROM_EMAIL");

        Map<String, Object> payload = Map.of(
                "from", from,
                "to", new String[]{to},
                "subject", DEFAULT_SUBJECT,
                "html", html
        );

        try {
            ResendSendResponse response = restClient.post()
                    .uri(RESEND_EMAILS_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(ResendSendResponse.class);

            if (response == null || response.id() == null || response.id().isBlank()) {
                throw new IllegalStateException("Resend did not return an email id");
            }

            log.info("Email accepted by Resend: id={} recipient={}", response.id(), to);
        } catch (RuntimeException exception) {
            log.error("Failed to send email through Resend to {}", to, exception);
            throw exception;
        }
    }

    private record ResendSendResponse(String id) {
    }
}
