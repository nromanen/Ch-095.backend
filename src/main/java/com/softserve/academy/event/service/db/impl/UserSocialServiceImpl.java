package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.entity.enums.Roles;
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
    private final UserSocialRepository userSocialRepository;

    @Autowired
    public UserSocialServiceImpl(UserRepository userRepository, UserSocialRepository userSocialRepository){
        this.userRepository = userRepository;
        this.userSocialRepository = userSocialRepository;
    }

    @Override
    public UserSocial save(UserSocial entity) {
        User user = new User();
        user.setActive(true);
        user.setRole(Roles.USER);
        entity.setUser(userRepository.save(user));
        return userSocialRepository.save(entity);
    }

//    private final UserRepository userRepository;
//    private final UserSocialRepository socialRepository;
//
//    @Autowired
//    public UserSocialServiceImpl(UserRepository userRepository, UserSocialRepository socialRepository){
//        this.userRepository = userRepository;
//        this.socialRepository = socialRepository;
//    }
//
//    @Override
//    public UserSocial save(UserSocial entity) {
//        entity.setUser(userRepository.save(entity.getUser()));
//        return socialRepository.save(entity);
//    }
}
