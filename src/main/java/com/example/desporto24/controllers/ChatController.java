package com.example.desporto24.controllers;

import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.service.interfaces.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Handling chat operations like sending messages and retrieving chat history.")
public class ChatController {

    private final ChatService chatService;

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
    public ResponseEntity<List<Chat>> getChat(@PathVariable("senderId") String sender, @PathVariable("recipientId") String recipient) {
        List<Chat> chat = chatService.findChatMessages(sender, recipient);
        return new ResponseEntity<>(chat, OK);
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
    public ResponseEntity<Chat> sendChatMessage(@RequestBody Chat chat) {
        Chat c = chatService.sendMessage(chat);
        return new ResponseEntity<>(c, OK);
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
    public ResponseEntity<Friend> getChatInfo(@PathVariable("senderId") String sender, @PathVariable("recipientId") String recipient) {
        Friend chatInfo = chatService.getChatInfo(sender, recipient);
        return new ResponseEntity<>(chatInfo, OK);
    }
}