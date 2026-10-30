package com.example.desporto24.filter;
import com.example.desporto24.utility.JWTokenProvider;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import static com.example.desporto24.constant.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // Criação de novos tokens para os utilizadores autenticados

    private final JWTokenProvider jwTokenProvider;
    private final ProfileRepository profileRepository;

    @Autowired
    private RefreshTokenSessionRepository refreshTokenSessions;

    public JwtAuthorizationFilter(
            JWTokenProvider jwTokenProvider,
            ProfileRepository profileRepository) {
        this.jwTokenProvider = jwTokenProvider;
        this.profileRepository = profileRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());

            try {
                String username = jwTokenProvider.getSubject(token);
                Profile profile = profileRepository.findUserByUsername(username)
                        .orElse(null);

                boolean profileActive = profile != null
                        && profile.getModerationSuspendedAt() == null
                        && Boolean.TRUE.equals(profile.getNotLocked())
                        && Boolean.TRUE.equals(profile.getEnabled());

                boolean currentTokenVersion = profile != null
                        && profile.getTokenVersion()
                        == jwTokenProvider.getTokenVersion(token);

                String familyId = jwTokenProvider.getAccessTokenFamilyId(token);
                // A revoked device must lose bearer access immediately. Old
                // access JWTs (issued before family binding) expire naturally.
                boolean sessionActive = familyId == null
                        || (refreshTokenSessions != null && profile != null
                        && profile.getId() != null
                        && refreshTokenSessions
                            .existsByProfileIdAndFamilyIdAndConsumedAtIsNullAndRevokedAtIsNullAndExpiresAtAfter(
                                    profile.getId(), familyId, Instant.now()));

                if (profileActive
                        && currentTokenVersion
                        && sessionActive
                        && jwTokenProvider.isTokenValid(username, token)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<GrantedAuthority> authorities =
                            jwTokenProvider.getAuthorities(token);
                    Authentication authentication =
                            jwTokenProvider.getAuthentication(
                                    username, authorities, request);
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (RuntimeException exception) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);
    }
}
