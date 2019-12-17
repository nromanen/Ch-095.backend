package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Set;

public interface SurveyService extends BasicService<Survey, Long> {
   // public boolean getEventById(String surveyId);
    public void save(Set<Contact> contactSet);
    Page<SimpleSurveyDTO> findAll(Pageable pageable);
    Page<SimpleSurveyDTO> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters);
    HttpStatus updateTitle(Long id, String title);
    SimpleSurveyDTO duplicateSurvey(Long id);
    String setTitleForSurvey(Long id, String title);
}
