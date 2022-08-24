package com.mkohan.render.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtTokenService {

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

    Optional<String> tryExtractUsername(String token);
}
