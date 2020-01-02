package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
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

    public SurveyRepositoryImpl() {
        super();
        countQuery = "select count(*) from " + clazz.getName() + " where user.email = :userEmail";
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndUserEmail(Pageable pageable, String userEmail) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_DEFAULT_FILTER_NAME);
        return getSurveyPage(pageable, session, userEmail);
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndStatusAndUserEmail(Pageable pageable, String status, String userEmail) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_STATUS_FILTER_NAME)
                .setParameter(SURVEY_STATUS_FILTER_ARGUMENT, SurveyStatus.valueOf(status).getNumber());
        return getSurveyPage(pageable, session, userEmail);
    }

    @SuppressWarnings("unchecked")
    private Page<SurveyDTO> getSurveyPage(Pageable pageable, Session session, String userEmail) {
        Query query = session.createQuery(
                "select new com.softserve.academy.event.dto.SurveyDTO(" +
                        "s.id,s.title,s.status,s.imageUrl," +
                        "(select count(cc) from c as cc where cc.canPass = true and cc.survey = s.id), count(c)) " +
                        "from " + clazz.getName() + " as s " +
                        "left join s.surveyContacts c on s.id = c.survey " +
                        "where s.user.email = :userEmail and s.active = true " +
                        "group by s.id order by s." + pageable.getSort().sorting())
                .setParameter("userEmail", userEmail);
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Long countResult = (Long) session.createQuery(countQuery)
                .setParameter("userEmail", userEmail).uniqueResult();
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
    }

}
