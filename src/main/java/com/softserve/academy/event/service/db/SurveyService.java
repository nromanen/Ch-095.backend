package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface SurveyService extends BasicService<Survey, Long> {

    Page<SimpleSurveyDTO> findAll(Pageable pageable);

    Page<SimpleSurveyDTO> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters);

    HttpStatus updateTitle(Long id, String title);

    SimpleSurveyDTO duplicateSurvey(Long id);

    String setTitleForSurvey(Long id, String title);

}
