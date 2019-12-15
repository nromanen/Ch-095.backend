package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SurveyContactConnectorRepositoryImpl extends BasicRepositoryImpl<SurveyContactConnector, Long> implements SurveyContactConnectorRepository {

    @Override
    public boolean isEnable(Long contactId, Long surveyId) {

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select t.enable " +
                "from " + clazz.getName() + " as t" +
                " where t.contact = " + contactId + " and t.survey = " + surveyId);
        List<Boolean> res = query.getResultList();
        if (res.isEmpty())return false;
        return res.get(0);
    }

    @Override
    public Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId) {
        List<SurveyContactConnector> result = sessionFactory.getCurrentSession()
                .createQuery("from " + clazz.getName() + " as t" +
                        " where t.contact = " + contactId + " and t.survey = " + surveyId)
                .getResultList();
        if(result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }
}
