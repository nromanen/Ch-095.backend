package com.softserve.academy.event.service;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.exception.EmailExistException;

public interface UserService {

    VerificationToken generateNewVerificationToken(String token);
    User newUserAccount(User account) throws EmailExistException;
    User getUser(String verificationToken);
    void createVerificationToken(User user, String token);
    TokenValidation validateVerificationToken(String token);

}
