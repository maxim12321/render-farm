package com.mkohan.render.services.impl;

import com.mkohan.render.entities.User;
import com.mkohan.render.exceptions.UserNotFoundException;
import com.mkohan.render.repositories.UserRepository;
import com.mkohan.render.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
