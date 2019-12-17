package com.softserve.academy.event.service.db;

import java.util.Optional;

public interface ContactService{
    Optional<Long> getIdByEmail(String email);
    public void saveEmail(String email);
}