package com.cassinisys.erp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by reddy on 9/2/15.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends ERPException {
    public UnauthorizedAccessException(String message) {
        super(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED, message);
    }
}
