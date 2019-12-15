package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyContactConnectorServiceImpl extends BasicServiceImpl<SurveyContactConnector, Long> implements SurveyContactConnectorService {

    private final SurveyContactConnectorRepository repository;

    @Autowired
    public SurveyContactConnectorServiceImpl(SurveyContactConnectorRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean isEnable(Long contactId, Long surveyId) {
        return repository.isEnable(contactId, surveyId);
    }

    @Override
    public Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId) {
        return repository.findByContactAndSurvey(contactId, surveyId);
    }
}
