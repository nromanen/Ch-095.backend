package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyContact;

import java.util.Optional;

public interface SurveyContactConnectorRepository extends BasicRepository<SurveyContact, Long> {

    boolean isEnable(Long contactId, Long surveyId);
    Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId);
}
