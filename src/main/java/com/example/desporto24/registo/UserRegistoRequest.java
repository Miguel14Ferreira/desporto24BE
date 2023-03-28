package com.example.desporto24.registo;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistoRequest {
    private String username;
    private String password;
    private String fullName;
    private Date dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String desportosFavoritos;
    private String phone;
    private String gender;
    private String email;
    private byte[] foto;
}