package com.mkohan.render.services.impl;

import com.mkohan.render.entities.User;
import com.mkohan.render.services.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceImplTest {

    private static final String INVALID_TOKEN = "invalid_token";

    private final JwtTokenService jwtTokenService = new JwtTokenServiceImpl();

    private final User user = new User("username", "password");

    @Mock
    private UserDetails userDetails;

    @Test
    public void generateToken_tokenIsValid() {
        when(userDetails.getUsername()).thenReturn(user.getUsername());

        final String token = jwtTokenService.generateToken(user);

        assertThat(token)
                .isNotBlank()
                .matches(t -> jwtTokenService.validateToken(t, userDetails));
    }

    @Test
    public void validateToken_invalidToken() {
        assertThat(jwtTokenService.validateToken(INVALID_TOKEN, userDetails))
                .isFalse();
    }

    @Test
    public void validateToken_invalidUsername() {
        when(userDetails.getUsername()).thenReturn("not " + user.getUsername());

        final String token = jwtTokenService.generateToken(user);

        assertThat(jwtTokenService.validateToken(token, userDetails))
                .isFalse();
    }

    @Test
    public void validateToken_valid() {
        when(userDetails.getUsername()).thenReturn(user.getUsername());

        final String token = jwtTokenService.generateToken(user);

        assertThat(jwtTokenService.validateToken(token, userDetails))
                .isTrue();
    }

    @Test
    public void tryExtractUsername_invalidToken() {
        assertThat(jwtTokenService.tryExtractUsername(INVALID_TOKEN))
                .isEmpty();
    }

    @Test
    public void tryExtractUsername_valid() {
        final String token = jwtTokenService.generateToken(user);

        assertThat(jwtTokenService.tryExtractUsername(token))
                .isPresent()
                .contains(user.getUsername());
    }
}
