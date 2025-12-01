package com.cassinisys.platform.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

/**
 * @author reddy
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends CassiniException {

    private static final long serialVersionUID = 1L;

    @Autowired
    private static MessageSource messageSource;

    private static final String DEFAULT_MESSAGE = messageSource.getMessage("resource_not_found", null, "Resource not found", Locale.getDefault());

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND,
                DEFAULT_MESSAGE);
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND, message);
    }
}
