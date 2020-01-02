package com.softserve.academy.event.exception.handler;

import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UnauthorizedException;
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

    @ExceptionHandler(SurveyNotFound.class)
    public ResponseEntity<Object> surveyNotFoundHandler(Exception e, WebRequest request) {
        return handler(e,request,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorizedHandler(Exception e, WebRequest request) {
        return handler(e,request,HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> handler(Exception e, WebRequest request, HttpStatus status) {
        log.error(e.getMessage());
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), status);
    }

}
