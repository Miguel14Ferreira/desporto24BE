package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.service.interfaces.NotificationService;
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

import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.ACCEPTED_FRIEND_REQUEST;
import static com.example.desporto24.constant.UserImplConstant.DISABLED_PERFIL;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Manage notifications and account-related actions")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Notifications", description = "Retrieve all notifications for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Notifications>> getNotifications(@PathVariable("username") String username) {
        List<Notifications> notifications = notificationService.getNotificationsFromProfile(username);
        return new ResponseEntity<>(notifications, OK);
    }

    @PostMapping("/block/{id}")
    @Operation(summary = "Block Account", description = "Disable a user account and send an email notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account disabled and notification sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid email provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> blockAcc(@RequestBody String email) {
        notificationService.disableProfileAndSendEmail(email);
        return buildResponse(OK, DISABLED_PERFIL);
    }

    @GetMapping("/friend-request/accept/{id}/{token}")
    @Operation(summary = "Accept Friend Request", description = "Accept a friend request using the provided ID and token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid token or ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> acceptFriendRequest(@PathVariable("id") Long id,
                                                            @PathVariable("token") String token) throws Exception {
        notificationService.acceptFriendRequest(id, token);
        return buildResponse(OK, ACCEPTED_FRIEND_REQUEST);
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message),
                status
        );
    }
}