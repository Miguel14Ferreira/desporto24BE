package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/** A durable Expo ticket awaiting a provider delivery receipt, without storing its push token. */
@Entity
@Table(name = "mobile_push_receipt", schema = "desporto24")
@Getter @Setter
public class MobilePushReceipt {
    @Id
    @Column(name = "ticket_id", nullable = false, length = 80)
    private String ticketId;

    @Column(name = "device_id", nullable = false, length = 36)
    private String deviceId;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "next_check_at", nullable = false)
    private Instant nextCheckAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;
}
