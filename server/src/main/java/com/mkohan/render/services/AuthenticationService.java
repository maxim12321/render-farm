package com.mkohan.render.services;

import com.mkohan.render.entities.User;

public interface AuthenticationService {

    User login(String username, String password);

    void signUp(String username, String password);
}
