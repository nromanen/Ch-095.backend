package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SurveyContactConnectorRepositoryImpl extends BasicRepositoryImpl<SurveyContactConnector, Long> implements SurveyContactConnectorRepository {
    @Override
    public boolean isEnable(Long contactId, Long surveyId) {

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select t.enable " +
                "from " + clazz.getName() + " as t" +
                " where t.contact = " + contactId + " and t.survey = " + surveyId);
        List<Boolean> res = query.getResultList();
        if (res.isEmpty()) return false;
        return res.get(0);
    }

    @Override
    public void addRow(Survey survey, Contact contact) {
        Session session = sessionFactory.getCurrentSession();
        SurveyContactConnector surveyContact = new SurveyContactConnector();
        surveyContact.setSurvey(survey);
        surveyContact.setContact(contact);
        surveyContact.setEnable(true);
        session.save(surveyContact);
    }

    @Override
    public SurveyContactConnector getByContactIdAndSurveyId(Long contactId, Long surveyId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select *" +
                "from " + clazz.getName() + " as t" +
                " where t.contact = " + contactId + " and t.survey = " + surveyId);
        List<SurveyContactConnector> res = query.getResultList();
        if (res.isEmpty()) return null;
        return res.get(0);
    }
}