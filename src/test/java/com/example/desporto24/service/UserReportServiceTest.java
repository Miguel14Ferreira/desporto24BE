package com.example.desporto24.service;

import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:user-report;MODE=LEGACY;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
@Import(UserReportService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserReportServiceTest {

    @Autowired private UserReportService service;
    @Autowired private UserReportRepository reportRepository;
    @Autowired private ProfileRepository profileRepository;

    private Profile reporter;
    private Profile reported;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        profileRepository.deleteAll();

        reporter = profile("reporter");
        reported = profile("reported");
    }

    @Test
    void createsOpenReportWithTrimmedReason() {
        service.reportUser(reporter.getUsername(), reported.getUsername(), "  comportamento abusivo  ");

        var reports = reportRepository.findAll();
        assertEquals(1, reports.size());
        assertEquals(reporter.getId(), reports.getFirst().getReporter().getId());
        assertEquals(reported.getId(), reports.getFirst().getReported().getId());
        assertEquals("comportamento abusivo", reports.getFirst().getReason());
        assertEquals(UserReportStatus.OPEN, reports.getFirst().getStatus());
        assertNotNull(reports.getFirst().getCreatedAt());
    }

    @Test
    void cannotReportSelf() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.reportUser(reporter.getUsername(), reporter.getUsername(), "motivo")
        );

        assertEquals(0, reportRepository.count());
    }

    @Test
    void cannotCreateDuplicateOpenReportForSameUser() {
        service.reportUser(reporter.getUsername(), reported.getUsername(), "primeiro motivo");

        assertThrows(
                IllegalStateException.class,
                () -> service.reportUser(reporter.getUsername(), reported.getUsername(), "outro motivo")
        );

        assertEquals(1, reportRepository.count());
    }

    private Profile profile(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(username + "-id");
        return profileRepository.saveAndFlush(profile);
    }
}
