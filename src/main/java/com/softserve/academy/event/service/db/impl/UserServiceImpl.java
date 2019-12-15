package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findFirstById(Long id) {
        return userRepository.findFirstById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public User update(User object) {
        return userRepository.update(object);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void detach(User entity) {
        userRepository.detach(entity);
    }
}
