package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyContactConnector;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveyContactConnectorServiceImpl implements SurveyContactConnectorService {

    private final SurveyContactConnectorRepository repository;

    @Autowired
    public SurveyContactConnectorServiceImpl(SurveyContactConnectorRepository repository){
        this.repository = repository;
    }

    @Override
    public Optional<SurveyContactConnector> findFirstById(Long id) {
        return repository.findFirstById(id);
    }

    @Override
    public List<SurveyContactConnector> findAll() {
        return repository.findAll();
    }

    @Override
    public SurveyContactConnector save(SurveyContactConnector entity) {
        return repository.save(entity);
    }

    @Override
    public SurveyContactConnector update(SurveyContactConnector object) {
        return repository.update(object);
    }

    @Override
    public void delete(SurveyContactConnector entity) {
        repository.delete(entity);
    }

    @Override
    public void detach(SurveyContactConnector entity) {
        repository.detach(entity);
    }

    @Override
    public boolean isEnable(Long contactId, Long surveyId) {
        return repository.isEnable(contactId, surveyId);
    }

    @Override
    public Optional<SurveyContactConnector> findByContactAndSurvey(Long contactId, Long surveyId) {
        return repository.findByContactAndSurvey(contactId, surveyId);
    }
}
