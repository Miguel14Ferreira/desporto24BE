package com.example.desporto24.controller;
import com.example.desporto24.Chat.ChatRequest;
import com.example.desporto24.Chat.ChatService;
import com.example.desporto24.Friend.SendFriendRequest;
import com.example.desporto24.Friend.SendFriendService;
import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.SessionDTO;
import com.example.desporto24.newIdea.NewIdeaRequest;
import com.example.desporto24.newIdea.NewIdeaService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.ProjectService;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.encryption.AES.decryptString;
import static com.example.desporto24.utility.ProfileUtils.getLoggedInUser;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/", "/api/auth"})
public class ProfileController extends ExceptionHandling {

    private final ProfileRepository profileRepository;
    private final NewIdeaService newIdeaService;
    private final AuthenticationManager authenticationManager;
    private final ProjectService projectService;
    private final JWTokenProvider jwtTokenProvider;
    private final LoginAttemptService loginAttemptService;
    private final SendFriendService sendFriendService;
    private final ChatService chatService;

    // User Authentication
    @PostMapping("/login")
    public ResponseEntity<Profile> login(@RequestBody ProfileDTO profileDTO) throws Exception {
        Profile profile = authenticate(profileDTO.getUsername(), profileDTO.getPassword());
        ProfilePrincipal profilePrincipal = new ProfilePrincipal(profile);
        HttpHeaders jwtHeader = createJwtHeader(profilePrincipal);
        return new ResponseEntity<>(profile, jwtHeader, HttpStatus.OK);
    }

    private Profile authenticate(String username, String password) throws Exception {
        Profile profile = profileRepository.findUserByUsername(username);
        if (profile == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }

        Authentication authentication;
        if (Boolean.FALSE.equals(profile.getMFA())) {
            authentication = handleRegularLogin(username, password, profile);
        } else {
            authentication = handleMFALogin(username, password, profile);
        }

        return getLoggedInUser(authentication);
    }

    private Authentication handleRegularLogin(String username, String password, Profile profile) throws Exception {
        loginAttemptService.hasExceededMaxAttempts(profile);
        loginAttemptService.addUserToLoginAttemptCache(profile);
        Authentication authentication = authenticationManager.authenticate(unauthenticated(username, password));
        loginAttemptService.evictUserFromLoginAttemptCache(profile);
        return authentication;
    }

    private Authentication handleMFALogin(String username, String password, Profile profile) throws Exception {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(username, password));
        projectService.sendVerificationCode(profile);
        return authentication;
    }

    // User Session Termination
    @PostMapping("/menu/terminateSession")
    public ResponseEntity<HttpResponse> terminateSession(@RequestBody ProfileDTO profileDTO) throws EmailNotFoundException {
        projectService.terminateSession(profileDTO);
        return buildResponse(HttpStatus.OK);
    }

    // User Registration
    @PostMapping("/login/register")
    public ResponseEntity<HttpResponse> registerUser(@ModelAttribute @Valid ProfileDTO profileDTO,
                                                     @RequestParam(value = "photo", required = false) MultipartFile photo)
            throws Exception, NotAnImageFileException {
        projectService.signUpProfile(profileDTO, photo);
        return buildResponse(HttpStatus.OK, NEW_USER_REGISTERED);
    }

    // Retrieve User Data
    @GetMapping("/menu/{username}")
    public ResponseEntity<Profile> getUserProfile(@PathVariable String username) throws Exception {
        Profile profile = projectService.findUserByUsername(username);
        decryptUserSensitiveData(profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    private void decryptUserSensitiveData(Profile profile) throws Exception {
        profile.setPhone(decryptString(profile.getPhone(), profile.getUserId()));
        profile.setAddress(decryptString(profile.getAddress(), profile.getUserId()));
    }

    // MFA Authentication
    @PostMapping("/login/MFA/{username}")
    public ResponseEntity<HttpResponse> confirmMfaToken(@RequestBody String mfaCode)
            throws NotFoundException, SMSAlreadyConfirmedException, SMSExpiredException, SMSNotFoundException {
        projectService.confirmMfaCode(mfaCode);
        return buildResponse(HttpStatus.OK);
    }

    @GetMapping("/login/MFA/{username}/resend")
    public ResponseEntity<HttpResponse> resendMfaToken(@PathVariable String username) throws Exception {
        Profile profile = profileRepository.findUserByUsername(username);
        projectService.sendVerificationCode(profile);
        return buildResponse(HttpStatus.OK, SMS_RESENT);
    }

    // Account Activation
    @GetMapping("/login/confirmRegistration/{token}")
    public ResponseEntity<HttpResponse> confirmRegistrationToken(@PathVariable String token)
            throws EmailNotFoundException, TokenExpiredException, AlreadyConfirmedTokenException, NotFoundException {
        projectService.confirmRegistrationToken(token);
        return buildResponse(HttpStatus.OK, REGISTRATION_CONFIRMED);
    }

    // Password Reset
    @PostMapping("/login/resetPassword")
    public ResponseEntity<HttpResponse> initiatePasswordReset(@RequestBody ProfileDTO profileDTO) throws Exception {
        projectService.resetPasswordStepOne(profileDTO);
        return buildResponse(HttpStatus.OK, PASSWORD_RESET_INITIATED);
    }

    @PutMapping("/login/resetPassword/{token}/{username}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String token,
                                                      @PathVariable String username,
                                                      @RequestBody String newPassword)
            throws Exception {
        projectService.resetPasswordStepTwo(username, newPassword, token);
        return buildResponse(HttpStatus.OK, PASSWORD_RESET_SUCCESS);
    }

    // Simplified Response Builder
    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status) {
        return buildResponse(status, status.getReasonPhrase());
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status, String message) {
        HttpResponse responseBody = new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message);
        return new ResponseEntity<>(responseBody, status);
    }

    // JWT Header Creation
    private HttpHeaders createJwtHeader(ProfilePrincipal profilePrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(profilePrincipal));
        return headers;
    }
}