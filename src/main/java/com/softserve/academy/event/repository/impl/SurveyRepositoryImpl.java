package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> {
    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public Page<Survey> findAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName());
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Query countQuery = session.createQuery("select count(*) from " + clazz.getName());
        Long countResult = (Long) countQuery.uniqueResult();
        pageable.setLastPage((int) ((countResult / pageable.getSize()) + 1));
        return new Page<Survey>(query.list(), pageable);
    }


}
