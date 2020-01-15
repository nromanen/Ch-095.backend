package com.softserve.academy.event.exception;

public class DataAlreadyUsedException extends RuntimeException {

    public DataAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

}
