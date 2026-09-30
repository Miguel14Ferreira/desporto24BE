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
    public void recordRegistrationCompleted(Profile profile) {
        if (profile == null || profile.getId() == null) {
            throw new IllegalArgumentException("Registration event requires a persisted profile");
        }

        if (pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.REGISTRATION_COMPLETED,
                profile.getId()
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.REGISTRATION_COMPLETED)
                .profile(profile)
                .createdAt(Instant.now())
                .build());
    }

    @Transactional
    public void recordSessionJoined(Profile profile, Long sessionId) {
        if (profile == null || profile.getId() == null || sessionId == null) {
            throw new IllegalArgumentException("Session join event requires persisted profile and session");
        }

        if (pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.SESSION_JOINED,
                profile.getId(),
                sessionId
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.SESSION_JOINED)
                .profile(profile)
                .subjectId(sessionId)
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
