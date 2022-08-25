package com.mkohan.render.services.impl;

import com.mkohan.render.entities.User;
import com.mkohan.render.exceptions.IncorrectCredentialsException;
import com.mkohan.render.exceptions.UsernameAlreadyExistsException;
import com.mkohan.render.repositories.UserRepository;
import com.mkohan.render.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Lock signUpLock = new ReentrantLock(true);

    @Override
    public User login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(IncorrectCredentialsException::new);
    }

    @Override
    public void signUp(String username, String password) {
        final User user = new User(username, passwordEncoder.encode(password));

        signUpLock.lock();
        try {
            final Optional<User> byUsername = userRepository.findByUsername(username);
            if (byUsername.isPresent()) {
                throw new UsernameAlreadyExistsException();
            }
            userRepository.saveAndFlush(user);
        } finally {
            signUpLock.unlock();
        }
    }
}
