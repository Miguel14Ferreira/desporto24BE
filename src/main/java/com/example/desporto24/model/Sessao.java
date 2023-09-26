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
    private Boolean privado;
    private String foto;
    private Date created_at;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "perfil_id")
    private Perfil perfil;

    public Sessao(String username, String desporto, String jogadores, Date datadejogo, String localidade, Boolean privado, String morada, String preco, String password) {
        this.username = username;
        this.desporto = desporto;
        this.jogadores = jogadores;
        this.dataDeJogo = datadejogo;
        this.localidade = localidade;
        this.privado = privado;
        this.morada = morada;
        this.preco = preco;
        this.password = password;
    }
}
