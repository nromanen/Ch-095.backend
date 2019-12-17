package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl  implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        TypedQuery<User> query = sessionFactory.getCurrentSession().createNamedQuery("findEmail", User.class);
        query.setParameter("email", email);
        List<User> user = query.getResultList();
        if (user.isEmpty()) {
            return Optional.empty();
        }
       return Optional.of(query.getResultList().get(0));
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User save(User entity) {
        Session session = sessionFactory.getCurrentSession();
        Long id = (Long) session.save(entity);
        return session.get(User.class, id);
    }

    @Override
    public User update(User object) {
        return null;
    }

    @Override
    public Optional<User> findFirstById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void detach(User entity) {

    }
}

