package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.exception.NotAnImageFileException;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.service.interfaces.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.List;
import static com.example.desporto24.constant.UserImplConstant.UPDATED_PERFIL;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "Endpoints for managing user profiles")
public class ProfileController extends ExceptionHandling {

    private final ProfileService profileService;

    // User Profile Endpoints
    @GetMapping("/{username}")
    @Operation(summary = "Get User Profile", description = "Retrieve a user's profile using their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProfileDTO> getUserProfile(@PathVariable String username) {
        ProfileDTO profile = profileService.findUserByUsername(username);
        return new ResponseEntity<>(profile, OK);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Search Profiles by Name", description = "Search for profiles using a partial or full name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "No profiles found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProfileDTO>> getProfileByName(@PathVariable String name) {
        List<ProfileDTO> profile = profileService.searchProfileByUsername(name);
        return new ResponseEntity<>(profile, OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Update User Profile", description = "Update a user's profile details and optionally their profile picture.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid profile data or image file"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> updateUser(@RequestParam String username,
                                                   @ModelAttribute @Valid ProfileDTO profileDTO,
                                                   @RequestParam(required = false) MultipartFile photo) throws Exception, NotAnImageFileException {
        profileService.updateUser(username, profileDTO, photo);
        return buildResponse(OK, UPDATED_PERFIL);
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update Password", description = "Change a user's username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid password data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> updateUserPassword(@ModelAttribute @Valid ProfileDTO profileDTO) {
        profileService.changeUsernameAndPassword(profileDTO);
        return buildResponse(OK, UPDATED_PERFIL);
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