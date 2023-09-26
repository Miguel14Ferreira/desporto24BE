package com.example.desporto24.registo.token;
import com.example.desporto24.model.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {
    @SequenceGenerator( name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "confirmation_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false,
               name = "perfil_id")
    private Perfil perfil;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, Perfil perfil) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.perfil = perfil;
    }

    public ConfirmationToken(String token) {
        this.token = token;
    }
}
