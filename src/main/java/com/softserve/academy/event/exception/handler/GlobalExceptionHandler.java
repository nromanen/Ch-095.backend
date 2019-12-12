package com.softserve.academy.event.exception.handler;

import com.softserve.academy.event.exception.UserNotFountException;
import com.softserve.academy.event.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ServerResponse<?> exceptionHandler(Exception e, WebRequest request) {
        String description = request.getDescription(false);
        log.error("Not default exception : " + e.getMessage());
        log.error("WebRequest description  : " + description);
        return ServerResponse.from(e.getMessage(), HttpStatus.CONFLICT, description);
    }

    @ExceptionHandler(UserNotFountException.class)
    public ServerResponse<?> userNotFoundHandler(Exception e, WebRequest request) {
        return defaultHandler(e, request, HttpStatus.NOT_FOUND);
    }

    private ServerResponse<?> defaultHandler(Exception e, WebRequest request, HttpStatus status) {
        String description = request.getDescription(false);
        log.error(e.getMessage());
        log.error("WebRequest description  : " + description);
        return ServerResponse.from(e.getMessage(), status, description);
    }

}
