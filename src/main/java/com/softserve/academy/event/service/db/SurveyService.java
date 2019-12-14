package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface SurveyService extends BasicService<Survey, Long> {

    Page<SimpleSurveyDTO> findAll(Pageable pageable);

    HttpStatus updateTitle(Long id, String title);

    SimpleSurveyDTO duplicateSurvey(Long id);

    String setTitleForSurvey(Long id, String title);

    Survey saveSurveyWithQuestions(Survey survey, long id, List<SurveyQuestion> surveyQuestions);

}