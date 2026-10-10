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
    public static final String TOKEN_VERSION = "Token-Version";
    public static final String TOKEN_TYPE = "Token-Type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
    public static final String REFRESH_TOKEN_COOKIE = "desporto24_refresh";
    public static final String FORBIDDEN_MESSAGE = "Tu percisas de efetuar o login para aceder a esta página";
    public static final String ACCESS_DENIED_MESSAGE = "Tu não tens permissão para aceder a esta página";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    public static final String[] PUBLIC_GET_URLS = {
            "/api/sessions",
            "/api/venues",
            "/api/venues/*"
    };

    public static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/google/**",
            "/api/auth/refresh",
            "/api/auth/register",
            "/api/auth/register/confirm",
            "/api/auth/register/resend-confirmation",
            "/api/auth/reset-password/**",
            "/api/auth/mfa/confirm",
            "/api/auth/mfa/resend",
            "/api/ideas/submit",
            "/api/pilot/events/access",
            "/actuator/health",
            "/actuator/health/**",
            "/media/**",
            "/error"
    };
}
