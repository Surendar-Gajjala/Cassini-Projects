package com.cassinisys.platform.service.config;

/**
 * Created by reddy on 10/17/15.
 */
public class ConfigServiceException extends RuntimeException {
    public ConfigServiceException() {
    }

//    @Transactional
    public ConfigServiceException(String message) {
        super(message);
    }

//    @Transactional
    public ConfigServiceException(String message, Throwable cause) {
        super(message, cause);
    }

//    @Transactional
    public ConfigServiceException(Throwable cause) {
        super(cause);
    }

//    @Transactional
    public ConfigServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
