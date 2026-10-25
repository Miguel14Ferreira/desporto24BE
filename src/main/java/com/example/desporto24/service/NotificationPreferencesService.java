package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.model.NotificationPreferences;
import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationPreferencesRepository;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Opt-out preferences for optional alerts only. Account verification, password
 * reset, account lock and other security-critical mail always remains enabled.
 * Absent preferences means the existing default (all optional alerts enabled).
 */
@Service
@RequiredArgsConstructor
public class NotificationPreferencesService {
    public static final String FRIEND_REQUEST = "FRIEND_REQUEST";
    public static final String PROFILE_UPDATE = "PROFILE_UPDATE";
    public static final String OTHER = "OTHER";

    private static final Set<String> TYPES = java.util.stream.Stream.concat(
            Arrays.stream(NotificationType.values()).map(Enum::name),
            java.util.stream.Stream.of(FRIEND_REQUEST, PROFILE_UPDATE, OTHER))
            .collect(Collectors.toUnmodifiableSet());

    private final NotificationPreferencesRepository repository;
    private final ProfileRepository profiles;

    public NotificationPreferencesRequest get(String username) {
        return repository.findById(username)
                .map(this::response)
                .orElseGet(() -> new NotificationPreferencesRequest(
                        true, true, Set.of(), Set.of()));
    }

    @Transactional
    public NotificationPreferencesRequest update(String username, NotificationPreferencesRequest request) {
        if (request == null || request.disabledEmailTypes() == null
                || request.disabledInAppTypes() == null
                || request.disabledEmailTypes().stream().anyMatch(type -> type == null || !TYPES.contains(type))
                || request.disabledInAppTypes().stream().anyMatch(type -> type == null || !TYPES.contains(type))) {
            throw new IllegalArgumentException("Tipos de notificação inválidos");
        }
        // Only the currently authenticated account is ever writable.
        if (profiles.findUserByUsername(username).isEmpty()) {
            throw new IllegalArgumentException("Conta não encontrada");
        }
        NotificationPreferences stored = repository.findById(username).orElseGet(() -> {
            NotificationPreferences fresh = new NotificationPreferences();
            fresh.setUsername(username);
            return fresh;
        });
        stored.setInAppEnabled(request.inAppEnabled());
        stored.setEmailEnabled(request.emailEnabled());
        stored.setDisabledInAppTypes(String.join(",", new TreeSet<>(request.disabledInAppTypes())));
        stored.setDisabledEmailTypes(String.join(",", new TreeSet<>(request.disabledEmailTypes())));
        return response(repository.save(stored));
    }

    public boolean allowsInApp(String username, Notifications notification) {
        NotificationPreferencesRequest prefs = get(username);
        return prefs.inAppEnabled() && !prefs.disabledInAppTypes().contains(category(notification));
    }

    public boolean allowsInApp(String username, NotificationType type) {
        NotificationPreferencesRequest prefs = get(username);
        return prefs.inAppEnabled() && !prefs.disabledInAppTypes().contains(type == null ? OTHER : type.name());
    }

    public boolean allowsOptionalEmail(String username, NotificationType type) {
        return allowsOptionalEmailCategory(username, type == null ? OTHER : type.name());
    }

    /** Also covers legacy optional types without a NotificationType enum value. */
    public boolean allowsOptionalEmailCategory(String username, String category) {
        if (!TYPES.contains(category)) {
            throw new IllegalArgumentException("Tipo de notificação inválido");
        }
        NotificationPreferencesRequest prefs = get(username);
        return prefs.emailEnabled() && !prefs.disabledEmailTypes().contains(category);
    }

    public boolean allowsOptionalEmailAddress(String email, NotificationType type) {
        return profiles.findUserByEmail(email)
                .map(profile -> allowsOptionalEmail(profile.getUsername(), type))
                .orElse(true); // External invitees have no account preferences yet.
    }

    public static String category(Notifications notification) {
        if (notification.getType() != null) return notification.getType().name();
        if (notification.isFriendRequest()) return FRIEND_REQUEST;
        if (notification.isUpdateProfile()) return PROFILE_UPDATE;
        return OTHER;
    }

    private NotificationPreferencesRequest response(NotificationPreferences stored) {
        return new NotificationPreferencesRequest(
                stored.isInAppEnabled(), stored.isEmailEnabled(),
                parse(stored.getDisabledInAppTypes()), parse(stored.getDisabledEmailTypes()));
    }

    private Set<String> parse(String value) {
        if (value == null || value.isBlank()) return Set.of();
        return Arrays.stream(value.split(",")).filter(TYPES::contains)
                .collect(Collectors.toUnmodifiableSet());
    }
}
