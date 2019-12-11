package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.repository.impl.BasicRepositoryImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl  implements UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User findByEmail(String email) {
        return sessionFactory.getCurrentSession().get(User.class, email);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User save(User entity) {
        return (User) sessionFactory.getCurrentSession().save(entity);
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

