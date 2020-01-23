package com.softserve.academy.event.exception;

public class SurveyAlreadyPassedException extends RuntimeException {
    public SurveyAlreadyPassedException(String message) {
        super(message);
    }
}
