package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Optional<Survey> findFirstByIdAndUserIdOrStatus(Long id, Long userId, SurveyStatus status) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(fromQuery + " where id = :id and (user.id = :userId or status = :status)")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("status", status);
        return Optional.ofNullable((Survey) query.uniqueResult());
    }

    @Override
    public Optional<Survey> findFirstByIdAndUserId(Long id, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(fromQuery + " where id = :id and user.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId);
        return Optional.ofNullable((Survey) query.uniqueResult());
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndUserId(Pageable pageable, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_DEFAULT_FILTER_NAME);
        return getSurveyPage(pageable, session, userId);
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndStatusUserId(Pageable pageable, String status, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(SURVEY_STATUS_FILTER_NAME)
                .setParameter(SURVEY_STATUS_FILTER_ARGUMENT, SurveyStatus.valueOf(status).getNumber());
        return getSurveyPage(pageable, session, userId);
    }

    @SuppressWarnings("unchecked")
    private Page<SurveyDTO> getSurveyPage(Pageable pageable, Session session, Long userId) {
        Query query = session.createQuery(
                "select new com.softserve.academy.event.dto.SurveyDTO(" +
                        "s.id,s.title,s.status,s.imageUrl," +
                        "(select count(cc) from c as cc where cc.canPass = true and cc.survey = s.id), count(c)) " +
                        "from " + clazz.getName() + " as s " +
                        "left join s.surveyContacts c on s.id = c.survey " +
                        "where s.user.id = :userId group by s.id ORDER BY s." + pageable.getSort().sorting())
                .setParameter("userId", userId);
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Long countResult = (Long) session.createQuery(countQuery + " where user.id = :userId")
                .setParameter("userId", userId).uniqueResult();
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
    }

    @Override
    public boolean isExistIdAndUserId(Long id, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(countQuery + " where id = :id and user.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId);
        return ((Long) query.getSingleResult()) > 0;
    }

}
