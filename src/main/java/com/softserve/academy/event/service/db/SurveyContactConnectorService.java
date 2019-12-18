package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.BasicRepository;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException;
}
