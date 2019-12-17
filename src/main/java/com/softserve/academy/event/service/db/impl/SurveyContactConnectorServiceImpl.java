package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyContactConnectorServiceImpl extends BasicServiceImpl<SurveyContactConnector, Long> implements SurveyContactConnectorService {
    private final SurveyContactConnectorRepository repository;

    @Autowired
    public SurveyContactConnectorServiceImpl(SurveyContactConnectorRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException {
        return repository.isEnable(contactId, surveyId);
    }
}
