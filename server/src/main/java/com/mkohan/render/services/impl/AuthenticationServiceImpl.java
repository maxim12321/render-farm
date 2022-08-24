package com.mkohan.render.services.impl;

import com.mkohan.render.entities.User;
import com.mkohan.render.exceptions.BadCredentialsException;
import com.mkohan.render.repositories.UserRepository;
import com.mkohan.render.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new BadCredentialsException();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(BadCredentialsException::new);
    }

    @Override
    public void signUp(String username, String password) {
        final User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
