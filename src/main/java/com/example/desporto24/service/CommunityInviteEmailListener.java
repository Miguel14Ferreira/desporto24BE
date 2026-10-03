package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Component
public class CommunityInviteEmailListener {

    private final EmailSender emailSender;
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
            emailSender.send(event.recipientEmail(), buildEmail(event));
        } catch (RuntimeException exception) {
            // The invitation and in-app notification have already been committed.
            log.warn("Could not send email for community invitation {}", event.inviteId(), exception);
        }
    }

    private String buildEmail(CommunityInviteCreatedEvent event) {
        String inviter = HtmlUtils.htmlEscape(event.inviterUsername());
        String community = HtmlUtils.htmlEscape(event.communityName());
        String link = HtmlUtils.htmlEscape(uiAppUrl.replaceAll("/+$", "") + "/menu/communities");
        return """
                <!doctype html>
                <html lang="pt"><head><meta charset="UTF-8"><title>Convite Desporto24</title></head>
                <body style="margin:0;padding:24px;background:#f3f6fa;font-family:Arial,sans-serif;color:#102033">
                <div style="max-width:560px;margin:auto;padding:24px;background:#ffffff;border-radius:12px">
                  <p style="font-weight:bold">Desporto24</p>
                  <h1 style="font-size:24px">Convite para uma comunidade</h1>
                  <p style="font-size:16px;line-height:1.6">@%s convidou-te para a comunidade <strong>%s</strong>.</p>
                  <p style="font-size:16px;line-height:1.6">Entra na tua conta para aceitar ou rejeitar o convite.</p>
                  <div lang="en">
                    <h2 style="font-size:20px">Community invitation</h2>
                    <p style="font-size:16px;line-height:1.6">@%s invited you to the <strong>%s</strong> community.
                    Sign in to accept or reject the invitation.</p>
                  </div>
                  <p><a href="%s" style="display:inline-block;padding:14px 20px;background:#124d72;color:#ffffff;text-decoration:none;border-radius:8px">Ver convite / View invitation</a></p>
                  <p style="font-size:14px;line-height:1.6">O convite também está disponível em Comunidades na app.<br>
                  <span lang="en">You can also find the invitation in Communities in the app.</span></p>
                </div></body></html>
                """.formatted(inviter, community, inviter, community, link);
    }
}
