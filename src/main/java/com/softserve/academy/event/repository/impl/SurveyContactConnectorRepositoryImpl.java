package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
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
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException {

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select t.enable " +
                "from " + clazz.getName() + " as t" +
                " where t.contact.id = :contactId and t.survey.id = :surveyId")
                .setParameter("contactId", contactId)
                .setParameter("surveyId", surveyId);
        List<Boolean> res = query.getResultList();
        if (res.isEmpty()){
            throw new IncorrectLinkException();
        }
        if (res.get(0)){
            return true;
        }
        throw new SurveyAlreadyPassedException();
    }
}
