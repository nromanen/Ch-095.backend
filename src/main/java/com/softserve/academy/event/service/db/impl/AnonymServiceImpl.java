package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Anonym;
import com.softserve.academy.event.repository.AnonymRepository;
import com.softserve.academy.event.service.db.AnonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AnonymServiceImpl implements AnonymService {
    private final AnonymRepository anonymRepository;

    @Autowired
    public AnonymServiceImpl(AnonymRepository anonymRepository) {
        this.anonymRepository = anonymRepository;
    }

    @Override
    public Anonym save(String description) {
        return anonymRepository.save(description);
    }
}
