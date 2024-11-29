package com.example.desporto24.model.dto;

import com.example.desporto24.model.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
public class ProfileDTO {

    @Id
    private String userId;
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
    private Boolean notLocked;
    private String oldEmail;
    private String profilePic;
    private Status statusId;
}
