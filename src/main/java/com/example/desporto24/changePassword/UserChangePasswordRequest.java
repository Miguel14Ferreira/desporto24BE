package com.example.desporto24.changePassword;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserChangePasswordRequest {
    private String username;
    private String newUsername;
    private String email;
    private String password;

    public UserChangePasswordRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}


