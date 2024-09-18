package com.example.desporto24.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SessionDTO {

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
}
