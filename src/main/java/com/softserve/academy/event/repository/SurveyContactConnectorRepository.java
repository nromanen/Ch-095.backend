package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContactConnector;

import java.util.Set;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {
    public boolean isEnable(Long contactId, Long surveyId);
    public void addRow(Survey survey, Contact contact);
}
