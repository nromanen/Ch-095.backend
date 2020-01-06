package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SurveyContactConnectorRepositoryImpl extends BasicRepositoryImpl<SurveyContact, Long> implements SurveyContactConnectorRepository {

    private final String fromQuery = "from " + clazz.getName() + " as t where t.contact.id = :contactId and t.survey.id = :surveyId";
    private static final String CONTACT_ID = "contactId";
    private static final String SURVEY_ID = "surveyId";

    @Override
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select t.canPass " + fromQuery)
                .setParameter(CONTACT_ID, contactId)
                .setParameter(SURVEY_ID, surveyId);
        List<Boolean> res = query.getResultList();
        if (res.isEmpty()) {
            throw new IncorrectLinkException();
        }
        if (res.get(0)) {
            return true;
        }
        throw new SurveyAlreadyPassedException();
    }

    @Override
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

    @Override
    public SurveyContact getByContactIdAndSurveyId(Long contactId, Long surveyId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(fromQuery)
                .setParameter(CONTACT_ID, contactId)
                .setParameter(SURVEY_ID, surveyId);
        List<SurveyContact> res = query.getResultList();
        if (res.isEmpty()) return null;
        return res.get(0);
    }
}