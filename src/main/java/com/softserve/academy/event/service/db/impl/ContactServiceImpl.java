package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.service.db.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return repository.getIdByEmail(email);
    }

    @Override
    public Optional<Contact> findFirstById(Long id) {
        return repository.findFirstById(id);
    }

    @Override
    public List<Contact> findAll() {
        return repository.findAll();
    }

    @Override
    public Contact save(Contact entity) {
        return repository.save(entity);
    }

    @Override
    public Contact update(Contact object) {
        return repository.update(object);
    }

    @Override
    public void delete(Contact entity) {
        repository.delete(entity);
    }

    @Override
    public void detach(Contact entity) {
        repository.detach(entity);
    }

}