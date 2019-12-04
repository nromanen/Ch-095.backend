package com.softserve.academy.event.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.softserve.academy.event.config", "com.softserve.academy.event.controller"})
public class WebConfig {

}
