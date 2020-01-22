package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Respondent;

public interface RespondentService  {
    Respondent save(Respondent respondent);

    Respondent save(Contact contact);

    Respondent save(Anonym anonym);
}
