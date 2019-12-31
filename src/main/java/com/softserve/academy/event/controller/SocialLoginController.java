package com.softserve.academy.event.controller;

import com.google.gson.Gson;
import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.entity.enums.OauthType;
import com.softserve.academy.event.entity.enums.Roles;
import com.softserve.academy.event.service.db.UserSocialService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class SocialLoginController {

    private final String SECRET_KEY = "fbb5140fc1be11e87b925eed98a97a343e68b445fbb5140fc1be11e87b925eed98a97a343e68b445";

    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    private final UserSocialService userSocialService;
    private final ClientRegistrationRepository clientRegistrationRepository;
//    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public SocialLoginController(ClientRegistrationRepository clientRegistrationRepository/*, OAuth2AuthorizedClientService authorizedClientService*/, UserSocialService userSocialService) {
        this.userSocialService = userSocialService;
        this.clientRegistrationRepository = clientRegistrationRepository;
//        this.authorizedClientService = authorizedClientService;
    }

    public String createToken(String body, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(body);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private String parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
//        Claims claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
//                .parseClaimsJws(jwt)
//                .getBody();
//        System.out.println("ID: " + claims.getId());
//        System.out.println("Subject: " + claims.getSubject());
//        System.out.println("Issuer: " + claims.getIssuer());
//        System.out.println("Expiration: " + claims.getExpiration());
//        return claims.getSubject();

        String[] split_string = jwt.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

//        System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
//        String header = new String(Base64.decode(base64EncodedHeader));
//        System.out.println("JWT Header : " + header);
//

        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(Base64.decode(base64EncodedBody)) + "}";
//        Jackson2JsonDecoder jackson2JsonDecoder = new Jackson2JsonDecoder();
        return new Gson().fromJson(body, Temp.class).sub;

//        return body;
    }

    private class Temp{
        public String sub;
        List<Roles> roles;
        public Long iat;
        public Long exp;
    }

    @GetMapping("/oauth_login")
    public ResponseEntity<Map<String, String>> getLoginPage() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        }

        return ResponseEntity.ok(oauth2AuthenticationUrls);
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication, HttpServletResponse httpServletResponse) {
        OAuth2User oAuth2User = authentication.getPrincipal();

        UserSocial userSocial = new UserSocial();
        userSocial.setType(OauthType.valueOf(authentication.getAuthorizedClientRegistrationId().toUpperCase()));
        userSocial.setNickname(oAuth2User.getAttribute("name"));
        userSocial.setEmail(oAuth2User.getAttribute("email"));
        switch (userSocial.getType()) {
            case FACEBOOK: {
                userSocial.setSocialId(oAuth2User.getAttribute("id"));
                break;
            }
            case GOOGLE: {
                userSocial.setSocialId(oAuth2User.getAttribute("sub"));
                break;
            }
        }
        try {
            userSocialService.save(userSocial);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            String token = createToken(userSocial.getEmail(), Collections.singletonList(Roles.USER.toString()));
            httpServletResponse.setHeader("userToken", token);
            httpServletResponse.addCookie(new Cookie("userToken", token));
            httpServletResponse.addCookie(new Cookie("user", userSocial.getEmail()));
            httpServletResponse.sendRedirect("http://localhost:4200/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "loginSuccess";
    }

    @GetMapping(value = "/test")
    public String test(@RequestHeader HttpHeaders httpHeaders, HttpServletRequest request) {

        Object something = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (something instanceof DefaultOidcUser) {
            return ((DefaultOidcUser)something).getEmail();                     // for google
        } else if (something instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User)something).getAttribute("email"); // for facebook
        }
        else return something.toString();
//        return "unauthorized";

//        try {
//            return "Parse" + parseJWT(httpHeaders.get("userToken").toString());
//        } catch (Exception e) {
//            return e.getClass().getName();
//        }
//        return Objects.requireNonNull(httpHeaders.get("userToken")).toString();
    }
}