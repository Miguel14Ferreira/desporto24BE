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
    @Column(nullable = false)
    private String assunto;
    @Column(nullable = false,columnDefinition = "longtext")
    private String mensagem;
    @Column(nullable = false)
    private String cumprimentos;
    @Column(nullable = false)
    private String assinatura;
    @Column(nullable = false)
    private String createdAt;
    @Column(nullable = false)
    private boolean isFriendRequest;
    private String token;
    @JoinColumn(nullable = false)
    private String perfil;

    public Notifications(String assunto, String mensagem, String cumprimentos, String assinatura, String createdAt, boolean isFriendRequest,String token, String perfil) {
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.cumprimentos = cumprimentos;
        this.assinatura = assinatura;
        this.createdAt = createdAt;
        this.isFriendRequest = isFriendRequest;
        this.token = token;
        this.perfil = perfil;
    }

    public Notifications(String token) {
        this.mensagem = mensagem;
    }
}
