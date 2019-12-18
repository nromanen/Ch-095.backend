package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;

import java.util.Optional;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {

    Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId);
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException;
}
