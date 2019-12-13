package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.repository.BasicRepository;
import com.softserve.academy.event.service.db.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
public class BasicServiceImpl<T extends Serializable, I extends Serializable> implements BasicService<T, I> {

    @Autowired
    private BasicRepository<T, I> basicRepository;

    @Override
    public Optional<T> findFirstById(I id) {
        return basicRepository.findFirstById(id);
    }

    @Override
    public List<T> findAll() {
        return basicRepository.findAll();
    }

    @Override
    public T save(T entity) {
        return basicRepository.save(entity);
    }

    @Override
    public T update(T object) {
        return basicRepository.update(object);
    }

    @Override
    public void delete(T entity) {
        basicRepository.delete(entity);
    }

    @Override
    public void detach(T entity) {
        basicRepository.detach(entity);
    }

}
