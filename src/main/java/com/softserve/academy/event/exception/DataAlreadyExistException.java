package com.softserve.academy.event.exception;

public class DataAlreadyExistException extends RuntimeException {

    public DataAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
