package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.service.db.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl extends BasicServiceImpl<Contact, Long> implements ContactService {
}
