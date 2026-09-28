package com.example.desporto24.controllers;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.MfaSetupConfirmRequest;
import com.example.desporto24.model.dto.auth.MfaSetupResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.TotpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile/me/mfa")
public class MfaSetupController {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final TotpService totpService;

    @PostMapping("/setup")
    public ResponseEntity<MfaSetupResponse> setup(Authentication authentication) {
        Profile profile = profileRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        String secret = totpService.generateSecret();
        profile.setMfaSecret(AES.encryptString(secret, profile.getUserId()));
        profile.setMFA(false);
        profileRepository.save(profile);

        String uri = totpService.provisioningUri(profile.getUsername(), secret);
        return ResponseEntity.ok(new MfaSetupResponse(
                secret,
                totpService.qrCodeDataUrl(uri),
                uri
        ));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ProfileDTO> confirm(
            @RequestBody @Valid MfaSetupConfirmRequest request,
            Authentication authentication) {
        Profile profile = profileRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (profile.getMfaSecret() == null || profile.getMfaSecret().isBlank()) {
            throw new IllegalStateException("Configuração MFA não iniciada");
        }

        String secret = AES.decryptString(profile.getMfaSecret(), profile.getUserId());
        if (!totpService.verify(secret, request.code().trim())) {
            throw new IllegalArgumentException("Código MFA inválido");
        }

        profile.setMFA(true);
        return ResponseEntity.ok(profileMapper.toDto(profileRepository.save(profile)));
    }

    @DeleteMapping
    public ResponseEntity<ProfileDTO> disable(Authentication authentication) {
        Profile profile = profileRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        profile.setMFA(false);
        profile.setMfaSecret(null);
        return ResponseEntity.ok(profileMapper.toDto(profileRepository.save(profile)));
    }
}
