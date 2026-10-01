package com.example.desporto24.service;

import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.moderation.AdminUserReportResponse;
import com.example.desporto24.model.moderation.UserReport;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<AdminUserReportResponse> openReports() {
        return userReportRepository.findAllByStatusOrderByCreatedAtAsc(UserReportStatus.OPEN)
                .stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Transactional
    public Optional<AdminUserReportResponse> updateStatus(Long reportId, UserReportStatus status) {
        if (status == UserReportStatus.OPEN) {
            throw new IllegalArgumentException("Admin status must close the report");
        }

        return userReportRepository.findById(reportId)
                .map(report -> {
                    report.setStatus(status);
                    return toAdminResponse(userReportRepository.save(report));
                });
    }

    @Transactional
    public void reportUser(String reporterUsername, String reportedUsername, String reason) {
        Profile reporter = profileRepository.findUserByUsername(reporterUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        Profile reported = profileRepository.findUserByUsernameIgnoreCase(reportedUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (reporter.getId().equals(reported.getId())) {
            throw new IllegalArgumentException("Users cannot report themselves");
        }

        if (userReportRepository.existsByReporterIdAndReportedIdAndStatus(
                reporter.getId(),
                reported.getId(),
                UserReportStatus.OPEN
        )) {
            throw new IllegalStateException("An open report already exists for this user");
        }

        userReportRepository.save(UserReport.builder()
                .reporter(reporter)
                .reported(reported)
                .reason(reason.trim())
                .status(UserReportStatus.OPEN)
                .createdAt(Instant.now())
                .build());
    }

    private AdminUserReportResponse toAdminResponse(UserReport report) {
        return new AdminUserReportResponse(
                report.getId(),
                report.getReporter().getUsername(),
                report.getReported().getUsername(),
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}
