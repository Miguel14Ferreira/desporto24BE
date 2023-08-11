package com.example.desporto24.changePassword;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserChangePasswordRequest {
    private String username;
    private String newUsername;
    private String email;
    private String password;
    }
