package com.example.desporto24.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 86400000; //1 dia millisegundos
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String DESPORTO_24 = "Desporto 24";
    public static final String DESPORTO_24_ADMINISTRATION = "Desporto 24 APP";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to login in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
   // public static final String[] PUBLIC_URLS = { "/api/auth/login" , "/api/auth/login/registerNewUser" ,"/api/auth/confirmRegistrationToken", "/api/auth/confirmEmergencyToken" };
    public static final String[] PUBLIC_URLS = {"**"};
}
