package com.example.desporto24.utility;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import org.springframework.security.core.Authentication;

public class ProfileUtils {
    public static Profile getAuthenticatedUser(Authentication authentication) {
        return ((Profile) authentication.getPrincipal());
    }
    public static Profile getLoggedInUser(Authentication authentication) {
        return ((ProfilePrincipal) authentication.getPrincipal()).getPerfil();
    }
}
