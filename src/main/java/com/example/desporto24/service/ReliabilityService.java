package com.example.desporto24.service;

import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.reliability.ReliabilitySummaryResponse;
import com.example.desporto24.model.dto.reliability.AttendanceHistoryResponse;
import com.example.desporto24.model.dto.reliability.AttendanceDisputeResponse;
import com.example.desporto24.model.session.AttendanceDispute;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.AttendanceDisputeRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.enumeration.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class ReliabilityService {

    private static final Set<DisputeStatus> EXCLUDED_DISPUTE_STATUSES =
            Set.of(DisputeStatus.OPEN, DisputeStatus.ACCEPTED);

    private final SessionParticipantRepository participantRepository;
    private final AttendanceDisputeRepository disputeRepository;
    private final ReliabilityPolicyProperties properties;
    private final ProfileRepository profileRepository;
    private final SessionRepository sessionRepository;
    private final NotificationsRepository notificationsRepository;
    private final PilotEventService pilotEventService;

    public AttendanceStatus leaveAttendanceStatus(Instant leaveAt, Instant start) {
        if (!leaveAt.isBefore(start)) {
            throw new IllegalStateException("Cannot leave a session after it has started");
        }

        Instant freeCancellationBoundary =
                start.minus(Duration.ofHours(properties.getFreeCancelHours()));

        if (!leaveAt.isAfter(freeCancellationBoundary)) {
            return AttendanceStatus.PENDING;
        }

        return AttendanceStatus.LATE_CANCELLED;
    }

    @Transactional(readOnly = true)
    public ReliabilitySummaryResponse summary(Profile profile, Instant now) {
        Instant from = now.minus(Duration.ofDays(properties.getLookbackDays()));
        List<SessionParticipant> history =
                participantRepository.findReliabilityHistory(profile.getId(), from, now);

        Set<Long> excludedParticipantIds = history.isEmpty()
                ? Set.of()
                : new java.util.HashSet<>(
                        disputeRepository.findParticipantIdsByParticipantIdsAndStatusIn(
                                history.stream()
                                        .map(SessionParticipant::getId)
                                        .toList(),
                                EXCLUDED_DISPUTE_STATUSES
                        )
                );

        List<SessionParticipant> effective = history.stream()
                .filter(p -> !excludedParticipantIds.contains(p.getId()))
                .toList();

        double score = effective.stream()
                .mapToDouble(this::penaltyWeight)
                .sum();

        int noShows = (int) effective.stream()
                .filter(p -> p.getAttendanceStatus() == AttendanceStatus.NO_SHOW)
                .count();

        int lateCancellations = (int) effective.stream()
                .filter(p -> p.getAttendanceStatus() == AttendanceStatus.LATE_CANCELLED)
                .count();

        int attended = (int) effective.stream()
                .filter(p -> p.getAttendanceStatus() == AttendanceStatus.ATTENDED)
                .count();

        int completed = attended + noShows;
        double attendanceRate =
                completed == 0 ? 100.0 : (attended * 100.0) / completed;

        Instant restrictionBoundary =
                now.minus(Duration.ofDays(properties.getRestrictionDays()));

        boolean recentPenalty = effective.stream()
                .filter(this::isPenalty)
                .map(this::eventTime)
                .anyMatch(t -> t != null && t.isAfter(restrictionBoundary));

        return new ReliabilitySummaryResponse(
                attendanceRate,
                completed,
                noShows,
                lateCancellations,
                score,
                score >= properties.getWarningThreshold(),
                score >= properties.getReducedPriorityThreshold(),
                score >= properties.getRestrictionThreshold() && recentPenalty
        );
    }

    @Transactional(readOnly = true)
    public ReliabilitySummaryResponse summaryByUsername(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        return summary(profile, Instant.now());
    }

    @Transactional(readOnly = true)
    public void requireJoinAllowed(Profile profile, Instant now) {
        if (summary(profile, now).joinRestricted()) {
            throw new AccessDeniedException(
                    "Joining new sessions is temporarily restricted due to recent no-shows");
        }
    }

    @Transactional(readOnly = true)
    public int priorityTier(Profile profile, Instant now) {
        return summary(profile, now).reducedPriority() ? 1 : 0;
    }

    @Transactional
    public void recordAttendance(
            Long sessionId,
            Long participantId,
            AttendanceStatus status,
            String creatorUsername) {

        if (status != AttendanceStatus.ATTENDED
                && status != AttendanceStatus.NO_SHOW) {
            throw new IllegalArgumentException(
                    "Attendance can only be ATTENDED or NO_SHOW");
        }

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Attendance cannot be recorded for a cancelled session");
        }

        Profile creator = profileRepository.findUserByUsername(creatorUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (session.getProfile() == null
                || !session.getProfile().getId().equals(creator.getId())) {
            throw new AccessDeniedException(
                    "Only the session creator can record attendance");
        }

        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalStateException("Participant not found"));

        if (!participant.getSession().getId().equals(sessionId)) {
            throw new IllegalArgumentException(
                    "Participant does not belong to this session");
        }

        Instant now = Instant.now();
        Instant allowedAt = session.getGameDate().toInstant()
                .plus(Duration.ofMinutes(
                        properties.getAttendanceMarkingDelayMinutes()));

        if (now.isBefore(allowedAt)) {
            throw new IllegalStateException(
                    "Attendance cannot be recorded yet");
        }

        participant.setAttendanceStatus(status);
        participant.setAttendanceReportedBy(creator);
        participant.setAttendanceRecordedAt(now);
        participantRepository.save(participant);

        if (status == AttendanceStatus.ATTENDED) {
            pilotEventService.recordAttendanceConfirmed(
                    participant.getProfile(),
                    sessionId
            );
        }
    }

    @Transactional(readOnly = true)
    public List<AttendanceHistoryResponse> attendanceHistory(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        List<SessionParticipant> history = participantRepository
                .findByProfileIdAndAttendanceStatusInOrderByAttendanceRecordedAtDesc(
                        profile.getId(),
                        Set.of(
                                AttendanceStatus.ATTENDED,
                                AttendanceStatus.NO_SHOW,
                                AttendanceStatus.LATE_CANCELLED
                        ),
                        PageRequest.of(0, 200)
                );

        java.util.Map<Long, AttendanceDispute> latestDisputesByParticipant =
                new java.util.HashMap<>();
        if (!history.isEmpty()) {
            disputeRepository.findByParticipantIdInOrderByCreatedAtDesc(
                            history.stream()
                                    .map(SessionParticipant::getId)
                                    .toList()
                    )
                    .forEach(dispute -> latestDisputesByParticipant.putIfAbsent(
                            dispute.getParticipant().getId(),
                            dispute
                    ));
        }

        return history.stream()
                .map(participant -> {
                    AttendanceDispute latestDispute =
                            latestDisputesByParticipant.get(participant.getId());

                    Session session = participant.getSession();
                    return new AttendanceHistoryResponse(
                            participant.getId(),
                            session.getId(),
                            session.getSport(),
                            session.getCity(),
                            session.getGameDate() == null
                                    ? null
                                    : session.getGameDate().toInstant(),
                            participant.getAttendanceStatus(),
                            participant.getAttendanceRecordedAt(),
                            latestDispute == null ? null : latestDispute.getStatus(),
                            participant.getAttendanceStatus() == AttendanceStatus.NO_SHOW
                                    && latestDispute == null
                    );
                })
                .toList();
    }

    @Transactional
    public void createDispute(
            Long participantId,
            String reason,
            String username) {

        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalStateException("Participant not found"));

        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (!participant.getProfile().getId().equals(actor.getId())) {
            throw new AccessDeniedException(
                    "Only the participant can dispute their attendance");
        }

        if (participant.getAttendanceStatus() != AttendanceStatus.NO_SHOW) {
            throw new IllegalStateException(
                    "Only a no-show can be disputed");
        }

        if (disputeRepository.findTopByParticipantIdOrderByCreatedAtDesc(participantId)
                .isPresent()) {
            throw new IllegalStateException(
                    "A dispute already exists for this attendance record");
        }

        Profile reporter = participant.getAttendanceReportedBy() != null
                ? participant.getAttendanceReportedBy()
                : participant.getSession().getProfile();

        disputeRepository.save(AttendanceDispute.builder()
                .participant(participant)
                .reporterProfile(reporter)
                .disputingProfile(actor)
                .reason(reason.trim())
                .status(DisputeStatus.OPEN)
                .createdAt(Instant.now())
                .build());
    }

    @Transactional(readOnly = true)
    public List<AttendanceDisputeResponse> disputes(DisputeStatus status) {
        return disputeRepository.findByStatusOrderByCreatedAtAsc(
                        status,
                        PageRequest.of(0, 200)
                )
                .stream()
                .map(dispute -> {
                    SessionParticipant participant = dispute.getParticipant();
                    Session session = participant.getSession();

                    return new AttendanceDisputeResponse(
                            dispute.getId(),
                            participant.getId(),
                            session.getId(),
                            session.getSport(),
                            session.getCity(),
                            session.getGameDate() == null
                                    ? null
                                    : session.getGameDate().toInstant(),
                            dispute.getDisputingProfile().getUsername(),
                            dispute.getReporterProfile().getUsername(),
                            dispute.getReason(),
                            dispute.getStatus(),
                            dispute.getCreatedAt(),
                            dispute.getResolvedAt()
                    );
                })
                .toList();
    }

    @Transactional
    public void resolveDispute(Long disputeId, DisputeStatus status) {
        if (status != DisputeStatus.ACCEPTED
                && status != DisputeStatus.REJECTED) {
            throw new IllegalArgumentException(
                    "Dispute must be ACCEPTED or REJECTED");
        }

        AttendanceDispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new IllegalStateException("Dispute not found"));

        if (dispute.getStatus() != DisputeStatus.OPEN) {
            throw new IllegalStateException("Dispute is already resolved");
        }

        dispute.setStatus(status);
        dispute.setResolvedAt(Instant.now());
        disputeRepository.save(dispute);
        notifyDisputeResolution(dispute);
    }

    private void notifyDisputeResolution(AttendanceDispute dispute) {
        boolean accepted = dispute.getStatus() == DisputeStatus.ACCEPTED;
        Session session = dispute.getParticipant().getSession();

        String subject = "Resultado da contestação de presença";
        String message = accepted
                ? "A tua contestação da falta na sessão de "
                    + session.getSport()
                    + " foi aceite. Este registo deixa de penalizar a tua fiabilidade."
                : "A tua contestação da falta na sessão de "
                    + session.getSport()
                    + " foi rejeitada. O registo de falta mantém-se na tua fiabilidade.";

        Notifications notification = new Notifications(
                subject,
                message,
                "Olá,",
                "DESPORTO24",
                Instant.now().toString(),
                false,
                false,
                false,
                true,
                null,
                dispute.getDisputingProfile().getUsername()
        );
        notification.setType(
                accepted
                        ? NotificationType.ATTENDANCE_DISPUTE_ACCEPTED
                        : NotificationType.ATTENDANCE_DISPUTE_REJECTED
        );
        notification.setSessionId(session.getId());
        notification.setRead(false);
        notificationsRepository.save(notification);
    }

    private boolean isPenalty(SessionParticipant participant) {
        return participant.getAttendanceStatus() == AttendanceStatus.NO_SHOW
                || participant.getAttendanceStatus()
                    == AttendanceStatus.LATE_CANCELLED;
    }

    private double penaltyWeight(SessionParticipant participant) {
        return switch (participant.getAttendanceStatus()) {
            case NO_SHOW -> properties.getNoShowWeight();
            case LATE_CANCELLED -> properties.getLateCancellationWeight();
            default -> 0.0;
        };
    }

    private Instant eventTime(SessionParticipant participant) {
        if (participant.getAttendanceRecordedAt() != null) {
            return participant.getAttendanceRecordedAt();
        }

        return participant.getSession() == null
                || participant.getSession().getGameDate() == null
                ? null
                : participant.getSession().getGameDate().toInstant();
    }
}
