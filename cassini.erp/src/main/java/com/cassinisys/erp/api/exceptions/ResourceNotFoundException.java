package com.cassinisys.erp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Darek Osiennik
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ERPException {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_MESSAGE = "Resource not found";

	public ResourceNotFoundException() {
		super(HttpStatus.NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND,
				DEFAULT_MESSAGE);
	}

	public ResourceNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND, message);
	}
}
