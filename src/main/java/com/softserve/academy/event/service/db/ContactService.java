package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Contact;

public interface ContactService extends BasicService<Contact, Long> {

    Long getIdByEmail(String email);
    boolean canPass(Long surveyId, String contactEmail);
}
