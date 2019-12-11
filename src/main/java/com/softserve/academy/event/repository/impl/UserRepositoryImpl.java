package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends BasicRepositoryImpl<User, Long> implements UserRepository {

}
