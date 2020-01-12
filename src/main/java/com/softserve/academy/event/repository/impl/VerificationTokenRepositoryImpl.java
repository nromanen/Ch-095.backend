package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.repository.VerificationTokenRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public VerificationTokenRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public VerificationToken findByToken(String token) {
        TypedQuery<VerificationToken> query = sessionFactory.getCurrentSession().createNamedQuery("findToken", VerificationToken.class);
        query.setParameter("token", token);
        List<VerificationToken> vToken = query.getResultList();
        if (vToken.isEmpty()) {
            return null;
        }
        return vToken.get(0);
    }
    @Override
    public Optional<VerificationToken> findFirstById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<VerificationToken> findAll() {
        return null;
    }

    @Override
    public VerificationToken save(VerificationToken entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    @Override
    public VerificationToken update(VerificationToken object) {
        return null;
    }

    @Override
    public void delete(VerificationToken entity) {

    }

}
