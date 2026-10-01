package com.example.desporto24.service;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.analytics.PilotEvent;
import com.example.desporto24.model.dto.analytics.PilotActivation7dResponse;
import com.example.desporto24.model.dto.analytics.PilotEventCountsResponse;
import com.example.desporto24.model.dto.analytics.PilotRepetition30dResponse;
import com.example.desporto24.repository.analytics.PilotEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PilotEventService {

    private static final Duration ACTIVATION_WINDOW = Duration.ofDays(7);
    private static final long ACTIVATION_MIN_ELIGIBLE = 50L;
    private static final Duration REPETITION_WINDOW = Duration.ofDays(30);
    private static final long REPETITION_MIN_ELIGIBLE = 20L;

    private final PilotEventRepository pilotEventRepository;

    @Transactional(readOnly = true)
    public PilotEventCountsResponse eventCounts() {
        return new PilotEventCountsResponse(
                pilotEventRepository.countByEventType(PilotEventType.ACCESS),
                pilotEventRepository.countByEventType(PilotEventType.REGISTRATION_COMPLETED),
                pilotEventRepository.countByEventType(PilotEventType.SESSION_SEARCHED),
                pilotEventRepository.countByEventType(PilotEventType.SESSION_CREATED),
                pilotEventRepository.countByEventType(PilotEventType.SESSION_JOINED),
                pilotEventRepository.countByEventType(PilotEventType.ATTENDANCE_CONFIRMED),
                pilotEventRepository.countByEventType(PilotEventType.SESSION_COMPLETED)
        );
    }

    @Transactional(readOnly = true)
    public PilotActivation7dResponse activation7d() {
        Instant eligibilityCutoff = Instant.now().minus(ACTIVATION_WINDOW);

        List<PilotEvent> eligibleRegistrations = pilotEventRepository
                .findByEventType(PilotEventType.REGISTRATION_COMPLETED)
                .stream()
                .filter(this::hasPersistedProfileAndTimestamp)
                .filter(event -> !event.getCreatedAt().isAfter(eligibilityCutoff))
                .toList();

        List<PilotEvent> activationEvents = new ArrayList<>();
        activationEvents.addAll(pilotEventRepository.findByEventType(PilotEventType.SESSION_SEARCHED));
        activationEvents.addAll(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED));
        activationEvents.addAll(pilotEventRepository.findByEventType(PilotEventType.SESSION_JOINED));

        long activatedRegistrations = eligibleRegistrations.stream()
                .filter(registration -> activationEvents.stream()
                        .anyMatch(action -> activatesWithinWindow(registration, action)))
                .count();

        long eligibleCount = eligibleRegistrations.size();
        BigDecimal ratePercent = eligibleCount == 0
                ? null
                : BigDecimal.valueOf(activatedRegistrations)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(eligibleCount), 2, RoundingMode.HALF_UP);

        return new PilotActivation7dResponse(
                activatedRegistrations,
                eligibleCount,
                ratePercent,
                ACTIVATION_MIN_ELIGIBLE,
                eligibleCount >= ACTIVATION_MIN_ELIGIBLE
        );
    }

    @Transactional(readOnly = true)
    public PilotRepetition30dResponse repetition30d() {
        Instant eligibilityCutoff = Instant.now().minus(REPETITION_WINDOW);

        List<PilotEvent> attendanceEvents = pilotEventRepository
                .findByEventType(PilotEventType.ATTENDANCE_CONFIRMED)
                .stream()
                .filter(this::hasPersistedProfileTimestampAndSubject)
                .sorted((left, right) -> left.getCreatedAt().compareTo(right.getCreatedAt()))
                .toList();

        Map<Long, PilotEvent> firstAttendanceByProfile = new LinkedHashMap<>();
        attendanceEvents.forEach(event -> firstAttendanceByProfile.putIfAbsent(
                event.getProfile().getId(),
                event
        ));

        List<PilotEvent> eligibleFirstAttendances = firstAttendanceByProfile.values().stream()
                .filter(event -> !event.getCreatedAt().isAfter(eligibilityCutoff))
                .toList();

        long repeatedParticipants = eligibleFirstAttendances.stream()
                .filter(firstAttendance -> attendanceEvents.stream()
                        .anyMatch(nextAttendance -> repeatsWithinWindow(firstAttendance, nextAttendance)))
                .count();

        long eligibleParticipants = eligibleFirstAttendances.size();
        BigDecimal ratePercent = eligibleParticipants == 0
                ? null
                : BigDecimal.valueOf(repeatedParticipants)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(eligibleParticipants), 2, RoundingMode.HALF_UP);

        return new PilotRepetition30dResponse(
                repeatedParticipants,
                eligibleParticipants,
                ratePercent,
                REPETITION_MIN_ELIGIBLE,
                eligibleParticipants >= REPETITION_MIN_ELIGIBLE
        );
    }

    private boolean hasPersistedProfileTimestampAndSubject(PilotEvent event) {
        return hasPersistedProfileAndTimestamp(event) && event.getSubjectId() != null;
    }

    private boolean repeatsWithinWindow(PilotEvent firstAttendance, PilotEvent nextAttendance) {
        if (!hasPersistedProfileTimestampAndSubject(nextAttendance)
                || !firstAttendance.getProfile().getId().equals(nextAttendance.getProfile().getId())
                || firstAttendance.getSubjectId().equals(nextAttendance.getSubjectId())) {
            return false;
        }

        Instant windowEnd = firstAttendance.getCreatedAt().plus(REPETITION_WINDOW);
        return nextAttendance.getCreatedAt().isAfter(firstAttendance.getCreatedAt())
                && !nextAttendance.getCreatedAt().isAfter(windowEnd);
    }

    private boolean hasPersistedProfileAndTimestamp(PilotEvent event) {
        return event.getProfile() != null
                && event.getProfile().getId() != null
                && event.getCreatedAt() != null;
    }

    private boolean activatesWithinWindow(PilotEvent registration, PilotEvent action) {
        if (!hasPersistedProfileAndTimestamp(action)
                || !registration.getProfile().getId().equals(action.getProfile().getId())) {
            return false;
        }

        Instant windowStart = registration.getCreatedAt();
        Instant windowEnd = windowStart.plus(ACTIVATION_WINDOW);
        return !action.getCreatedAt().isBefore(windowStart)
                && !action.getCreatedAt().isAfter(windowEnd);
    }

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
    public void recordSessionSearched(Profile profile) {
        if (profile == null || profile.getId() == null) {
            throw new IllegalArgumentException("Session search event requires a persisted profile");
        }

        if (pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.SESSION_SEARCHED,
                profile.getId()
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.SESSION_SEARCHED)
                .profile(profile)
                .createdAt(Instant.now())
                .build());
    }

    @Transactional
    public void recordSessionCreated(Profile profile, Long sessionId) {
        if (profile == null || profile.getId() == null || sessionId == null) {
            throw new IllegalArgumentException("Session creation event requires persisted profile and session");
        }

        if (pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_CREATED,
                sessionId
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.SESSION_CREATED)
                .profile(profile)
                .subjectId(sessionId)
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
    public void recordAttendanceConfirmed(Profile profile, Long sessionId) {
        if (profile == null || profile.getId() == null || sessionId == null) {
            throw new IllegalArgumentException("Attendance event requires persisted profile and session");
        }

        if (pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.ATTENDANCE_CONFIRMED,
                profile.getId(),
                sessionId
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.ATTENDANCE_CONFIRMED)
                .profile(profile)
                .subjectId(sessionId)
                .createdAt(Instant.now())
                .build());
    }

    @Transactional
    public void recordSessionCompleted(Profile profile, Long sessionId) {
        if (profile == null || profile.getId() == null || sessionId == null) {
            throw new IllegalArgumentException("Session completion event requires persisted profile and session");
        }

        if (pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_COMPLETED,
                sessionId
        )) {
            return;
        }

        pilotEventRepository.save(PilotEvent.builder()
                .eventType(PilotEventType.SESSION_COMPLETED)
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
