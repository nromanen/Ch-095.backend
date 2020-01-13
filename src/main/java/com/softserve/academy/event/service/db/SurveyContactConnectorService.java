package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;

public interface SurveyContactConnectorService extends BasicService<SurveyContact, Long> {

    SurveyContact findByContactAndSurvey(Long contactId, Long surveyId);

    boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException;
}