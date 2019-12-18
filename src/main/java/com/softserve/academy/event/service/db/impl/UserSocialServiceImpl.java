package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.repository.UserSocialRepository;
import com.softserve.academy.event.service.db.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class UserSocialServiceImpl implements UserSocialService {

    private final UserRepository userRepository;
    private final UserSocialRepository socialRepository;

    @Autowired
    public UserSocialServiceImpl(UserRepository userRepository, UserSocialRepository socialRepository){
        this.userRepository = userRepository;
        this.socialRepository = socialRepository;
    }

    @Override
    public UserSocial save(UserSocial entity) {
        entity.setUser(userRepository.save(entity.getUser()));
        return socialRepository.save(entity);
    }
}
