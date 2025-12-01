package com.cassinisys.platform.config;

/**
 * Created by reddy on 9/2/15.
 */
public class InvalidApiKeyException extends RuntimeException {

    public InvalidApiKeyException() {
    }

    public InvalidApiKeyException(String message) {
        super(message);
    }

    public InvalidApiKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidApiKeyException(Throwable cause) {
        super(cause);
    }

    public InvalidApiKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
