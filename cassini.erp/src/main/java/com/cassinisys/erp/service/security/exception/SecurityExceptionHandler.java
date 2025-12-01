package com.cassinisys.erp.service.security.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cassinisys.erp.api.exceptions.APIError;

/**
 * Created by reddy on 7/27/15.
 */
@ControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ InvalidLoginException.class })
    protected ResponseEntity<Object> handleInvalidLoginRequest(RuntimeException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        APIError error = new APIError("1001", e.getMessage());
        return handleExceptionInternal(e, error, headers, HttpStatus.UNAUTHORIZED, request);
    }
}
