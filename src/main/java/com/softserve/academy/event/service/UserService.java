package com.softserve.academy.event.service;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.validation.EmailExistsException;

public interface UserService {

    VerificationToken generateNewVerificationToken(String token);
    User newUserAccount(UserDto accountDto) throws EmailExistsException;
    User getUser(String verificationToken);
    void saveRegisteredUser(User user);
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
    String validateVerificationToken(String token);
    public void createPasswordResetTokenForUser(final User user, final String token);
    public User findUserByEmail(final String email);
}
