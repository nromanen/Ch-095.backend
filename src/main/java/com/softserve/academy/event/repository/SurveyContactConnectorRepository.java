package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContactConnector;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContactConnector, Long> {
    boolean isEnable(Long contactId, Long surveyId);

    void addRow(Survey survey, Contact contact);

    SurveyContactConnector getByContactIdAndSurveyId(Long contactId, Long surveyId);


}
