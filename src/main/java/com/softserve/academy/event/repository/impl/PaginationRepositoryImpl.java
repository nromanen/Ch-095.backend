package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.repository.PaginationRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

@Repository
public abstract class PaginationRepositoryImpl<T extends Serializable, I extends Serializable> extends BasicRepositoryImpl<T, I> implements PaginationRepository<T, I> {

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName());
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        Query countQuery = session.createQuery("select count(*) from " + clazz.getName());
        Long countResult = (Long) countQuery.uniqueResult();
        pageable.setLastPage((int) ((countResult / pageable.getSize()) + 1));
        return new Page<T>(query.list(), pageable);
    }

}
