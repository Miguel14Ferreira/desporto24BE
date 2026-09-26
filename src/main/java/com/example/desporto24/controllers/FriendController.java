package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.friend.FriendSummaryResponse;
import com.example.desporto24.service.interfaces.FriendService;
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
@RequestMapping("/api/friends")
@Tag(name = "Friends", description = "Operations related to managing friendships")
public class FriendController {

    private final FriendService friendService;

    // Friendship Management
    @PostMapping("/add/{username}")
    public ResponseEntity<HttpResponse> addFriendAuthenticated(
            @PathVariable String username,
            Authentication authentication) {
        SendFriendRequest friendRequest = new SendFriendRequest();
        friendRequest.setUsername1(authentication.getName());
        friendRequest.setUsername2(username);
        friendService.sendFriendRequest(friendRequest);
        return buildResponse(FRIEND_REQUEST);
    }

    @PostMapping("/add")
    @Operation(summary = "Add Friend", description = "Send a friend request to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> addFriend(
            @ModelAttribute SendFriendRequest friendRequest,
            Authentication authentication) {
        SendFriendRequest authenticatedRequest = new SendFriendRequest();
        authenticatedRequest.setUsername1(authentication.getName());
        authenticatedRequest.setUsername2(friendRequest.getUsername2());
        friendService.sendFriendRequest(authenticatedRequest);
        return buildResponse(FRIEND_REQUEST);
    }

    @PutMapping("/{username}/block")
    public ResponseEntity<HttpResponse> blockFriendAuthenticated(
            @PathVariable String username,
            Authentication authentication) {
        friendService.blockFriend(authentication.getName(), username);
        return buildResponse(BLOCKED);
    }

    @PutMapping("/{username}/unblock")
    public ResponseEntity<HttpResponse> unblockFriendAuthenticated(
            @PathVariable String username,
            Authentication authentication) {
        friendService.unblockFriend(authentication.getName(), username);
        return buildResponse(UNBLOCKED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<HttpResponse> removeFriendAuthenticated(
            @PathVariable String username,
            Authentication authentication) {
        friendService.removeFriend(authentication.getName(), username);
        return buildResponse(username + REMOVED_FRIEND);
    }

    @PutMapping("/block")
    @Operation(summary = "Block Friend", description = "Block a user to prevent them from viewing your profile or sending messages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User blocked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> blockFriend(
            @ModelAttribute SendFriendRequest sendFriendRequest,
            Authentication authentication) {
        friendService.blockFriend(authentication.getName(), sendFriendRequest.getUsername2());
        return buildResponse(BLOCKED);
    }

    @PutMapping("/unblock")
    @Operation(summary = "Unblock Friend", description = "Unblock a previously blocked user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unblocked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> unblockFriend(
            @ModelAttribute SendFriendRequest sendFriendRequest,
            Authentication authentication) {
        friendService.unblockFriend(authentication.getName(), sendFriendRequest.getUsername2());
        return buildResponse(UNBLOCKED);
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove Friend", description = "Remove a friend from your friend list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> removeFriend(
            @ModelAttribute SendFriendRequest sendFriendRequest,
            Authentication authentication) {
        friendService.removeFriend(authentication.getName(), sendFriendRequest.getUsername2());
        return buildResponse(sendFriendRequest.getUsername2() + REMOVED_FRIEND);
    }

    @GetMapping("/me")
    public ResponseEntity<List<FriendSummaryResponse>> getMyFriendList(Authentication authentication) {
        return new ResponseEntity<>(
                friendService.getFriendSummaries(authentication.getName()),
                OK
        );
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get Friend List", description = "Retrieve the friend list for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend list retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FriendSummaryResponse>> getFriendList(
            @PathVariable("username") String username,
            Authentication authentication) {
        if (!authentication.getName().equals(username)) {
            throw new AccessDeniedException("Cannot access another user's friend list");
        }
        return new ResponseEntity<>(
                friendService.getFriendSummaries(username),
                OK
        );
    }

    private ResponseEntity<HttpResponse> buildResponse(String message) {
        return new ResponseEntity<>(new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase().toUpperCase(), message), HttpStatus.OK);
    }
}