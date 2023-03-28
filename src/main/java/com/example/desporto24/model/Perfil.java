package com.example.desporto24.model;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
public class Perfil implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String username;
    private String newUsername;
    private String password;
    private String fullName;
    private Date dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String phone;
    private String gender;
    private String email;
    private String desportosFavoritos;
    private byte[] foto;
    private Boolean NotLocked;
    private Boolean enabled;
    private String role;
    private String[] authorities;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;

    public Perfil() {
    }

    public Perfil(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Perfil(String username, String newUsername, String password) {
        this.username = username;
        this.newUsername = newUsername;
        this.password = password;
    }

    public Perfil(String username, String password, String fullName, Date dateOfBirth, String address, String country, String location, String phone, String desportosFavoritos, String gender, String email,byte[] foto) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.phone = phone;
        this.desportosFavoritos = desportosFavoritos;
        this.gender = gender;
        this.email = email;
        this.foto = foto;
    }

    public Perfil(String username, String fullName, Date dateOfBirth, String address, String country, String location, String phone, String desportosFavoritos, String gender, String email, byte[] foto) {
        this.username = username;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.phone = phone;
        this.desportosFavoritos = desportosFavoritos;
        this.gender = gender;
        this.email = email;
        this.foto = foto;
    }

    public boolean isNotLocked() {
        return NotLocked;
    }
    public boolean isActive() {
        return enabled;
    }
}