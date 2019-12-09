package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.PasswordDto;
import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.registration.RegistrationCompleteEvent;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.UserService;
import com.softserve.academy.event.util.GenericResponse;
import com.softserve.academy.event.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class AppController {

    @Autowired
    UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

    //Todo setting emailServer
    @Autowired
    JavaMailSender  mailSender;

    @Autowired
    Environment env;

    @GetMapping(value = "/user/registration")
    public String registrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping(value = "/user/registration")
    @ResponseBody
    public GenericResponse registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
       //Todo need mapper
        User registered = null;
        try {
             registered = userService.newUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }
//        final User registered = userService.newUserAccount(accountDto);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
    return new GenericResponse("success");
    }


    @GetMapping("/registrationConfirm")
    public GenericResponse confirmRegistration(final HttpServletRequest request, final Model  model, @RequestParam("token")final String token) {
        Locale locale = request.getLocale();
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            //Todo mapper
            final User user = userService.getUser(token);
            user.setActive(true);
            userService.saveRegisteredUser(user);
            //model.addAttribute("message", messageSource.getMessage("message.accountVerified", null, locale));
            return new GenericResponse(messageSource.getMessage("message.accountVerified", null, locale));
        }

//        model.addAttribute("message", messageSource.getMessage("auth.message." + result, null, locale));
//        model.addAttribute("expired", "expired".equals(result));
//        model.addAttribute("token", token);
        return new GenericResponse(messageSource.getMessage("auth.message" + result, null, locale));
    }

    @GetMapping(value = "/user/resendRegistrationToken")
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token")final String existingToken) {
        VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        User user = userService.getUser(newToken.getToken());
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);
        mailSender.send(email);
        return new GenericResponse(messageSource.getMessage("message.resendToken", null, request.getLocale()));
    }

//    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
//        final User user = userService.findUserByEmail(userEmail);
//        if (user != null) {
//            final String token = UUID.randomUUID().toString();
//            userService.createPasswordResetTokenForUser(user, token);
//            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
//        }
//        return new GenericResponse(messageSource.getMessage("message.resetPasswordEmail", null, request.getLocale()));
//    }
//
//    @GetMapping(value = "/user/changePassword")
//    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
//        final String result = securityUserService.validatePasswordResetToken(id, token);
//        if (result != null) {
//            model.addAttribute("message", messageSource.getMessage("auth.message." + result, null, locale));
//            return "redirect:/login?lang=" + locale.getLanguage();
//        }
//        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
//    }
//
//    // change user password
//    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
//        final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
//        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
//            throw new InvalidOldPasswordException();
//        }
//        userService.changeUserPassword(user, passwordDto.getNewPassword());
//        return new GenericResponse(messageSource.getMessage("message.updatePasswordSuc", null, locale));
//    }


    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messageSource.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messageSource.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }
//TODO email setting
    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
