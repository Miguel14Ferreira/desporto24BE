package com.example.desporto24.model.dto;
import com.example.desporto24.model.Status;
import jakarta.persistence.Id;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
public class ProfileDTO implements Serializable {

    @Id
    private String userId;
    private String username;
    private String newUsername;
    private String password;
    private String fullName;
    private String nickname;
    private String dateOfBirth;
    private String address;
    private String country;
    private String location;
    private String favoriteSports;
    private String bio;
    private String skillLevel;
    private String availability;
    private String indicativePhone;
    private String phone;
    private String gender;
    private String email;
    private String postalCode;
    @JsonProperty("mfa")
    private Boolean MFA;
    private Boolean notLocked;
    private Boolean enabled;
    private String role;
    private String oldEmail;
    private Status statusId;
    private int loginAttempts;
    private String[] authorities;
    private String profilePicUrl;
    private String lastLoginDateDisplay;
    private String joinDateDisplay;
    private Boolean isFirstTimeLogin;
}
