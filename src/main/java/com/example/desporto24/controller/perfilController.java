package com.example.desporto24.controller;
import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.changePassword.UserChangePasswordService;
import com.example.desporto24.domain.HttpResponse;
import com.example.desporto24.exception.ResourceNotFoundException;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.login.LoginRequest;
import com.example.desporto24.login.LoginService;
import com.example.desporto24.model.*;
import com.example.desporto24.registo.SessaoRegistoRequest;
import com.example.desporto24.registo.SessaoRegistoService;
import com.example.desporto24.repository.IdeiasRepository;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.ideiasService;
import com.example.desporto24.service.ProjectService;
import com.example.desporto24.registo.UserRegistoRequest;
import com.example.desporto24.registo.UserRegistoService;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.update.UserUpdateService;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
@AllArgsConstructor
@RequestMapping(path ={"/","/api/auth"})
public class perfilController extends ExceptionHandling {
    @Autowired
    private final PerfilRepository perfilRepository;
    @Autowired
    private final ideiasService ideiasService;
    @Autowired
    private final IdeiasRepository ideiasRepository;
    @Autowired
    private final UserRegistoService registoService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    private final ProjectService perfilService;

    private final UserUpdateService updateService;

    private final UserChangePasswordService changePasswordService;

    private final JWTokenProvider jwTokenProvider;

    private final SessaoRegistoService sessaoRegistoService;

    private final LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws UserNotFoundException, UsernameExistException, EmailNotVerifiedException, EmailExistException, MessagingException, PhoneExistException, IOException, NotAnImageFileException, AccountDisabledException {
        autenticate(loginRequest.getUsername(), loginRequest.getPassword());
        Perfil loginUser = loginService.login(loginRequest);
        PerfilPrincipal perfilPrincipal = new PerfilPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(perfilPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/login/registerNewUser")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistoRequest signUpRequest) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException, jakarta.mail.MessagingException {
        Perfil registerperfil = registoService.register(signUpRequest);
        return new ResponseEntity<>(registerperfil, OK);
    }

    /*
    @PostMapping("/menu/createNewEvent")
    public ResponseEntity<?> registerSession(@RequestBody SessaoRegistoRequest sessaoRegistoRequest) throws SessionExistException, jakarta.mail.MessagingException {
        String registerSessao = sessaoRegistoService.createSessao(sessaoRegistoRequest);
        return new ResponseEntity<>(registerSessao, OK);
    }
     */

    @GetMapping(path = "/login/{code}")
    public String confirmCode(@PathVariable ("code") String code){
        return perfilService.confirmCode(code);
    }

    @GetMapping(path = "/login/registerNewUser/{token}")
    public String confirmRegistrationToken(@PathVariable ("token") String token){
        return registoService.confirmToken(token);
    }

    @GetMapping(path = "/tokenEmergency")
    public String confirmEmergencyToken(@PathVariable ("token") String token){
        return registoService.confirmEmergencyToken(token);
    }

    @GetMapping("/menu/alterardados/id/{id}")
    public ResponseEntity<Perfil> getUserById(@PathVariable Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empregado com o seguinte id: " + id + ", não existe."));
        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/menu/allUsers")
    public ResponseEntity<List<Perfil>> getAllUsers(){
        List<Perfil> perfil = perfilService.getPerfis();
        return new ResponseEntity<>(perfil,OK);
    }

    /*@PutMapping("/menu/alterardados")
    public ResponseEntity<Perfil> updateUser(@RequestBody UserUpdateRequest updateRequest) throws EmailExistException, MessagingException, PhoneExistException, UsernameExistException, IOException, NotAImageFileException {
        Perfil updatePerfil = updateService.update(updateRequest);
        return new ResponseEntity<>(updatePerfil, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Perfil> getUser(@PathVariable String username){
        Perfil getPerfil = perfilService.findUserByUsername(username);
        return new ResponseEntity<>(getPerfil,OK);
    }

    /*@PostMapping("/menu/alterardados")
    public ResponseEntity<Perfil> updateImagemPerfil(@RequestParam("username") String perfil, @RequestParam(value = "profileImage") MultipartFile foto) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException, PhoneExistException {
        Perfil user = perfilService.updateUserFoto(perfil, foto);
        return new ResponseEntity<>(user, OK);
    }*/

    @PutMapping("/menu/alterarPassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody UserChangePasswordRequest changeRequest) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, EqualUsernameAndPasswordException, jakarta.mail.MessagingException {
        String changePassword = changePasswordService.alterarPassword(changeRequest);
        return new ResponseEntity<>(changePassword, OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteId(@PathVariable("id") long id){
        perfilService.deleteUser(id);
        return response(OK, "User deleted successfully");
    }
/*
    @PostMapping("/updateProfileImage")
    public ResponseEntity<?> updateProfileImage(@RequestParam("username") String username,@RequestParam("foto")MultipartFile foto) throws EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAImageFileException {
        Perfil perfil = perfilService.updateImage(username,foto);
        return new ResponseEntity<>(perfil,OK);
    }
 */

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message);
        return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping(path = "/image/{username}/{fileName}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getProfileImage(@PathVariable("username")String username,@PathVariable("fileName")String foto) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + foto));
    }

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

    @GetMapping(path = "/image/{session}/{fileName}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getSessionImage(@PathVariable("session")String session,@PathVariable("fileName")String foto) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + session + FORWARD_SLASH + foto));
    }

    @GetMapping(path = "/image/session/{username}",produces = {IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE})
    public byte[] getTempSessionImage(@PathVariable("session")String username) throws IOException {
        URL url = new URL(TEMP_SESSION_IMAGE_BASE_URL + username);
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


    @PostMapping("/contacts")
    public ResponseEntity<Ideias> saveIdeia(@RequestBody Ideias ideias) {
        Ideias i = ideiasService.salvarIdeia(ideias);
        return new ResponseEntity<>(i,OK);
    }

    private HttpHeaders getJwtHeader(PerfilPrincipal perfilPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwTokenProvider.generateJwtToken(perfilPrincipal));
        return headers;
    }

    private void autenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }
}

