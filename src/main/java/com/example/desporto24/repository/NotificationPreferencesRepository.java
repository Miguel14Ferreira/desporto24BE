package com.example.desporto24.repository;

import com.example.desporto24.model.NotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, String> {
}
