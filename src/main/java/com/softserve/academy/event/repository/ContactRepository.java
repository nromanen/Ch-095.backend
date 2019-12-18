package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.User;

import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {
    Optional<Long> getIdByEmail(String email);

    void saveEmail(String email, User user);

    Contact getEmailAndUserId(String email, Long userId);
}