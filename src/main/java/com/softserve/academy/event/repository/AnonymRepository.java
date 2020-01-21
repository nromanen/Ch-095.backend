package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Anonym;

public interface AnonymRepository extends BasicRepository<Anonym, Long> {

    Anonym save(String description);
}
