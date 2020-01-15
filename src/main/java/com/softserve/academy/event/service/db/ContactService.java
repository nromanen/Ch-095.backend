package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface ContactService extends BasicService<Contact, Long>  {

    Optional<Long> getIdByEmail(String email);

    boolean canPass(Long surveyId, String contactEmail);

    Page<Contact> findAllByPageableAndFilter(Pageable pageable, String filter);

    Long save(ContactDTO contactDTO);

    void update(ContactDTO contactDTO);

    void delete(Long id);

}
