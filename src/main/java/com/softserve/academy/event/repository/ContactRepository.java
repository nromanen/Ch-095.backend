package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {

    void saveWithConflictUpdate(Contact contact);

    void saveWithConflictIgnore(Contact contact);

    Page<Contact> findAllByPageable(Pageable pageable);

    Page<Contact> findAllByPageableAndFilterLikeNameOrEmail(Pageable pageable, String text);

    Optional<Contact> findByEmail(String email);

    Optional<Long> getIdByEmail(String email);

    Contact getEmailAndUserId(String email, Long userId);

    boolean isSurveysContainContact(Long contactId);

}
