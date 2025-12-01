package com.cassinisys.erp.service.notification.sms;

/**
 * Created by reddy on 9/7/15.
 */
public class SMSException extends RuntimeException {
    public SMSException() {
    }

    public SMSException(String message) {
        super(message);
    }

    public SMSException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMSException(Throwable cause) {
        super(cause);
    }

    public SMSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
