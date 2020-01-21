package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Respondent;
import com.softserve.academy.event.repository.RespondentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RespondentRepositoryImpl extends BasicRepositoryImpl<Respondent, Long> implements RespondentRepository {

    @Override
    public Respondent save(Contact contact) {
        return save(new Respondent(contact));
    }

    @Override
    public Respondent save(Anonym anonym) {
        return save(new Respondent(anonym));
    }
}
