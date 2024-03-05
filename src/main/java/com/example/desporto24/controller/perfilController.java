package com.example.desporto24.controller;
import com.example.desporto24.Chat.ChatRequest;
import com.example.desporto24.Chat.ChatService;
import com.example.desporto24.Friend.SendFriendRequest;
import com.example.desporto24.Friend.SendFriendService;
import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.changePassword.UserChangePasswordService;
import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.login.LoginRequest;
import com.example.desporto24.login.LoginService;
import com.example.desporto24.model.*;
import com.example.desporto24.newIdea.NewIdeaRequest;
import com.example.desporto24.newIdea.NewIdeaService;
import com.example.desporto24.registo.Notifications.Notifications;
import com.example.desporto24.registo.SessaoRegistoRequest;
import com.example.desporto24.registo.SessaoRegistoService;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.IdeiasRepository;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.ProjectService;
import com.example.desporto24.registo.UserRegistoRequest;
import com.example.desporto24.registo.UserRegistoService;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.update.UserUpdateRequest;
import com.example.desporto24.update.UserUpdateService;
import com.example.desporto24.utility.JWTokenProvider;
import com.twilio.exception.ApiException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.utility.PerfilUtils.getLoggedInUser;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ={"/","/api/auth"})
public class perfilController extends ExceptionHandling {
    private final PerfilRepository perfilRepository;
    private final NewIdeaService ideiasService;
    private final UserRegistoService registoService;
    private final AuthenticationManager authenticationManager;
    private final ProjectService perfilService;
    private final UserUpdateService updateService;
    private final UserChangePasswordService changePasswordService;
    private final JWTokenProvider jwTokenProvider;
    private final SessaoRegistoService sessaoRegistoService;
    private final LoginAttemptService loginAttemptService;
    private final SendFriendService sendFriendService;
    private final ChatService chatService;
    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);


    // Autenticação de utilizador
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws UserNotFoundException, UsernameExistException, EmailNotVerifiedException, EmailExistException, MessagingException, PhoneExistException, IOException, NotAnImageFileException, AccountDisabledException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
            Perfil p = authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            PerfilPrincipal perfilPrincipal = new PerfilPrincipal(p);
            HttpHeaders jwtHeader = getJwtHeader(perfilPrincipal);
            return new ResponseEntity<>(p, jwtHeader, OK);
            }

    private Perfil authenticate(String username, String password) throws EmailNotVerifiedException, AccountDisabledException, UserNotFoundException {
        try {
            Perfil p = perfilRepository.findUserByUsername(username);
            if (null == p) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
            } else {
                loginAttemptService.hasExceededMaxAttempts(p);
                loginAttemptService.addUserToLoginAttemptCache(p);
                Authentication authentication = authenticationManager.authenticate(unauthenticated(username, password));
                Perfil logged = getLoggedInUser(authentication);
                if (!logged.getMFA()) {
                    loginAttemptService.evictUserFromLoginAttemptCache(p);
                } else {
                    perfilService.sendVerificationCode(p);
                    loginAttemptService.evictUserFromLoginAttemptCache(p);
                }
                return logged;
            }
            }catch(UserNotFoundException e){
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
    }

    @PostMapping("menu/terminarSessao")
    public ResponseEntity<?> terminarSessao(@RequestBody Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, MessagingException, PhoneExistException, UsernameExistException, EmailNotVerifiedException, EmailNotFoundException {
            perfilService.terminarSessao(perfil);
            return new ResponseEntity<>(OK);
    }

    // Registo de utilizador
    @PostMapping("/login/registerNewUser")
    public ResponseEntity<?> registerUser(@ModelAttribute @Valid UserRegistoRequest signUpRequest, @RequestParam(value = "foto", required = false) MultipartFile foto) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException, UserNotFoundException, EmailNotFoundException {
        registoService.register(signUpRequest, foto);
        return response(OK,NEW_USER_REGISTER);
    }

    // Obter todos os dados do utilizador
    @GetMapping("/menu/{username}")
    public ResponseEntity<?> menu(@PathVariable("username")String username){
            Perfil a = perfilService.findUserByUsername(username);
            return new ResponseEntity<>(a, OK);
    }

    // MFA autenticação
    @PostMapping(path = "/login/MFAauthentication/{username}")
    public ResponseEntity<?> confirmMFAToken(@RequestBody String mfaCode) throws EmailExistException, MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, NotAImageFileException, NotFoundException, SMSAlreadyConfirmedException, SMSExpiredException, SMSNotFoundException {
        perfilService.confirmCode(mfaCode);
        return new ResponseEntity<>(OK);
    }

    //Reenvio do SMS para o utilizador
    @GetMapping(path = "/login/MFAauthentication/{username}")
    public ResponseEntity<?> resendToken(@PathVariable("username")String username) throws EmailExistException, MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, NotAImageFileException {
        Perfil perfil = perfilRepository.findUserByUsername(username);
        perfilService.sendVerificationCode(perfil);
        return response(OK,SMS_MFA);
    }


    // Ativação da conta do utilizador
    @GetMapping(path = "/login/registerNewUser/confirmTokenRegistration/{token}")
    public ResponseEntity<?> confirmRegistrationToken(@PathVariable("token") String Token) throws EmailNotFoundException, TokenExpiredException, AlreadyConfirmedTokenException, NotFoundException {
        perfilService.confirmToken(Token);
        return response(OK,CONFIRMED_TOKEN);
        }

    // Desativação da conta do utilizador e envio de novo mail para efetuar reset à password
    @GetMapping(path = "/confirmEmergencyToken/{token}/{username}")
    public ResponseEntity<?> confirmEmergencyToken(@PathVariable("token")String token,@PathVariable("username")String username) throws MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, NotAImageFileException, EmailNotFoundException, TokenExpiredException, AlreadyConfirmedTokenException, NotFoundException {
        perfilService.confirmEmergencyToken(token,username);
        return new ResponseEntity<>(OK);
    }

    // Definição de nova password por email, este passo recebe o email
    @PostMapping("/login/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest) throws EmailNotFoundException, EmailSendingException {
         changePasswordService.alterarPasswordPorTokenStep1(userChangePasswordRequest);
        return response(OK,FORGET_PASSWORD);
    }

    // Definição de nova password por email, este passo recebe o username e nova password
    @PutMapping("/login/resetPassword/{token}/{username}")
    public ResponseEntity<?> resetPassword2(@PathVariable("token") String token,@PathVariable("username") String username, @RequestBody UserChangePasswordRequest userChangePasswordRequest) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException, EqualUsernameAndPasswordException, EmailNotFoundException, UserNotFoundException, TokenExpiredException, AlreadyConfirmedTokenException, NotFoundException {
        userChangePasswordRequest.setUsername(username);
        changePasswordService.alterarPasswordPorTokenStep2(userChangePasswordRequest,token);
        return new ResponseEntity<>(OK);
    }

    @PutMapping("/confirmEmergencyToken/resetPassword/{token}/{email}")
    public ResponseEntity<?> emergencyTokenResetPassword(@PathVariable("token") String token,@PathVariable("email") String email,@RequestBody String password) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException, EqualUsernameAndPasswordException, EmailNotFoundException {
        perfilService.EmergencyResetPassword(token,email,password);
        return response(OK,FORGET_PASSWORD2);
    }

    // Obtenção de todos os utilizadores registados na aplicação
    @GetMapping("/menu/perfis/{perfil}")
    public ResponseEntity<List<Perfil>> pesquisaPerfil(@PathVariable("perfil")String username) {
        List<Perfil> perfil = perfilService.procurarPerfil(username);
        return new ResponseEntity<>(perfil, OK);
    }


    // Criação de nova sessão pelo utilizador
    @PostMapping("/menu/createEvent")
    public ResponseEntity<?> createSessao(@ModelAttribute @Valid SessaoRegistoRequest request, MultipartFile foto) throws SessionExistException, MessagingException, EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException {
        sessaoRegistoService.createSessao(request,foto);
        return response(OK,NEW_SESSION);
    }

    // Obtenção de todas as sessões existentes de momento
    @GetMapping("/menu")
    public ResponseEntity<List<Sessao>> getAllSessoes(){
        List<Sessao> sessoes = perfilService.getSessoes();
        return new ResponseEntity<>(sessoes,OK);
    }

    @GetMapping("/menu/friendList/chat/{senderId}/{recipientId}")
    public ResponseEntity<List<Chat>> getChat(@PathVariable("senderId") String sender,@PathVariable("recipientId") String recipient){
        List<Chat> chat = perfilService.findChatMessages(sender,recipient);
        return new ResponseEntity<>(chat,OK);
    }
    @PostMapping("/menu/friendList/chat/{senderId}/{recipientId}")
    public ResponseEntity<?> sendChatMessage(@RequestBody ChatRequest chatRequest) throws Exception {
        Chat c = chatService.sendMsg(chatRequest);
            return new ResponseEntity<>(c,OK);
    }
    // Obtenção de lista de amigos do utilizador
    @GetMapping("/menu/friendList/{username}")
    public ResponseEntity<List<Perfil>> getFriendList(@PathVariable("username")String username){
        List<Perfil> perfis = perfilService.getFriends(username);
        return new ResponseEntity<>(perfis,OK);
    }

    @GetMapping("/menu/notifications/{username}")
    public ResponseEntity<List<Notifications>> getNotifications(@PathVariable("username")String username){
        List<Notifications> notifications = perfilService.getNotificationsFromPerfil(username);
        return new ResponseEntity<>(notifications,OK);
    }

    // Envio de pedido de amizade para um novo utilizador
    @PostMapping("/menu/perfis")
    public ResponseEntity<?> addFriend(@ModelAttribute SendFriendRequest friendRequestR) throws RequestFriendException, MessagingException {
        sendFriendService.sendFriendRequest(friendRequestR);
            return response(OK,FRIEND_REQUEST);
    }

    // Alteração de dados do utilizador
    @PutMapping("/menu/alterardados")
    public ResponseEntity<?> updateUser(@RequestParam String username,@ModelAttribute @Valid UserUpdateRequest updateRequest,@RequestParam(required = false) MultipartFile foto) throws EmailExistException, MessagingException, PhoneExistException, UsernameExistException, IOException, NotAImageFileException, NotAnImageFileException, UserNotFoundException, EmailNotFoundException {
        updateService.update(username,updateRequest, foto);
        return response(OK,UPDATED_PERFIL);
    }

    // Recebe novas sugestões/ideias de qualquer utilizador
    @PostMapping("/contact")
    public ResponseEntity<?> registerNewIdea(@RequestBody NewIdeaRequest ideias) throws MessagingException {
        ideiasService.registerNewIdea(ideias);
        return response(OK,NEW_IDEA);
    }

    // Alteração da password do utilizador ou do nome do utilizador ou ambos
    @PutMapping("/menu/alterarPassword")
    public ResponseEntity<?> updateUserPassword(@ModelAttribute @Valid UserChangePasswordRequest changeRequest) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, EqualUsernameAndPasswordException, jakarta.mail.MessagingException, UserNotFoundException, EmailNotFoundException {
        changePasswordService.alterarPassword(changeRequest);
        return response(OK,UPDATED_PERFIL);
    }

    @PostMapping("/menu/notifications/{id}")
    public ResponseEntity<?> blockAcc(@RequestBody String username) throws MessagingException, EmailNotFoundException {
        perfilService.disablePerfilByBlock(username);
        return response(OK,DISABLED_PERFIL);
    }

    @DeleteMapping("/menu/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") long id){
            perfilService.deleteNotification(id);
        return response(OK,DELETED_NOTIFICATION);
    }
    // Confirmação de adição de novo utilizador à lista de amigos
    @GetMapping("/menu/notifications/{id}/{token}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable("id") Long id,@PathVariable("token") String token){
            perfilService.acceptFriendRequest(id,token);
        return response(OK,ACCEPTED_FRIEND_REQUEST);
    }

    // Eliminação do utilizador sendo administrador
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteId(@PathVariable("id") long id){
        perfilService.deleteUser(id);
        return new ResponseEntity<>(OK);
    }

    // Personalização de resposta ao utilizador na tentativa de execução de uma ação
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message);
        return new ResponseEntity<>(body, httpStatus);
    }

    // Obtenção da foto de perfil do utilizado que tenha fotografia existente
    @GetMapping(path = "/user/{username}/{fileName}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getProfileImage(@PathVariable("username")String username,@PathVariable("fileName")String foto) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + foto));
    }

    // Obtenção da foto de perfil do utilizador que não tenha fotografia existente(foto default)
    @GetMapping(path = "/image/profile/{username}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getTempProfileImage(@PathVariable("username")String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InputStream inputStream = url.openStream()) {
            int bytesRed;
            byte [] chunk = new byte[1024];
            while ((bytesRed = inputStream.read(chunk)) > 0){
                byteArrayOutputStream.write(chunk, 0, bytesRed);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    //Obtenção de foto da localização da sessão criada
    @GetMapping(path = "/image/{session}/{fileName}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getSessionImage(@PathVariable("session")String session,@PathVariable("fileName")String foto) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + session + FORWARD_SLASH + foto));
    }

    // Atribuição de token ao utilizador quando autenticado
    private HttpHeaders getJwtHeader(PerfilPrincipal perfilPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwTokenProvider.generateJwtToken(perfilPrincipal));
        return headers;
    }
}

