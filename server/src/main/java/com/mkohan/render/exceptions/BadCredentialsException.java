package com.mkohan.render.exceptions;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Incorrect Credentials");
    }
}
