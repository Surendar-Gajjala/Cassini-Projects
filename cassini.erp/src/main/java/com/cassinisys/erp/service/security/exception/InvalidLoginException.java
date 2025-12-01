package com.cassinisys.erp.service.security.exception;

/**
 * Created by reddy on 7/27/15.
 */
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
