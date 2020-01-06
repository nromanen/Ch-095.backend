package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BasicRepositoryImpl<User, Long>  implements UserRepository {

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
    public String getEmailByUserId(Long id) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createNamedQuery("findEmailById", User.class);
        query.setParameter("id", id);
        return query.getSingleResult().getEmail();
    }

}

