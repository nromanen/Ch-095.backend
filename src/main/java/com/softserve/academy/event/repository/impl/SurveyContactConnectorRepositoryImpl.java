package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class SurveyContactConnectorRepositoryImpl extends BasicRepositoryImpl<SurveyContact, Long> implements SurveyContactConnectorRepository {

    private final String fromQuery = "from " + clazz.getName() + " as t where t.contact.id = :contactId and t.survey.id = :surveyId";
    private static final String CONTACT_ID = "contactId";
    private static final String SURVEY_ID = "surveyId";

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException {
        List<Boolean> res = sessionFactory.getCurrentSession()
                .createQuery("select t.canPass " + fromQuery)
                .setParameter(CONTACT_ID, contactId)
                .setParameter(SURVEY_ID, surveyId)
                .getResultList();
        if (res.isEmpty()) {
            log.error("This survey is not available for the current user");
            throw new IncorrectLinkException("Sorry, but you can`t pass survey by this link");
        }
        if (res.get(0)) {
            return true;
        }
        log.error("This user has already passed survey");
        throw new SurveyAlreadyPassedException("Sorry, but you have already passed this survey");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId) {
        List<SurveyContact> result = sessionFactory.getCurrentSession()
                .createQuery(fromQuery)
                .setParameter(SURVEY_ID, surveyId)
                .setParameter(CONTACT_ID, contactId)
                .getResultList();
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }
}