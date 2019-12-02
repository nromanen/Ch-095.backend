package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.repository.BasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public abstract class BasicRepositoryImpl<T extends Serializable, I extends Serializable> implements BasicRepository<T, I> {

    private final Class<T> clazz;
//    private SessionFactory sessionFactory;

    @Autowired
    public BasicRepositoryImpl() {
        clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public List<T> findAll() {
//        try (Session session = sessionFactory.openSession()) {
//            return (List<T>) session.createQuery("from " + clazz.getName()).getResultList();
//        }
        return null;
    }

    /*@Override
    public List<T> findAll(Sort sort) {
        try (Session session = sessionFactory.openSession()) {
            Sort tempSort = sort;
            StringBuilder builder = new StringBuilder();
            while (true) {
                builder.append(tempSort.getField())
                        .append(" ")
                        .append(tempSort.getDirection().name());
                tempSort = tempSort.getNextSort();
                if (tempSort == null) {
                    break;
                }
                builder.append(",");
            }
            return (List<T>) session
                    .createQuery("from " + clazz.getName() + " order by " + builder.toString())
                    .getResultList();
        }
    }*/

    @Override
    public Optional<T> findFirstById(I id) {
//        try (Session session = sessionFactory.openSession()) {
//            return Optional.of(session.get(clazz,id));
//        }
        return null;
    }

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public T update(T object) {
        return null;
    }

    @Override
    public void deleteById(I id) {

    }

}
