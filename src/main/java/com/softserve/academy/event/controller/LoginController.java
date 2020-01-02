package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {

    private final UserService userService;

    private final UserMapper userMapper;

    final EmailService emailService;


    @Autowired
    public LoginController(UserService userService, UserMapper userMapper, EmailService emailService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

   @PostMapping(value = "/registration")
    public ResponseEntity<String> registerUserAccount(@RequestBody UserDto accountDto) {
       try {
           UserDto registered = userMapper.userToDto(userService.newUserAccount(userMapper.userDtoToUser(accountDto)));
           VerificationToken verificationToken = userService.createVerificationToken(userMapper.userDtoToUser(registered));
           emailConfirm(registered.getEmail(), verificationToken.getToken());
           return new ResponseEntity<>(HttpStatus.CREATED);
       }catch (EmailExistException e) {
           return new ResponseEntity(e, HttpStatus.NOT_ACCEPTABLE);
       }
    }


    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token")String token)  {
        TokenValidation result = userService.validateVerificationToken(token);
        if (TokenValidation.TOKEN_VALID.equals(result)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/resendRegistrationToken")
    public ResponseEntity resendRegistrationToken( @RequestParam("token") String existingToken) {
       VerificationToken newToken = userService.updateTokenExpiration(existingToken);
       UserDto userDto = userMapper.userToDto(newToken.getUser());
       emailConfirm(userDto.getEmail(), newToken.getToken());
        return  new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/login")
    public ResponseEntity getLogin() {
        Long id = userService.getAuthenticationId().get();
        return new ResponseEntity(HttpStatus.OK);
    }

    private void emailConfirm(String email, String token) {
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:4200/confirm?token=" + token;
        String message = "Thank you for registration. Please click on the below link to activate your account.";
        emailService.sendMail(email,subject,message + confirmationUrl);
    }
}
