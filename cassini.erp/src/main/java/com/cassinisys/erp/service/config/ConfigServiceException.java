package com.cassinisys.erp.service.config;

/**
 * Created by reddy on 10/17/15.
 */
public class ConfigServiceException extends RuntimeException {
    public ConfigServiceException() {
    }

    public ConfigServiceException(String message) {
        super(message);
    }

    public ConfigServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigServiceException(Throwable cause) {
        super(cause);
    }

    public ConfigServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
