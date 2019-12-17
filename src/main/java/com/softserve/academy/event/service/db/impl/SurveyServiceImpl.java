package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepositoryImpl repository;

    public SurveyServiceImpl(SurveyRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Survey> findFirstById(Long id) {
        return repository.findFirstById(id);
    }

    @Override
    public List<Survey> findAll() {
        return repository.findAll();
    }

    @Override
    public Survey save(Survey entity) {
        return repository.save(entity);
    }

    @Override
    public Survey update(Survey object) {
        return repository.update(object);
    }

    @Override
    public void delete(Survey entity) {
        repository.delete(entity);
    }

    @Override
    public void detach(Survey entity) {
        repository.detach(entity);
    }

    @Override
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        Page<Survey> page = repository.findAll(pageable);
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable);
    }

    @Override
    public Page<SimpleSurveyDTO> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        Page<Survey> page = repository.findAllFiltered(pageable,
                Objects.nonNull(filters) ? filters :
                        Collections.singletonMap("surveyStatusField",
                                Collections.singletonMap("status", SurveyStatus.TEMPLATE.getNumber()))
        );
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable); // convert to dto
    }

    @Override
    public HttpStatus updateTitle(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return HttpStatus.OK;
    }

    @Override
    public SimpleSurveyDTO duplicateSurvey(Long id) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setId(null);
        detach(survey);
        save(survey);
        return SimpleSurveyDTO.toSimpleUser(survey);
    }

    @Override
    public String setTitleForSurvey(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return survey.getTitle();
    }

}
