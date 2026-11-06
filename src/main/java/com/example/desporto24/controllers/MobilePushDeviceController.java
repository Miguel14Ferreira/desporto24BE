package com.example.desporto24.controllers;

import com.example.desporto24.service.MobilePushDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Only the authenticated account can enroll or revoke its own device. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/me/devices")
public class MobilePushDeviceController {
    private final MobilePushDeviceService devices;

    @PutMapping
    public MobilePushDeviceService.RegistrationStatus register(
            Authentication authentication,
            @RequestBody MobilePushDeviceService.Registration registration) {
        return devices.register(authentication.getName(), registration);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> unregister(Authentication authentication,
                                           @PathVariable String deviceId) {
        devices.unregister(authentication.getName(),deviceId);
        return ResponseEntity.noContent().build();
    }
}
