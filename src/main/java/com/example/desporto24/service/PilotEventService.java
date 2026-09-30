package com.example.desporto24.service;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.analytics.PilotEvent;
import com.example.desporto24.repository.analytics.PilotEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PilotEventService {

    private final PilotEventRepository pilotEventRepository;

    @Transactional
    public void recordFirstAccess(String deviceId) {
        String normalizedDeviceId = deviceId.trim();

        if (pilotEventRepository.existsByEventTypeAndDeviceId(
                PilotEventType.ACCESS,
                normalizedDeviceId
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.ACCESS)
                .deviceId(normalizedDeviceId)
                .createdAt(Instant.now())
                .build());
    }

    @Transactional
    public void recordBusinessEvent(
            PilotEventType eventType,
            Profile profile,
            Long subjectId) {
        if (eventType == PilotEventType.ACCESS) {
            throw new IllegalArgumentException("ACCESS events must use recordFirstAccess");
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(eventType)
                .profile(profile)
                .subjectId(subjectId)
                .createdAt(Instant.now())
                .build());
    }
}
