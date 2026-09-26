package com.example.desporto24.controllers;

import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;
import com.example.desporto24.service.interfaces.IdeaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.desporto24.constant.UserImplConstant.NEW_IDEA;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ideas")
@Tag(name = "Ideas", description = "Endpoint for submitting new ideas")
public class IdeaController {

    private final IdeaService ideaService;

    @PostMapping("/submit")
    @Operation(summary = "Submit New Idea", description = "Allows users to submit a new idea.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idea submitted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid idea data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpResponse> registerNewIdea(
            @RequestBody @Valid IdeaSubmissionRequest request) {
        ideaService.submitNewIdea(request);
        return buildResponse();
    }

    private ResponseEntity<HttpResponse> buildResponse() {
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase().toUpperCase(), NEW_IDEA),
                HttpStatus.OK
        );
    }
}