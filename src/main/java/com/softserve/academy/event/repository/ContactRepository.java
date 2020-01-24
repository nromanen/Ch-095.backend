package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    Optional<Long> getIdByEmail(String email);

    Optional<Contact> findByEmailAndUserId(String email, Long userId);

    List<Contact> listContactsByUserId(Long userId);

    List<Contact> findAvailableContacts(Long surveyId, Long userId);
}