package com.softserve.academy.event.service.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.EditSurveyDTO;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

public interface SurveyService {

    Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status);

    void updateTitle(Long id, String title);

    void updateStatus(Long id, SurveyStatus status);

    long duplicate(DuplicateSurveySettings settings);

    void disable(Long id);

    void delete(Long id);

    Optional<Survey> findFirstById(long surveyId);

    Optional<Survey> findFirstByIdForNormPeople(long surveyId);

    Survey saveSurveyWithQuestions(SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException;

    Survey updateSurvey(Long surveyId, SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException;

    boolean isCommonWithIdAndNameExist(Long id, String name);
  
    ResponseEntity<EditSurveyDTO> loadSurvey(Long surveyId) throws IOException;
  
    List<String> getSurveyContacts(long surveyId);
}

