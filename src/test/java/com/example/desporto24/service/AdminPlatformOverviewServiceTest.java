package com.example.desporto24.service;

import com.example.desporto24.controllers.AdminPlatformOverviewController;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminPlatformOverviewServiceTest {
    @Mock ProfileRepository profiles;
    @Mock SessionRepository sessions;
    @Mock CommunityRepository communities;
    @Mock CommunityTournamentRepository tournaments;
    @Mock VenueRepository venues;
    @Mock UserReportRepository reports;
    @Mock OnlinePresenceService onlinePresence;
    @InjectMocks AdminPlatformOverviewService service;

    @Test
    void readsIndependentLiveCountsAndDoesNotFabricateOnlinePresence() {
        when(profiles.countRegisteredProfiles()).thenReturn(110L);
        when(profiles.countEnabledRegisteredProfiles()).thenReturn(107L);
        when(profiles.countRegistrationsSince(any(Date.class))).thenReturn(9L);
        when(profiles.countRecentLoginsSince(any(Date.class))).thenReturn(14L);
        when(sessions.count()).thenReturn(120L);
        when(sessions.countUpcomingSessions(any(Date.class))).thenReturn(24L);
        when(sessions.countUpcomingSessionsBetween(any(Date.class), any(Date.class)))
                .thenReturn(10L);
        when(sessions.countByStatus(SessionStatus.COMPLETED)).thenReturn(70L);
        when(sessions.countByStatus(SessionStatus.CANCELLED)).thenReturn(8L);
        when(communities.count()).thenReturn(15L);
        when(tournaments.count()).thenReturn(18L);
        when(tournaments.countByStatus(TournamentStatus.IN_PROGRESS)).thenReturn(3L);
        when(venues.count()).thenReturn(900L);
        when(reports.countByStatus(UserReportStatus.OPEN)).thenReturn(4L);
        when(onlinePresence.countOnlineAt(any())).thenReturn(6L);

        var snapshot = service.snapshot();

        assertEquals(110L, snapshot.registeredUsers());
        assertEquals(107L, snapshot.enabledUsers());
        assertEquals(9L, snapshot.registrationsLast7Days());
        assertEquals(14L, snapshot.loginsLast15Minutes());
        assertEquals(6L, snapshot.onlineUsers(), "Presence comes from a separate heartbeat");
        assertEquals(120L, snapshot.totalSessions());
        assertEquals(24L, snapshot.upcomingSessions());
        assertEquals(10L, snapshot.sessionsNext7Days());
        assertEquals(70L, snapshot.completedSessions());
        assertEquals(8L, snapshot.cancelledSessions());
        assertEquals(15L, snapshot.communities());
        assertEquals(18L, snapshot.tournaments());
        assertEquals(3L, snapshot.activeTournaments());
        assertEquals(900L, snapshot.venues());
        assertEquals(4L, snapshot.openUserReports());
        assertNotNull(snapshot.generatedAt());
        verify(profiles, never()).findAll();
        verify(sessions, never()).findAll();
        verify(onlinePresence).countOnlineAt(any());
    }


    @Test
    void venueQualityCountsRealMissingFieldsWithoutLoadingEntities() {
        when(venues.qualityByDistrict()).thenReturn(List.of(
                new Object[]{"Lisboa", 100L, 12L, 25L, 38L, 4L},
                new Object[]{null, 8L, 7L, 8L, 8L, 8L},
                new Object[]{"Porto", 50L, 3L, 4L, 10L, 0L}
        ));

        var quality = service.venueQuality();

        assertEquals(158L, quality.venues());
        assertEquals(22L, quality.withoutSport());
        assertEquals(37L, quality.withoutCoordinates());
        assertEquals(56L, quality.withoutAddress());
        assertEquals(12L, quality.withoutAddressAndCoordinates());
        assertEquals(List.of("Lisboa", "Sem distrito", "Porto"),
                quality.districts().stream()
                        .map(AdminPlatformOverviewService.VenueDistrictCoverage::district).toList());
        assertNotNull(quality.generatedAt());
        verify(venues).qualityByDistrict();
        verify(venues, never()).findAll();
        verify(venues, never()).findAllByOrderByNameAsc();
    }

    @Test
    void qualityEndpointInheritsAdminOnlyPolicyAndHasNoVenueIdentifiers() throws Exception {
        var mapping = AdminPlatformOverviewController.class
                .getMethod("venueQuality")
                .getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/venue-quality"}, mapping.value());
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')",
                AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class).value());
        var fields = java.util.Arrays.stream(
                AdminPlatformOverviewService.VenueDistrictCoverage.class.getRecordComponents())
                .map(java.lang.reflect.RecordComponent::getName).toList();
        assertFalse(fields.contains("id"));
        assertFalse(fields.contains("address"));
        assertFalse(fields.contains("latitude"));
        assertFalse(fields.contains("longitude"));
    }

    @Test
    void overviewIsAdminOnlyAndReturnsNoPersonalFields() {
        var policy = AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class);
        assertNotNull(policy);
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')", policy.value());
        var names = java.util.Arrays.stream(
                AdminPlatformOverviewService.PlatformOverview.class.getRecordComponents())
                .map(java.lang.reflect.RecordComponent::getName).toList();
        assertFalse(names.contains("username"));
        assertFalse(names.contains("email"));
        assertFalse(names.contains("profileId"));
    }
}
