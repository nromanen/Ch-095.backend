package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.TokenValidation;

import java.util.Optional;

public interface UserService extends BasicService<User, Long> {

    Optional<Long> getAuthenicationId();

    VerificationToken generateNewVerificationToken(String token);

    User newUserAccount(User account);

    User getUser(String verificationToken);

    User getUserByName(String username);

    void createVerificationToken(User user, String token);

    TokenValidation validateVerificationToken(String token);

}
