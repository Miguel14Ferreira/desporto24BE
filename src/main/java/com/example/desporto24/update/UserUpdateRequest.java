package com.example.desporto24.update;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserUpdateRequest {
    @NotEmpty(message = "O nome de utilizador não pode estar vazio")
    private String username;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String indicativePhone;
    private String phone;
    private String desportosFavoritos;
    private String gender;
    @NotEmpty(message = "O email não pode estar vazio")
    private String email;
    private String postalCode;
    private Boolean mfa;
}
