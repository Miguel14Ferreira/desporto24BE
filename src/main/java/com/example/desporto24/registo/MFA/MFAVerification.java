package com.example.desporto24.registo.MFA;

import com.example.desporto24.model.Perfil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class MFAVerification {
    @SequenceGenerator( name = "mfaverification_token_sequence",
            sequenceName = "mfaverification_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "mfaverification_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;
    @JoinColumn(nullable = false,
            name = "perfil_id")
    private String perfil;

    public MFAVerification(String code, LocalDateTime createdAt, LocalDateTime expiredAt, String perfil) {
        this.code = code;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.perfil = perfil;
    }
}
