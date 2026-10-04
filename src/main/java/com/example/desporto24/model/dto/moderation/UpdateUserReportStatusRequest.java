package com.example.desporto24.model.dto.moderation;

import com.example.desporto24.enumeration.UserReportStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserReportStatusRequest(@NotNull UserReportStatus status) {
}
