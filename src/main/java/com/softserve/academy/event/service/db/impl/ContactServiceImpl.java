package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.ContactNotFound;
import com.softserve.academy.event.exception.DataAlreadyUsedException;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.EmailValidator;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;

@Service
@Transactional
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final UserService userService;
    private final ContactRepository repository;
    private final SurveyContactConnectorService surveyContactConnectorService;

    @Autowired
    public ContactServiceImpl(UserService userService, ContactRepository repository, SurveyContactConnectorService surveyContactConnectorService) {
        this.userService = userService;
        this.repository = repository;
        this.surveyContactConnectorService = surveyContactConnectorService;
    }

    @Override
    public List<Contact> findAllByCurrentUser() {
        return repository.findAllByUserEmail(getCurrentUserEmail());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Page<Contact> findAllByPageableAndFilter(Pageable pageable, String filter) {
        if (Objects.nonNull(filter) && filter.length() > 0) {
            return repository.findAllByPageableAndFilterLikeNameOrEmail(pageable, filter);
        } else {
            return repository.findAllByPageable(pageable);
        }
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return repository.getIdByEmail(email);
    }

    @Override
    public boolean canPass(Long surveyId, String contactEmail) {
        Optional<Long> contactId = getIdByEmail(contactEmail);
        try {
            return surveyContactConnectorService.isEnable(contactId.orElse(null), surveyId);
        } catch (IncorrectLinkException | SurveyAlreadyPassedException e) {
            return false;
        }
    }

    @Override
    public List<Contact> findAvailableContacts(Long surveyId, Long userId) {
        return repository.findAvailableContacts(surveyId, userId);
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
    public Contact save(ContactDTO contactDTO) {
        return save(
                new Contact(
                        null,
                        userService.findByEmail(getCurrentUserEmail()),
                        contactDTO.getName(),
                        contactDTO.getEmail(),
                        new HashSet<>())
        );
    }

    @Override
    public void saveAll(List<Contact> contacts, boolean importNames) {
        EmailValidator.validate(contacts);
        User user = userService.findByEmail(getCurrentUserEmail());
        if (importNames) {
            contacts.forEach(contact -> {
                contact.setUser(user);
                repository.saveWithConflictUpdate(contact);
            });
        } else {
            contacts.forEach(contact -> {
                contact.setUser(user);
                repository.saveWithConflictIgnore(contact);
            });
        }
    }

    @Override
    public Contact update(Contact object) {
        return repository.update(object);
    }

    @Override
    public void update(ContactDTO contactDTO) {
        Contact contact = findFirstById(contactDTO.getId())
                .orElseThrow(ContactNotFound::new);
        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());
        update(contact);
    }

    @Override
    public void delete(Contact contact) {
        repository.delete(contact);
    }

    @Override
    public void delete(Long id) {
        if (repository.isSurveysContainContact(id)) {
            throw new DataAlreadyUsedException("This contact uses in survey(s) and can't be deleted");
        }
        delete(repository.findFirstById(id)
                .orElseThrow(ContactNotFound::new));
    }

}
