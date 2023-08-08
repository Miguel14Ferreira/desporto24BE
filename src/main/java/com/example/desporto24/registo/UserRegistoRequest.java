package com.example.desporto24.registo;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
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
    private String phone;
    private String gender;
    private String email;
    private String postalCode;
    private String foto;
}