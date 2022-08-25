package com.mkohan.render.exceptions;

public class IncorrectCredentialsException extends RuntimeException {

    public IncorrectCredentialsException() {
        super("Incorrect Credentials");
    }
}
