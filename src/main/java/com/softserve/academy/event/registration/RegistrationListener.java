//package com.softserve.academy.event.registration;
//
//import com.softserve.academy.event.entity.User;
//import com.softserve.academy.event.service.db.EmailService;
////import com.softserve.academy.event.service.UserService;
//import com.softserve.academy.event.service.db.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class RegistrationListener  {
//
//    private final UserService userService;
//
//    private final EmailService emailService;
//
//    @Autowired
//    public RegistrationListener(UserService userService, EmailService emailService) {
//        this.userService = userService;
//        this.emailService = emailService;
//    }
//
//    @EventListener
//    public void handleEvent(RegistrationCompleteEvent event) {
//         confirmRegistration(event);
//}
//
//    private void confirmRegistration(RegistrationCompleteEvent event) {
//        User user = event.getUser();
//        String token = UUID.randomUUID().toString();
//        userService.createVerificationToken(user, token);
//
//        String userEmailAddress = user.getEmail();
//        String subject = "Registration Confirmation";
//        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
//        String message = "Thank you for registering. Please click on the below link to activate your account.";
//        emailService.sendMail(userEmailAddress,subject,message + confirmationUrl);
//    }
//}
