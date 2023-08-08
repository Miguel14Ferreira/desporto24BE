package com.example.desporto24.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {
    @NotEmpty(message = "Nome de utilizador não pode estar vazio!")
    private String username;
    @NotEmpty(message = "Password não pode estar vazio!")
    private String password;
    private String roleName;
    private String permissions;
}
