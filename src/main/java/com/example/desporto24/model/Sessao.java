package com.example.desporto24.model;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
public class Sessao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String desporto;
    private String username;
    private String jogadores;
    private Date dataDeJogo;
    private String localidade;
    private String morada;
    private String preco;
    private String password;
    private String foto;

    public Sessao(String desporto, String username, String jogadores, Date dataDeJogo, String localidade, String morada, String preco, String password) {
        this.desporto = desporto;
        this.username = username;
        this.jogadores = jogadores;
        this.dataDeJogo = dataDeJogo;
        this.localidade = localidade;
        this.morada = morada;
        this.preco = preco;
        this.password = password;
    }

    public Sessao() {

    }
}
