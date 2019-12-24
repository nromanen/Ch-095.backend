package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.repository.UserSocialRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserSocialRepositoryImpl extends BasicRepositoryImpl<UserSocial, Long> implements UserSocialRepository {

}
