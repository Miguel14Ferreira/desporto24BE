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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfilePhoneHashBackfill implements ApplicationRunner {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        while (true) {
            List<Profile> candidates =
                    profileRepository.findTop100ByPhoneHashIsNullOrderByIdAsc();
            if (candidates.isEmpty()) {
                return;
            }

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

                    profile.setPhoneHash(AES.hashLookupValue(phone));
                    updated.add(profile);
                } catch (RuntimeException exception) {
                    log.warn(
                            "Skipping phone hash backfill for profile {}",
                            profile.getId());
                }
            }

            if (updated.isEmpty()) {
                return;
            }

            profileRepository.saveAll(updated);
            profileRepository.flush();
        }
    }
}
