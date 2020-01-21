package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Respondent;
import com.softserve.academy.event.repository.RespondentRepository;
import com.softserve.academy.event.service.db.RespondentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RespondentServiceImpl implements RespondentService {

    private final RespondentRepository respondentRepository;

    @Autowired
    public RespondentServiceImpl(RespondentRepository respondentRepository) {
        this.respondentRepository = respondentRepository;
    }

    @Override
    public Respondent save(Respondent respondent) {
        return respondentRepository.save(respondent);
    }

    @Override
    public Respondent save(Contact contact) {
        return respondentRepository.save(contact);
    }

    @Override
    public Respondent save(Anonym anonym) {
        return respondentRepository.save(anonym);
    }
}
