package com.example.desporto24.service;

import com.example.desporto24.exception.EmailNotVerifiedException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.desporto24.constant.UserImplConstant.*;

/**
 * Temporary per-username failed-login throttle. Distinct from administrative
 * account suspension: a guessed password must NEVER flip Profile.notLocked.
 *
 * This bounded, in-process cache is an MVP control, NOT distributed IP throttling.
 */
@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int LOCK_WINDOW_MINUTES = 15;

    private final ProfileRepository profileRepository;
    private final PasswordHashService passwordHashService;
    private final Cache<String, Integer> failures;

    @Autowired
    public LoginAttemptService(ProfileRepository profileRepository, PasswordHashService passwordHashService) {
        this(profileRepository, passwordHashService, Ticker.systemTicker());
    }

    // Test clock lets us verify that a blocked user can retry without waiting 15 minutes.
    LoginAttemptService(ProfileRepository profileRepository, PasswordHashService passwordHashService,
                        Ticker ticker) {
        this.profileRepository = profileRepository;
        this.passwordHashService = passwordHashService;
        this.failures = CacheBuilder.newBuilder()
                .expireAfterWrite(LOCK_WINDOW_MINUTES, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .ticker(ticker)
                .build();
    }

    public void requireAttemptAllowed(String username) {
        Integer count = failures.getIfPresent(normalize(username));
        if (count != null && count >= MAXIMUM_NUMBER_OF_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiadas tentativas de login. Tenta novamente dentro de 15 minutos.");
        }
    }

    public void recordMissingAccountAttempt(String username) {
        recordFailure(username);
    }

    public void handleLoginAttempt(String username, String password, Profile profile) {
        requireAttemptAllowed(username);
        if (!Boolean.TRUE.equals(profile.getEnabled())) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
        if (!profile.getUsername().equals(username) || !passwordHashService.matchesAndUpgrade(profile, password)) {
            // Save only a diagnostic counter, never the administrative lock flag.
            profile.setLoginAttempts(recordFailure(username));
            profileRepository.save(profile);
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }
        failures.invalidate(normalize(username));
    }

    private int recordFailure(String username) {
        final int[] count = new int[1];
        failures.asMap().compute(normalize(username), (key, previous) -> {
            count[0] = previous == null
                    ? 1 : Math.min(MAXIMUM_NUMBER_OF_ATTEMPTS, previous + 1);
            return count[0];
        });
        return count[0];
    }

    private String normalize(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }
}
