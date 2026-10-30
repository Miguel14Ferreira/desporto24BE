package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.example.desporto24.email.TransactionalEmailTemplate;

@Slf4j
@Component
public class CommunityInviteEmailListener {

    private final EmailSender emailSender;
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private NotificationPreferencesService preferenceService;
    private final String uiAppUrl;

    public CommunityInviteEmailListener(
            EmailSender emailSender,
            @Value("${ui.app.url}") String uiAppUrl) {
        this.emailSender = emailSender;
        this.uiAppUrl = uiAppUrl;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommunityInviteCreated(CommunityInviteCreatedEvent event) {
        if (event.recipientEmail() == null || event.recipientEmail().isBlank()) {
            return;
        }

        try {
            // A failed opt-out check must not send optional mail or turn the
            // already-committed invitation into an apparent request failure.
            if (preferenceService != null
                    && !preferenceService.allowsOptionalEmailAddress(event.recipientEmail(),
                            com.example.desporto24.enumeration.NotificationType.COMMUNITY_INVITE)) {
                return;
            }
            emailSender.send(event.recipientEmail(), "Convite para uma comunidade | Community invitation — Jynlo",
                    buildEmail(event));
        } catch (RuntimeException exception) {
            // No email was sent on a preference lookup error; invitation remains committed.
            log.warn("Optional community invitation email skipped for invitation {}", event.inviteId(), exception);
        }
    }

    private String buildEmail(CommunityInviteCreatedEvent event) {
        // The recipient's invite inbox, not the generic communities discovery landing.
        // AuthenticationGuard preserves this target across login/registration.
        String link = uiAppUrl.replaceAll("/+$", "")
                + "/menu/communities?view=mine&invite=" + event.inviteId();
        return TransactionalEmailTemplate.render(
                "Convite para uma comunidade",
                "@" + event.inviterUsername() + " convidou-te para a comunidade " + event.communityName()
                        + ". Entra na tua conta para aceitar ou rejeitar o convite.",
                "Community invitation",
                "@" + event.inviterUsername() + " invited you to the " + event.communityName()
                        + " community. Sign in to accept or reject the invitation.",
                "Ver convite / View invitation",
                link,
                "O convite também está disponível em Comunidades na app.",
                "You can also find the invitation in Communities in the app."
        );
    }
}
