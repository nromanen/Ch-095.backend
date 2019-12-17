package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Sort;

import java.util.Optional;
import java.util.Set;

public interface EventRepository {

    Optional<Survey> findFirstById(long id);
    Optional<Survey> findFirstById(Survey survey);
    Set<Survey> findAll();
    Set<Survey> findAll(Sort sort);
    Optional<Survey> updateByEvent(Survey survey);
    boolean deleteById(long id);
    boolean deleteById(Survey survey);

}
