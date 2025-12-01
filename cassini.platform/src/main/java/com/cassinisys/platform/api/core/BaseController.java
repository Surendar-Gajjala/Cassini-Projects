package com.cassinisys.platform.api.core;

import com.cassinisys.platform.exceptions.APIError;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.security.EvaluationConstraints;
import com.cassinisys.platform.service.security.SessionWrapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by reddy on 12/16/14.
 */

/**
 * account.cassinisys.com/cust1
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "PLATFORM.CORE", description = "Core endpoints")
@ControllerAdvice
public abstract class BaseController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BaseController.class);


    @Autowired
    private SessionWrapper sessionWrapper;

    protected SessionWrapper getSessionWrapper() {
        return sessionWrapper;
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public
    @ResponseBody
    ModelMap handleEmptyResultDataAccessException(
            EmptyResultDataAccessException ex, HttpServletResponse response)
            throws Exception {
        LOGGER.error(ex.getMessage(), ex);
        return new APIError(ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody APIError dataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return new APIError(ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "Database error")
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public @ResponseBody APIError databaseError(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return new APIError(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody APIError accessDeniedError(AccessDeniedException ex, HttpServletResponse response){
        LOGGER.error(EvaluationConstraints.NOPERMISSION, ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new APIError(EvaluationConstraints.NOPERMISSION);
    }

    @ExceptionHandler(CassiniException.class)
    public @ResponseBody APIError handleIsException(CassiniException ex,
                                      HttpServletResponse response) {
        LOGGER.error(ex.getMessage(), ex);
        response.setStatus(ex.getStatus());
        return new APIError(ex.getCode().toString(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    ModelMap handleAllException(Exception ex,
                                HttpServletResponse response) throws Exception {
        LOGGER.error(ex.getMessage(), ex);
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new APIError(ex.getMessage());
    }
}
