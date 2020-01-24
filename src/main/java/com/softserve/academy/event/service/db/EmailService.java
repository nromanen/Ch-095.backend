package com.softserve.academy.event.service.db;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(String recipientAddress, String subject, String message);

    void sendEmailForUser(Long idUser, String idSurvey, String[] anEmail);

    void mailHealthCheck() throws MessagingException;

}