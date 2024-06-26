package com.example.desporto24.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String sport;
    private String username;
    private String players;
    private Date gameDate;
    private String city;
    private String address;
    private String price;
    private String password;
    private Boolean privateGame;
    private String gamePhoto;
    private Boolean enabled;
    private Date created_at;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "perfil_id")
    private Profile profile;

    public Session(String username, String desporto, String jogadores, Date datadejogo, String localidade, Boolean privado, String morada, String preco, String password) {
        this.username = username;
        this.sport = desporto;
        this.players = jogadores;
        this.gameDate = datadejogo;
        this.city = localidade;
        this.privateGame = privado;
        this.address = morada;
        this.price = preco;
        this.password = password;
    }
}
