package com.example.desporto24.repository.moderation;

import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.model.moderation.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    boolean existsByReporterIdAndReportedIdAndStatus(
            Long reporterId,
            Long reportedId,
            UserReportStatus status
    );

    List<UserReport> findAllByStatusOrderByCreatedAtAsc(UserReportStatus status);
}
