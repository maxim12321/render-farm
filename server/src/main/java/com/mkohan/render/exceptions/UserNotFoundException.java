package com.mkohan.render.exceptions;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
