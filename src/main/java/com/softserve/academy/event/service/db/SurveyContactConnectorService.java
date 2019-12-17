package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.repository.BasicRepository;

import java.util.Optional;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContact, Long> {

    boolean isEnable(Long contactId, Long surveyId);
    Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId);
}
