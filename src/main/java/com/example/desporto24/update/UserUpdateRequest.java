package com.example.desporto24.update;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserUpdateRequest {
    private String username;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String phone;
    private String desportosFavoritos;
    private String gender;
    private String email;
    private String postalCode;
    private byte[] foto;
}
