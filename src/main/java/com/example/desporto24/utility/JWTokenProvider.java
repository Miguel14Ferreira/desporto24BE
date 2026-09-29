package com.example.desporto24.utility;
import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.*;
import static com.example.desporto24.constant.SecurityConstant.*;
import static java.util.Arrays.stream;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.desporto24.model.ProfilePrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTokenProvider {

    @Value("${desporto.app.jwtSecret}")
    private String secret;

    @Value("${desporto.app.jwtExpirationMs:1800000}")
    private long expirationTimeMs;

    @Value("${desporto.app.refreshExpirationMs:604800000}")
    private long refreshExpirationTimeMs;

    public String generateJwtToken(ProfilePrincipal profilePrincipal){
        String [] claims = getClaimsFromPerfil(profilePrincipal);
        return JWT.create().withIssuer(DESPORTO_24).withAudience(DESPORTO_24_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(profilePrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withClaim(TOKEN_VERSION, profilePrincipal.getProfile().getTokenVersion())
                .withClaim(TOKEN_TYPE, ACCESS_TOKEN_TYPE)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMs))
                .sign(HMAC512(secret.getBytes()));
    }

    public String generateRefreshToken(ProfilePrincipal profilePrincipal) {
        return JWT.create().withIssuer(DESPORTO_24).withAudience(DESPORTO_24_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(profilePrincipal.getUsername())
                .withClaim(TOKEN_VERSION, profilePrincipal.getProfile().getTokenVersion())
                .withClaim(TOKEN_TYPE, REFRESH_TOKEN_TYPE)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationTimeMs))
                .sign(HMAC512(secret.getBytes()));
    }

    public boolean isRefreshTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        String type = verifier.verify(token).getClaim(TOKEN_TYPE).asString();
        return REFRESH_TOKEN_TYPE.equals(type)
                && StringUtils.isNotEmpty(username)
                && !isTokenExpired(verifier, token);
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,authorities,authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String username, String token){
        JWTVerifier verifier = getJWTVerifier();
        String type = verifier.verify(token).getClaim(TOKEN_TYPE).asString();
        return (type == null || ACCESS_TOKEN_TYPE.equals(type))
                && StringUtils.isNotEmpty(username)
                && !isTokenExpired(verifier, token);
    }

    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    public long getTokenVersion(String token) {
        JWTVerifier verifier = getJWTVerifier();
        Long version = verifier.verify(token)
                .getClaim(TOKEN_VERSION)
                .asLong();
        return version == null ? 0L : version;
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try{
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(DESPORTO_24).build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    private String[] getClaimsFromPerfil(ProfilePrincipal perfil) {
        List<String> authorithies = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : perfil.getAuthorities()){
            authorithies.add(grantedAuthority.getAuthority());
        }
        return authorithies.toArray(new String[0]);
    }
}
