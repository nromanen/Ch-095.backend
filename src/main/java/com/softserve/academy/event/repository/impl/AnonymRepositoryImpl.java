package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.repository.AnonymRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AnonymRepositoryImpl extends BasicRepositoryImpl<Anonym, Long> implements AnonymRepository {

    @Override
    public Anonym save(String description) {
        return this.save(new Anonym(description));
    }
}
