package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.config.HibernateConfig;
import com.softserve.academy.event.config.WebConfig;
import com.softserve.academy.event.controller.ApplicationInitializerTest;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringJUnitWebConfig({ApplicationInitializerTest.class})
@WithMockUser(username = "clevercattv@gmail.com", authorities = {"USER"})
@ContextConfiguration(classes = {HibernateConfig.class, WebConfig.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/beforeSurveyService.sql")
class SurveyServiceImplTest {

    @Autowired
    private SurveyService surveyService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }


}
