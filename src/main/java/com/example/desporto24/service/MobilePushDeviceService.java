package com.example.desporto24.service;

import com.example.desporto24.model.MobilePushDevice;
import com.example.desporto24.repository.MobilePushDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

/** Device tokens are never returned or logged by any API route. */
@Service
@RequiredArgsConstructor
public class MobilePushDeviceService {
    private final MobilePushDeviceRepository repository;

    public record Registration(String deviceId, String expoPushToken, String platform) {}
    public record RegistrationStatus(boolean registered) {}

    private static String username(String actor) {
        return actor.toLowerCase(Locale.ROOT);
    }

    private static String validId(String raw) {
        try {
            if(raw == null)throw new IllegalArgumentException();
            String normalized = UUID.fromString(raw).toString();
            if(!normalized.equals(raw.toLowerCase(Locale.ROOT)))throw new IllegalArgumentException();
            return normalized;
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid device ID");
        }
    }

    private static String validToken(String raw) {
        if(raw == null || raw.length() > 240
                || !raw.matches("(?:Expo|Exponent)PushToken\\[[A-Za-z0-9_-]{16,200}\\]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid push token");
        }
        return raw;
    }

    @Transactional
    public RegistrationStatus register(String actor, Registration registration) {
        if(registration == null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid device");
        String deviceId = validId(registration.deviceId());
        String pushToken = validToken(registration.expoPushToken());
        String platform = registration.platform();
        if(!"android".equals(platform) && !"ios".equals(platform)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid platform");
        }
        String owner=username(actor);
        // Never allow a new user to silently take ownership of another account's device.
        repository.findByPushToken(pushToken).ifPresent(existing -> {
            if(!existing.getUsername().equals(owner) || !existing.getDeviceId().equals(deviceId))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Device token already registered");
        });
        MobilePushDevice device = repository.findById(deviceId).orElseGet(MobilePushDevice::new);
        if(device.getUsername()!=null && !owner.equals(device.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Device belongs to another account");
        device.setDeviceId(deviceId);
        device.setUsername(owner);
        device.setPushToken(pushToken);
        device.setPlatform(platform);
        device.setUpdatedAt(Instant.now());
        repository.save(device);
        return new RegistrationStatus(true);
    }

    @Transactional
    public void unregister(String actor,String rawId) {
        String deviceId=validId(rawId);
        repository.findById(deviceId).ifPresent(device -> {
            if(device.getUsername().equals(username(actor)))repository.delete(device);
        });
    }
}
