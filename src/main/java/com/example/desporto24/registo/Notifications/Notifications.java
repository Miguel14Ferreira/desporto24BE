package com.example.desporto24.registo.Notifications;
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
public class Notifications {
    @SequenceGenerator( name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "notification_sequence")
    private Long id;
    @Column(nullable = false,columnDefinition = "longtext")
    private String mensagem;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private boolean isFriendRequest;
    @JoinColumn(nullable = false)
    private String perfil;

    public Notifications(String mensagem, LocalDateTime createdAt,Boolean isFriendRequest, String perfil) {
        this.mensagem = mensagem;
        this.createdAt = createdAt;
        this.isFriendRequest = isFriendRequest;
        this.perfil = perfil;
    }

    public Notifications(String token) {
        this.mensagem = mensagem;
    }
}
