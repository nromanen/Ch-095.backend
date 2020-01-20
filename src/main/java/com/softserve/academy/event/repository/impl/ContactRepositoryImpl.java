package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ContactRepositoryImpl extends BasicRepositoryImpl<Contact, Long> implements ContactRepository {

    @Override
    public Optional<Contact> findByEmail(String email) {
        TypedQuery<Contact> query = sessionFactory.getCurrentSession().createNamedQuery("findEmailContact", Contact.class).setMaxResults(1);
        query.setParameter("email", email);
        List<Contact> contacts = query.getResultList();
        if (contacts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getResultList().get(0));
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select c.id " +
                "from " + clazz.getName() + " as c" +
                " where c.email like \'" + email + "\'");
        List<Long> res = query.getResultList();
        if (res.isEmpty()) return Optional.empty();
        return Optional.ofNullable(res.get(0));
    }

    public Optional<Contact> findByEmailAndUserId(String email, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " as t " +
                "where t.email = :email and t.user.id = :userId")
                .setParameter("email", email)
                .setParameter("userId", userId);
        List<Contact> res = query.getResultList();
        if (res.isEmpty()) return Optional.empty();
        return Optional.ofNullable(res.get(0));
    }

    public List<Contact> findAvailableContacts(Long surveyId, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " as t " +
                " left join fetch t.surveyContacts c " + " where t.id not in( " +
                " select c.contact.id from " + "c " + " where c.survey.id = :surveyId) and t.user.id = :userId")
                .setParameter("surveyId", surveyId)
                .setParameter("userId", userId);
        List<Contact> res = query.getResultList();
        return res.stream().collect(Collectors.toList());
    }

    @Override
    public List<Contact> listContactsByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " as t " + " where t.user.id = :userId")
                .setParameter("userId", userId);
        List<Contact> res = query.getResultList();
        return res.stream().collect(Collectors.toList());
    }
}