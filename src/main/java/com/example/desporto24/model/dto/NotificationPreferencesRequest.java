package com.example.desporto24.model.dto;

import java.util.Set;

public record NotificationPreferencesRequest(
        boolean inAppEnabled, boolean emailEnabled,
        Set<String> disabledInAppTypes, Set<String> disabledEmailTypes) {
}
