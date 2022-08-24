package com.mkohan.render.services;

import com.mkohan.render.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtTokenService {

    String generateToken(User user);

    boolean validateToken(String token, UserDetails userDetails);

    Optional<String> tryExtractUsername(String token);
}
