package com.softserve.academy.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.softserve.academy.event.service" })
@PropertySource("classpath:social.properties")
@EnableGlobalMethodSecurity(securedEnabled = true)
//@Order(1)
public class SocialConfig extends WebSecurityConfigurerAdapter {
    public SocialConfig(Environment env) {
        this.env = env;
    }

//    private final Environment env;
//
//    private static List<String> clients = Arrays.asList("google", "facebook");
//
//    @Autowired
//    public SocialConfig(Environment env) {
//        this.env = env;
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/oauth_login")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .oauth2Login()
//                .loginPage("/oauth_login");
//    }
//
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        List<ClientRegistration> registrations = clients.stream()
//                .map(this::getRegistration)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new InMemoryClientRegistrationRepository(registrations);
//    }
//
//    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
//
//    private ClientRegistration getRegistration(String client) {
//        String clientId = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-id");
//
//        if (clientId == null) {
//            return null;
//        }
//
//        String clientSecret = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-secret");
//
//        if (client.equals("google")) {
//            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
//                    .clientId(clientId).clientSecret(clientSecret).build();
//        }
//        if (client.equals("facebook")) {
//            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
//                    .clientId(clientId).clientSecret(clientSecret).build();
//        }
//        return null;
//    }
//
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth_login", "/loginFailure")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth_login")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize-client")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .defaultSuccessUrl("/loginSuccess")
               .failureUrl("/loginFailure");
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        return accessTokenResponseClient;
    }


    // additional configuration for non-Spring Boot projects
    private static List<String> clients = Arrays.asList("facebook", "google");

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private final Environment env;

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return null;
    }
}
