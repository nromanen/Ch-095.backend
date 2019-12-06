package com.softserve.academy.event.service;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.impl.SurveyRepositoryImpl;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepositoryImpl surveyRepository;

    public SurveyService(SurveyRepositoryImpl surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        Page<Survey> page = surveyRepository.findAll(pageable);
        return new Page<SimpleSurveyDTO>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable); // convert to dto
    }

    @Transactional
    public HttpStatus updateTitle(Long id, String title){
        Survey survey = surveyRepository.findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        surveyRepository.update(survey);
        return HttpStatus.OK;
    }

}
