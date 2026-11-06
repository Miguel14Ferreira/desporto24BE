package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "mobile_push_device", schema = "desporto24",
       uniqueConstraints = @UniqueConstraint(name = "uq_mobile_push_token",
           columnNames = "push_token"))
@Getter @Setter
public class MobilePushDevice {
    @Id
    @Column(name = "device_id", nullable = false, length = 36)
    private String deviceId;

    @Column(name = "username", nullable = false, length = 128)
    private String username;

    // Token is a delivery credential; never serialize entities in API responses.
    @Column(name = "push_token", nullable = false, length = 240)
    private String pushToken;

    @Column(name = "platform", nullable = false, length = 16)
    private String platform;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
