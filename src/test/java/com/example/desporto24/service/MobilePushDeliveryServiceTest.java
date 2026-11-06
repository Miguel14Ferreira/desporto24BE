package com.example.desporto24.service;

import com.example.desporto24.model.dto.NotificationPreferencesRequest;
import com.example.desporto24.repository.MobilePushDeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Set;
import static org.mockito.Mockito.*;

class MobilePushDeliveryServiceTest {
    private final MobilePushDeviceRepository devices=mock(MobilePushDeviceRepository.class);
    private final NotificationPreferencesService preferences=mock(NotificationPreferencesService.class);
    private final MobilePushDeliveryService sender=
            new MobilePushDeliveryService(devices,preferences,new ObjectMapper());

    @Test
    void disabledByDefaultNeverLooksUpDeviceTokens() {
        ReflectionTestUtils.setField(sender,"enabled",false);
        ReflectionTestUtils.setField(sender,"accessToken","test-only");
        sender.afterCommit(new MobilePushDeliveryService.StoredNotification("player",42L,"CHAT",true));
        verifyNoInteractions(devices,preferences);
    }

    @Test
    void missingExpoCredentialFailsClosedEvenIfFlagEnabled() {
        ReflectionTestUtils.setField(sender,"enabled",true);
        ReflectionTestUtils.setField(sender,"accessToken","");
        sender.afterCommit(new MobilePushDeliveryService.StoredNotification("player",42L,"CHAT",true));
        verifyNoInteractions(devices,preferences);
    }

    @Test
    void accountPreferencesCanSuppressPushWithoutQueryingDevices() {
        ReflectionTestUtils.setField(sender,"enabled",true);
        ReflectionTestUtils.setField(sender,"accessToken","test-only");
        when(preferences.get("player")).thenReturn(
                new NotificationPreferencesRequest(true,true,Set.of("CHAT"),Set.of()));
        sender.afterCommit(new MobilePushDeliveryService.StoredNotification("PLAYER",42L,"CHAT",true));
        verifyNoInteractions(devices);
    }

    @Test
    void globallyDisabledInAppNotificationsSuppressDelivery() {
        ReflectionTestUtils.setField(sender,"enabled",true);
        ReflectionTestUtils.setField(sender,"accessToken","test-only");
        when(preferences.get("player")).thenReturn(
                new NotificationPreferencesRequest(false,true,Set.of(),Set.of()));
        sender.afterCommit(new MobilePushDeliveryService.StoredNotification("player",42L,"OTHER",true));
        verifyNoInteractions(devices);
    }
}
