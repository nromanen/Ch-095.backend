package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class SurveyRepositoryImpl extends PaginationRepositoryImpl<Survey, Long> implements SurveyRepository {

    public Page<Survey> findAll(Pageable pageable) {
        return super.findAll(pageable);
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
