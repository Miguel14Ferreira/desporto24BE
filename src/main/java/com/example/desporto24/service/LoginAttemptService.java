package com.example.desporto24.service;

import com.example.desporto24.exception.domain.AccountDisabledException;
import com.example.desporto24.exception.domain.EmailNotVerifiedException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.repository.PerfilRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import static com.example.desporto24.constant.UserImplConstant.*;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;
    private final PerfilRepository perfilRepository;

    public LoginAttemptService(PerfilRepository perfilRepository) {
        super();
        this.perfilRepository = perfilRepository;
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(Perfil username) {
        username.setLogginAttempts(0);
        perfilRepository.save(username);
    }

    public void addUserToLoginAttemptCache(Perfil username) {
        int attempts;
        try {
            attempts = ATTEMPT_INCREMENT + username.getLogginAttempts();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }
        username.setLogginAttempts(attempts);
        perfilRepository.save(username);
    }

    public boolean hasExceededMaxAttempts(Perfil username) throws AccountDisabledException, EmailNotVerifiedException {
        try {
            if (username.getLogginAttempts() >= MAXIMUM_NUMBER_OF_ATTEMPTS) {
                username.setNotLocked(false);
                throw new AccountDisabledException(ACCOUNT_DISABLED);
            } else if (!username.getEnabled()) {
                throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
            }
        } catch (EmailNotVerifiedException exception) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        } catch (AccountDisabledException exception) {
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }
        return false;
    }

}
