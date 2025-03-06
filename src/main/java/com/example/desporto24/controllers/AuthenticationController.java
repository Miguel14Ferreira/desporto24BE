package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.desporto24.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and session management")
public class AuthenticationController {

    private final AuthenticationService authenticateService;
    private final JWTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate a user and return JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profile.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Profile> login(@RequestBody ProfileDTO profileDTO) {
        Profile profile = authenticateService.authenticate(profileDTO);
        HttpHeaders jwtHeader = createJwtHeader(new ProfilePrincipal(profile));
        return new ResponseEntity<>(profile, jwtHeader, OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout User", description = "Terminates the user session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged out"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void terminateSession(@RequestBody ProfileDTO profileDTO) {
        authenticateService.terminateSession(profileDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "Register New User", description = "Registers a new user with an optional profile photo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> registerUser(@ModelAttribute @Valid ProfileDTO profileDTO,
                                                     @RequestParam(value = "photo", required = false) MultipartFile photo) {

        authenticateService.signUpProfile(profileDTO, photo);
        return buildResponse(NEW_USER_REGISTERED);
    }

    @GetMapping("/register/confirm")
    @Operation(summary = "Confirm Password Reset", description = "Completes the password reset process with a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Invalid token or new password"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> confirmRegistration(@RequestParam String token) {
        authenticateService.confirmToken(token);
        return buildResponse(REGISTRATION_CONFIRMED);
    }

    @PostMapping("/reset-password/initiate")
    @Operation(summary = "Initiate Password Reset", description = "Starts the password reset process.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset process initiated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> initiatePasswordReset(@RequestBody String email) {
        authenticateService.resetPasswordStepOne(email);
        return buildResponse(PASSWORD_RESET_INITIATED);
    }

    @PutMapping("/reset-password/confirm")
    @Operation(summary = "Confirm Password Reset", description = "Completes the password reset process with a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Invalid token or new password"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam String token,
                                                      @RequestParam String password) {
        authenticateService.resetPasswordStepTwo(password, token);
        return buildResponse(PASSWORD_RESET_SUCCESS);
    }

    @PostMapping("/mfa/confirm")
    @Operation(summary = "Confirm MFA Token", description = "Verifies the multi-factor authentication code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MFA code successfully confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid MFA code"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> confirmMfaToken(@RequestBody String mfaCode) {
        authenticateService.confirmMfaCode(mfaCode);
        return buildResponse();
    }

    @GetMapping("/mfa/resend")
    @Operation(summary = "Resend MFA Token", description = "Resends the MFA code to the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MFA code resent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> resendMfaToken(@RequestParam String username) {
        authenticateService.sendVerificationCode(username);
        return buildResponse(SMS_RESENT);
    }

    private ResponseEntity<HttpResponse> buildResponse(String message) {
        return new ResponseEntity<>(new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase().toUpperCase(), message), HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> buildResponse() {
        return buildResponse(HttpStatus.OK.getReasonPhrase());
    }

    private HttpHeaders createJwtHeader(ProfilePrincipal profilePrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(profilePrincipal));
        return headers;
    }
}