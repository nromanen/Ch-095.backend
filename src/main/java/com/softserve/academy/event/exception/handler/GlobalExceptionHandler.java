package com.softserve.academy.event.exception.handler;

import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.exception.UserNotFountException;
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
        return new ResponseEntity<>(e.getMessage() + "\n" + description, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFountException.class)
    public ResponseEntity<Object> userNotFoundHandler(Exception e, WebRequest request) {
        return defaultHandler(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Object> emailExistHandler(Exception e, WebRequest request) {
        return defaultHandler(e, request, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> defaultHandler(Exception e, WebRequest request, HttpStatus status) {
        String description = request.getDescription(false);
        log.error(e.getMessage());
        log.error("WebRequest description  : " + description);
        return new ResponseEntity<>(e.getMessage(), status);
    }

}
