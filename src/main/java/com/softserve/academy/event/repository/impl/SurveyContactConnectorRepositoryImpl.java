package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
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
}
