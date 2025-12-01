package com.cassinisys.platform.exceptions;

import java.io.Serializable;

/**
 * @author reddy
 */
public enum ErrorCodes implements Serializable {
	GENERAL,
	UNAUTHORIZED,
	RESOURCE_NOT_FOUND,
	SESSION_TIME_OUT
}
