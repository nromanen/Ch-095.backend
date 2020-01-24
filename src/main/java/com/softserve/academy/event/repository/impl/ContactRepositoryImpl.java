package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.SecurityUserUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;

@Repository
public class ContactRepositoryImpl extends BasicRepositoryImpl<Contact, Long> implements ContactRepository {

    @Override
    public void saveWithConflictUpdate(Contact contact) {
        sessionFactory.getCurrentSession()
                .createNativeQuery("INSERT INTO contacts(name, email, user_id)" +
                        " VALUES (:name, :email, :userId)" +
                        " ON CONFLICT (email,user_id)" +
                        " DO UPDATE SET name = :name")
                .setParameter("name", contact.getName())
                .setParameter("email", contact.getEmail())
                .setParameter("userId", contact.getUser().getId())
                .executeUpdate();
    }

    @Override
    public void saveWithConflictIgnore(Contact contact) {
        sessionFactory.getCurrentSession()
                .createNativeQuery("INSERT INTO contacts(name, email, user_id)" +
                        " VALUES (:name, :email, :userId)" +
                        " ON CONFLICT DO NOTHING")
                .setParameter("name", contact.getName())
                .setParameter("email", contact.getEmail())
                .setParameter("userId", contact.getUser().getId())
                .executeUpdate();
    }

    @Override
    public Optional<Contact> findFirstById(Long id) {
        return Optional.ofNullable((Contact) sessionFactory.getCurrentSession()
                .createQuery("from " + clazz.getName() + " where id = :id and user.email = :user")
                .setParameter("id", id)
                .setParameter("user", SecurityUserUtil.getCurrentUserEmail())
                .setMaxResults(1)
                .getSingleResult());
    }

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

    @Override
    public boolean isSurveysContainContact(Long contactId) {
        return ((Long) sessionFactory.getCurrentSession()
                .createQuery("select count(c) from " + SurveyContact.class.getName()
                        + " as c where c.contact.id = :id")
                .setParameter("id", contactId)
                .setMaxResults(1)
                .getSingleResult()) > 0;
    }

}
