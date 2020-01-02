package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.registration.RegistrationCompleteEvent;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.mapper.UserMapper;
import com.softserve.academy.event.registration.RegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final ApplicationEventPublisher eventPublisher;

    private final EmailService emailService;


    @Autowired
    public LoginController(UserService userService, UserMapper userMapper, ApplicationEventPublisher eventPublisher, EmailService emailService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
        this.emailService = emailService;
    }

   @PostMapping(value = "/registration")
    public ResponseEntity registerUserAccount(@RequestBody UserDto accountDto, HttpServletRequest request) throws EmailExistException {
        UserDto registered = userMapper.userToDto(userService.newUserAccount(userMapper.userDtoToUser(accountDto)));;
        eventPublisher.publishEvent(new RegistrationCompleteEvent(userMapper.userDtoToUser(registered), request.getLocale(), getAppUrl(request)));
        return new ResponseEntity(HttpStatus.CREATED);
    }


    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity confirmRegistration( @RequestParam("token")String token, HttpServletRequest request)  {
        TokenValidation result = userService.validateVerificationToken(token);
        if (TokenValidation.TOKEN_VALID.equals(result)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/resendRegistrationToken")
    public ResponseEntity resendRegistrationToken( @RequestParam("token") String existingToken, HttpServletRequest request) {
       VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
       UserDto user = userMapper.userToDto(userService.getUser(newToken.getToken()));
       String subject = "Resend registration Confirmation";
       String confirmationUrl = getAppUrl(request) + "/registrationConfirm?token=" + newToken.getToken();
       String message = "Thank you for registering. Please click on the below link to activate your account.";
       emailService.sendMail(user.getEmail(), subject, message + confirmationUrl);
        return  new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/login")
    public ResponseEntity getLogin() {
        Long id = userService.getAuthenicationId().get();
        return new ResponseEntity(HttpStatus.OK);
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}
