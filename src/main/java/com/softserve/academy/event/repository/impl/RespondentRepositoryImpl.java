package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Respondent;
import com.softserve.academy.event.repository.RespondentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RespondentRepositoryImpl extends BasicRepositoryImpl<Respondent, Long> implements RespondentRepository {

    private final String fromQuery = "from " + clazz.getName() + " as r where r.contact.id = :contactId";

    @Override
    public Respondent save(Contact contact) {
        return findFirstByContactId(contact.getId())
                .orElse(save(new Respondent(contact)));
    }

    @Override
    public Respondent save(Anonym anonym) {
        return save(new Respondent(anonym));
    }

    private Optional<Respondent> findFirstByContactId(Long contactId) {
        List result = sessionFactory.getCurrentSession()
                .createQuery(fromQuery)
                .setParameter("contactId", contactId)
                .setMaxResults(1)
                .getResultList();
        if (result.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(result.get(0));
    }
}
