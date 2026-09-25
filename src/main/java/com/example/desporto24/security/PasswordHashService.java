package com.example.desporto24.security;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class PasswordHashService {

    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean matchesAndUpgrade(Profile profile, String rawPassword) {
        String stored = profile.getPassword();
        if (stored == null || rawPassword == null) {
            return false;
        }
        if (stored.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, stored);
        }

        String legacy = AES.decryptString(stored, profile.getUserId());
        boolean matches = MessageDigest.isEqual(
                legacy.getBytes(StandardCharsets.UTF_8),
                rawPassword.getBytes(StandardCharsets.UTF_8)
        );
        if (matches) {
            profile.setPassword(passwordEncoder.encode(rawPassword));
            profileRepository.save(profile);
        }
        return matches;
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
