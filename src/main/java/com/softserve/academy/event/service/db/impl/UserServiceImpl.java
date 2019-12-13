package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.db.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends BasicServiceImpl<User, Long> implements UserService {

}
