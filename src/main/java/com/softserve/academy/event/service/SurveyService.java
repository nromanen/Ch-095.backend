package com.softserve.academy.event.service;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.impl.SurveyRepositoryImpl;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepositoryImpl repository;

    public SurveyService(SurveyRepositoryImpl repository) {
        this.repository = repository;
    }

    @Transactional
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        Page<Survey> page = repository.findAll(pageable);
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable); // convert to dto
    }

    @Transactional(dontRollbackOn = Exception.class)
    public SimpleSurveyDTO duplicateSurvey(Long id){
        Survey survey = repository.findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setId(null);
        repository.detach(survey);
        repository.save(survey);
        return SimpleSurveyDTO.toSimpleUser(survey);
    }

    @Transactional
    public String setTitleForSurvey(Long id, String title){
        Survey survey = repository.findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        repository.update(survey);
        return survey.getTitle();
    }

    @Transactional
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
