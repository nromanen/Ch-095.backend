package com.softserve.academy.event.service;

public interface EmailService {

    void sendMail(String recipientAddress, String subject, String message);


}