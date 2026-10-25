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
        if (preferenceService != null
                && event.recipientEmail() != null
                && !preferenceService.allowsOptionalEmailAddress(event.recipientEmail(),
                        com.example.desporto24.enumeration.NotificationType.COMMUNITY_INVITE)) {
            return;
        }
        if (event.recipientEmail() == null || event.recipientEmail().isBlank()) {
            return;
        }

        try {
            emailSender.send(event.recipientEmail(), "Convite para uma comunidade | Community invitation — Jynlo",
                    buildEmail(event));
        } catch (RuntimeException exception) {
            // The invitation and in-app notification have already been committed.
            log.warn("Could not send email for community invitation {}", event.inviteId(), exception);
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
