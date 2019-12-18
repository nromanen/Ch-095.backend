package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.BasicRepository;

import java.util.Optional;

public interface SurveyContactConnectorService extends BasicRepository<SurveyContact, Long> {
  
    Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId);
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException;
}
