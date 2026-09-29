package com.example.desporto24.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleIdentityService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleIdentityService(@Value("${desporto.google.client-id:}") String clientId) {
        if (clientId == null || clientId.isBlank()) {
            this.verifier = null;
            return;
        }

        try {
            this.verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .build();
        } catch (Exception exception) {
            throw new IllegalStateException("Não foi possível configurar a autenticação Google", exception);
        }
    }

    public GoogleIdentity verify(String credential) {
        if (verifier == null) {
            throw new IllegalStateException("GOOGLE_CLIENT_ID must be configured");
        }

        try {
            GoogleIdToken token = verifier.verify(credential);
            if (token == null) {
                throw new BadCredentialsException("Credencial Google inválida");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            Object verifiedClaim = payload.get("email_verified");
            boolean emailVerified = Boolean.TRUE.equals(verifiedClaim)
                    || "true".equalsIgnoreCase(String.valueOf(verifiedClaim));

            String email = payload.getEmail();
            String subject = payload.getSubject();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            if (!emailVerified || email == null || email.isBlank() || subject == null || subject.isBlank()) {
                throw new BadCredentialsException("Conta Google não verificada");
            }

            return new GoogleIdentity(subject, email, name, picture);
        } catch (BadCredentialsException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BadCredentialsException("Não foi possível validar a credencial Google", exception);
        }
    }

    public record GoogleIdentity(
            String subject,
            String email,
            String name,
            String picture
    ) {
    }
}
