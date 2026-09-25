package com.example.desporto24.service.impl;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.SessionDistanceCalculator;
import com.example.desporto24.service.SessionViewMapper;
import com.example.desporto24.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final SessionViewMapper sessionViewMapper;
    private final SessionDistanceCalculator distanceCalculator;
    private final PasswordHashService passwordHashService;

    @Override
    @Transactional
    public SessionDetailResponse create(CreateSessionRequest request, String username) {
        Profile creator = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (!request.gameDate().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Session date must be in the future");
        }
        if (request.privateGame() && (request.password() == null || request.password().isBlank())) {
            throw new IllegalArgumentException("Private sessions require a password");
        }

        Instant now = Instant.now();
        Session session = new Session();
        session.setProfile(creator);
        session.setUserId(creator.getUserId());
        session.setUsername(creator.getUsername());
        session.setSport(request.sport());
        session.setGameDate(Date.from(request.gameDate()));
        session.setCity(request.city());
        session.setAddress(request.address());
        session.setMaxParticipants(request.maxParticipants());
        session.setPriceAmount(request.priceAmount() == null ? BigDecimal.ZERO : request.priceAmount());
        session.setPrivateGame(request.privateGame());
        session.setPasswordHash(request.privateGame() ? passwordHashService.encode(request.password()) : null);
        session.setLatitude(request.latitude());
        session.setLongitude(request.longitude());
        session.setStatus(SessionStatus.OPEN);
        session.setEnabled(true);
        session.setCreated_at(Date.from(now));
        session.setUpdatedAt(now);

        Session saved = sessionRepository.save(session);

        participantRepository.save(SessionParticipant.builder()
                .session(saved)
                .profile(creator)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(now)
                .build());

        return sessionViewMapper.toDetail(saved, creator);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDetailResponse getById(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile viewer = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        return sessionViewMapper.toDetail(session, viewer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionSummaryResponse> discover(SessionSearchCriteria criteria) {
        List<Session> candidates = sessionRepository.findByStatusInAndGameDateAfterOrderByGameDateAsc(
                List.of(SessionStatus.OPEN, SessionStatus.FULL), new Date());

        double effectiveRadius = criteria.radiusKm() == null ? 25.0 : criteria.radiusKm();
        boolean hasCoordinates = criteria.latitude() != null && criteria.longitude() != null;

        return candidates.stream()
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
                .map(s -> toDiscoveryView(s, criteria, hasCoordinates))
                .filter(v -> !hasCoordinates
                        || (v.distanceKm() != null && v.distanceKm() <= effectiveRadius))
                .filter(v -> !"available".equalsIgnoreCase(criteria.availability())
                        || v.participantCount() < v.maxParticipants())
                .sorted(discoveryComparator(criteria.sort()))
                .toList();
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
                    s -> s.maxParticipants() - s.participantCount()
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
