package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

    Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status);

    void updateTitle(Long id, String title);

    void updateStatus(Long id, SurveyStatus status);

    long duplicate(DuplicateSurveySettings settings);

    void disable(Long id);

    void delete(Long id);

    Optional<Survey> findFirstById(long surveyId);

    Survey saveSurveyWithQuestions(Survey survey, List<SurveyQuestion> surveyQuestions);

    Survey updateSurvey(Long surveyId, List<SurveyQuestion> surveyQuestions);

    boolean isCommonWithIdAndNameExist(Long id, String name);

    List<String> getSurveyContacts(long surveyId);
}

