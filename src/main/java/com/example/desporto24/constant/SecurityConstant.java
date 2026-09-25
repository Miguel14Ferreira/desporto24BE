package com.example.desporto24.constant;

public final class SecurityConstant {

    private SecurityConstant() {
    }

    public static final long EXPIRATION_TIME = 3600000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Não é possível verificar o token";
    public static final String DESPORTO_24 = "Desporto 24";
    public static final String DESPORTO_24_ADMINISTRATION = "Desporto 24 APP";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "Tu percisas de efetuar o login para aceder a esta página";
    public static final String ACCESS_DENIED_MESSAGE = "Tu não tens permissão para aceder a esta página";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    public static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/register/confirm",
            "/api/auth/reset-password/**",
            "/api/auth/mfa/confirm",
            "/api/auth/mfa/resend",
            "/image/**",
            "/error"
    };
}
