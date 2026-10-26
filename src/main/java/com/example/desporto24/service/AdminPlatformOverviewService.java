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
import java.util.List;
import java.util.Comparator;
import org.springframework.data.domain.PageRequest;

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

    /** Missing postal address is not the same as missing usable location (GPS may exist). */
    @Transactional(readOnly = true)
    public VenueQualityCoverage venueQuality() {
        List<VenueDistrictCoverage> districts = venues.qualityByDistrict().stream()
                .map(row -> new VenueDistrictCoverage(
                        row[0] == null || row[0].toString().isBlank()
                                ? "Sem distrito" : row[0].toString(),
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).longValue(),
                        ((Number) row[3]).longValue(),
                        ((Number) row[4]).longValue(),
                        ((Number) row[5]).longValue()))
                .sorted(Comparator.comparingLong(VenueDistrictCoverage::withoutSport).reversed()
                        .thenComparing(VenueDistrictCoverage::district))
                .toList();
        return new VenueQualityCoverage(
                districts.stream().mapToLong(VenueDistrictCoverage::venues).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutSport).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutCoordinates).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutAddress).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutAddressAndCoordinates).sum(),
                districts, Instant.now());
    }

    /** Read-only admin queue of existing venues without a complete GPS pair. */
    @Transactional(readOnly = true)
    public VenueGpsReviewPage missingGpsReview(String district, int page) {
        if (page < 0 || page > 1000) throw new IllegalArgumentException("Página de revisão GPS inválida");
        String filter = district == null ? "" : district.trim();
        if (!filter.isEmpty() && (filter.length() > 120 || filter.equals("Sem distrito"))) {
            throw new IllegalArgumentException("Filtro de distrito inválido");
        }
        var slice = venues.missingGpsReview(filter, PageRequest.of(page, 20));
        return new VenueGpsReviewPage(slice.getContent().stream()
                .map(v -> new VenueGpsReviewItem(
                        v.getId(), v.getName(), v.getCity(), v.getDistrict(),
                        v.getAddress(), v.getSourceReference(),
                        v.getInformationSource() == null ? null : v.getInformationSource().name()))
                .toList(), page, slice.hasNext());
    }

    public record VenueGpsReviewItem(long id, String name, String city, String district,
                                     String address, String sourceReference, String informationSource) {}
    public record VenueGpsReviewPage(List<VenueGpsReviewItem> venues, int page, boolean hasMore) {}

    public record VenueDistrictCoverage(
            String district, long venues, long withoutSport, long withoutCoordinates,
            long withoutAddress, long withoutAddressAndCoordinates) {}

    public record VenueQualityCoverage(
            long venues, long withoutSport, long withoutCoordinates,
            long withoutAddress, long withoutAddressAndCoordinates,
            List<VenueDistrictCoverage> districts, Instant generatedAt) {}

    public record PlatformOverview(
            long registeredUsers, long enabledUsers, long registrationsLast7Days,
            long loginsLast15Minutes, Long onlineUsers,
            long totalSessions, long upcomingSessions, long sessionsNext7Days,
            long completedSessions, long cancelledSessions,
            long communities, long tournaments, long activeTournaments,
            long venues, long openUserReports, Instant generatedAt) {}
}
