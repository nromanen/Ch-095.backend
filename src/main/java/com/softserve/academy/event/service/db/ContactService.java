package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.User;

import java.util.Optional;

public interface ContactService {
    Optional<Long> getIdByEmail(String email);

    void saveEmail(String email, User user);
}
