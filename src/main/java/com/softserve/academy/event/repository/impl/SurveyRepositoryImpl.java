package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.softserve.academy.event.util.Constants.*;

@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> implements SurveyRepository {

    private final String COUNT_QUERY;
    private final String FROM_SURVEY;

    public SurveyRepositoryImpl() {
        super();
        COUNT_QUERY = "select count(*) from " + clazz.getName();
        FROM_SURVEY = "from " + clazz.getName();
    }

    @Override
    public Page<Survey> findAllByPageable(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        return getSurveyPage(pageable, session);
    }

    @Override
    public Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status) {
        Session session = sessionFactory.getCurrentSession();
        if (Objects.nonNull(status) && status.length() > 0) {
            session.enableFilter(SURVEY_STATUS_FILTER_NAME)
                    .setParameter(SURVEY_STATUS_FILTER_ARGUMENT, SurveyStatus.valueOf(status).getNumber());
        } else {
            session.enableFilter(SURVEY_DEFAULT_FILTER_NAME);
        }
        return getSurveyPage(pageable, session);
    }

    @SuppressWarnings("unchecked")
    private Page<Survey> getSurveyPage(Pageable pageable, Session session) {
        Query query = session.createQuery(FROM_SURVEY + " ORDER BY :pagination")
                .setParameter("pagination", pageable.getSort().getColumn() + pageable.getSort().getDirection().name());
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Query countQuery = session.createQuery(COUNT_QUERY);
        Long countResult = (Long) countQuery.uniqueResult();
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
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

//    @Override
//    public void save(Set<Contact> contactSet) {
//        Session session = sessionFactory.getCurrentSession();
//        session.save(contactSet);
//    }
}
