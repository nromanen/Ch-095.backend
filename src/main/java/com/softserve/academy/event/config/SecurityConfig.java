package com.softserve.academy.event.config;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.entity.enums.OauthType;
import com.softserve.academy.event.entity.enums.Roles;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.MyAuthenticationEntryPoint;
import com.softserve.academy.event.service.MySavedRequestAwareAuthenticationSuccessHandler;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.db.UserSocialService;
import com.softserve.academy.event.service.db.impl.UserDetailsServiceImpl;
import com.spring.component.EmailOAuth2UserService;
import com.spring.entity.Profile;
import com.spring.enums.Role;
import com.spring.exception.RestException;
import com.spring.service.JwtService;
import com.spring.service.ProfileService;
import com.spring.service.implementations.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MY IMPORTS
 **/


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.softserve.academy.event.service" })
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, MyAuthenticationEntryPoint myAuthenticationEntryPoint, MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler,
                          UserService userService, UserRepository userRepository, UserSocialService socialService) {
        this.userDetailsService = userDetailsService;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;

        this.userService = userService;
        this.userRepository = userRepository;
        this.socialService = socialService;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/resendRegistrationToken", "/registrationConfirm", "/registration").permitAll()
                .antMatchers("/testAccess/{token}", "/testAccess/check").permitAll()
                .antMatchers("/survey/**").permitAll()
                .antMatchers("/fileupload").permitAll()
                .antMatchers("/sendEmails").permitAll()
                .antMatchers("/statistic/**").permitAll()
                .anyRequest().authenticated()
               // .antMatchers("/login").hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .and()
                .csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    /** SOMETHING MY **/
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserSocialService socialService;



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and()
//                .authorizeRequests()
//                .antMatchers("/admin", "/admin/**").hasAuthority(Role.ADMIN.name())
//                .antMatchers("/", "/registration", "/registration/**",
//                        "/recover-password", "/recover-password/**", "/top_comp_employer",
//                        "/top_comp_employee", "/project", "/view/company/**", "/access").permitAll()
//                .antMatchers(SwaggerConstants.SWAGGER_ENDPOINTS.toArray(new String[] {})).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService))
//                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
//                .csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .oauth2Login()
//                .authorizationEndpoint()
//                .baseUri("/authorization")
//                .and()
//                .successHandler(authenticationSuccessHandler())
//                .userInfoEndpoint()
//                .userService(oAuth2UserService())
//                .and()
//                .authorizedClientService(clientService())
//                .clientRegistrationRepository(clientRegistrationRepository());
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration = corsConfiguration.applyPermitDefaultValues();

        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("filename");

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                Optional<User> userOptional = userService.findByEmail(authentication.getName());
                User user = null;

                if (!userOptional.isPresent()) {

                    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                    user = new User();
                    user.setEmail(oAuth2User.getName());
                    user.setPassword(bCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
                    user.setActive(true);

                    user.setRole(Roles.USER);

                    userService.save(user);

                    Map<String, Object> profileData = oAuth2User.getAttributes();

                    if (profileData.size() > 0) {
                        User  user1= new User();

                        UserSocial userSocial = new UserSocial();
                        userSocial.setEmail((String) profileData.get("nickname"));
                        userSocial.setType(OauthType.FACEBOOK);
                        userSocial.setUser(user1);

                        socialService.save(userSocial);
                    }
                } else {
                    user = userOptional.get();
                    if (!user.isActive()) {
                        response.sendRedirect(propertiesService.getClientUrl() + "/login?blocked=true");
                        return;
                    }
                }

                response.sendRedirect(propertiesService.getClientUrl() + "/authorization/oauth2?token=" +
                        jwtService.generateToken(user.getUsername(),
                                user.getRole()));
            }
        };
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new EmailOAuth2UserService();
    }

    @Bean
    public OAuth2AuthorizedClientService clientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        LOG.debug("in clientRegistrationRepository() method");
        List<String> clients = Arrays.asList(PropertiesService.GITHUB_CLIENT, PropertiesService.GOOGLE_CLIENT);

        return new InMemoryClientRegistrationRepository(clients.stream()
                .map(this::getClientRegistration)
                .collect(Collectors.toList()));
    }

    private ClientRegistration getClientRegistration(String client) {

        ClientRegistration.Builder builder = null;

        if (client.equals(PropertiesService.GOOGLE_CLIENT)) {
            builder = CommonOAuth2Provider.GOOGLE
                    .getBuilder(client)
                    .scope("email", "profile");
        } else if (client.equals(PropertiesService.GITHUB_CLIENT)) {
            builder = CommonOAuth2Provider.GITHUB
                    .getBuilder(client)
                    .scope("user:email");
        } else {
            throw new RestException("Invalid client name");
        }

        Map<String, String> credentials = propertiesService.getOAuthClientCredentials(client);

        return builder
                .clientId(credentials.get(PropertiesService.OAUTH2_CLIENT_ID))
                .clientSecret(credentials.get(PropertiesService.OAUTH2_CLIENT_SECRET))
                .build();

    }


}
