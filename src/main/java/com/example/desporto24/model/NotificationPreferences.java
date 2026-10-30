package com.example.desporto24.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_preferences", schema = "desporto24")
@Getter
@Setter
public class NotificationPreferences {
    @Id
    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "in_app_enabled", nullable = false)
    private boolean inAppEnabled = true;

    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled = true;

    @Column(name = "disabled_in_app_types", nullable = false, columnDefinition = "text")
    private String disabledInAppTypes = "";

    @Column(name = "disabled_email_types", nullable = false, columnDefinition = "text")
    private String disabledEmailTypes = "";
}
