package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {

    Page<Contact> findAllByPageable(Pageable pageable);

    Page<Contact> findAllByPageableAndNameOrEmail(Pageable pageable, String text);

    Optional<Contact> findByEmail(String email);

    Optional<Long> getIdByEmail(String email);

    Contact getEmailAndUserId(String email, Long userId);
}
