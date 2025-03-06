package com.example.desporto24.service;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.AccountDisabledException;
import com.example.desporto24.exception.EmailNotVerifiedException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.ProfileRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.desporto24.constant.UserImplConstant.*;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private final ProfileRepository profileRepository;

    public LoginAttemptService(ProfileRepository profileRepository) {
        super();
        this.profileRepository = profileRepository;
        CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public @NotNull Integer load(@NotNull String key) {
                        return 0;
                    }
                });
    }

    public void handleLoginAttempt(String username, String password, Profile profile)
            throws AccountDisabledException, EmailNotVerifiedException, BadCredentialsException {

        if (profile.getLoginAttempts() >= MAXIMUM_NUMBER_OF_ATTEMPTS) {
            profile.setNotLocked(false);
            profileRepository.save(profile);
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }

        if (!profile.getEnabled()) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }

        // Validate login attempt
        if (!profile.getUsername().equals(username)) {
            handleInvalidLogin(profile);
        } else if (!AES.decryptString(profile.getPassword(), profile.getUserId()).equals(password)) {
            handleInvalidLogin(profile);
        }
    }

    private void handleInvalidLogin(Profile profile) {
        int attempts = profile.getLoginAttempts() + ATTEMPT_INCREMENT;
        profile.setLoginAttempts(attempts);
        profileRepository.save(profile);
        throw new BadCredentialsException(INCORRECT_CREDENTIALS);
    }
}
