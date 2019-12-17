package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ContactRepositoryImpl extends BasicRepositoryImpl<Contact, Long> implements ContactRepository {
    @Override
    public Optional<Long> getIdByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select c.id " +
                "from " + clazz.getName() + " as c" +
                " where c.email like \'" + email + "\'");
        List<Long> res = query.getResultList();
        if (res.isEmpty())return Optional.empty();
        return Optional.ofNullable(res.get(0));
    }
}

    @Override
    public void saveEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Contact contact = new Contact();
        contact.setEmail(email);
        session.save(contact);
    }
}