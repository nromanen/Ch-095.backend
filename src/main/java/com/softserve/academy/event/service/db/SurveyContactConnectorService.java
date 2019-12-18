package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.BasicRepository;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContactConnector, Long> {
    boolean isEnable(Long contactId, Long surveyId);

    void addRow(Contact contact, Survey survey);
}