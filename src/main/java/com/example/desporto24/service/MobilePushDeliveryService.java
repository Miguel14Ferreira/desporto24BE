package com.example.desporto24.service;

import com.example.desporto24.repository.MobilePushDeviceRepository;
import com.example.desporto24.register.Notifications.Notifications;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Fail-closed, optional Expo transport. Push payloads must not include private message text. */
@Service
@RequiredArgsConstructor
public class MobilePushDeliveryService {
    private static final URI EXPO_SEND = URI.create("https://exp.host/--/api/v2/push/send");
    private final MobilePushDeviceRepository devices;
    private final NotificationPreferencesService preferences;
    private final ConversationNotificationMuteService mutes;
    private final ObjectMapper json;
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3)).build();

    @Value("${jynlo.push.enabled:false}")
    private boolean enabled;
    @Value("${jynlo.push.access-token:}")
    private String accessToken;

    public record StoredNotification(String username, Long notificationId,
            String category, Long sessionId, Long communityId, String relatedUsername,
            boolean permitted) {}

    /**
     * No delivery on transaction rollback. Notifications are only a cue to reopen
     * the authenticated inbox, never a transport for message bodies or addresses.
     */
    @Async("jynloPushExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(StoredNotification event) {
        if (!enabled || accessToken == null || accessToken.isBlank() || !event.permitted()
                || event.username() == null || event.username().isBlank()) return;
        String account = event.username().toLowerCase(Locale.ROOT);
        // Re-evaluate account preferences at delivery time.
        var rules = preferences.get(account);
        if (!rules.inAppEnabled() || rules.disabledInAppTypes().contains(event.category())) return;
        if ("CHAT_MESSAGE".equals(event.category())) {
            // A chat push without a trustworthy conversation reference cannot
            // be checked against per-conversation mutes: fail closed.
            String kind;
            String key;
            if (event.sessionId()!=null && event.sessionId()>0) {
                kind="session"; key=event.sessionId().toString();
            } else if (event.communityId()!=null && event.communityId()>0) {
                kind="community"; key=event.communityId().toString();
            } else if (event.relatedUsername()!=null
                    && event.relatedUsername().toLowerCase(Locale.ROOT).matches("[a-z0-9._-]{1,128}")) {
                kind="direct"; key=event.relatedUsername().toLowerCase(Locale.ROOT);
            } else return;
            if (mutes.muted(account,kind,key)) return;
        }
        List<com.example.desporto24.model.MobilePushDevice> recipients =
                devices.findTop20ByUsernameOrderByUpdatedAtDesc(account);
        for (var device : recipients) {
            if (!account.equals(device.getUsername())) continue;
            deliver(device.getPushToken(), event.notificationId());
        }
    }

    private void deliver(String token, Long notificationId) {
        try {
            byte[] body = json.writeValueAsBytes(Map.of(
                    "to", token,
                    "title", "Jynlo",
                    "body", "Tens uma nova notificação.",
                    "sound", "default",
                    "channelId", "jynlo-messages",
                    "data", Map.of("kind", "notification", "id", notificationId == null ? 0 : notificationId)
            ));
            HttpRequest request = HttpRequest.newBuilder(EXPO_SEND)
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body)).build();
            var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return;
            JsonNode tickets = json.readTree(response.body()).path("data");
            JsonNode ticket = tickets.isArray() && !tickets.isEmpty() ? tickets.get(0) : tickets;
            if ("error".equals(ticket.path("status").asText())
                    && "DeviceNotRegistered".equals(ticket.path("details").path("error").asText())) {
                devices.findByPushToken(token).ifPresent(devices::delete);
            }
            // Expo receipt collection and permanent-error handling are a separate
            // required step before treating push delivery as production-ready.
        } catch (Exception ignored) {
            // Push delivery must never make a successful notification transaction fail.
            // Do not log tokens, external errors or private message metadata.
        }
    }
}
