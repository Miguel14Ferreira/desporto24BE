package com.example.desporto24.register.Notifications;
import com.example.desporto24.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@RequiredArgsConstructor
public class Notifications {
    @SequenceGenerator( name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "notification_sequence")
    private Long id;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false,columnDefinition = "text")
    private String message;
    @Column(nullable = false)
    private String greeting;
    @Column(nullable = false)
    private String signature;
    @Column(nullable = false)
    private String createdAt;
    @Column(nullable = false)
    private boolean isFriendRequest;
    private boolean isUpdateProfile;
    private boolean isSessionInvite;
    private boolean isNormalMessage;
    private String token;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "related_username")
    private String relatedUsername;

    @Column(name = "read", nullable = false)
    private boolean read;

    @JoinColumn(nullable = false)
    private String username;

    public Notifications(String subject, String message, String greeting, String signature, String createdAt, boolean isFriendRequest, boolean isUpdateProfile, boolean isSessionInvite, boolean isNormalMessage, String token, String username) {
        this.subject = subject;
        this.message = message;
        this.greeting = greeting;
        this.signature = signature;
        this.createdAt = createdAt;
        this.isFriendRequest = isFriendRequest;
        this.isUpdateProfile = isUpdateProfile;
        this.isSessionInvite = isSessionInvite;
        this.isNormalMessage = isNormalMessage;
        this.token = token;
        this.username = username;
    }
}
