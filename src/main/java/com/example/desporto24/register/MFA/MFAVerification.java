package com.example.desporto24.register.MFA;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MFAVerification {

    @SequenceGenerator(
            name = "mfaverification_token_sequence",
            sequenceName = "mfaverification_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mfaverification_token_sequence")
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(unique = true)
    private String challengeId;

    private String codeHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @JoinColumn(nullable = false, name = "perfil_id")
    private String perfil;

    public MFAVerification(String code, LocalDateTime createdAt, LocalDateTime expiredAt, String perfil) {
        this.code = code;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.perfil = perfil;
    }

    public MFAVerification(String challengeId, String codeHash, LocalDateTime createdAt,
                           LocalDateTime expiredAt, String perfil) {
        this.code = challengeId;
        this.challengeId = challengeId;
        this.codeHash = codeHash;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.perfil = perfil;
    }
}
