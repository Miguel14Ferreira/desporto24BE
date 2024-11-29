package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.NotAnImageFileException;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.SessionDTO;
import com.example.desporto24.service.interfaces.SessionService;
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
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@Tag(name = "Sessions", description = "Endpoints for session management")
public class SessionController {

    private final SessionService sessionService;

    // Session Management
    @PostMapping("/create")
    @Operation(summary = "Create New Session", description = "Creates a new session with the provided details and an optional image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid session data or image file"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> createSession(@ModelAttribute @Valid SessionDTO sessionDTO,
                                                      @RequestParam MultipartFile photo) throws Exception, NotAnImageFileException {
        sessionService.createSession(sessionDTO, photo);
        return buildResponse();
    }

    @GetMapping("/")
    @Operation(summary = "Get All Sessions", description = "Retrieves all available sessions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getSessions();
        return new ResponseEntity<>(sessions, OK);
    }

    private ResponseEntity<HttpResponse> buildResponse() {
        return new ResponseEntity<>(new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase().toUpperCase(), com.example.desporto24.constant.UserImplConstant.NEW_SESSION), HttpStatus.OK);
    }
}