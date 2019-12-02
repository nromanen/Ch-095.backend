package com.softserve.academy.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource("classpath:database.properties")
public class HibernateConfig {

    private static final String ENTITY_PACKAGE = "hibernate.entity.package";

    @Bean
    public LocalSessionFactoryBean getSessionFactory(Environment environment) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        Properties properties = new Properties();

        properties.put(DRIVER, Objects.requireNonNull(environment.getProperty(DRIVER)));
        properties.put(URL, Objects.requireNonNull(environment.getProperty(URL)));
        properties.put(USER, Objects.requireNonNull(environment.getProperty(USER)));
        properties.put(PASS, Objects.requireNonNull(environment.getProperty(PASS)));
        properties.put(SHOW_SQL, Objects.requireNonNull(environment.getProperty(SHOW_SQL)));
        properties.put(HBM2DDL_AUTO, Objects.requireNonNull(environment.getProperty(HBM2DDL_AUTO)));
        properties.put(DIALECT, Objects.requireNonNull(environment.getProperty(DIALECT)));

        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.setPackagesToScan(environment.getProperty(ENTITY_PACKAGE));

        return sessionFactoryBean;
    }

}