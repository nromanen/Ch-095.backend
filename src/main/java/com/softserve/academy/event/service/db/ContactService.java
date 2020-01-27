package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContactService extends BasicService<Contact, Long>  {

    List<Contact> findAllByCurrentUser();

    Optional<Long> getIdByEmail(String email);

    boolean canPass(Long surveyId, String contactEmail);

    List<Contact> findAvailableContacts(Long surveyId, Long userId);

    Page<Contact> findAllByPageableAndFilter(Pageable pageable, String filter);

    Contact save(ContactDTO contactDTO);

    void saveAll(List<Contact> contact, boolean importNames);

    void update(ContactDTO contactDTO);

    void delete(Long id);

}
