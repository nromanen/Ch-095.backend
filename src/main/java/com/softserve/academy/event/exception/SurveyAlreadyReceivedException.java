package com.softserve.academy.event.exception;

public class SurveyAlreadyReceivedException extends RuntimeException {
    public SurveyAlreadyReceivedException(String message) {
        super(message);
    }
}
