package com.example.desporto24.exception.domain;
import com.example.desporto24.domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.persistence.NoResultException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandling {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected static final String ACCOUNT_LOCKED = "A tua conta foi bloqueada. Por favor contacta a administração pelo email: desporto24app@gmail.com";
    protected static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    protected static final String INTERNAL_SERVER_ERROR_MSG = "An error ocurred while processing the request";
    protected static final String INCORRECT_CREDENTIALS = "Nome de utilizador ou password incorretos, por favor tenta novamente.";
    protected static final String ACCOUNT_DISABLED = "A tua conta foi desativada. Se isto é um erro,por favor contacta a administração pelo email: desporto24app@gmail.com";
    protected static final String ERROR_PROCESSING_FILE = "Um erro ocorreu a processar o ficheiro";
    protected static final String NOT_ENOUGH_PERMISSION = "Não tens permissões suficientes.";
    protected static final String TOKEN_NOT_VERIFIED = "O teu email ainda não está verificado, por favor verifica o teu email";
    protected static final String ERROR_PATH = "/error";
    protected static final String ALREADY_FRIENDS = "Tu já tens este perfil na tua lista de amigos!";

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(){
        return createHttpResponse(BAD_REQUEST,ACCOUNT_DISABLED);
    }

    @ExceptionHandler(RequestFriendException.class)
    public ResponseEntity<HttpResponse> alreadyFriends(){
        return createHttpResponse(BAD_REQUEST,ALREADY_FRIENDS);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(){
        return createHttpResponse(BAD_REQUEST,INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(){
        return createHttpResponse(FORBIDDEN,NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(){
        return createHttpResponse(UNAUTHORIZED,ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(){
        return createHttpResponse(BAD_REQUEST, TOKEN_EXPIRED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> tokenNotFoundException(){
        return createHttpResponse(BAD_REQUEST, TOKEN_NOT_FOUND);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<HttpResponse> emailNotVerified(){
        return createHttpResponse(UNAUTHORIZED, TOKEN_NOT_VERIFIED);
    }

    @ExceptionHandler(AlreadyConfirmedTokenException.class)
    public ResponseEntity<HttpResponse> tokenNotFound(){
        return createHttpResponse(BAD_REQUEST,TOKEN_ALREADY_CONFIRMED);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception){
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception){
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception){
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception){
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException exception){
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception){
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception){
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception){
        LOGGER.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, ERROR_PROCESSING_FILE);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus,String message){
        return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase()),httpStatus);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404(NotFoundException exception){
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
}
