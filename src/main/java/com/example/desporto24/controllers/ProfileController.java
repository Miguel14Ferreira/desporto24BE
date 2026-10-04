package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.profile.UpdateProfileRequest;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.model.dto.profile.PublicProfileResponse;
import com.example.desporto24.service.AccountDeletionService;
import com.example.desporto24.service.interfaces.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import static com.example.desporto24.constant.SecurityConstant.REFRESH_TOKEN_COOKIE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "Endpoints for managing user profiles")
public class ProfileController extends ExceptionHandling {

    private final ProfileService profileService;
    private final AccountDeletionService accountDeletionService;

    // User Profile Endpoints
    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getMyProfile(Authentication authentication) {
        return new ResponseEntity<>(
                profileService.findUserByUsername(authentication.getName()),
                OK
        );
    }

    @PutMapping(value = "/me", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDTO> updateMyProfile(
            @RequestPart("profile") @Valid UpdateProfileRequest request,
            Authentication authentication) {
        return new ResponseEntity<>(
                profileService.updateMyProfile(authentication.getName(), request),
                OK
        );
    }

    @PutMapping(value = "/me/photo", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDTO> updateMyProfilePhoto(
            @RequestPart("photo") MultipartFile photo,
            Authentication authentication) {
        return new ResponseEntity<>(
                profileService.updateMyProfilePhoto(authentication.getName(), photo),
                OK
        );
    }

    @PutMapping("/me/password")
    public ResponseEntity<HttpResponse> changeMyPassword(
            @RequestBody @Valid ChangePasswordRequest request,
            Authentication authentication) {
        profileService.changeMyPassword(authentication.getName(), request);
        return buildResponse(OK, com.example.desporto24.constant.UserImplConstant.PASSWORD_RESET_SUCCESS);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        accountDeletionService.deleteMyAccount(authentication.getName());

        ResponseCookie clearRefreshCookie = ResponseCookie
                .from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                .build();
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get Public User Profile", description = "Retrieve the public fields of a user's profile.")
    public ResponseEntity<PublicProfileResponse> getUserProfile(
            @PathVariable String username,
            Authentication authentication) {
        return new ResponseEntity<>(
                profileService.findPublicProfile(authentication.getName(), username),
                OK
        );
    }

    @GetMapping("/discover")
    public ResponseEntity<List<PublicProfileResponse>> discoverProfiles(Authentication authentication) {
        return new ResponseEntity<>(
                profileService.discoverPublicProfiles(authentication.getName()),
                OK
        );
    }

    @GetMapping("/search")
    @Operation(
            summary = "Find compatible sports profiles",
            description = "Search public profiles by optional sport, location, age, level and name filters."
    )
    public ResponseEntity<List<PublicProfileResponse>> searchProfiles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String skillLevel,
            Authentication authentication) {
        return new ResponseEntity<>(
                profileService.searchPublicProfiles(
                        authentication.getName(),
                        name,
                        sport,
                        location,
                        minAge,
                        maxAge,
                        skillLevel
                ),
                OK
        );
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Search Public Profiles by Name", description = "Search public profile fields using a partial or full username.")
    public ResponseEntity<List<PublicProfileResponse>> getProfileByName(
            @PathVariable String name,
            Authentication authentication) {
        return new ResponseEntity<>(
                profileService.searchPublicProfiles(
                        authentication.getName(),
                        name,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                OK
        );
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check the health status of the ProfileController service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Health check successful")
    })
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("ProfileController is up and running!", OK);
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message), status);
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status) {
        return buildResponse(status, status.getReasonPhrase());
    }
}