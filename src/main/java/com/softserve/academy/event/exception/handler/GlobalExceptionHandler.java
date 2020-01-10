package com.softserve.academy.event.exception.handler;

import com.softserve.academy.event.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e, WebRequest request) {
        String description = request.getDescription(false);
        log.error("Not default exception : " + e.getMessage());
        log.error("WebRequest description  : " + description);
        return new ResponseEntity<>(e.getMessage() + "\n" + description, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Object> userNotFoundHandler(Exception e, WebRequest request) {
        return handler(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SurveyNotFound.class)
    public ResponseEntity<Object> surveyNotFoundHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Object> emailExistHandler(Exception e, WebRequest request) {
        return handler(e, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorizedHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IncorrectLinkException.class)
    public ResponseEntity<Object> incorrectLinkHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SurveyAlreadyPassedException.class)
    public ResponseEntity<Object> surveyAlreadyPassedHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }

    private ResponseEntity<Object> handler(Exception e, WebRequest request, HttpStatus status) {
        log.error(e.getMessage());
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), status);
    }

}
