package com.example.desporto24.utility;

import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.PerfilPrincipal;
import org.springframework.security.core.Authentication;

public class PerfilUtils {
    public static Perfil getAuthenticatedUser(Authentication authentication) {
        return ((Perfil) authentication.getPrincipal());
    }
    public static Perfil getLoggedInUser(Authentication authentication) {
        return ((PerfilPrincipal) authentication.getPrincipal()).getPerfil();
    }
}
