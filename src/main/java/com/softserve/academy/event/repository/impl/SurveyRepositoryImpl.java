package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;
import java.util.Set;

@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> {

    private final SessionFactory sessionFactory;

    public SurveyRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Page<Survey> findAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        return getSurveyPage(pageable, session);
    }

    public Page<Survey> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        Session session = sessionFactory.getCurrentSession();
        filters.forEach((key, value) -> {
            Filter filter = session.enableFilter(key);
            value.forEach(filter::setParameter);
        });
        return getSurveyPage(pageable, session);
    }

    @SuppressWarnings("unchecked")
    private Page<Survey> getSurveyPage(Pageable pageable, Session session) {
        Query query = session.createQuery("from " + clazz.getName());
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Query countQuery = session.createQuery("select count(*) from " + clazz.getName());
        Long countResult = (Long) countQuery.uniqueResult();
        pageable.setLastPage((int) ((countResult / pageable.getSize()) + 1));
        return new Page<Survey>(query.list(), pageable);
    }

//    @Override
//    public boolean getEventById(String surveyId) {
//        Session session = sessionFactory.getCurrentSession();
//        Long surveyIdd = Long.valueOf(surveyId);
//        Query query = session.createQuery("select t.enable " +
//                "from " + clazz.getName() + " as t" +
//                " where t.survey = " + surveyIdd);
//        List<Boolean> res = query.getResultList();
//        if (res.isEmpty())return false;
//        return res.get(0);
//    }

    @Override
    public void save(Set<Contact> contactSet) {
        Session session = sessionFactory.getCurrentSession();
        session.save(contactSet);
    }
}
