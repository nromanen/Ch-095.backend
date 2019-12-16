package com.softserve.academy.event.registration;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.EmailService;
import com.softserve.academy.event.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class RegistrationListener  {
   @Autowired
   private UserService userService;

   @Autowired
   EmailService emailService;

    @EventListener
    public void handleEvent(RegistrationCompleteEvent event) {
         confirmRegistration(event);
}

    private void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String userEmailAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Thank you for registering. Please click on the below link to activate your account.";
        emailService.sendMail(userEmailAddress,subject,message + confirmationUrl);
    }
}
