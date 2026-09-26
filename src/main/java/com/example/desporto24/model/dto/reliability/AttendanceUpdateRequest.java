package com.example.desporto24.model.dto.reliability;

import com.example.desporto24.enumeration.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceUpdateRequest(@NotNull AttendanceStatus status) {
}
