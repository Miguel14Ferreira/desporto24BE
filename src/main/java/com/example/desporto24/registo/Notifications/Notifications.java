package com.example.desporto24.registo.Notifications;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private boolean isUpdatePerfil;
    private boolean isSessionInvite;
    private boolean isNormalMessage;
    private String token;
    @JoinColumn(nullable = false)
    private String perfil;


    public Notifications(String assuntoNotificaçãoBoasVindas, String notificacaoBoasVindas, String cumprimentoNotificacaoBoasVindas, String assinatura, String data3, boolean b, boolean b1, boolean b2, boolean b3, String token, String username) {
        this.assunto = assuntoNotificaçãoBoasVindas;
        this.mensagem = notificacaoBoasVindas;
        this.cumprimentos = cumprimentoNotificacaoBoasVindas;
        this.assinatura = assinatura;
        this.createdAt = data3;
        this.isFriendRequest = b;
        this.isUpdatePerfil = b1;
        this.isSessionInvite = b2;
        this.isNormalMessage = b3;
        this.token = token;
        this.perfil = username;
    }
}
