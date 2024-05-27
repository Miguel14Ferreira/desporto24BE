package com.example.desporto24.register;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String desportosFavoritos;
    private String indicativePhone;
    private String phone;
    private String gender;
    private String email;
    private String postalCode;
}