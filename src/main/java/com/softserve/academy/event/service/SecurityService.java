package com.softserve.academy.event.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}



