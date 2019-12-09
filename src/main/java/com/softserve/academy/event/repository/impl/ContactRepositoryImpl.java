package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContactRepositoryImpl extends BasicRepositoryImpl<Contact, Long> implements ContactRepository {
}
