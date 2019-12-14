package com.softserve.academy.event.repository;

import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.io.Serializable;
import java.util.Map;

public interface PaginationRepository<T extends Serializable, I extends Serializable> extends BasicRepository<T, I> {

     Page<T> findAll(Pageable pageable);

}
