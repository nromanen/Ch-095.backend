package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.exception.EmailExistException;

import java.util.Optional;

public interface UserService extends BasicService<User, Long> {
    Optional<Long> getAuthenticationId();
    VerificationToken updateTokenExpiration(String token);

    User newUserAccount(User account) throws EmailExistException;

    User getUser(String verificationToken);
    String getToken(User user);


    VerificationToken createVerificationToken(User user);

    TokenValidation validateVerificationToken(String token);
}
