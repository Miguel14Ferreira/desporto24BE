package com.example.desporto24.enumeration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.example.desporto24.constant.Authority.*;

public enum Role {
    // Diferentes tipos de atributos dos utilizadores
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    private String [] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String [] getAuthorities(){
        return authorities;
    }
}
