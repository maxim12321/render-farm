package com.mkohan.render.services;

import com.mkohan.render.entities.User;

public interface UserService {

    User getByUsername(String username);
}
