package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyContactConnector;

import java.util.Optional;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {

    boolean isEnable(Long contactId, Long surveyId);
    Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId);
}
