package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/** Aggregated, database-backed operational counters. No personal data or full-table entity reads. */
@Service
@RequiredArgsConstructor
public class AdminPlatformOverviewService {
    private final ProfileRepository profiles;
    private final SessionRepository sessions;
    private final CommunityRepository communities;
    private final CommunityTournamentRepository tournaments;
    private final VenueRepository venues;
    private final UserReportRepository reports;
    private final OnlinePresenceService onlinePresence;

    @Transactional(readOnly = true)
    public PlatformOverview snapshot() {
        Instant now = Instant.now();
        Date current = Date.from(now);
        Date inSevenDays = Date.from(now.plus(7, ChronoUnit.DAYS));
        return new PlatformOverview(
                profiles.countRegisteredProfiles(),
                profiles.countEnabledRegisteredProfiles(),
                profiles.countRegistrationsSince(Date.from(now.minus(7, ChronoUnit.DAYS))),
                profiles.countRecentLoginsSince(Date.from(now.minus(15, ChronoUnit.MINUTES))),
                onlinePresence.countOnlineAt(now),
                sessions.count(),
                sessions.countUpcomingSessions(current),
                sessions.countUpcomingSessionsBetween(current, inSevenDays),
                sessions.countByStatus(SessionStatus.COMPLETED),
                sessions.countByStatus(SessionStatus.CANCELLED),
                communities.count(),
                tournaments.count(),
                tournaments.countByStatus(TournamentStatus.IN_PROGRESS),
                venues.count(),
                reports.countByStatus(UserReportStatus.OPEN),
                now
        );
    }

    public record PlatformOverview(
            long registeredUsers, long enabledUsers, long registrationsLast7Days,
            long loginsLast15Minutes, Long onlineUsers,
            long totalSessions, long upcomingSessions, long sessionsNext7Days,
            long completedSessions, long cancelledSessions,
            long communities, long tournaments, long activeTournaments,
            long venues, long openUserReports, Instant generatedAt) {}
}
