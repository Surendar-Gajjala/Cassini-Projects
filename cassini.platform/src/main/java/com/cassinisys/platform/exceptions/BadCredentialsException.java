package com.cassinisys.platform.exceptions;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Bad credentials - incorrect username and/or password");
    }
}
