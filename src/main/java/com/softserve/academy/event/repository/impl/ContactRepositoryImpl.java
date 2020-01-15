package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;

@Repository
public class ContactRepositoryImpl extends BasicRepositoryImpl<Contact, Long> implements ContactRepository {

    @Override
    public Page<Contact> findAllByPageable(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        return getContactPage(
                pageable,
                session.createQuery("from " + clazz.getName() + " as c" +
                        " where c.user.email = :userEmail" +
                        " order by c." + pageable.sorting())
                        .setParameter("userEmail", getCurrentUserEmail()),
                (Long) session.createQuery("select count(c) from " + clazz.getName() +
                        " as c where c.user.email = :userEmail ")
                        .setParameter("userEmail", getCurrentUserEmail()).uniqueResult()
        );
    }

    @Override
    public Page<Contact> findAllByPageableAndFilterLikeNameOrEmail(Pageable pageable, String filter) {
        Session session = sessionFactory.getCurrentSession();
        return getContactPage(
                pageable,
                session.createQuery("from " + clazz.getName() + " as c" +
                        " where c.user.email = :userEmail and (c.name like :filter or c.email like :filter)" +
                        " order by c." + pageable.sorting())
                        .setParameter("userEmail", getCurrentUserEmail())
                        .setParameter("filter", "%" + filter + "%"),
                (Long) session.createQuery("select count(c) from " + clazz.getName() +
                        " as c where c.user.email = :userEmail and (c.name = :filter or c.email = :filter)")
                        .setParameter("userEmail", getCurrentUserEmail())
                        .setParameter("filter", "%" + filter + "%").uniqueResult()
        );
    }

    @SuppressWarnings("unchecked")
    private Page<Contact> getContactPage(Pageable pageable, Query query, Long countResult) {
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
    }

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

    public Contact getEmailAndUserId(String email, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " as t " +
                "where t.email = :email and t.user.id = :userId")
                .setParameter("email", email)
                .setParameter("userId", userId);
        List<Contact> res = query.getResultList();
        if (res.isEmpty()) return null;
        return res.get(0);
    }
}
