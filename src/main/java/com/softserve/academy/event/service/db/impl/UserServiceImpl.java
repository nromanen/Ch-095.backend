package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.repository.VerificationTokenRepository;
import com.softserve.academy.event.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Long> getAuthenticationId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            Long id = userRepository.findByEmail(email).orElseThrow(UserNotFound::new).getId();
            return Optional.of(id);
        }
       return Optional.empty();
    }

    @Override
    public VerificationToken updateTokenExpiration(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        verificationToken.updateToken(UUID.randomUUID().toString());
        return tokenRepository.save(verificationToken);
    }

    @Override
    public User newUserAccount(User userAccount) {
        if (emailExists(userAccount.getEmail())) {
            throw new EmailExistException("There is an account with that email address: " + userAccount.getEmail());
        }
        User user = new User();
        user.setEmail(userAccount.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken vToken = new VerificationToken(token, user);
        return tokenRepository.save(vToken);
    }

    @Override
    public TokenValidation validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TokenValidation.TOKEN_INVALID;
        }
        final User user = verificationToken.getUser();
        final Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - calendar.getTime().getTime()) <= 0) {
            return TokenValidation.TOKEN_EXPIRED;
        }
        user.setActive(true);
        userRepository.save(user);
        return TokenValidation.TOKEN_VALID;
    }

    @Override
    public String getEmailByUserId(Long id) {
        return userRepository.getEmailByUserId(id);
    }

    @Override
    public Optional<User> findFirstById(Long id) {
        return userRepository.findFirstById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public User update(User object) {
        return userRepository.update(object);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void detach(User entity) {
        userRepository.detach(entity);
    }
}
