package com.cassinisys.platform.service.portal;

public class PortalException extends RuntimeException {

    public PortalException(String message) {
        super(message);
    }

    public PortalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortalException(Throwable cause) {
        super(cause);
    }
}
