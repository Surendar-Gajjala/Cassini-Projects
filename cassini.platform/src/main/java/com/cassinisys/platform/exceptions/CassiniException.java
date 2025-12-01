package com.cassinisys.platform.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author reddy
 */
public class CassiniException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private ErrorCodes code;

    public CassiniException() {
        code = ErrorCodes.GENERAL;
        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CassiniException(String message) {
        super(message);
        code = ErrorCodes.GENERAL;
        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CassiniException(HttpStatus status, ErrorCodes code) {
        this.status = status.value();
        this.code = code;
    }

    public CassiniException(HttpStatus status, ErrorCodes code, String message) {
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

