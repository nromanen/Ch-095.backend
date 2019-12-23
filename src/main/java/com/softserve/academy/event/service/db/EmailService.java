package com.softserve.academy.event.service.db;

public interface EmailService {

    void sendMail(String recipientAddress, String subject, String message);

    void sendMailWithLink(String userEmail, String recipientAddress, String subject, String message);

    boolean isValid(String email);

}
