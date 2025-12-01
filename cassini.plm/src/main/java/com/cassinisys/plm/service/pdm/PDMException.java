package com.cassinisys.plm.service.pdm;

import com.cassinisys.platform.exceptions.ErrorCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class PDMException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer status;
    private ErrorCodes code;

    public PDMException() {
        code = ErrorCodes.GENERAL;
        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public PDMException(String message) {
        super(message);
        code = ErrorCodes.GENERAL;
        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public PDMException(HttpStatus status, ErrorCodes code) {
        this.status = status.value();
        this.code = code;
    }

    public PDMException(HttpStatus status, ErrorCodes code, String message) {
        super(message);
        this.status = status.value();
        this.code = code;
    }
}
