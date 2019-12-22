package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import static com.softserve.academy.event.util.Constants.*;

@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> implements SurveyRepository {

    private final String countQuery;
    private final String fromQuery;

    public SurveyRepositoryImpl() {
        super();
        countQuery = "select count(*) from " + clazz.getName();
        fromQuery = "from " + clazz.getName();
    }

    @Override
    public Page<Survey> findAllByPageable(Pageable pageable, User user) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_DEFAULT_FILTER_NAME);
        return getSurveyPage(pageable, session, user);
    }

    @Override
    public Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status, User user) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_STATUS_FILTER_NAME)
                .setParameter(SURVEY_STATUS_FILTER_ARGUMENT, SurveyStatus.valueOf(status).getNumber());
        return getSurveyPage(pageable, session, user);
    }

    @SuppressWarnings("unchecked")
    private Page<Survey> getSurveyPage(Pageable pageable, Session session, User user) {
        Query query = session.createQuery(fromQuery + " where user = :user ORDER BY :pagination")
                .setParameter("pagination", pageable.getSort().sorting())
                .setParameter("user", user);
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Long countResult = (Long) session.createQuery(countQuery + " where user = :user")
                .setParameter("user", user).uniqueResult();
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
    }

}
