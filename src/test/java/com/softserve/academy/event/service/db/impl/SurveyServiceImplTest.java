package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.config.HibernateConfig;
import com.softserve.academy.event.config.WebConfig;
import com.softserve.academy.event.controller.ApplicationInitializer;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig({ApplicationInitializer.class})
@WithMockUser(username = "clevercattv@gmail.com", authorities = {"USER"})
@ContextConfiguration(classes = {HibernateConfig.class, WebConfig.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/surveyService.sql")
class SurveyServiceImplTest {

    @Autowired
    private SurveyService surveyService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider
    public static Object[][] findAllByPageableAndStatusProvider() {
        return new Object[][]{
                {new Pageable(12, 0, 1, Sort.from(Sort.Direction.ASC, "id")), null},
                {new Pageable(12, 0, 1, Sort.from(Sort.Direction.ASC, "id")), ""},
                {new Pageable(12, 0, 1, Sort.from(Sort.Direction.ASC, "id")), "ACTIVE"},
        };
    }

    @Test
    @UseDataProvider("findAllByPageableAndStatusProvider")
    public void findAllByPageableAndStatus(Pageable pageable, String status) {
        List<SurveyDTO> surveys = surveyService.findAllByPageableAndStatus(pageable, status).getItems();
        assertEquals(0, surveys.size());
    }

    @Test
    void updateTitle() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void duplicateSurvey() {
    }

    @Test
    void delete() {
    }

    @Test
    void findFirstById() {
    }

    @Test
    void saveSurveyWithQuestions() {
    }

}
