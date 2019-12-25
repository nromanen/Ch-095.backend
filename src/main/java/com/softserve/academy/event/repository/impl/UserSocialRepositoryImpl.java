package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.repository.UserSocialRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserSocialRepositoryImpl extends BasicRepositoryImpl<UserSocial, Long> implements UserSocialRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserSocialRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Long indexOf(UserSocial entity) throws IndexOutOfBoundsException{
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select u.id " +
                "from " + clazz.getName() + " as u " +
                "where u.socialid = :socialId and u.type = :socialType")
                .setParameter("socialId", entity.getSocialId())
                .setParameter("socialType", entity.getType().toString());
        List res =  query.getResultList();
        if (res.size() > 0) return (Long) res.get(0);
        throw new IndexOutOfBoundsException();
    }
}
