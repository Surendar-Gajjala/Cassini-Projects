package com.cassinisys.plm.service.plm;

/**
 * Created by reddy on 6/7/16.
 */
public class ItemServiceException extends RuntimeException {
    public ItemServiceException() {
        super();
    }

    public ItemServiceException(String message) {
        super(message);
    }

    public ItemServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemServiceException(Throwable cause) {
        super(cause);
    }

    protected ItemServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
