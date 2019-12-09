package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.User;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRepository extends BasicRepository<User, Long> {
    User findByEmail(String email);
}

