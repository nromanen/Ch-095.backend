package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.BasicRepository;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId);
}
