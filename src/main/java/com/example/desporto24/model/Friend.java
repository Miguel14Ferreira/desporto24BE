package com.example.desporto24.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private Date createdAt;
    @ManyToOne
    @JoinColumn(nullable = false,name = "perfil1")
    private Perfil perfil1;
    @ManyToOne
    @JoinColumn(nullable = false,name = "perfil2")
    private Perfil perfil2;
}
