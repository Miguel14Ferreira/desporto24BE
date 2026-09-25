package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.service.interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and session management")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate a user and return a JWT or an MFA challenge.")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid ProfileDTO credentials) {
        LoginResponse result = authenticationService.login(credentials);
        return ResponseEntity.status(result.mfaRequired() ? ACCEPTED : OK).body(result);
    }

    @PostMapping("/mfa/confirm")
    @Operation(summary = "Confirm MFA challenge", description = "Completes MFA and returns the JWT only after a valid challenge.")
    public ResponseEntity<LoginResponse> confirmMfaToken(@RequestBody @Valid MfaConfirmRequest request) {
        return ResponseEntity.ok(authenticationService.confirmMfa(request));
    }

    @PostMapping("/logout")
    public void terminateSession(@RequestBody ProfileDTO profileDTO) {
        authenticationService.terminateSession(profileDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@ModelAttribute @Valid ProfileDTO profileDTO,
                                                     @RequestParam(value = "photo", required = false) MultipartFile photo) {
        authenticationService.signUpProfile(profileDTO, photo);
        return buildResponse(NEW_USER_REGISTERED);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<HttpResponse> confirmRegistration(@RequestParam String token) {
        authenticationService.confirmToken(token);
        return buildResponse(REGISTRATION_CONFIRMED);
    }

    @PostMapping("/reset-password/initiate")
    public ResponseEntity<HttpResponse> initiatePasswordReset(@RequestBody String email) {
        authenticationService.resetPasswordStepOne(email);
        return buildResponse(PASSWORD_RESET_INITIATED);
    }

    @PutMapping("/reset-password/confirm")
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam String token,
                                                      @RequestParam String password) {
        authenticationService.resetPasswordStepTwo(password, token);
        return buildResponse(PASSWORD_RESET_SUCCESS);
    }

    @GetMapping("/mfa/resend")
    public ResponseEntity<HttpResponse> resendMfaToken(@RequestParam String username) {
        authenticationService.sendVerificationCode(username);
        return buildResponse(SMS_RESENT);
    }

    private ResponseEntity<HttpResponse> buildResponse(String message) {
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK,
                        HttpStatus.OK.getReasonPhrase().toUpperCase(), message),
                HttpStatus.OK
        );
    }
}
