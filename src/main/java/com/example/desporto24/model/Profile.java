package com.example.desporto24.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
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
    private String postalCode;
    private String indicativePhone;
    private String phone;
    private String gender;
    private String email;
    private String favoriteSports;

    @Column(length = 280)
    private String bio;

    @Column(name = "skill_level", length = 32)
    private String skillLevel;

    @Column(length = 120)
    private String availability;

    private String profilePicUrl;
    private int loginAttempts;
    private Boolean NotLocked;
    private Boolean enabled;
    private Boolean MFA;
    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "google_subject", unique = true)
    private String googleSubject;

    @Column(name = "community_creation_limit")
    private Integer communityCreationLimit;

    private String role;
    private String[] authorities;
    private String lastLoginDateDisplay;
    private String joinDateDisplay;
    private Date lastLoginDate;
    private Date joinDate;
    private Status statusId;
    private String oldEmail;
    private Boolean isFirstTimeLogin;
    private long tokenVersion;

    @Column(name = "moderation_suspended_at")
    private Instant moderationSuspendedAt;

    @Column(name = "terms_accepted_at")
    private Instant termsAcceptedAt;

    @Column(name = "terms_version", length = 32)
    private String termsVersion;

    @Column(name = "privacy_version", length = 32)
    private String privacyVersion;

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Profile(String username, String newUsername, String password) {
        this.username = username;
        this.newUsername = newUsername;
        this.password = password;
    }

    public Profile(String username, String password, String fullName, String dateOfBirth, String address, String country, String location, String postalCode, String indicativePhone, String phone, String gender, String email, String favoriteSports) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.postalCode = postalCode;
        this.indicativePhone = indicativePhone;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.favoriteSports = favoriteSports;
    }

    public Profile(String fullName, String dateOfBirth, String address, String country, String location, String postalCode, String indicativePhone, String phone, String gender, String email, String favoriteSports, Boolean MFA) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.country = country;
        this.location = location;
        this.postalCode = postalCode;
        this.indicativePhone = indicativePhone;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.favoriteSports = favoriteSports;
        this.MFA = MFA;
    }

    public Profile(String email) {
        this.email = email;
    }
}