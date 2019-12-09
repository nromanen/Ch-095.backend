package com.softserve.academy.event.registration;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {
   @Autowired
   private UserService userService;

   @Autowired
    private MessageSource messageSource;

   @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String userEmailAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Thank you for registering. Please click on the below link to activate your account." ;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmailAddress);
        email.setSubject(subject);
        email.setText(message + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
