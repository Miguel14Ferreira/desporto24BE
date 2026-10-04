package com.example.desporto24.service.impl;

import com.example.desporto24.constant.SportsCatalog;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.JoinState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionRecurrence;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.dto.session.JoinSessionResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.dto.session.SessionParticipantResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.SessionDistanceCalculator;
import com.example.desporto24.service.SessionViewMapper;
import com.example.desporto24.service.SessionNotificationService;
import com.example.desporto24.service.PilotEventService;
import com.example.desporto24.service.ReliabilityService;
import com.example.desporto24.service.WaitlistPromotionService;
import com.example.desporto24.service.interfaces.SessionService;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final VenueRepository venueRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final SessionViewMapper sessionViewMapper;
    private final SessionDistanceCalculator distanceCalculator;
    private final PasswordHashService passwordHashService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WaitlistPromotionService waitlistPromotionService;
    private final ReliabilityService reliabilityService;
    private final SessionNotificationService sessionNotificationService;
    private final PilotEventService pilotEventService;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Override
    @Transactional
    public SessionDetailResponse create(CreateSessionRequest request, String username) {
        return create(request, null, username);
    }

    @Override
    @Transactional
    public SessionDetailResponse create(CreateSessionRequest request, MultipartFile photo, String username) {
        Profile creator = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Community linkedCommunity = null;
        if (request.communityId() != null) {
            linkedCommunity = communityRepository.findById(request.communityId())
                    .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));

            if (!communityMemberRepository.existsByCommunityIdAndProfileId(
                    linkedCommunity.getId(), creator.getId())) {
                throw new AccessDeniedException(
                        "Só membros da comunidade podem criar sessões associadas"
                );
            }
        }

        String sport = request.sport() == null ? "" : request.sport().trim();
        if (!SportsCatalog.ALLOWED.contains(sport)) {
            throw new IllegalArgumentException("Desporto inválido");
        }

        Venue linkedVenue = null;
        if (request.venueId() != null) {
            linkedVenue = venueRepository.findById(request.venueId())
                    .orElseThrow(() -> new IllegalArgumentException("Recinto não encontrado"));
        }

        if (!request.gameDate().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Session date must be in the future");
        }
        if (request.privateGame() && (request.password() == null || request.password().isBlank())) {
            throw new IllegalArgumentException("Private sessions require a password");
        }

        SessionRecurrence recurrence = request.recurrence() == null
                ? SessionRecurrence.NONE
                : request.recurrence();
        int occurrences = 1;
        long intervalDays = 0;

        if (recurrence != SessionRecurrence.NONE) {
            if (linkedCommunity == null) {
                throw new IllegalArgumentException(
                        "Recurring sessions must belong to a community"
                );
            }
            if (request.recurrenceOccurrences() == null) {
                throw new IllegalArgumentException(
                        "Recurring sessions require recurrenceOccurrences"
                );
            }

            occurrences = request.recurrenceOccurrences();
            intervalDays = recurrence == SessionRecurrence.WEEKLY ? 7 : 14;
        }

        String recurrenceSeriesId = recurrence == SessionRecurrence.NONE
                ? null
                : UUID.randomUUID().toString();

        Session firstSaved = null;
        for (int occurrence = 0; occurrence < occurrences; occurrence++) {
            Instant gameDate = request.gameDate().plus(
                    intervalDays * occurrence,
                    ChronoUnit.DAYS
            );
            Session saved = saveCreatedSession(
                    request,
                    photo,
                    creator,
                    linkedCommunity,
                    linkedVenue,
                    sport,
                    gameDate,
                    recurrenceSeriesId
            );
            if (firstSaved == null) {
                firstSaved = saved;
            }
        }

        return sessionViewMapper.toDetail(firstSaved, creator);
    }

    private Session saveCreatedSession(
            CreateSessionRequest request,
            MultipartFile photo,
            Profile creator,
            Community linkedCommunity,
            Venue linkedVenue,
            String sport,
            Instant gameDate,
            String recurrenceSeriesId) {
        Instant now = Instant.now();
        Session session = new Session();
        session.setProfile(creator);
        session.setUserId(creator.getUserId());
        session.setSport(sport);
        session.setGameDate(Date.from(gameDate));
        session.setCity(linkedVenue == null ? request.city() : linkedVenue.getCity());
        session.setAddress(linkedVenue == null ? request.address() : linkedVenue.getAddress());
        session.setMaxParticipants(request.maxParticipants());
        session.setPriceAmount(request.priceAmount() == null ? BigDecimal.ZERO : request.priceAmount());
        session.setPrivateGame(request.privateGame());
        session.setPasswordHash(request.privateGame() ? passwordHashService.encode(request.password()) : null);
        session.setLatitude(
                linkedVenue != null && linkedVenue.getLatitude() != null
                        ? linkedVenue.getLatitude()
                        : request.latitude()
        );
        session.setLongitude(
                linkedVenue != null && linkedVenue.getLongitude() != null
                        ? linkedVenue.getLongitude()
                        : request.longitude()
        );
        session.setStatus(SessionStatus.OPEN);
        session.setEnabled(true);
        session.setCreated_at(Date.from(now));
        session.setUpdatedAt(now);
        session.setCommunity(linkedCommunity);
        session.setAudience(linkedCommunity == null ? SessionAudience.PUBLIC : SessionAudience.COMMUNITY_ONLY);
        session.setVenue(linkedVenue);
        session.setRecurrenceSeriesId(recurrenceSeriesId);

        Session saved = sessionRepository.save(session);

        participantRepository.save(SessionParticipant.builder()
                .session(saved)
                .profile(creator)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(now)
                .build());

        if (photo != null && !photo.isEmpty()) {
            if (mediaStorage == null) {
                throw new IllegalStateException("Media storage is not configured");
            }
            var uri = mediaStorage.store(
                    "sessions/" + saved.getId() + "/" + UUID.randomUUID(),
                    photo
            );
            saved.setGamePhoto(uri.toString());
            saved = sessionRepository.save(saved);
        }

        pilotEventService.recordSessionCreated(creator, saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDetailResponse getById(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile viewer = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        if (session.getAudience() == SessionAudience.COMMUNITY_ONLY
                && session.getCommunity() != null
                && !communityMemberRepository.existsByCommunityIdAndProfileId(
                        session.getCommunity().getId(), viewer.getId())) {
            throw new AccessDeniedException("Esta sessão está reservada à comunidade");
        }
        return sessionViewMapper.toDetail(session, viewer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionSummaryResponse> series(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile viewer = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (session.getAudience() == SessionAudience.COMMUNITY_ONLY
                && session.getCommunity() != null
                && !communityMemberRepository.existsByCommunityIdAndProfileId(
                        session.getCommunity().getId(), viewer.getId())) {
            throw new AccessDeniedException("Esta sessão está reservada à comunidade");
        }

        if (session.getRecurrenceSeriesId() == null || session.getRecurrenceSeriesId().isBlank()) {
            return List.of(sessionViewMapper.toSummary(session, null, viewer));
        }

        return sessionRepository
                .findByRecurrenceSeriesIdOrderByGameDateAsc(session.getRecurrenceSeriesId())
                .stream()
                .map(item -> sessionViewMapper.toSummary(item, null, viewer))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionSummaryResponse> discover(SessionSearchCriteria criteria) {
        return discoverSessions(criteria);
    }

    @Override
    @Transactional
    public List<SessionSummaryResponse> discover(SessionSearchCriteria criteria, String username) {
        List<SessionSummaryResponse> result = discoverSessions(criteria);

        if (username != null && !username.isBlank()) {
            profileRepository.findUserByUsername(username)
                    .ifPresent(pilotEventService::recordSessionSearched);
        }

        return result;
    }

    private List<SessionSummaryResponse> discoverSessions(SessionSearchCriteria criteria) {
        List<Session> candidates = sessionRepository.findByStatusInAndGameDateAfterOrderByGameDateAsc(
                List.of(SessionStatus.OPEN, SessionStatus.FULL), new Date());

        double effectiveRadius = criteria.radiusKm() == null ? 25.0 : criteria.radiusKm();
        boolean hasCoordinates = criteria.latitude() != null && criteria.longitude() != null;

        return candidates.stream()
                .filter(s -> s.getAudience() != SessionAudience.COMMUNITY_ONLY)
                .filter(s -> criteria.sport() == null
                        || criteria.sport().isBlank()
                        || s.getSport().equalsIgnoreCase(criteria.sport()))
                .filter(s -> criteria.city() == null
                        || criteria.city().isBlank()
                        || (s.getCity() != null
                        && s.getCity().toLowerCase(Locale.ROOT)
                        .contains(criteria.city().toLowerCase(Locale.ROOT))))
                .filter(s -> criteria.from() == null
                        || !s.getGameDate().toInstant().isBefore(criteria.from()))
                .filter(s -> criteria.to() == null
                        || !s.getGameDate().toInstant().isAfter(criteria.to()))
                .filter(s -> criteria.privateGame() == null
                        || Boolean.TRUE.equals(s.getPrivateGame()) == criteria.privateGame())
                .filter(s -> !Boolean.TRUE.equals(criteria.freeOnly())
                        || s.getPriceAmount() == null
                        || s.getPriceAmount().compareTo(BigDecimal.ZERO) == 0)
                .filter(s -> criteria.minPrice() == null
                        || effectivePrice(s).compareTo(criteria.minPrice()) >= 0)
                .filter(s -> criteria.maxPrice() == null
                        || effectivePrice(s).compareTo(criteria.maxPrice()) <= 0)
                .map(s -> toDiscoveryView(s, criteria, hasCoordinates))
                .filter(v -> !hasCoordinates
                        || (v.distanceKm() != null && v.distanceKm() <= effectiveRadius))
                .filter(v -> !"available".equalsIgnoreCase(criteria.availability())
                        || v.participantCount() < v.maxParticipants())
                .sorted(discoveryComparator(criteria.sort()))
                .toList();
    }

    private BigDecimal effectivePrice(Session session) {
        return session.getPriceAmount() == null ? BigDecimal.ZERO : session.getPriceAmount();
    }

    private SessionSummaryResponse toDiscoveryView(
            Session session, SessionSearchCriteria criteria, boolean hasCoordinates) {
        Double distanceKm = null;
        if (hasCoordinates && session.getLatitude() != null && session.getLongitude() != null) {
            distanceKm = distanceCalculator.kilometres(
                    criteria.latitude(), criteria.longitude(),
                    session.getLatitude(), session.getLongitude());
        }
        return sessionViewMapper.toSummary(session, distanceKm);
    }

    private Comparator<SessionSummaryResponse> discoveryComparator(String sort) {
        if (sort == null || sort.isBlank() || "distance".equalsIgnoreCase(sort)) {
            return Comparator.comparing(
                    SessionSummaryResponse::distanceKm,
                    Comparator.nullsLast(Double::compareTo)
            ).thenComparing(SessionSummaryResponse::gameDate);
        }
        if ("vacancies".equalsIgnoreCase(sort)) {
            return Comparator.comparingInt(
                    (SessionSummaryResponse s) -> s.maxParticipants() - s.participantCount()
            ).reversed().thenComparing(SessionSummaryResponse::gameDate);
        }
        if ("price".equalsIgnoreCase(sort)) {
            return Comparator.comparing(SessionSummaryResponse::priceAmount)
                    .thenComparing(SessionSummaryResponse::gameDate);
        }
        return Comparator.comparing(SessionSummaryResponse::gameDate);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SessionParticipantResponse> participants(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        boolean isCreator = session.getProfile() != null
                && session.getProfile().getId().equals(actor.getId());
        boolean isActiveParticipant = participantRepository
                .existsBySessionIdAndProfileIdAndState(
                        sessionId,
                        actor.getId(),
                        ParticipantState.ACTIVE
                );

        if (!isCreator && !isActiveParticipant) {
            throw new AccessDeniedException(
                    "Só participantes da sessão podem ver quem vai participar"
            );
        }

        return participantRepository
                .findBySessionIdAndStateOrderByJoinedAtAsc(sessionId, ParticipantState.ACTIVE)
                .stream()
                .map(participant -> new SessionParticipantResponse(
                        participant.getId(),
                        participant.getProfile().getUsername(),
                        participant.getAttendanceStatus(),
                        participant.getJoinedAt(),
                        participant.getTeamCode()
                ))
                .toList();
    }

    @Override
    @Transactional
    public JoinSessionResponse join(Long sessionId, JoinSessionRequest request, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        assertJoinable(session);
        if (session.getAudience() == SessionAudience.COMMUNITY_ONLY
                && session.getCommunity() != null
                && !communityMemberRepository.existsByCommunityIdAndProfileId(
                        session.getCommunity().getId(), profile.getId())) {
            throw new AccessDeniedException("Esta sessão está reservada à comunidade");
        }
        Instant joinAt = Instant.now();
        reliabilityService.requireJoinAllowed(profile, joinAt);

        if (participantRepository.existsBySessionIdAndProfileIdAndState(
                sessionId, profile.getId(), ParticipantState.ACTIVE)) {
            return new JoinSessionResponse(JoinState.PARTICIPANT, null);
        }

        var existingWaitlist = waitlistRepository.findBySessionIdAndProfileId(sessionId, profile.getId());
        if (existingWaitlist.isPresent()) {
            return new JoinSessionResponse(
                    JoinState.WAITLISTED,
                    waitlistPosition(sessionId, existingWaitlist.get().getId())
            );
        }

        if (Boolean.TRUE.equals(session.getPrivateGame())) {
            String supplied = request == null ? null : request.password();
            if (supplied == null
                    || session.getPasswordHash() == null
                    || !passwordEncoder.matches(supplied, session.getPasswordHash())) {
                throw new BadCredentialsException("Invalid session password");
            }
        }

        int maxParticipants = session.getMaxParticipants() == null ? 0 : session.getMaxParticipants();
        long activeParticipants = participantRepository.countBySessionIdAndState(
                sessionId, ParticipantState.ACTIVE);

        if (activeParticipants < maxParticipants) {
            SessionParticipant participant = participantRepository
                    .findBySessionIdAndProfileId(sessionId, profile.getId())
                    .orElseGet(SessionParticipant::new);
            participant.setSession(session);
            participant.setProfile(profile);
            participant.setState(ParticipantState.ACTIVE);
            participant.setAttendanceStatus(AttendanceStatus.PENDING);
            participant.setJoinedAt(Instant.now());
            participant.setExitedAt(null);
            participant.setAttendanceReportedBy(null);
            participant.setAttendanceRecordedAt(null);
            participantRepository.save(participant);
            pilotEventService.recordSessionJoined(profile, sessionId);

            long newCount = activeParticipants + 1;
            session.setStatus(newCount >= maxParticipants ? SessionStatus.FULL : SessionStatus.OPEN);
            session.setUpdatedAt(Instant.now());
            sessionRepository.save(session);
            return new JoinSessionResponse(JoinState.PARTICIPANT, null);
        }

        SessionWaitlist waitlistEntry = waitlistRepository.save(SessionWaitlist.builder()
                .session(session)
                .profile(profile)
                .priorityTier(reliabilityService.priorityTier(profile, joinAt))
                .createdAt(joinAt)
                .build());

        session.setStatus(SessionStatus.FULL);
        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);

        return new JoinSessionResponse(
                JoinState.WAITLISTED,
                waitlistPosition(sessionId, waitlistEntry.getId())
        );
    }

    private void assertJoinable(Session session) {
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is not open for joining");
        }
        if (session.getGameDate() == null || !session.getGameDate().toInstant().isAfter(Instant.now())) {
            throw new IllegalStateException("Session has already started");
        }
    }

    private int waitlistPosition(Long sessionId, Long entryId) {
        List<SessionWaitlist> ordered = waitlistRepository
                .findBySessionIdOrderByPriorityTierAscCreatedAtAsc(sessionId);
        for (int i = 0; i < ordered.size(); i++) {
            if (ordered.get(i).getId().equals(entryId)) {
                return i + 1;
            }
        }
        throw new IllegalStateException("Waitlist entry not found after save");
    }


    @Override
    @Transactional
    public void leave(Long sessionId, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (session.getProfile().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Session creator must cancel the session instead of leaving");
        }
        requireParticipantChangesAllowed(session);

        var activeMembership = participantRepository
                .findBySessionIdAndProfileIdAndState(
                        sessionId, actor.getId(), ParticipantState.ACTIVE);

        if (activeMembership.isEmpty()) {
            var waitlistEntry = waitlistRepository
                    .findBySessionIdAndProfileId(sessionId, actor.getId());

            if (waitlistEntry.isPresent()) {
                waitlistRepository.delete(waitlistEntry.get());
                return;
            }

            throw new IllegalStateException(
                    "User is not an active participant or waitlisted"
            );
        }

        SessionParticipant membership = activeMembership.get();
        Instant now = Instant.now();
        AttendanceStatus leaveAttendance = reliabilityService.leaveAttendanceStatus(
                now, session.getGameDate().toInstant());

        membership.setState(ParticipantState.LEFT);
        membership.setExitedAt(now);

        if (leaveAttendance == AttendanceStatus.LATE_CANCELLED) {
            membership.setAttendanceStatus(AttendanceStatus.LATE_CANCELLED);
            membership.setAttendanceRecordedAt(now);
        }

        participantRepository.save(membership);

        waitlistPromotionService.promoteNext(session, now);
    }

    @Override
    @Transactional
    public void cancel(Long sessionId, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireCreator(session, actor);

        if (session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Completed sessions cannot be cancelled");
        }

        session.setStatus(SessionStatus.CANCELLED);
        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);
        sessionNotificationService.notifySessionCancelled(session);
    }

    @Override
    @Transactional
    public void complete(Long sessionId, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireCreator(session, actor);

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled sessions cannot be completed");
        }
        if (session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is already completed");
        }

        Instant now = Instant.now();
        if (session.getGameDate() == null || session.getGameDate().toInstant().isAfter(now)) {
            throw new IllegalStateException("Session cannot be completed before it starts");
        }

        session.setStatus(SessionStatus.COMPLETED);
        session.setUpdatedAt(now);
        sessionRepository.save(session);
        pilotEventService.recordSessionCompleted(actor, sessionId);
    }

    @Override
    @Transactional
    public void reportDidNotOccur(Long sessionId, String username) {
        reportProblem(sessionId, SessionProblemType.NOT_OCCURRED, null, username);
    }

    @Override
    @Transactional
    public void reportProblem(Long sessionId, SessionProblemType type, String details, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireCreator(session, actor);

        Instant now = Instant.now();
        if (session.getGameDate() == null || session.getGameDate().toInstant().isAfter(now)) {
            throw new IllegalStateException("Session problem cannot be reported before it starts");
        }
        if (session.getProblemType() != null) {
            throw new IllegalStateException("A session problem has already been reported");
        }
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is already closed");
        }

        session.setProblemType(type);
        session.setProblemDetails(details == null || details.isBlank() ? null : details.trim());
        session.setUpdatedAt(now);

        if (type == SessionProblemType.NOT_OCCURRED) {
            session.setOutcome(SessionOutcome.NOT_OCCURRED);
            session.setStatus(SessionStatus.CANCELLED);
        }

        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void removeParticipant(Long sessionId, Long participantId, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireCreator(session, actor);
        requireParticipantChangesAllowed(session);

        SessionParticipant membership = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalStateException("Participant not found"));

        if (!membership.getSession().getId().equals(sessionId)) {
            throw new IllegalArgumentException("Participant does not belong to this session");
        }
        if (membership.getProfile().getId().equals(session.getProfile().getId())) {
            throw new AccessDeniedException("Creator cannot remove themselves");
        }
        if (membership.getState() != ParticipantState.ACTIVE) {
            return;
        }

        Instant now = Instant.now();
        membership.setState(ParticipantState.REMOVED);
        membership.setExitedAt(now);
        participantRepository.save(membership);
        sessionNotificationService.notifyParticipantRemoved(session, membership.getProfile());

        waitlistPromotionService.promoteNext(session, now);
    }

    private void requireParticipantChangesAllowed(Session session) {
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Participants cannot be changed after the session ends");
        }
    }

    private void requireCreator(Session session, Profile actor) {
        if (session.getProfile() == null
                || !session.getProfile().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Only the session creator can perform this action");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MySessionsResponse mine(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        List<SessionSummaryResponse> created = sessionRepository
                .findByProfileIdOrderByGameDateAsc(profile.getId()).stream()
                .map(s -> sessionViewMapper.toSummary(s, null))
                .toList();

        Set<Long> createdIds = created.stream()
                .map(SessionSummaryResponse::id)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        List<SessionSummaryResponse> joined = participantRepository
                .findByProfileIdAndStateOrderByJoinedAtAsc(profile.getId(), ParticipantState.ACTIVE)
                .stream()
                .map(SessionParticipant::getSession)
                .filter(s -> !createdIds.contains(s.getId()))
                .map(s -> sessionViewMapper.toSummary(s, null))
                .toList();

        List<SessionSummaryResponse> waitlisted = waitlistRepository
                .findByProfileIdOrderByCreatedAtAsc(profile.getId())
                .stream()
                .map(entry -> sessionViewMapper.toSummary(entry.getSession(), null))
                .toList();

        return new MySessionsResponse(created, joined, waitlisted);
    }
}
