package com.softserve.academy.event.repository;


import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException;
}
