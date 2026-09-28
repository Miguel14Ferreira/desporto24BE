package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.model.dto.auth.MfaResendRequest;
import com.example.desporto24.model.dto.auth.PasswordResetInitiateRequest;
import com.example.desporto24.model.dto.auth.PasswordResetConfirmRequest;
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.security.SensitiveActionRateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;

import static com.example.desporto24.constant.SecurityConstant.REFRESH_TOKEN_COOKIE;

import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and session management")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SensitiveActionRateLimiter sensitiveActionRateLimiter;

    @Value("${desporto.app.refreshExpirationMs:604800000}")
    private long refreshExpirationMs;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate a user and return a JWT or an MFA challenge.")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid ProfileDTO credentials) {
        LoginResponse result = authenticationService.login(credentials);
        if (result.mfaRequired()) {
            return ResponseEntity.status(ACCEPTED).body(result);
        }
        return withRefreshCookie(result);
    }

    @PostMapping("/mfa/confirm")
    @Operation(summary = "Confirm MFA challenge", description = "Completes MFA and returns the JWT only after a valid challenge.")
    public ResponseEntity<LoginResponse> confirmMfaToken(@RequestBody @Valid MfaConfirmRequest request) {
        return withRefreshCookie(authenticationService.confirmMfa(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh session", description = "Rotates the HttpOnly refresh cookie and returns a fresh access token.")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }

        LoginResponse result = authenticationService.refresh(refreshToken);
        return withRefreshCookie(result);
    }

    @PostMapping("/mfa/resend")
    @Operation(summary = "Resend MFA challenge", description = "Invalidates the client-side reference to the old challenge by returning a fresh challenge.")
    public ResponseEntity<LoginResponse> resendMfaToken(@RequestBody @Valid MfaResendRequest request) {
        sensitiveActionRateLimiter.requireCooldown(
                "mfa-resend",
                request.challengeId(),
                Duration.ofSeconds(60)
        );
        return ResponseEntity.accepted().body(authenticationService.resendMfa(request.challengeId()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> terminateSession(Authentication authentication) {
        authenticationService.terminateSession(authentication.getName());
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString())
                .build();
    }

    @PostMapping(value = "/register", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> registerUser(
            @RequestPart("profile") @Valid RegistrationRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        authenticationService.signUpProfile(request, photo);
        return buildResponse(NEW_USER_REGISTERED);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<HttpResponse> confirmRegistration(@RequestParam String token) {
        authenticationService.confirmToken(token);
        return buildResponse(REGISTRATION_CONFIRMED);
    }

    @PostMapping("/register/resend-confirmation")
    public ResponseEntity<HttpResponse> resendRegistrationConfirmation(@RequestParam String username) {
        sensitiveActionRateLimiter.requireCooldown(
                "registration-confirmation",
                username,
                Duration.ofSeconds(60)
        );
        try {
            authenticationService.resendRegistrationConfirmation(username);
        } catch (UserNotFoundException ignored) {
            // Keep a generic response to avoid exposing whether an account exists.
        }
        return buildResponse(CONFIRMATION_EMAIL_RESENT);
    }

    @PostMapping("/reset-password/initiate")
    public ResponseEntity<HttpResponse> initiatePasswordReset(
            @RequestBody @Valid PasswordResetInitiateRequest request) {
        sensitiveActionRateLimiter.requireCooldown(
                "password-reset",
                request.email(),
                Duration.ofSeconds(60)
        );
        authenticationService.resetPasswordStepOne(request.email());
        return buildResponse(PASSWORD_RESET_INITIATED);
    }

    @PutMapping("/reset-password/confirm")
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestBody @Valid PasswordResetConfirmRequest request) {
        authenticationService.resetPasswordStepTwo(request.newPassword(), request.token());
        return buildResponse(PASSWORD_RESET_SUCCESS);
    }

    private ResponseEntity<LoginResponse> withRefreshCookie(LoginResponse result) {
        String username = result.profile() == null ? null : result.profile().getUsername();
        if (username == null || username.isBlank()) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }

        String refreshToken = authenticationService.createRefreshToken(username);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie(refreshToken).toString())
                .body(result);
    }

    private ResponseCookie refreshCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ofMillis(refreshExpirationMs))
                .build();
    }

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ZERO)
                .build();
    }

    private ResponseEntity<HttpResponse> buildResponse(String message) {
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK,
                        HttpStatus.OK.getReasonPhrase().toUpperCase(), message),
                HttpStatus.OK
        );
    }
}
