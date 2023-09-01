package com.example.desporto24.controller;
import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.changePassword.UserChangePasswordService;
import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.ResourceNotFoundException;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.login.LoginRequest;
import com.example.desporto24.login.LoginService;
import com.example.desporto24.model.*;
import com.example.desporto24.newIdea.NewIdeaRequest;
import com.example.desporto24.newIdea.NewIdeaService;
import com.example.desporto24.registo.SessaoRegistoRequest;
import com.example.desporto24.registo.SessaoRegistoService;
import com.example.desporto24.repository.IdeiasRepository;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.ProjectService;
import com.example.desporto24.registo.UserRegistoRequest;
import com.example.desporto24.registo.UserRegistoService;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.update.UserUpdateRequest;
import com.example.desporto24.update.UserUpdateService;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ={"/","/api/auth"})
public class perfilController extends ExceptionHandling {
    private final PerfilRepository perfilRepository;
    private final NewIdeaService ideiasService;
    private final IdeiasRepository ideiasRepository;
    private final UserRegistoService registoService;
    private final AuthenticationManager authenticationManager;
    private final ProjectService perfilService;
    private final UserUpdateService updateService;
    private final UserChangePasswordService changePasswordService;
    private final JWTokenProvider jwTokenProvider;
    private final SessaoRegistoService sessaoRegistoService;
    private final LoginService loginService;


    // Autenticação de utilizador
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws UserNotFoundException, UsernameExistException, EmailNotVerifiedException, EmailExistException, MessagingException, PhoneExistException, IOException, NotAnImageFileException, AccountDisabledException {
        autenticate(loginRequest.getUsername(), loginRequest.getPassword());
        Perfil loginUser = loginService.login(loginRequest);
        PerfilPrincipal perfilPrincipal = new PerfilPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(perfilPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }


    // Registo de utilizador
    @PostMapping("/login/registerNewUser")
    public ResponseEntity<?> registerUser(@ModelAttribute @Valid UserRegistoRequest signUpRequest, @RequestParam(value = "foto", required = false) MultipartFile foto) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException {
        Perfil registerperfil = registoService.register(signUpRequest, foto);
        return new ResponseEntity<>(registerperfil, OK);
    }

    // MFA autenticação
    @GetMapping(path = "/login/{code}")
    public String confirmCode(@PathVariable("code") String code) {
        return perfilService.confirmCode(code);
    }


    // Ativação da conta do utilizador
    @PutMapping(path = "/login/registerNewUser/confirmTokenRegistration")
    public ResponseEntity<?> confirmRegistrationToken(@RequestBody UserRegistoRequest request) throws EmailExistException, MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException {
        Perfil registerperfil2 = registoService.register2(request);
        return new ResponseEntity<>(registerperfil2, OK);
    }

    // Desativação da conta do utilizador
    @PutMapping(path = "/confirmEmergencyToken")
    public ResponseEntity<?> confirmRegistrationToken(@RequestParam String username) throws EmailExistException, MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, NotAImageFileException {
        Perfil updateUser = perfilService.updatePerfilEmergency(username);
        return new ResponseEntity<>(updateUser, OK);
    }

    // Definição de nova password por email, este passo recebe o email
    @PostMapping("/login/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException, EqualUsernameAndPasswordException, EmailNotVerifiedException {
        Perfil alterarPassword = changePasswordService.alterarPasswordPorTokenStep1(userChangePasswordRequest);
        return new ResponseEntity<>(alterarPassword, OK);
    }

    // Definição de nova password por email, este passo recebe o username e nova password
    @PutMapping("/resetPassword/newPassword")
    public ResponseEntity<?> resetPassword2(@RequestBody UserChangePasswordRequest userChangePasswordRequest) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException, EqualUsernameAndPasswordException {
        Perfil alterarPassword = changePasswordService.alterarPasswordPorTokenStep2(userChangePasswordRequest);
        return new ResponseEntity<>(alterarPassword, OK);
    }

    // Obtenção de todos os utilizadores registados na aplicação
    @GetMapping("/menu/perfis")
    public ResponseEntity<List<Perfil>> getAllPerfis() {
        List<Perfil> perfil = perfilService.getPerfis();
        return new ResponseEntity<>(perfil, OK);
    }

    // Criação de nova sessão pelo utilizador
    @PostMapping("/menu/createEvent")
    public ResponseEntity<?> createSessao(@ModelAttribute @Valid SessaoRegistoRequest request, MultipartFile foto) throws SessionExistException, MessagingException, EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException {
        Sessao sessao = sessaoRegistoService.createSessao(request,foto);
        return new ResponseEntity<>(sessao,OK);
    }

    // Obtenção de todas as sessões existentes de momento
    @GetMapping("/menu")
    public ResponseEntity<List<Sessao>> getAllSessoes(){
        List<Sessao> sessoes = perfilService.getSessoes();
        return new ResponseEntity<>(sessoes,OK);
    }

    // Alteração de dados do utilizador
    @PutMapping("/menu/alterardados")
    public ResponseEntity<?> updateUser(@RequestParam String username,@ModelAttribute @Valid UserUpdateRequest updateRequest,@RequestParam(required = false) MultipartFile foto) throws EmailExistException, MessagingException, PhoneExistException, UsernameExistException, IOException, NotAImageFileException, NotAnImageFileException {
        Perfil updatePerfil = updateService.update(username,updateRequest, foto);
        return new ResponseEntity<>(updatePerfil, OK);
    }

    // Recebe novas sugestões/ideias de qualquer utilizador
    @PostMapping("/contact")
    public ResponseEntity<?> registerNewIdea(@RequestBody NewIdeaRequest ideias) throws MessagingException {
        Ideias registerIdea = ideiasService.registerNewIdea(ideias);
        return new ResponseEntity<>(registerIdea, OK);
    }

    // Alteração da password do utilizador ou do nome do utilizador ou ambos
    @PutMapping("/menu/alterarPassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody UserChangePasswordRequest changeRequest) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, EqualUsernameAndPasswordException, jakarta.mail.MessagingException {
        Perfil changePassword = changePasswordService.alterarPassword(changeRequest);
        return new ResponseEntity<>(changePassword, OK);
    }

    // Eliminação do utilizador sendo administrador
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteId(@PathVariable("id") long id){
        perfilService.deleteUser(id);
        return response(OK, "User deleted successfully");
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

    // Autenticação do utilizador
    private void autenticate(String username, String password) {
        authenticationManager.authenticate(unauthenticated(username,password));
    }
}

