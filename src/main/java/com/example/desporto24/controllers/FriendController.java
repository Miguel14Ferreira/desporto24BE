package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
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
    @PostMapping("/add")
    @Operation(summary = "Add Friend", description = "Send a friend request to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> addFriend(@ModelAttribute SendFriendRequest friendRequest) {
        friendService.sendFriendRequest(friendRequest);
        return buildResponse(FRIEND_REQUEST);
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
    public ResponseEntity<HttpResponse> blockFriend(@ModelAttribute SendFriendRequest sendFriendRequest) {
        friendService.blockFriend(sendFriendRequest.getUsername1(), sendFriendRequest.getUsername2());
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
    public ResponseEntity<HttpResponse> unblockFriend(@ModelAttribute SendFriendRequest sendFriendRequest) {
        friendService.unblockFriend(sendFriendRequest.getUsername1(), sendFriendRequest.getUsername2());
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
    public ResponseEntity<HttpResponse> removeFriend(@ModelAttribute SendFriendRequest sendFriendRequest) {
        friendService.removeFriend(sendFriendRequest.getUsername1(), sendFriendRequest.getUsername2());
        return buildResponse(sendFriendRequest.getUsername2() + REMOVED_FRIEND);
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
    public ResponseEntity<List<Profile>> getFriendList(@PathVariable("username") String username) {
        List<Profile> friends = friendService.getFriends(username);
        return new ResponseEntity<>(friends, OK);
    }

    private ResponseEntity<HttpResponse> buildResponse(String message) {
        return new ResponseEntity<>(new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase().toUpperCase(), message), HttpStatus.OK);
    }
}