package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SurveyService {

    Page<Survey> findAll(Pageable pageable);

    Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status);

    Page<Survey> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters);

    HttpStatus updateTitle(Long id, String title);

    HttpStatus updateStatus(Long id, SurveyStatus status);

    Survey duplicateSurvey(DuplicateSurveySettings settings);

    void delete(Survey entity);

    Optional<Survey> findFirstById(long surveyId);

    Survey saveSurveyWithQuestions(Survey survey, List<SurveyQuestion> surveyQuestions);
}
