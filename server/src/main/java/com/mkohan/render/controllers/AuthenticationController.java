package com.mkohan.render.controllers;

import com.mkohan.render.dtos.AuthenticationRequest;
import com.mkohan.render.entities.User;
import com.mkohan.render.services.AuthenticationService;
import com.mkohan.render.services.JwtTokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid AuthenticationRequest request) {
        final User user = authenticationService.login(request.getUsername(), request.getPassword());
        return jwtTokenService.generateToken(user);
    }

    @PostMapping("/sign_up")
    public void signUp(@RequestBody @Valid AuthenticationRequest request) {
        authenticationService.signUp(request.getUsername(), request.getPassword());
    }
}
