package com.softserve.academy.event.config;

import com.softserve.academy.event.service.MyAuthenticationEntryPoint;
import com.softserve.academy.event.service.MySavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.softserve.academy.event.service" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.frontend.url}")
    private String frontUrl;

    private final UserDetailsService userDetailsService;

    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, MyAuthenticationEntryPoint myAuthenticationEntryPoint, MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;
    }

    private CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Collections.singletonList(frontUrl));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        return configuration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(e -> corsConfiguration())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/resendRegistrationToken", "/registrationConfirm", "/registration").permitAll()
                .antMatchers("/testAccess/{token}", "/testAccess/check").permitAll()
                .antMatchers("/survey/**", "/survey").permitAll()
                .antMatchers("/fileupload").permitAll()
                .antMatchers("/sendEmails").permitAll()
                .antMatchers("/statistic/**").permitAll()
                .antMatchers("/question").permitAll()
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
    public AuthenticationProvider authenticationProvider() {
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

}
