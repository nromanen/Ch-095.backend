package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.BasicRepository;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId);
    public void addRow(Survey survey, Contact contact);
}