package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyContactConnector;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId);
}
