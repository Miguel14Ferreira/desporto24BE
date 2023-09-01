package com.example.desporto24.registo;


import lombok.*;

import javax.annotation.Nullable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SessaoRegistoRequest {
    private String desporto;
    private String username;
    private String jogadores;
    private Date datadejogo;
    private String localidade;
    private String morada;
    @Nullable
    private String preco;
    @Nullable
    private String password;
    private Boolean privado;
}
