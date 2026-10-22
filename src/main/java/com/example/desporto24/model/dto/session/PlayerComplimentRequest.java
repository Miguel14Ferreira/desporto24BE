package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.PlayerComplimentType;
import jakarta.validation.constraints.NotNull;

public record PlayerComplimentRequest(@NotNull PlayerComplimentType type) {}
