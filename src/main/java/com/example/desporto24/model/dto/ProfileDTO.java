package com.example.desporto24.model.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProfileDTO {
    private String username;
    private String newUsername;
    private String password;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String favoriteSports;
    private String indicativePhone;
    private String phone;
    private String gender;
    private String email;
    private String postalCode;
    private Boolean MFA;
}
