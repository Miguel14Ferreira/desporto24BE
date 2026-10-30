package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

/**
 * The in-app alert shares the moderation transaction; optional email is only
 * delivered AFTER_COMMIT, so an approval rollback never sends a false decision.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VenueEditDecisionNotificationService {
    private final NotificationsRepository notifications;
    private final NotificationPreferencesService preferences;
    private final ProfileRepository profiles;
    private final EmailService emailService;
    private final ApplicationEventPublisher events;

    @Value("${ui.app.url:https://d24sports.com}")
    private String appUrl;

    public void decision(long suggestionId, long venueId, String venueName,
                         String submitter, boolean approved, String decisionNote) {
        if (submitter == null || submitter.isBlank()) return;
        if (preferences.allowsInApp(submitter, NotificationType.VENUE_EDIT_DECISION)) {
            var item = new Notifications();
            item.setUsername(submitter);
            item.setSubject(approved ? "Sugestão de recinto aprovada" : "Sugestão de recinto rejeitada");
            item.setMessage("A tua proposta para \"" + venueName + "\" foi "
                    + (approved ? "aprovada." : "rejeitada.")
                    + (decisionNote == null || decisionNote.isBlank() ? ""
                        : " Justificação: " + decisionNote));
            item.setGreeting("");
            item.setSignature("");
            item.setCreatedAt(Instant.now().toString());
            item.setType(NotificationType.VENUE_EDIT_DECISION);
            item.setVenueId(venueId);
            item.setVenueEditSuggestionId(suggestionId);
            item.setRead(false);
            notifications.save(item);
        }
        // Email is optional and separate from the in-app toggle.
        if (preferences.allowsOptionalEmail(submitter, NotificationType.VENUE_EDIT_DECISION)) {
            events.publishEvent(new DecisionEmail(submitter, venueId, venueName, approved, decisionNote));
        }
    }

    public record DecisionEmail(String username, long venueId, String venueName,
                                boolean approved, String note) {}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOptionalEmail(DecisionEmail decision) {
        try {
            if (!preferences.allowsOptionalEmail(decision.username(), NotificationType.VENUE_EDIT_DECISION)) return;
            var profile = profiles.findUserByUsernameIgnoreCase(decision.username()).orElse(null);
            if (profile == null || !Boolean.TRUE.equals(profile.getEnabled())
                    || profile.getEmail() == null || profile.getEmail().isBlank()) return;
            String title = decision.approved() ? "A tua sugestão foi aprovada" : "A tua sugestão foi rejeitada";
            String destination = appUrl.replaceAll("/+$", "") + "/menu/venues/" + decision.venueId();
            String html = "<div style=\"font-family:Arial,sans-serif;line-height:1.6\">"
                    + "<h2>" + escape(title) + "</h2>"
                    + "<p>A tua proposta de alteração para o recinto <strong>"
                    + escape(decision.venueName()) + "</strong> foi "
                    + (decision.approved() ? "aprovada." : "rejeitada.") + "</p>"
                    + (decision.note() == null || decision.note().isBlank() ? ""
                        : "<p><strong>Resposta da administração:</strong> " + escape(decision.note()) + "</p>")
                    + "<p><a href=\"" + escape(destination) + "\">Consultar recinto na Jynlo</a></p>"
                    + "<p>Estas notificações opcionais podem ser desativadas nas tuas preferências da Jynlo.</p>"
                    + "</div>";
            emailService.send(profile.getEmail(), "Jynlo — " + title, html);
        } catch (RuntimeException ex) {
            // The approved/rejected decision must never be undone by an email outage.
            log.warn("Optional venue decision email could not be delivered; suggestion decision remains final", ex);
        }
    }

    private static String escape(String value) {
        return (value == null ? "" : value).replace("&", "&amp;")
                .replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
