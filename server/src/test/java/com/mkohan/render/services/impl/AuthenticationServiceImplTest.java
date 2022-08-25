package com.mkohan.render.services.impl;

import com.mkohan.render.entities.User;
import com.mkohan.render.exceptions.IncorrectCredentialsException;
import com.mkohan.render.exceptions.UsernameAlreadyExistsException;
import com.mkohan.render.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded";

    private static final User USER = new User("username", "password");

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void login_badCredentials() {
        when(authenticationManager.authenticate(argThat(this::isValidAuthentication)))
                .thenThrow(BadCredentialsException.class);

        assertThatThrownBy(() -> authenticationService.login(USERNAME, PASSWORD))
                .isInstanceOf(IncorrectCredentialsException.class);
    }

    @Test
    public void login_usernameNotFound() {
        when(authenticationManager.authenticate(argThat(this::isValidAuthentication)))
                .thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(USERNAME, PASSWORD))
                .isInstanceOf(IncorrectCredentialsException.class);
    }

    @Test
    public void login_successful() {
        when(authenticationManager.authenticate(argThat(this::isValidAuthentication)))
                .thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(USER));

        assertThat(authenticationService.login(USERNAME, PASSWORD))
                .isSameAs(USER);
    }

    private boolean isValidAuthentication(Authentication auth) {
        return USERNAME.equals(auth.getPrincipal()) && PASSWORD.equals(auth.getCredentials());
    }

    @Test
    public void signUp_usernameAlreadyExists() {
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(USER));

        assertThatThrownBy(() -> authenticationService.signUp(USERNAME, PASSWORD))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    public void signUp_success() {
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(PASSWORD))
                .thenReturn(ENCODED_PASSWORD);

        authenticationService.signUp(USERNAME, PASSWORD);

        verify(userRepository).saveAndFlush(argThat(this::isUserValid));
    }

    private boolean isUserValid(User user) {
        return USERNAME.equals(user.getUsername()) && ENCODED_PASSWORD.equals(user.getPassword());
    }

    @Test
    public void signUp_concurrent() throws InterruptedException {
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        when(userRepository.saveAndFlush(any()))
                .thenAnswer(mock -> {
                    Thread.sleep(1000);

                    when(userRepository.findByUsername(USERNAME))
                            .thenReturn(Optional.of(USER));

                    return USER;
                });

        final Thread thread = new Thread(() -> authenticationService.signUp(USERNAME, PASSWORD));
        thread.start();
        Thread.sleep(100);

        assertThatThrownBy(() -> authenticationService.signUp(USERNAME, PASSWORD))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        thread.join();

        // Verify that user saved EXACTLY once
        verify(userRepository).saveAndFlush(any());
    }
}
