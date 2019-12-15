package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.BasicRepository;

import java.util.Optional;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContactConnector, Long> {

    boolean isEnable(Long contactId, Long surveyId);
    Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId);
}
