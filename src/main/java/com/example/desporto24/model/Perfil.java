package com.example.desporto24.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Perfil implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    @NotEmpty(message = "Nome de utilizador não pode estar vazio!")
    private String username;
    private String newUsername;
    @NotEmpty(message = "Password não pode estar vazio!")
    private String password;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String postalCode;
    private String phone;
    private String gender;
    @Unique
    @Email(message = "Email inválido, por favor coloca um email válido!")
    @NotEmpty(message = "Email não pode estar vazio!")
    private String email;
    private String desportosFavoritos;
    private String foto;
    private int logginAttempts;
    private Boolean NotLocked;
    private Boolean enabled;
    private Boolean MFA;
    private String role;
    private String[] authorities;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;

    public Perfil(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Perfil(String username, String newUsername, String password) {
        this.username = username;
        this.newUsername = newUsername;
        this.password = password;
    }

    public Perfil(String username, String password, String fullName, String dateOfBirth, String address, String country, String location, String postalCode, String phone, String gender, String email, String desportosFavoritos, String foto) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.postalCode = postalCode;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.desportosFavoritos = desportosFavoritos;
        this.foto = foto;
    }

    public Perfil(String username, String fullName, String dateOfBirth, String address, String country, String location, String postalCode, String phone, String gender, String email, String desportosFavoritos, String foto) {
        this.username = username;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.postalCode = postalCode;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.desportosFavoritos = desportosFavoritos;
        this.foto = foto;
    }

    public boolean isNotLocked() {
        return NotLocked;
    }
}