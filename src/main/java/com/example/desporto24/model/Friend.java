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
    @JoinColumn(nullable = false,name = "profile1")
    private Profile profile1;
    @ManyToOne
    @JoinColumn(nullable = false,name = "profile2")
    private Profile profile2;

    private boolean chatBlocked;

    private String whoBlocked;
}
