package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.impl.SurveyRepositoryImpl;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepositoryImpl repository;

    public SurveyServiceImpl(SurveyRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Survey> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        return repository.findAllFiltered(pageable,
                Objects.nonNull(filters) ? filters :
                        Collections.singletonMap("surveyStatusField",
                                Collections.singletonMap("status", SurveyStatus.TEMPLATE.getNumber()))
        );
    }

    @Override
    public HttpStatus updateTitle(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return HttpStatus.OK;
    }

    @Override
    public Survey duplicateSurvey(DuplicateSurveySettings settings) {
        Survey survey = findFirstById(settings.getId()).orElseThrow(RuntimeException::new);
        detach(survey);
        survey.setId(null);
        survey.setCreationDate(new Date());
        survey.setStatus(SurveyStatus.NON_ACTIVE);
        if (settings.isClearContacts()) {
            survey.setContacts(new HashSet<>());
        }
        save(survey);
        return survey;
    }

    @Override
    public String setTitleForSurvey(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return survey.getTitle();
    }

}
