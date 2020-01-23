package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Respondent;


public interface RespondentRepository extends BasicRepository<Respondent, Long> {
    Respondent save(Contact contact);

    Respondent save(Anonym anonym);
}
