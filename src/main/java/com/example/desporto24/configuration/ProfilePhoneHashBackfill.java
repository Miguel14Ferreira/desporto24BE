package com.example.desporto24.configuration;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfilePhoneHashBackfill implements ApplicationRunner {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        long lastSeenId = 0L;
        Map<String, Long> hashesSeenInRun = new HashMap<>();
        while (true) {
            List<Profile> candidates =
                    profileRepository.findTop100ByPhoneHashIsNullAndIdGreaterThanOrderByIdAsc(lastSeenId);
            if (candidates.isEmpty()) {
                return;
            }

            lastSeenId = candidates.get(candidates.size() - 1).getId();

            List<Profile> updated = new ArrayList<>();
            for (Profile profile : candidates) {
                if (profile.getPhone() == null || profile.getUserId() == null) {
                    continue;
                }

                try {
                    String phone = AES.decryptString(
                            profile.getPhone(),
                            profile.getUserId());

                    if (phone == null || phone.isBlank()) {
                        continue;
                    }

                    String phoneHash = AES.hashLookupValue(phone);
                    Long duplicateProfileId = hashesSeenInRun.get(phoneHash);
                    if (duplicateProfileId == null) {
                        duplicateProfileId = profileRepository.findByPhoneHash(phoneHash)
                                .filter(existing -> !existing.getId().equals(profile.getId()))
                                .map(Profile::getId)
                                .orElse(null);
                    }

                    if (duplicateProfileId != null) {
                        log.warn(
                                "Skipping duplicate phone hash for profile {} because profile {} already uses it",
                                profile.getId(),
                                duplicateProfileId);
                        continue;
                    }

                    hashesSeenInRun.put(phoneHash, profile.getId());
                    profile.setPhoneHash(phoneHash);
                    updated.add(profile);
                } catch (RuntimeException exception) {
                    log.warn(
                            "Skipping phone hash backfill for profile {}",
                            profile.getId());
                }
            }

            if (!updated.isEmpty()) {
                profileRepository.saveAll(updated);
                profileRepository.flush();
            }
        }
    }
}
