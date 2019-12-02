package com.softserve.academy.event.repository;

import com.softserve.academy.event.util.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AdvancedRepository<T extends Serializable, I extends Serializable> extends BasicRepository<T, I> {

    Optional<T> findFirst(T item);

    List<T> findAllById(Iterable<I> ids);

    void delete(T object);

    boolean existsById(I id);


}