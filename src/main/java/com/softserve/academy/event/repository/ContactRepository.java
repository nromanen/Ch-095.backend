package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;

import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {
    Optional<Long> getIdByEmail(String email);
    public void saveEmail(String email);
}