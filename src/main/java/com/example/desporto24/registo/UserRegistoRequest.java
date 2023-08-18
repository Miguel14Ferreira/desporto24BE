package com.example.desporto24.registo;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistoRequest {
    @NotEmpty(message = "Nome de utilizador não pode estar vazio!")
    private String username;
    @NotEmpty(message = "Password não pode estar vazio!")
    private String password;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String desportosFavoritos;
    private String indicativePhone;
    private String phone;
    @NotEmpty(message = "Género não pode estar vazio")
    private String gender;
    @NotEmpty(message = "Email não pode estar vazio")
    private String email;
    private String postalCode;
}