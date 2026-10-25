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
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Manage notifications and account-related actions")
public class NotificationController {

    private final NotificationService notificationService;
    private final com.example.desporto24.service.NotificationPreferencesService preferenceService;


    @GetMapping("/me/preferences")
    public com.example.desporto24.model.dto.NotificationPreferencesRequest myPreferences(
            Authentication authentication) {
        return preferenceService.get(authentication.getName());
    }

    @PutMapping("/me/preferences")
    public com.example.desporto24.model.dto.NotificationPreferencesRequest updatePreferences(
            Authentication authentication,
            @RequestBody com.example.desporto24.model.dto.NotificationPreferencesRequest request) {
        return preferenceService.update(authentication.getName(), request);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Notifications>> getMyNotifications(Authentication authentication) {
        return new ResponseEntity<>(
                notificationService.getNotificationsFromProfile(authentication.getName()),
                OK
        );
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<Long> getMyUnreadNotificationCount(Authentication authentication) {
        return new ResponseEntity<>(
                notificationService.countUnreadNotifications(authentication.getName()),
                OK
        );
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get Notifications", description = "Retrieve all notifications for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Notifications>> getNotifications(
            @PathVariable("username") String username,
            Authentication authentication) {
        if (!authentication.getName().equals(username)) {
            throw new AccessDeniedException("Cannot access another user's notifications");
        }
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
    public ResponseEntity<HttpResponse> blockAcc(
            @RequestBody String email,
            Authentication authentication) {
        notificationService.disableProfileAndSendEmail(email, authentication.getName());
        return buildResponse(OK, DISABLED_PERFIL);
    }

    @PostMapping("/me/block")
    @Operation(
            summary = "Block own account access",
            description = "Immediately blocks the authenticated account and invalidates existing sessions."
    )
    public ResponseEntity<Void> blockMyAccount(Authentication authentication) {
        notificationService.blockOwnAccount(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Delete all own notifications",
            description = "Deletes every notification belonging to the authenticated user."
    )
    public ResponseEntity<Void> deleteAllMyNotifications(Authentication authentication) {
        notificationService.deleteAllNotifications(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            Authentication authentication) {
        notificationService.markRead(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyNotification(
            @PathVariable Long id,
            Authentication authentication) {
        notificationService.deleteNotification(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete Notification", description = "Delete a notification from the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted notification successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid notification ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> deleteNotification(
            @RequestBody Notifications notifications,
            Authentication authentication) {
        if (notifications.getId() == null) {
            throw new IllegalArgumentException("Notification id is required");
        }
        notificationService.deleteNotification(notifications.getId(), authentication.getName());
        return buildResponse(OK, DELETED_NOTIFICATION);
    }

    @PostMapping("/friend-request/{id}/accept")
    public ResponseEntity<HttpResponse> acceptMyFriendRequest(
            @PathVariable Long id,
            Authentication authentication) {
        notificationService.acceptFriendRequest(id, authentication.getName());
        return buildResponse(OK, ACCEPTED_FRIEND_REQUEST);
    }

    @DeleteMapping("/friend-request/{id}/reject")
    public ResponseEntity<Void> rejectMyFriendRequest(
            @PathVariable Long id,
            Authentication authentication) {
        notificationService.rejectFriendRequest(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/friend-request/accept/{id}/{token}")
    @Operation(summary = "Accept Friend Request", description = "Legacy token-based friend request acceptance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid token or ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> acceptFriendRequest(
            @PathVariable("id") Long id,
            @PathVariable("token") String token,
            Authentication authentication) {
        notificationService.acceptFriendRequest(id, authentication.getName());
        return buildResponse(OK, ACCEPTED_FRIEND_REQUEST);
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message),
                status
        );
    }
}