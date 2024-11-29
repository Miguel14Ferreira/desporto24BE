package com.example.desporto24.model;


import com.example.desporto24.model.dto.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private Date createdAt;
    @ManyToOne
    @JoinColumn(nullable = false,name = "perfil1")
    private ProfileDTO profile1;
    @ManyToOne
    @JoinColumn(nullable = false,name = "perfil2")
    private ProfileDTO profile2;

    private boolean chatBlocked;

    private String whoBlocked;
}
