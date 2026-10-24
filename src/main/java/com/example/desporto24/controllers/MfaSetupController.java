package com.example.desporto24.controllers;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.MfaSetupConfirmRequest;
import com.example.desporto24.model.dto.auth.MfaSetupResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.security.MfaSettingsAttemptLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
    private final MfaSettingsAttemptLimiter mfaSettingsAttemptLimiter;
    private final EmailService emailService;
    private final NotificationsRepository notificationsRepository;

    @PostMapping("/setup")
    public ResponseEntity<MfaSetupResponse> setup(Authentication authentication) {
        Profile profile = profileRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        // A valid access JWT alone must NEVER be enough to reset or disable
        // an active second factor. Users must call DELETE with their current TOTP.
        if (Boolean.TRUE.equals(profile.getMFA())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Desativa primeiro o MFA com um código do autenticador válido.");
        }

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

        mfaSettingsAttemptLimiter.reserveAttempt(profile.getUsername());
        String secret = AES.decryptString(profile.getMfaSecret(), profile.getUserId());
        if (!totpService.verify(secret, request.code().trim())) {
            throw new IllegalArgumentException("Código MFA inválido");
        }

        mfaSettingsAttemptLimiter.resetAfterSuccess(profile.getUsername());
        profile.setMFA(true);
        Profile saved = profileRepository.save(profile);
        sendMfaSecurityNotice(saved, true);
        return ResponseEntity.ok(profileMapper.toDto(saved));
    }

    @DeleteMapping
    public ResponseEntity<ProfileDTO> disable(
            @RequestBody @Valid MfaSetupConfirmRequest request,
            Authentication authentication) {
        Profile profile = profileRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (!Boolean.TRUE.equals(profile.getMFA())
                || profile.getMfaSecret() == null
                || profile.getMfaSecret().isBlank()) {
            throw new IllegalStateException("MFA não está ativo");
        }

        mfaSettingsAttemptLimiter.reserveAttempt(profile.getUsername());
        String secret = AES.decryptString(profile.getMfaSecret(), profile.getUserId());
        if (!totpService.verify(secret, request.code().trim())) {
            throw new IllegalArgumentException("Código MFA inválido");
        }

        mfaSettingsAttemptLimiter.resetAfterSuccess(profile.getUsername());
        profile.setMFA(false);
        profile.setMfaSecret(null);
        Profile saved = profileRepository.save(profile);
        sendMfaSecurityNotice(saved, false);
        return ResponseEntity.ok(profileMapper.toDto(saved));
    }

    private void sendMfaSecurityNotice(Profile profile, boolean enabled) {
        String title = enabled
                ? "Autenticação multifator ativada"
                : "Autenticação multifator desativada";

        String description = enabled
                ? "A autenticação multifator por Authenticator App foi ativada na tua conta DESPORTO24."
                : "A autenticação multifator foi desativada na tua conta DESPORTO24.";

        if (profile.getEmail() != null && !profile.getEmail().isBlank()) {
            emailService.send(
                    profile.getEmail(),
                    "<h2>" + title + "</h2>"
                            + "<p>Olá " + profile.getUsername() + ",</p>"
                            + "<p>" + description + "</p>"
                            + "<p>Se não foste tu, altera imediatamente a tua palavra-passe e contacta a equipa DESPORTO24.</p>"
                            + "<p>Cumprimentos,<br>DESPORTO24 MANAGEMENT TEAM</p>"
            );
        }

        Notifications notification = new Notifications(
                title,
                description + " Se não foste tu, bloqueia temporariamente os acessos à tua conta.",
                "Cumprimentos,",
                "DESPORTO24 MANAGEMENT TEAM",
                java.time.Instant.now().toString(),
                false,
                true,
                false,
                false,
                null,
                profile.getUsername()
        );
        notificationsRepository.save(notification);
    }
}
