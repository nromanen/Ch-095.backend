package com.softserve.academy.event.exception;

public class ContactNotFound extends RuntimeException {

    public ContactNotFound() {
        super("No such survey");
    }

}
