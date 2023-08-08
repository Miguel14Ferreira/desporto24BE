package com.example.desporto24.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Sessao {

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
    private Date created_at;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "perfil_id")
    private Perfil perfil;

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
}
