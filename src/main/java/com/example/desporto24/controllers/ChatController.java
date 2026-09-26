package com.example.desporto24.controllers;

import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.dto.chat.ChatInfoResponse;
import com.example.desporto24.model.dto.chat.ChatMessageRequest;
import com.example.desporto24.service.interfaces.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Handling chat operations like sending messages and retrieving chat history.")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/me/{username}")
    public ResponseEntity<List<Chat>> getMyChat(
            @PathVariable String username,
            Authentication authentication) {
        return new ResponseEntity<>(
                chatService.findChatMessages(authentication.getName(), username),
                OK
        );
    }

    @PostMapping("/me/{username}")
    public ResponseEntity<Chat> sendMyChatMessage(
            @PathVariable String username,
            @RequestBody @Valid ChatMessageRequest request,
            Authentication authentication) {
        Chat chat = new Chat();
        chat.setUsername1(authentication.getName());
        chat.setUsername2(username);
        chat.setText(request.text().trim());
        return new ResponseEntity<>(chatService.sendMessage(chat), OK);
    }

    @GetMapping("/me/{username}/info")
    public ResponseEntity<ChatInfoResponse> getMyChatInfo(
            @PathVariable String username,
            Authentication authentication) {
        Friend friend = chatService.getChatInfo(authentication.getName(), username);
        return new ResponseEntity<>(
                new ChatInfoResponse(friend.isChatBlocked(), friend.getWhoBlocked()),
                OK
        );
    }

    // Chat Management
    @GetMapping("/{senderId}/{recipientId}")
    @Operation(summary = "Fetch Chat Messages", description = "Retrieve chat messages between two users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chat messages",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))),
            @ApiResponse(responseCode = "404", description = "Chat messages not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Chat>> getChat(
            @PathVariable("senderId") String sender,
            @PathVariable("recipientId") String recipient,
            Authentication authentication) {
        String other = resolveOtherParticipant(sender, recipient, authentication.getName());
        return new ResponseEntity<>(
                chatService.findChatMessages(authentication.getName(), other),
                OK
        );
    }

    @PostMapping("/{senderId}/{recipientId}")
    @Operation(summary = "Send Chat Message", description = "Send a chat message to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))),
            @ApiResponse(responseCode = "400", description = "Invalid message format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Chat> sendChatMessage(
            @PathVariable("senderId") String sender,
            @PathVariable("recipientId") String recipient,
            @RequestBody Chat chat,
            Authentication authentication) {
        String other = resolveOtherParticipant(sender, recipient, authentication.getName());
        Chat authenticatedMessage = new Chat();
        authenticatedMessage.setUsername1(authentication.getName());
        authenticatedMessage.setUsername2(other);
        authenticatedMessage.setText(chat.getText());
        return new ResponseEntity<>(
                chatService.sendMessage(authenticatedMessage),
                OK
        );
    }

    @GetMapping("/info/{senderId}/{recipientId}")
    @Operation(summary = "Get Chat Info", description = "Retrieve information about a chat session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chat info",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Friend.class))),
            @ApiResponse(responseCode = "404", description = "Chat info not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ChatInfoResponse> getChatInfo(
            @PathVariable("senderId") String sender,
            @PathVariable("recipientId") String recipient,
            Authentication authentication) {
        String other = resolveOtherParticipant(sender, recipient, authentication.getName());
        Friend friend = chatService.getChatInfo(authentication.getName(), other);
        return new ResponseEntity<>(
                new ChatInfoResponse(friend.isChatBlocked(), friend.getWhoBlocked()),
                OK
        );
    }

    private String resolveOtherParticipant(
            String first,
            String second,
            String authenticatedUsername) {
        if (authenticatedUsername.equals(first)) {
            return second;
        }
        if (authenticatedUsername.equals(second)) {
            return first;
        }
        throw new AccessDeniedException("Authenticated user is not part of this chat");
    }
}