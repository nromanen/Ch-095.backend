package com.softserve.academy.event.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringInitializer extends AbstractSecurityWebApplicationInitializer {
    public SpringInitializer() {
        super(WebSecurityConfig.class);
    }
}
