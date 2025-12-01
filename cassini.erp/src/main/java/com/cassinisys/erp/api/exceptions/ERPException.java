package com.cassinisys.erp.api.exceptions;

import org.springframework.http.HttpStatus;


public class ERPException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer status;
	private ErrorCodes code;

	public ERPException() {
		code = ErrorCodes.GENERAL;
		status = HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

	public ERPException(HttpStatus status, ErrorCodes code) {
		this.status = status.value();
		this.code = code;
	}

	public ERPException(HttpStatus status, ErrorCodes code, String message) {
		super(message);
		this.status = status.value();
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public ErrorCodes getCode() {
		return code;
	}

	public void setCode(ErrorCodes code) {
		this.code = code;
	}

}

