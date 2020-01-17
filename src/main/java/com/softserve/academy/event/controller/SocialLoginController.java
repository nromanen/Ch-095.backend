package com.softserve.academy.event.controller;

import com.softserve.academy.event.service.db.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/")
@PropertySource("classpath:application.properties")
@Slf4j
public class SocialLoginController {

    private final Map<String, String> oAuth2ClientRegistrations;

    @Value("${app.frontend.url}")
    private String frontUrl;

    private final UserService userService;

    @Autowired
    public SocialLoginController(Map<String, String> oAuth2ClientRegistrations, UserService userService) {
        this.oAuth2ClientRegistrations = oAuth2ClientRegistrations;
        this.userService = userService;
    }

    @GetMapping("/oauth_login")
    public ResponseEntity<Map<String, String>> getLoginPage() {
        return ResponseEntity.ok(oAuth2ClientRegistrations);
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication, HttpServletResponse httpServletResponse) {

        userService.newSocialUser(authentication.getPrincipal());

        try {
            httpServletResponse.sendRedirect(frontUrl + "/login");
            return "login success";
        } catch (IOException e) {
            log.error("Redirect error", e);
            return "login failed";
        }
    }

    @GetMapping(value = "/authenticatedEmail")
    public String getAuthenticatedEmail() {
        return userService.getAuthenticatedUserEmail();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus) {
        HttpSession session = request.getSession(false);
        if (request.isRequestedSessionIdValid() && session != null) {
            session.invalidate();
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        SecurityContextHolder.getContext().setAuthentication(null);
        sessionStatus.setComplete();
        return "";
    }
}
